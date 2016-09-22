package org.motechproject.commcare.domain.report;

import org.motechproject.commcare.domain.CommcareMetadataInfo;

import java.util.List;

/**
 * Wrapper class for storing list of instances of the {@link ReportMetadataInfo} class. It's a part of the MOTECH model.
 */
public class ReportsMetadataInfo {

    private List<ReportMetadataInfo> reportMetadataInfoList;
    private CommcareMetadataInfo metadataInfo;

    public ReportsMetadataInfo(List<ReportMetadataInfo> reportsInfoList, CommcareMetadataInfo metadataInfo) {
        this.reportMetadataInfoList = reportsInfoList;
        this.metadataInfo = metadataInfo;
    }

    public List<ReportMetadataInfo> getReportMetadataInfoList() {
        return reportMetadataInfoList;
    }

    public void setReportMetadataInfoList(List<ReportMetadataInfo> reportMetadataInfoList) {
        this.reportMetadataInfoList = reportMetadataInfoList;
    }

    public CommcareMetadataInfo getMetadataInfo() {
        return metadataInfo;
    }

    public void setMetadataInfo(CommcareMetadataInfo metadataInfo) {
        this.metadataInfo = metadataInfo;
    }
}