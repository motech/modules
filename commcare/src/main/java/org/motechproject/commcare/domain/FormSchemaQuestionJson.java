package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity(name = "Form Schema Question")
public class FormSchemaQuestionJson {

    @Expose
    @Field(displayName = "Question Label")
    @SerializedName("label")
    private String questionLabel;

    @Expose
    @Field(displayName = "Question Repeat")
    @SerializedName("repeat")
    private String questionRepeat;

    @Expose
    @Field(displayName = "Question Tag")
    @SerializedName("tag")
    private String questionTag;

    @Expose
    @Field(displayName = "Question Value")
    @SerializedName("value")
    private String questionValue;

    @Expose
    @Field(displayName = "Form Question Options")
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
