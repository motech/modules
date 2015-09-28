package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a single form stub. It only contains the date at which it was received, the form ID and the related cases
 * IDs.
 */
public class FormStubJson {

    @SerializedName("received_on")
    private String receivedOn;

    @SerializedName("form_id")
    private String formId;

    @SerializedName("case_ids")
    private List<String> caseIds;

    public String getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(String receivedOn) {
        this.receivedOn = receivedOn;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public List<String> getCaseIds() {
        return caseIds;
    }

    public void setCaseIds(List<String> caseIds) {
        this.caseIds = caseIds;
    }
}
