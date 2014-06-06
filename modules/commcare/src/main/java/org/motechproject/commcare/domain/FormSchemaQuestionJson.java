package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FormSchemaQuestionJson {

    @SerializedName("label")
    private String questionLabel;
    @SerializedName("repeat")
    private String questionRepeat;
    @SerializedName("tag")
    private String questionTag;
    @SerializedName("value")
    private String questionValue;
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
