package org.motechproject.rapidpro.webservice.dto;

import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

import java.util.UUID;

/**
 * Describes the current state of each node in a Rapidpro flow Run.
 */
public class Step {
    private UUID node;
    @JsonProperty("left_on")
    private DateTime leftOn;
    private String text;
    private String value;
    @JsonProperty("arrived_on")
    private DateTime arrivedOn;
    private String type;

    public UUID getNode() {
        return node;
    }

    public void setNode(UUID node) {
        this.node = node;
    }

    public DateTime getLeftOn() {
        return leftOn;
    }

    public void setLeftOn(DateTime leftOn) {
        this.leftOn = leftOn;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DateTime getArrivedOn() {
        return arrivedOn;
    }

    public void setArrivedOn(DateTime arrivedOn) {
        this.arrivedOn = arrivedOn;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Step{" +
                "node=" + node +
                ", leftOn=" + leftOn +
                ", text='" + text + '\'' +
                ", value='" + value + '\'' +
                ", arrivedOn=" + arrivedOn +
                ", type='" + type + '\'' +
                '}';
    }
}
