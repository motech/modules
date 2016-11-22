package org.motechproject.commcare.pull;

import com.google.common.collect.Lists;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.commcare.domain.CaseInfo;
import org.motechproject.commcare.domain.CasesInfo;
import org.motechproject.commcare.events.CaseEvent;
import org.motechproject.commcare.events.FullFormFailureEvent;
import org.motechproject.commcare.events.MalformedFormStatusMessageEvent;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.commons.api.Range;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The implementation of {@link CommcareCaseImporter}. Uses the {@link CommcareCaseService} for
 * retrieval of cases.
 */
public class CommcareCaseImporterImpl implements CommcareCaseImporter{

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareCaseImporterImpl.class);

    private static final int PAGE_SIZE_FOR_FETCH = 100;

    private CommcareCaseService caseService;
    private EventRelay eventRelay;

    private int fetchSize = PAGE_SIZE_FOR_FETCH;

    private Thread importThread;
    private boolean importInProgress = false;

    private int importCount;
    private int totalCount;
    private int pageCount;
    private String lastImportedDate;
    private String lastImportedCaseId;
    private boolean inError;
    private String errorMessage;

    public CommcareCaseImporterImpl(EventRelay eventRelay, CommcareCaseService caseService) {
        this.eventRelay = eventRelay;
        this.caseService = caseService;
    }

    @Override
    public int countForImport(Range<DateTime> dateRange, String configName) {
        validateDateRange(dateRange);

        LOGGER.info("Counting forms for import for dateRange: {}-{} [config: {}]", dateRange.getMin(),
                dateRange.getMax(), configName);

        CasesInfo cases = caseService.getCasesByCasesTimeWithMetadata(dateTimeToString(dateRange.getMin()),
                dateTimeToString(dateRange.getMax()), 1, 1, configName);

        int count = cases.getMetadataInfo().getTotalCount();
        LOGGER.info("Case count: {}", count);

        return count;
    }

    public void importSingleCase(final String caseId, final String configName) {
        LOGGER.info("Initiating import request for case with id : {} [config: {}]", caseId, configName);
        importInProgress = true;

        importThread = new Thread(new Runnable() {
            @Override
            public void run() {
                LOGGER.debug("Import thread started");
                try {
                    LOGGER.debug("Retrieving case info for case with id : {}", caseId);
                    // Fetching the case
                    CaseInfo caseInfo = caseService.getCaseByCaseId(caseId, configName);

                    LOGGER.debug("Retrieved the case");
                    // Sending event for the fetched case
                    CaseEvent caseEvent = CaseEvent.fromCaseInfo(caseInfo, configName);
                    eventRelay.sendEventMessage(caseEvent.toMotechEventWithData());

                    lastImportedCaseId = caseInfo.getCaseId();

                    LOGGER.info("Imported case with ID: {}", caseInfo.getCaseId());
                } catch (RuntimeException e) {
                    LOGGER.error("Error while importing case with id : {} [config: {}]", e, caseId, configName);
                    handleImportError(e, configName);
                }
                LOGGER.info("Case import finished for case with id : {}. ", caseId);

                importInProgress = false;
            }
        });
        LOGGER.debug("Starting import thread");

        importThread.start();
    }

    public void startImport(final Range<DateTime> dateRange, final String configName) {
        validateDateRange(dateRange);

        LOGGER.info("Initiating case import for historical cases from {} to {} [config: {}]",
                dateRange.getMin(), dateRange.getMax(), configName);

        initForImport(dateRange, configName);

        importThread = new Thread(new Runnable() {
            @Override
            public void run() {
                boolean hasMore = false;

                LOGGER.debug("Import thread started");

                do {
                    try {
                        LOGGER.debug("Retrieving cases from page {}, with page size {}",
                                pageCount, fetchSize);
                        // fetching the cases
                        CasesInfo caseList = caseService.getCasesByCasesTimeWithMetadata(dateTimeToString(dateRange.getMin()),
                                dateTimeToString(dateRange.getMax()), fetchSize, pageCount, configName);

                        LOGGER.debug("Retrieved a list of {} cases", caseList.getCaseInfoList().size());

                        // sending events for each case
                        importCaseList(caseList, configName);

                        LOGGER.debug("Imported {} cases", caseList.getCaseInfoList().size());

                        // the first page is the last one
                        hasMore = pageCount > 1;

                        // we decrement the page
                        if (hasMore) {
                            LOGGER.debug("Proceeding to the next batch of cases");
                            pageCount--;
                        }

                    } catch (RuntimeException e) {
                        LOGGER.error("Error while importing cases", e);
                        LOGGER.error("{} of {} cases imported.", importCount, totalCount);
                        handleImportError(e, configName);
                    }
                } while (importInProgress && hasMore);

                LOGGER.info("Case import finished. {} of {} cases imported. ", importCount, totalCount);

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
                throw new IllegalStateException("Interrupted while stopping case import", e);
            }
        }

        LOGGER.info("Import stopped");
    }

    @Override
    public boolean isImportInProgress() {
        return importInProgress;
    }

    @Override
    public CaseImportStatus importStatus() {
        LOGGER.debug("Retrieving import status");

        CaseImportStatus status = new CaseImportStatus();

        status.setTotalCases(totalCount);
        status.setCasesImported(importCount);
        status.setLastImportDate(lastImportedDate);
        status.setLastImportCaseId(lastImportedCaseId);
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

    private void importCaseList(CasesInfo caseList, String configName) {
        // iterate backwards
        for (CaseInfo caseInfo : Lists.reverse(caseList.getCaseInfoList())) {
            CaseEvent caseEvent = CaseEvent.fromCaseInfo(caseInfo, configName);
            eventRelay.sendEventMessage(caseEvent.toMotechEventWithData());

            lastImportedCaseId = caseInfo.getCaseId();
            importCount++;

            LOGGER.info("Imported case with ID: {}", caseInfo.getCaseId());
        }
    }

    private void initForImport(Range<DateTime> dateRange, String configName) {
        importInProgress = true;
        importCount = 0;
        lastImportedDate = null;
        lastImportedCaseId = null;
        totalCount = countForImport(dateRange, configName);
        inError = false;
        errorMessage = null;
        // calculate the page number, since we are going backwards
        pageCount = (int) Math.ceil((double) totalCount / fetchSize);

        LOGGER.debug("Initialized for import");
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

    private String dateTimeToString(DateTime dateTime) {
        if (dateTime == null) {
            return null;
        } else {
            DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
            return formatter.print(dateTime);
        }
    }

    private void handleImportError(RuntimeException e, String configName) {
        errorMessage = e.getMessage();
        inError = true;

        // stop import
        importInProgress = false;

        FullFormFailureEvent failureEvent = new FullFormFailureEvent(configName, errorMessage);
        eventRelay.sendEventMessage(failureEvent.toMotechEvent());

        // Trigger a status message in the Admin UI
        String msg = "Error while importing case: " + errorMessage;
        MalformedFormStatusMessageEvent statusMessageEvent = new MalformedFormStatusMessageEvent(msg);
        eventRelay.sendEventMessage(statusMessageEvent.toMotechEvent());
    }
}