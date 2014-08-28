package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.List;

@Entity(name = "Commcare Application")
public class CommcareApplicationJson {

    @Expose
    @Field(displayName = "Commcare Application Id")
    @SerializedName("id")
    private String commcareAppId;

    @Expose
    @Field(displayName = "Application Name")
    @SerializedName("name")
    private String applicationName;

    @Expose
    @Field(displayName = "Resource URI")
    @SerializedName("resource_uri")
    private String resourceUri;

    @Expose
    @Field(displayName = "Commcare Modules")
    @SerializedName("modules")
    private List<CommcareModuleJson> modules;

    public CommcareApplicationJson(String commcareAppId, String applicationName, String resourceUri, List<CommcareModuleJson> modules) {
        this.commcareAppId = commcareAppId;
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

    public String getCommcareAppId() {
        return commcareAppId;
    }

    public void setCommcareAppId(String commcareAppId) {
        this.commcareAppId = commcareAppId;
    }
}
