package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppStructureResponseJson {

    @Expose
    @SerializedName("meta")
    private CommcareMetadataJson metadata;

    @Expose
    @SerializedName("objects")
    private List<CommcareApplicationJson> applications;

    public CommcareMetadataJson getMetadata() {
        return metadata;
    }

    public List<CommcareApplicationJson> getApplications() {
        return applications;
    }
}
