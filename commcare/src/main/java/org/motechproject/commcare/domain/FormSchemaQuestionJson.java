package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a single question from a CommCare form.
 */
public class FormSchemaQuestionJson implements Serializable {
    private static final long serialVersionUID = -2133822667622624721L;

    @Expose
    @SerializedName("label")
    private String questionLabel;

    @Expose
    @SerializedName("repeat")
    private String questionRepeat;

    @Expose
    @SerializedName("tag")
    private String questionTag;

    @Expose
    @SerializedName("value")
    private String questionValue;

    @Expose
    @SerializedName("options")
    private List<FormSchemaQuestionOptionJson> options;

    public String getQuestionLabel() {
        return questionLabel;
    }

    public void setQuestionLabel(String questionLabel) {
        this.questionLabel = questionLabel;
    }

    public String getQuestionRepeat() {
        return questionRepeat;
    }

    public void setQuestionRepeat(String questionRepeat) {
        this.questionRepeat = questionRepeat;
    }

    public String getQuestionTag() {
        return questionTag;
    }

    public void setQuestionTag(String questionTag) {
        this.questionTag = questionTag;
    }

    public String getQuestionValue() {
        return questionValue;
    }

    public void setQuestionValue(String questionValue) {
        this.questionValue = questionValue;
    }

    public List<FormSchemaQuestionOptionJson> getOptions() {
        return options;
    }

    public void setOptions(List<FormSchemaQuestionOptionJson> options) {
        this.options = options;
    }
}