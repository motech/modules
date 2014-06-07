package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AppStructureResponseJson {

    @SerializedName("meta")
    private CommcareMetadataJson metadata;

    @SerializedName("objects")
    private List<CommcareApplicationJson> applications;

    public CommcareMetadataJson getMetadata() {
        return metadata;
    }

    public List<CommcareApplicationJson> getApplications() {
        return applications;
    }
}
