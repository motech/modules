package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity(name = "Commcare Module")
public class CommcareModuleJson {

    @Field(displayName = "Case Properties")
    @SerializedName("case_properties")
    private List<String> caseProperties;

    @Field(displayName = "Case Type")
    @SerializedName("case_type")
    private String caseType;

    @Field(displayName = "Form Schemas")
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
