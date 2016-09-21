package org.motechproject.rapidpro.webservice.dto;


import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Response from RapidPro following a successful flow run request.
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class FlowRunResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    private int run;
    @JsonProperty("flow_uuid")
    private UUID flowUUID;
    private UUID contact;
    @JsonProperty("created_on")
    private DateTime createdOn;
    private List<Map<String, String>> values;
    private List<Step> steps;
    private int flow;
    private boolean completed;
    @JsonProperty("expired_on")
    private DateTime expiredOn;
    @JsonProperty("expires_on")
    private DateTime expiresOn;

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public UUID getFlowUUID() {
        return flowUUID;
    }

    public void setFlowUUID(UUID flowUUID) {
        this.flowUUID = flowUUID;
    }

    public UUID getContact() {
        return contact;
    }

    public void setContact(UUID contact) {
        this.contact = contact;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public List<Map<String, String>> getValues() {
        return values;
    }

    public void setValues(List<Map<String, String>> values) {
        this.values = values;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public int getFlow() {
        return flow;
    }

    public void setFlow(int flow) {
        this.flow = flow;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    public DateTime getExpiredOn() {
        return expiredOn;
    }

    public void setExpiredOn(DateTime expiredOn) {
        this.expiredOn = expiredOn;
    }

    public DateTime getExpiresOn() {
        return expiresOn;
    }

    public void setExpiresOn(DateTime expiresOn) {
        this.expiresOn = expiresOn;
    }

    @Override
    public String toString() {
        return "FlowRunResponse{" +
                "run=" + run +
                ", flowUUID=" + flowUUID +
                ", contact=" + contact +
                ", createdOn=" + createdOn +
                ", values=" + values +
                ", steps=" + steps +
                ", flow=" + flow +
                ", completed=" + completed +
                ", expiredOn=" + expiredOn +
                ", expiresOn=" + expiresOn +
                '}';
    }
}
