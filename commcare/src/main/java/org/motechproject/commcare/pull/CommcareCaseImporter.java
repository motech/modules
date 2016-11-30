package org.motechproject.commcare.pull;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;

/**
 * The interface for the class managing case imports. The import
 * can be based on a caseId or a date range. All cases retrieved from Commcare that match
 * the provided caseId or date range will be treated as if they would just arrived through
 * the case forwarding mechanism - an event will be published for each case.
 * In case of failures, the import will be stopped, events for failure will also be fired,
 * same as in the case forwarding controller. The import is done asynchronously in a separate thread,
 * so this interface also allows checking the status of an ongoing import.
 */
public interface CommcareCaseImporter {

    /**
     * Counts how many cases match the provided criteria. A single request to the case list
     * API will be made underneath, and the returned total count will be returned by this method.
     * @param dateRange the date range for the import
     * @param configName the name of the configuration to use, if null is provided the default configuration
     *                   will be used
     * @return the total number of cases matching the provided criteria
     * @throws IllegalArgumentException if the date range is null or invalid (start date after end date)
     * @throws IllegalStateException if there is an already ongoing import in progress
     */
    int countForImport(Range<DateTime> dateRange, String configName);

    /**
     * Initiates asynchronous import for the provided caseId. Cases will be fetched from Commcare by doing HTTP
     * request to the case list API. The import will run in a separate thread. An event will be fired for the
     * successfully imported case. If an error occurs import will be stopped.
     * @param caseId the uuid of the case to be imported.
     * @param configName the name of the configuration to use, if null is provided the default configuration
     *                   will be used
     */
    void importSingleCase(final String caseId, final String configName);

    /**
     * Initiates asynchronous import for the provided criteria. Cases will be fetched from Commcare by doing HTTP
     * request to the case list API. The default fetch size per request is 100, but that can be controlled using the
     * {@link #setFetchSize(int)} method. The import will run in a separate thread. An event will be fired for each
     * successfully imported case. If an error occurs, the import will be stopped.
     * @param dateRange the date range for the import
     * @param configName the name of the configuration to use, if null is provided the default configuration
     *                   will be used
     * @throws IllegalArgumentException if the date range is null or invalid (start date after end date)
     * @throws IllegalStateException if there is an already ongoing import in progress
     */
    void startImport(final Range<DateTime> dateRange, final String configName);

    /**
     * Stops the ongoing import. The effect is not guaranteed to be immediate.
     */
    void stopImport();

    /**
     * Checks whether a case with the given id exists.
     * @param caseId the uuid of the case to be imported.
     * @param configName the name of the configuration to use, if null is provided the default configuration
     *                   will be used
     * @return boolean value
     */
    boolean checkCaseIdForImport(final String caseId, final String configName);

    /**
     * Checks whether there is an ongoing import in progress.
     * @return true if there is an ongoing import, false otherwise
     */
    boolean isImportInProgress();

    /**
     * Retrieves an {@link CaseImportStatus} object representing the status of the current, or
     * the last import. {@link CaseImportStatus#isImportInProgress()} can be used to check if the import is ongoing,
     * {@link CaseImportStatus#isError()} can be checked to see if the import failed. The received_on date for the
     * last successfully imported case can be retrieved using {@link CaseImportStatus#getLastImportDate()}.
     * @return the status object for the current or last import
     */
    CaseImportStatus importStatus();

    /**
     * Sets the fetch size for the queries to the Commcare case list API. In other words, sets how many cases
     * will be retrieved per request to the API. The default is 100. Setting a too large number might cause memory
     * issues, setting a too low number might result in a large number of HTTP requests.
     * @param fetchSize the fetch size for requests
     */
    void setFetchSize(int fetchSize);
}
