package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

public class FormSchemaQuestionOptionJson {

    @SerializedName("label")
    private String label;
    @SerializedName("value")
    private String value;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
