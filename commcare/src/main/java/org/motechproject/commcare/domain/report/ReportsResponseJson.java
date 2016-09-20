package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;
import org.motechproject.commcare.domain.CommcareMetadataJson;

import java.util.List;

/**
 * Wrapper class for storing list of instances of the {@link ReportJson} class and their metadata. It's part of the
 * CommCareHQ model.
 */
public class ReportsResponseJson {

    @SerializedName("meta")
    private CommcareMetadataJson metadata;

    @SerializedName("objects")
    private List<ReportJson> reports;

    public CommcareMetadataJson getMetadata() {
        return metadata;
    }

    public void setMetadata(CommcareMetadataJson metadata) {
        this.metadata = metadata;
    }

    public List<ReportJson> getReports() {
        return reports;
    }

    public void setReports(List<ReportJson> reports) {
        this.reports = reports;
    }
}