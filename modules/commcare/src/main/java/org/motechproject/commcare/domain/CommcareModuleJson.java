package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommcareModuleJson {

    @SerializedName("case_properties")
    private List<String> caseProperties;
    @SerializedName("case_type")
    private String caseType;
    @SerializedName("forms")
    private List<FormSchemaJson> formSchemas;

    public List<String> getCaseProperties() {
        return caseProperties;
    }

    public void setCaseProperties(List<String> caseProperties) {
        this.caseProperties = caseProperties;
    }

    public String getCaseType() {
        return caseType;
    }

    public void setCaseType(String caseType) {
        this.caseType = caseType;
    }

    public List<FormSchemaJson> getFormSchemas() {
        return formSchemas;
    }

    public void setFormSchemas(List<FormSchemaJson> formSchemas) {
        this.formSchemas = formSchemas;
    }
}
