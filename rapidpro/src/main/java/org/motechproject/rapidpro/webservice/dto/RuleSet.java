package org.motechproject.rapidpro.webservice.dto;

import org.codehaus.jackson.annotate.JsonProperty;

import java.io.Serializable;
import java.util.UUID;

/**
 * Describes the constraints for each node in a RapidPro flow.
 */
public class RuleSet implements Serializable {
    private static final long serialVersionUID = 1L;

    private int id;
    private UUID node;
    @JsonProperty("ruleset_type")
    private String ruleSetType;
    private String label;
    @JsonProperty("response_type")
    private String responseType;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UUID getNode() {
        return node;
    }

    public void setNode(UUID node) {
        this.node = node;
    }

    public String getRuleSetType() {
        return ruleSetType;
    }

    public void setRuleSetType(String ruleSetType) {
        this.ruleSetType = ruleSetType;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getResponseType() {
        return responseType;
    }

    public void setResponseType(String responseType) {
        this.responseType = responseType;
    }

    @Override
    public String toString() {
        return "RuleSet{" +
                "id=" + id +
                ", node=" + node +
                ", ruleSetType='" + ruleSetType + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
