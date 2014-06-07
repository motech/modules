package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;
import java.util.Map;

@Entity(name = "Form Schema")
public class FormSchemaJson {

    @Field(displayName = "Form Names")
    @SerializedName("name")
    private Map<String, String> formNames;

    @Field(displayName = "Form Questions")
    @SerializedName("questions")
    private List<FormSchemaQuestionJson> questions;

    public Map<String, String> getFormNames() {
        return formNames;
    }

    public void setFormNames(Map<String, String> formNames) {
        this.formNames = formNames;
    }

    public List<FormSchemaQuestionJson> getQuestions() {
        return questions;
    }

    public void setQuestions(List<FormSchemaQuestionJson> questions) {
        this.questions = questions;
    }
}
