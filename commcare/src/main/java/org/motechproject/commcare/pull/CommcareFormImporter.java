package org.motechproject.commcare.pull;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.events.FullFormEvent;
import org.motechproject.commcare.events.FullFormFailureEvent;
import org.motechproject.commcare.request.FormListRequest;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.commons.api.Range;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("session")
public class CommcareFormImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareFormImporter.class);

    private static final int PAGE_SIZE_FOR_FETCH = 100;

    @Autowired
    private CommcareFormService formService;

    @Autowired
    private EventRelay eventRelay;

    private Thread importThread;
    private boolean importInProgress = false;
    private int importCount;
    private int totalCount;
    private int pageCount;
    private DateTime lastImportedDate;

    public int countForFormPull(Range<DateTime> dateRange, String configName) {
        validateDateRange(dateRange);

        FormListRequest request = buildFormListRequest(dateRange, 1, 1);
        CommcareFormList formList = formService.retrieveFormList(request, configName);

        return formList.getMeta().getTotalCount();
    }

    public void startFormPull(final Range<DateTime> dateRange, final String configName) {
        validateNoExportInProgress();
        validateDateRange(dateRange);

        initForImport(dateRange, configName);

        // we start from the last page, since Commcare orders by received_on descending, we want ascending
        final FormListRequest request = buildFormListRequest(dateRange, PAGE_SIZE_FOR_FETCH, pageCount);

        importThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasMore;
                do {
                    // fetch the data
                    try {
                        CommcareFormList formList = formService.retrieveFormList(request, configName);

                        // send events for forms
                        importFormList(formList);

                        // the first page is the last one
                        hasMore = request.getPageNumber() > 1;

                        // we decrement the page
                        if (hasMore) {
                            request.previousPage();
                        }
                    } catch (RuntimeException e) {
                        handleImportError(e, configName);
                        throw e;
                    }
                } while (importInProgress && hasMore);

                importInProgress = false;
            }
        });

        importThread.start();
    }

    public void stopImport() {
        // will break the loop
        importInProgress = false;
        // wait for the thread
        try {
            importThread.join();
        } catch (InterruptedException e) {
            throw new IllegalStateException("Interrupted while stopping form import", e);
        }
    }


    public boolean isImportInProgress() {
        return importInProgress;
    }

    private void initForImport(Range<DateTime> dateRange, String configName) {
        importInProgress = true;
        importCount = 0;
        lastImportedDate = null;
        totalCount =  countForFormPull(dateRange, configName);
        // calculate the page number, since we are going backwards
        pageCount = (int) Math.ceil(totalCount / PAGE_SIZE_FOR_FETCH);
    }

    private void importFormList(CommcareFormList formList) {
        // iterate backwards
        for (CommcareForm form : Lists.reverse(formList.getObjects())) {
            FullFormEvent formEvent = new FullFormEvent(form.getForm(), form.getReceivedOn(), form.getConfigName());

            eventRelay.sendEventMessage(formEvent.toMotechEvent());

            lastImportedDate = DateTime.parse(form.getReceivedOn());
            importCount++;
        }
    }

    private void validateDateRange(Range<DateTime> dateRange) {
        if (dateRange == null || (dateRange.getMin() == null && dateRange.getMax() == null)) {
            throw new IllegalArgumentException(
                    "No date range provided - a date range with at least one boundary is mandatory");
        }

        DateTime start = dateRange.getMin();
        DateTime end = dateRange.getMax();

        if (start != null && end != null && end.isBefore(start)) {
            throw new IllegalArgumentException(
                    String.format("Date range start must be before date range end. Got start - %s, end - %s",
                            start, end));
        }
    }

    private void validateNoExportInProgress() {
        if (importInProgress) {
            throw new IllegalStateException("An import is already in progress for this session, it has to be stopped " +
                    "before starting a next one");
        }
    }

    private FormListRequest buildFormListRequest(Range<DateTime> dateRange, int pageSize, int pageNumber) {
        FormListRequest request = new FormListRequest();

        request.setReceivedOnStart(dateRange.getMin());
        request.setReceivedOnEnd(dateRange.getMax());
        request.setPageSize(pageSize);
        request.setPageNumber(pageNumber);

        return request;
    }

    private void handleImportError(RuntimeException e, String configName) {
        FullFormFailureEvent failureEvent = new FullFormFailureEvent(e.getMessage(), configName);
        eventRelay.sendEventMessage(failureEvent.toMotechEvent());
    }
}
