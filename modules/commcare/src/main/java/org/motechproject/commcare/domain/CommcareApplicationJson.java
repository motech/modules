package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CommcareApplicationJson {

    @SerializedName("name")
    private String applicationName;
    @SerializedName("resource_uri")
    private String resourceUri;
    @SerializedName("modules")
    private List<CommcareModuleJson> modules;

    public CommcareApplicationJson(String applicationName, String resourceUri, List<CommcareModuleJson> modules) {
        this.applicationName = applicationName;
        this.resourceUri = resourceUri;
        this.modules = modules;
    }

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getResourceUri() {
        return resourceUri;
    }

    public void setResourceUri(String resourceUri) {
        this.resourceUri = resourceUri;
    }

    public List<CommcareModuleJson> getModules() {
        return modules;
    }

    public void setModules(List<CommcareModuleJson> modules) {
        this.modules = modules;
    }
}
