package org.motechproject.commcare.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Persistent;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

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
    @SerializedName("modules")
    @Ignore
    @NotPersistent
    private List<CommcareModuleJson> modules;

    @Field(displayName = "Commcare Modules", type = "text")
    @Persistent(defaultFetchGroup = "true")
    private String serializedModules;

    public CommcareApplicationJson(String commcareAppId, String applicationName, String resourceUri, List<CommcareModuleJson> modules) {
        this.commcareAppId = commcareAppId;
        this.applicationName = applicationName;
        this.resourceUri = resourceUri;
        setModules(modules);
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
        if (modules != null) {
            return modules;
        } else if (serializedModules != null) {
            deserializeModules();
            return modules;
        }
        return Collections.emptyList();
    }

    public final void setModules(List<CommcareModuleJson> modules) {
        this.modules = modules;
        serializeModules();
    }

    public String getCommcareAppId() {
        return commcareAppId;
    }

    public void setCommcareAppId(String commcareAppId) {
        this.commcareAppId = commcareAppId;
    }

    public String getSerializedModules() {
        if (serializedModules != null) {
            return serializedModules;
        } else if (modules != null) {
            serializeModules();
            return serializedModules;
        }
        return null;
    }

    public void setSerializedModules(String serializedModules) {
        this.serializedModules = serializedModules;
        deserializeModules();
    }

    private void serializeModules() {
        if (modules != null) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            this.serializedModules = gson.toJson(modules);
        }
    }

    private void deserializeModules() {
        if (serializedModules != null) {
            Type deserializeType = new TypeToken<List<CommcareModuleJson>>() { } .getType();
            MotechJsonReader motechJsonReader = new MotechJsonReader();
            this.modules  = (List<CommcareModuleJson>) motechJsonReader.readFromStringOnlyExposeAnnotations(serializedModules, deserializeType);
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(commcareAppId, applicationName, resourceUri, getSerializedModules());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        CommcareApplicationJson other = (CommcareApplicationJson) obj;

        return Objects.equals(this.commcareAppId, other.commcareAppId) &&
                Objects.equals(this.applicationName, other.applicationName) &&
                Objects.equals(this.resourceUri, other.resourceUri) &&
                Objects.equals(this.getSerializedModules(), other.getSerializedModules());
    }
}
