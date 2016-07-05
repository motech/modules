package org.motechproject.rapidpro.webservice.dto;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * Representation of a Rapidpro Contact.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private String name;
    private String language;
    private List<String> urns;
    @JsonProperty("group_uuids")
    private List<String> groupUUIDs;
    private Map<String, String> fields;
    private boolean blocked;
    private boolean failed;
    @JsonProperty("modified_on")
    private DateTime modifiedOn;
    private String phone;

    public Contact() {
        this.urns = new ArrayList<>();
        this.groupUUIDs = new ArrayList<>();
        this.fields = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public List<String> getUrns() {
        return urns;
    }

    public void setUrns(List<String> urns) {
        if (urns == null) {
            this.urns = new ArrayList<>();
        } else {
            this.urns = urns;
        }
    }

    public List<String> getGroupUUIDs() {
        return this.groupUUIDs;
    }

    public void setGroupUUIDs(List<String> groupUUIDs) {
        if (groupUUIDs == null) {
            this.groupUUIDs = new ArrayList<>();
        } else {
            this.groupUUIDs = groupUUIDs;
        }
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        if (fields == null) {
            this.fields = new HashMap<>();
        } else {
            this.fields = fields;
        }
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public boolean isBlocked() {
        return blocked;
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }

    public boolean isFailed() {
        return failed;
    }

    public void setFailed(boolean failed) {
        this.failed = failed;
    }

    public DateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(DateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public String getPhone() {
        return phone;
    }

    @JsonIgnore
    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "uuid='" + uuid + '\'' +
                ", name='" + name + '\'' +
                ", language='" + language + '\'' +
                ", urns=" + urns +
                ", groupUUIDs=" + groupUUIDs +
                ", fields=" + fields +
                ", blocked=" + blocked +
                ", failed=" + failed +
                ", modifiedOn=" + modifiedOn +
                ", phone='" + phone + '\'' +
                '}';
    }
}
