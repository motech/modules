package org.motechproject.commcare.domain.report;

import org.motechproject.commcare.domain.CommcareMetadataInfo;

import java.util.List;

/**
 * Wrapper class for storing list of instances of the {@link ReportInfo} class. It's a part of the MOTECH model.
 */
public class ReportsInfo {

    private List<ReportInfo> reportInfoList;
    private CommcareMetadataInfo metadataInfo;

    public ReportsInfo(List<ReportInfo> reportsInfoList, CommcareMetadataInfo metadataInfo) {
        this.reportInfoList = reportsInfoList;
        this.metadataInfo = metadataInfo;
    }

    public List<ReportInfo> getReportInfoList() {
        return reportInfoList;
    }

    public void setReportInfoList(List<ReportInfo> reportInfoList) {
        this.reportInfoList = reportInfoList;
    }

    public CommcareMetadataInfo getMetadataInfo() {
        return metadataInfo;
    }

    public void setMetadataInfo(CommcareMetadataInfo metadataInfo) {
        this.metadataInfo = metadataInfo;
    }
}