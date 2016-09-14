package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.report.ReportDataInfo;


/**
 * This service is responsible for Interacting with CommCareHQ's programmatic Report Data APIs and uploading JSON data
 * from single report.
 */

public interface CommcareReportDataService {

    /**
     * Query CommCareHQ for a report by its report id.
     *
     * @param reportId  the id of the report on CommCareHQ
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  the ReportDataInfo object representing the state of the report data or null if that report does not exist.
     */
    ReportDataInfo getReportByReportId(String reportId, String configName);

    /**
     * Same as {@link #getReportByReportId(String, String) getReportByReportId} but uses default Commcare configuration.
     */
    ReportDataInfo getReportByReportId(String reportId);

}
