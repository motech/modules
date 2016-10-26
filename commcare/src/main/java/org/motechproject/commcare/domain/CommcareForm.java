package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a single CommCare form. Form is a series of questions and prompts that the user views sequentially on the
 * phone. This is pretty much the core content of CommCare applications. When users submit data, they submit completed
 * forms. Forms are based on the XForms standard.
 */
public class CommcareForm {

    private FormValueElement form;

    private String id;

    private String md5;

    private Map<String, MetadataValue> metadata = new HashMap<>();

    @SerializedName("received_on")
    private String receivedOn;

    @SerializedName("resource_uri")
    private String resourceUri;

    @SerializedName("app_id")
    private String appId;

    private String type;

    private String uiversion;

    private String version;

    private String configName;

    private boolean archived;

    public FormValueElement getForm() {
        return form;
    }

    public void setForm(FormValueElement form) {
        this.form = form;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public Map<String, MetadataValue> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, MetadataValue> metadata) {
        this.metadata = metadata;
    }

    public String getReceivedOn() {
        return receivedOn;
    }

    public void setReceivedOn(String receivedOn) {
        this.receivedOn = receivedOn;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUiversion() {
        return uiversion;
    }

    public void setUiversion(String uiversion) {
        this.uiversion = uiversion;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}
