package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

/**
 * Represents a single CommCare module. A CommCare module is a set of forms related to one topic area. A single CommCare
 * application can contain multiple modules. It's part of the MOTECH model.
 */
public class CommcareModuleJson implements Serializable {

    private static final long serialVersionUID = 4408034863223848508L;

    @Expose
    @SerializedName("case_properties")
    private List<String> caseProperties;

    @Expose
    @SerializedName("case_type")
    private String caseType;

    @Expose
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
