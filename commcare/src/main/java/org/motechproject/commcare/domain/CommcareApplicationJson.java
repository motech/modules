package org.motechproject.commcare.domain;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import org.apache.commons.lang.StringEscapeUtils;
import org.motechproject.commons.api.json.MotechJsonReader;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.event.CrudEventType;
import org.motechproject.mds.util.SecurityMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jdo.annotations.NotPersistent;
import javax.jdo.annotations.Persistent;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single CommCare application. CommCare application is a complete CommCare executable that gets downloaded
 * and installed on a phone. This is a schema representation of such an application. It's part of the MOTECH model.
 */
@Entity(name = "Commcare Application")
@CrudEvents(CrudEventType.NONE)
@Access(value = SecurityMode.PERMISSIONS, members = {"manageCommcare"})
public class CommcareApplicationJson {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareApplicationJson.class);

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

    @Field(displayName = "Source configuration")
    private String configName;

    public CommcareApplicationJson() {
        this(null, null, null, null);
    }

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

    public final void serializeModules() {
        if (modules != null) {
            Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
            serializedModules = StringEscapeUtils.escapeJava(gson.toJson(modules));
        }
    }

    private void deserializeModules() {
        if (serializedModules != null) {
            Type deserializeType = new TypeToken<List<CommcareModuleJson>>() { } .getType();
            MotechJsonReader motechJsonReader = new MotechJsonReader();
            try {
                modules = (List<CommcareModuleJson>) motechJsonReader.readFromStringOnlyExposeAnnotations(StringEscapeUtils.unescapeJava(serializedModules), deserializeType);
            } catch (JsonParseException e) {
                LOGGER.error("Failed to deserialize CommCare schema from its JSON representation in the database. Fix the errors in the schema or force Commcare module to download the fresh schema.", e);
                modules = Collections.emptyList();
            }
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

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String domain) {
        this.configName = domain;
    }
}
