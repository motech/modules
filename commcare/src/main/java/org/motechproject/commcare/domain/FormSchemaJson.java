package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a single CommCare form schema.
 */
public class FormSchemaJson implements Serializable {

    private static final long serialVersionUID = 3033023909405645226L;

    @Expose
    @SerializedName("name")
    private Map<String, String> formNames;

    @Expose
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

    /**
     * Gets form name for English locale, or if English locale is not present, then returns the first one found.
     * @return Form name for English locale, or random one, if not found
     */
    public String getFormName() {
        String value = formNames.get("en");

        if (value == null) {
            Set<String> locales = formNames.keySet();
            value = locales.isEmpty() ? "" : formNames.get(locales.iterator().next());
        }

        return value;
    }
}
