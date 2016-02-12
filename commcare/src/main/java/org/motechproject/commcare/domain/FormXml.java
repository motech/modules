package org.motechproject.commcare.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents an XML of a Commcare form, that is sent to the Commcare server via
 * Commcare submission API.
 */
public class FormXml {

    private List<FormValueElement> formFields = new ArrayList<>();
    private Map<String, MetadataValue> metadata = new HashMap<>();
    private String name;
    private String version;
    private String uiversion;
    private String xmlns;

    public void addFormField(FormValueElement field) {
        formFields.add(field);
    }

    public List<FormValueElement> getFormFields() {
        return formFields;
    }

    public void setFormFields(List<FormValueElement> formFields) {
        this.formFields = formFields;
    }

    public Map<String, MetadataValue> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, MetadataValue> metadata) {
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getUiversion() {
        return uiversion;
    }

    public void setUiversion(String uiversion) {
        this.uiversion = uiversion;
    }

    public String getXmlns() {
        return xmlns;
    }

    public void setXmlns(String xmlns) {
        this.xmlns = xmlns;
    }
}
