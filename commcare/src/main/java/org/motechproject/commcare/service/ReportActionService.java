package org.motechproject.commcare.service;

/**
 * This service is responsible for handling Commcare report actions in tasks.
 */
public interface ReportActionService {

    /**
     * This task action allows to query the Commcare Report UCR API.
     * When the report is queried, is parsed by MOTECH and a Received Report
     * event is raised.
     *
     * @param configName  the name of the configuration used for connecting to CommcareHQ
     * @param reportId  the Id of report
     * @param reportName  the name of report
     * @param urlParsedFilters  parsed filter fields used in requests url
     */
    void queryReport (String configName, String reportId, String reportName, String urlParsedFilters);
}
