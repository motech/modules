package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Represents a single answer for a specific question from a CommCare form.
 */
public class FormSchemaQuestionOptionJson implements Serializable {

    private static final long serialVersionUID = -1212672275414260040L;

    @Expose
    @SerializedName("label")
    private String label;

    @Expose
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
