package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

public class FormSchemaJson {

    @SerializedName("name")
    private Map<String, String> formNames;
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
