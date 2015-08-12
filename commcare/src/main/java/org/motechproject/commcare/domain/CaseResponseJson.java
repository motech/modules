package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Wrapper class for storing list of instances of the {@link CaseJson} class and their metadata. It's part of the
 * CommCareHQ model.
 */
public class CaseResponseJson {

    @SerializedName("meta")
    private CommcareMetadataJson metadata;

    @SerializedName("objects")
    private List<CaseJson> cases;

    public CommcareMetadataJson getMetadata() {
        return metadata;
    }

    public List<CaseJson> getCases() {
        return cases;
    }


}
