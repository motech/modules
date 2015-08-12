package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Wrapper class for storing list of instances of the {@link CommcareApplicationJson} class and their metadata. It's a
 * class representing data as sent from the CommCareHQ server.
 */
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
