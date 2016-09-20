package org.motechproject.commcare.service;

import org.motechproject.commcare.domain.report.ReportsInfo;

/**
 *  Responsible for interacting with CommCareHQ's Report Metadata and Data API.
 */
public interface CommcareReportService {

    /**
     * Query CommCareHQ for all reports.
     *
     * @param configName  the name of the configuration used for connecting to CommcareHQ, null means default configuration
     * @return  {@link ReportsInfo} object that contains reports metadata and the list of {@link ReportsInfo} objects representing reports
     *          found on the given CommcareHQ configuration
     */
    ReportsInfo getReportsList(String configName);

    /**
     * Same as {@link #getReportsList(String)} but uses default Commcare configuration.
     */
    ReportsInfo getReportsList();
}