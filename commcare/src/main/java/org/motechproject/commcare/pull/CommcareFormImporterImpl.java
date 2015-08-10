package org.motechproject.commcare.pull;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.events.FullFormEvent;
import org.motechproject.commcare.events.FullFormFailureEvent;
import org.motechproject.commcare.events.MalformedFormStatusMessageEvent;
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
public class CommcareFormImporterImpl implements CommcareFormImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareFormImporterImpl.class);

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
    private String lastImportedDate;
    private boolean inError;
    private String errorMessage;

    @Override
    public int countForFormImport(Range<DateTime> dateRange, String configName) {
        LOGGER.debug("Counting forms for import for dateRange: {}-{} [config: {}]", dateRange.getMin(),
                dateRange.getMax(), configName);

        validateDateRange(dateRange);

        FormListRequest request = buildFormListRequest(dateRange, 1, 1);
        CommcareFormList formList = formService.retrieveFormList(request, configName);

        int count = formList.getMeta().getTotalCount();
        LOGGER.debug("Form count: {}", count);

        return count;
    }

    @Override
    public void startFormImport(final Range<DateTime> dateRange, final String configName) {
        validateNoExportInProgress();
        validateDateRange(dateRange);

        LOGGER.info("Initiating form import for historical forms from {} to {} [config: {}]",
                dateRange.getMin(), dateRange.getMax(), configName);

        initForImport(dateRange, configName);

        // we start from the last page, since Commcare orders by received_on descending, we want ascending
        final FormListRequest request = buildFormListRequest(dateRange, PAGE_SIZE_FOR_FETCH, pageCount);

        importThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasMore;

                LOGGER.debug("Import thread started");

                do {
                    // fetch the data
                    try {
                        LOGGER.debug("Retrieving forms from page {}, with page size {}",
                                request.getPageNumber(), request.getPageSize());

                        CommcareFormList formList = formService.retrieveFormList(request, configName);

                        LOGGER.debug("Retrieved a list of {} forms", formList.getObjects().size());

                        // send events for forms
                        importFormList(formList);

                        LOGGER.debug("Imported {} forms", formList.getObjects().size());

                        // the first page is the last one
                        hasMore = request.getPageNumber() > 1;

                        // we decrement the page
                        if (hasMore) {
                            LOGGER.debug("Proceeding to the next batch of forms");
                            request.previousPage();
                        }
                    } catch (RuntimeException e) {
                        handleImportError(e, configName);
                        throw e;
                    }
                } while (importInProgress && hasMore);

                LOGGER.info("Form import finished");

                importInProgress = false;
            }
        });

        LOGGER.debug("Starting import thread");

        importThread.start();
    }

    @Override
    public void stopImport() {
        LOGGER.info("Stopping import");

        // will break the loop
        importInProgress = false;
        // wait for the thread
        if (importThread != null) {
            try {
                LOGGER.debug("Joining the import thread");
                importThread.join();
            } catch (InterruptedException e) {
                throw new IllegalStateException("Interrupted while stopping form import", e);
            }
        }

        LOGGER.info("Import stopped");
    }

    @Override
    public boolean isImportInProgress() {
        return importInProgress;
    }

    @Override
    public FormImportStatus importStatus() {
        LOGGER.debug("Retrieving import status");

        FormImportStatus status = new FormImportStatus();

        status.setTotalForms(totalCount);
        status.setFormsImported(importCount);
        status.setLastImportDate(lastImportedDate);

        if (inError) {
            status.setErrorMsg(errorMessage);
            status.setError(true);
        }

        return status;
    }

    private void initForImport(Range<DateTime> dateRange, String configName) {
        importInProgress = true;
        importCount = 0;
        lastImportedDate = null;
        totalCount =  countForFormImport(dateRange, configName);
        inError = false;
        errorMessage = null;
        // calculate the page number, since we are going backwards
        pageCount = (int) Math.ceil(totalCount / PAGE_SIZE_FOR_FETCH);

        LOGGER.debug("Initialized for import");
    }

    private void importFormList(CommcareFormList formList) {
        // iterate backwards
        for (CommcareForm form : Lists.reverse(formList.getObjects())) {
            FullFormEvent formEvent = new FullFormEvent(form.getForm(), form.getReceivedOn(), form.getConfigName());

            eventRelay.sendEventMessage(formEvent.toMotechEvent());

            lastImportedDate = form.getReceivedOn();
            importCount++;

            LOGGER.info("Imported form with ID: {}, received on: {}", form.getId(), form.getReceivedOn());
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
        errorMessage = e.getMessage();
        inError = true;

        // stop import
        importInProgress = false;

        FullFormFailureEvent failureEvent = new FullFormFailureEvent(errorMessage, configName);
        eventRelay.sendEventMessage(failureEvent.toMotechEvent());

        // Trigger a status message in the Admin UI
        String msg = "Error while importing form: " + errorMessage;
        MalformedFormStatusMessageEvent statusMessageEvent = new MalformedFormStatusMessageEvent(msg);
        eventRelay.sendEventMessage(statusMessageEvent.toMotechEvent());
    }
}
