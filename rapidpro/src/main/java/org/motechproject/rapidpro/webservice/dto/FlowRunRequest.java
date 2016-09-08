package org.motechproject.rapidpro.webservice.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Contains all of the necessary fields to initiate a flow run request to RapidPro.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class FlowRunRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("flow_uuid")
    private UUID flowUUID;
    private List<UUID> groups;
    private List<UUID> contacts;
    @JsonProperty("restart_participants")
    private boolean restartParticipants;
    private Map<String, String> extra;

    private FlowRunRequest(FlowRunRequestBuilder builder) {
        this.flowUUID = builder.flowUUID;
        this.groups = builder.groups;
        this.contacts = builder.contacts;
        this.restartParticipants = builder.restartParticipants;
        this.extra = builder.extra;
    }

    public UUID getFlowUUID() {
        return flowUUID;
    }

    public void setFlowUUID(UUID flowUUID) {
        this.flowUUID = flowUUID;
    }

    public List<UUID> getGroups() {
        return groups;
    }

    public void setGroups(List<UUID> groups) {
        this.groups = groups;
    }

    public List<UUID> getContacts() {
        return contacts;
    }

    public void setContacts(List<UUID> contacts) {
        this.contacts = contacts;
    }

    public boolean isRestartParticipants() {
        return restartParticipants;
    }

    public void setRestartParticipants(boolean restartParticipants) {
        this.restartParticipants = restartParticipants;
    }

    public Map<String, String> getExtra() {
        return extra;
    }

    public void setExtra(Map<String, String> extra) {
        this.extra = extra;
    }

    @Override
    public String toString() {
        return "FlowRunRequest{" +
                "flowUUID=" + flowUUID +
                ", groups=" + groups +
                ", contacts=" + contacts +
                ", restartParticipants=" + restartParticipants +
                ", extra=" + extra +
                '}';
    }

    public static class FlowRunRequestBuilder {
        private final UUID flowUUID;
        private List<UUID> groups;
        private List<UUID> contacts;
        private boolean restartParticipants;
        private Map<String, String> extra;

        public FlowRunRequestBuilder(UUID flowUUID) {
            this.flowUUID = flowUUID;
            this.groups = new ArrayList<>();
            this.contacts = new ArrayList<>();
            this.extra = new HashMap<>();
        }

        public FlowRunRequestBuilder setGroups(List<UUID> groups) {
            this.groups = groups;
            return this;
        }

        public FlowRunRequestBuilder setContacts(List<UUID> contacts) {
            this.contacts = contacts;
            return this;
        }

        public FlowRunRequestBuilder setRestartParticipants(boolean restartParticipants) {
            this.restartParticipants = restartParticipants;
            return this;
        }

        public FlowRunRequestBuilder setExtra(Map<String, String> extra) {
            this.extra = extra;
            return this;
        }

        public FlowRunRequestBuilder addContact(UUID contact) {
            this.contacts.add(contact);
            return this;
        }

        public FlowRunRequestBuilder addGroup(UUID group) {
            this.groups.add(group);
            return this;
        }

        public FlowRunRequestBuilder addExtraPair(String key, String value) {
            this.extra.put(key, value);
            return this;
        }

        public FlowRunRequest build() {
            return new FlowRunRequest(this);
        }
    }
}
