package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.report.ReportDataInfo;


/**
 * Responsible for interacting with CommCareHQ's Report Data APIs and uploading JSON data
 * from a single report.
 */
public interface CommcareReportService {

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
