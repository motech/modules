package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

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
