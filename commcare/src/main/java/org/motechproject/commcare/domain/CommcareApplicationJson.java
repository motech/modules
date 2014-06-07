package org.motechproject.commcare.domain;

import com.google.gson.annotations.SerializedName;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity(name = "Commcare Application")
public class CommcareApplicationJson {

    @Field(displayName = "Application Name")
    @SerializedName("name")
    private String applicationName;

    @Field(displayName = "Resource URI")
    @SerializedName("resource_uri")
    private String resourceUri;

    @Field(displayName = "Commcare Modules")
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
