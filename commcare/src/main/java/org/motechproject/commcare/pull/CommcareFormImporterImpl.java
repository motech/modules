package org.motechproject.commcare.pull;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.motechproject.commcare.builder.FormListRequestBuilder;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.events.FullFormEvent;
import org.motechproject.commcare.events.FullFormFailureEvent;
import org.motechproject.commcare.events.FailedImportStatusMessageEvent;
import org.motechproject.commcare.request.FormListRequest;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.commons.api.Range;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of {@link CommcareFormImporter}. Uses the {@link CommcareFormService} for
 * retrieval of forms.
 */
public class CommcareFormImporterImpl implements CommcareFormImporter {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareFormImporterImpl.class);

    private static final int PAGE_SIZE_FOR_FETCH = 100;

    private CommcareFormService formService;
    private EventRelay eventRelay;

    private int fetchSize = PAGE_SIZE_FOR_FETCH;

    private Thread importThread;
    private boolean importInProgress;

    private int importCount;
    private int totalCount;
    private int pageCount;
    private String lastImportedDate;
    private String lastImportedFormId;
    private boolean inError;
    private String errorMessage;
    private String lastFormXMLNSToBeImported;

    public CommcareFormImporterImpl(EventRelay eventRelay, CommcareFormService formService) {
        this.eventRelay = eventRelay;
        this.formService = formService;
    }

    @Override
    public int countForImport(Range<DateTime> dateRange, String configName) {
        validateDateRange(dateRange);

        LOGGER.info("Counting forms for import for dateRange: {}-{} [config: {}]", dateRange.getMin(),
                dateRange.getMax(), configName);

        FormListRequest request = formListRequestBuilder(dateRange, 1, 1).build();
        CommcareFormList formList = formService.retrieveFormList(request, configName);

        int count = formList.getMeta().getTotalCount();
        LOGGER.info("Form count: {}", count);

        return count;
    }

    @Override
    public boolean checkFormIdForImport(String formId, String configName) {
        LOGGER.info("Checking form with id {} [config: {}]", formId, configName);
        CommcareForm form = formService.retrieveForm(formId, configName);
        if (form.getId() != null) {
            LOGGER.info("Form with id {} exists", form.getId());
            return true;
        } else {
            LOGGER.info("Form with id {} doesnot exist", formId);
            return false;
        }
    }


    @Override
    public void startImport(final Range<DateTime> dateRange, final String configName) {
        validateNoImportInProgress();
        validateDateRange(dateRange);

        LOGGER.info("Initiating form import for historical forms from {} to {} [config: {}]",
                dateRange.getMin(), dateRange.getMax(), configName);

        initForImport(dateRange, configName);

        importThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasMore = false;

                LOGGER.debug("Import thread started");

                // we start from the last page, since Commcare orders by received_on descending, we want ascending
                // we reuse the builder
                final FormListRequestBuilder requestBuilder = formListRequestBuilder(dateRange, fetchSize, pageCount);

                do {
                    // fetch the data
                    try {
                        // rebuild the request
                        FormListRequest request = requestBuilder.build();

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
                            requestBuilder.withPreviousPage();
                        }
                    } catch (RuntimeException e) {
                        LOGGER.error("Error while importing forms", e);
                        LOGGER.error("{} of {} forms imported. Last form xmlns to be imported was {}", importCount, totalCount, lastFormXMLNSToBeImported);
                        handleImportError(e, configName);
                    }
                } while (importInProgress && hasMore);

                LOGGER.info("Form import finished. {} of {} forms imported. ", importCount, totalCount);


                importInProgress = false;
            }
        });

        LOGGER.debug("Starting import thread");

        importThread.start();
    }

    @Override
    public void startImportById(final String formId, final String configName) {
        LOGGER.debug("Initiating form import with form id {} [config: {}]",
                formId, configName);
        CommcareForm form = formService.retrieveForm(formId, configName);
        form.getForm().addAttribute("app_id", form.getAppId());
        FullFormEvent formEvent = new FullFormEvent(form.getForm(), form.getReceivedOn(), form.getConfigName());
        eventRelay.sendEventMessage(formEvent.toMotechEvent());
        lastFormXMLNSToBeImported = formEvent.getAttributes().get("xmlns");
        lastImportedDate = form.getReceivedOn();
        lastImportedFormId = form.getId();
        totalCount = 1;
        importCount = 1;
        LOGGER.info("Imported form with id {}", formId);
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
        status.setLastImportFormId(lastImportedFormId);
        status.setImportInProgress(importInProgress);

        if (inError) {
            status.setErrorMsg(errorMessage);
            status.setError(true);
        }

        return status;
    }

    @Override
    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    private void initForImport(Range<DateTime> dateRange, String configName) {
        importInProgress = true;
        importCount = 0;
        lastImportedDate = null;
        lastImportedFormId = null;
        totalCount =  countForImport(dateRange, configName);
        inError = false;
        errorMessage = null;
        // calculate the page number, since we are going backwards
        pageCount = (int) Math.ceil((double) totalCount / fetchSize);
        lastFormXMLNSToBeImported = null;

        LOGGER.debug("Initialized for import");
    }

    private void importFormList(CommcareFormList formList) {
        // iterate backwards
        for (CommcareForm form : Lists.reverse(formList.getObjects())) {
            form.getForm().addAttribute("app_id", form.getAppId());
            FullFormEvent formEvent = new FullFormEvent(form.getForm(), form.getReceivedOn(), form.getConfigName());

            lastFormXMLNSToBeImported = formEvent.getAttributes().get("xmlns");
            MotechEvent motechEvent = formEvent.toMotechEvent();
            eventRelay.sendEventMessage(motechEvent);

            lastImportedDate = form.getReceivedOn();
            lastImportedFormId = form.getId();
            importCount++;

            LOGGER.info("Imported form with ID: {}, received on: {}", form.getId(), form.getReceivedOn());
        }
    }

    private void validateDateRange(Range<DateTime> dateRange) {
        if (dateRange == null) {
            throw new IllegalArgumentException("Date range cannot be null, provide an empty one for all results");
        }

        DateTime start = dateRange.getMin();
        DateTime end = dateRange.getMax();

        if (start != null && end != null && end.isBefore(start)) {
            throw new IllegalArgumentException(
                    String.format("Date range start must be before date range end. Got start - %s, end - %s",
                            start, end));
        }
    }

    private void validateNoImportInProgress() {
        if (importInProgress) {
            throw new IllegalStateException("An import is already in progress for this session, it has to be stopped " +
                    "before starting a next one");
        }
    }

    private FormListRequestBuilder formListRequestBuilder(Range<DateTime> dateRange, int pageSize, int pageNumber) {
        return new FormListRequestBuilder()
                .withReceivedOnStart(dateRange.getMin()).withReceivedOnEnd(dateRange.getMax())
                .withPageSize(pageSize).withPageNumber(pageNumber);
    }

    private void handleImportError(RuntimeException e, String configName) {
        errorMessage = e.getMessage();
        inError = true;

        // stop import
        importInProgress = false;

        FullFormFailureEvent failureEvent = new FullFormFailureEvent(configName, errorMessage);
        eventRelay.sendEventMessage(failureEvent.toMotechEvent());

        // Trigger a status message in the Admin UI
        String msg = "Error while importing form: " + errorMessage;
        FailedImportStatusMessageEvent statusMessageEvent = new FailedImportStatusMessageEvent(msg);
        eventRelay.sendEventMessage(statusMessageEvent.toMotechEvent());
    }
}
