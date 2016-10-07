package org.motechproject.commcare.domain.report;

import org.motechproject.commcare.domain.CommcareMetadataInfo;

import java.util.List;
import java.util.Objects;

/**
 * Wrapper class for storing list of instances of the {@link ReportMetadataInfo} class. It's a part of the MOTECH model.
 */
public class ReportsMetadataInfo {

    private List<ReportMetadataInfo> reportMetadataInfoList;
    private CommcareMetadataInfo metadataInfo;

    public ReportsMetadataInfo() { }

    public ReportsMetadataInfo(List<ReportMetadataInfo> reportMetadataInfoList, CommcareMetadataInfo metadataInfo) {
        this.reportMetadataInfoList = reportMetadataInfoList;
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

    @Override
    public int hashCode() {
        return Objects.hash(reportMetadataInfoList, metadataInfo);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ReportsMetadataInfo)) {
            return false;
        }

        ReportsMetadataInfo other = (ReportsMetadataInfo) o;

        return Objects.equals(reportMetadataInfoList, other.reportMetadataInfoList)
                && Objects.equals(metadataInfo, other.metadataInfo);
    }
}
