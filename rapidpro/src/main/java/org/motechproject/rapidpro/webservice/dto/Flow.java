package org.motechproject.rapidpro.webservice.dto;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

/**
 * Representation of a Rapidpro flow.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Flow implements Serializable {
    private static final long serialVersionUID = 1L;

    private UUID uuid;
    private String name;
    private boolean archived;
    private List<String> labels;
    @JsonProperty("created_on")
    private DateTime createdOn;
    private int expires;
    private int runs;
    @JsonProperty("completed_runs")
    private int completedRuns;
    @JsonProperty("rulesets")
    private List<RuleSet>ruleSets;

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isArchived() {
        return archived;
    }

    public void setArchived(boolean archived) {
        this.archived = archived;
    }

    public List<String> getLabels() {
        return labels;
    }

    public void setLabels(List<String> labels) {
        this.labels = labels;
    }

    public DateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(DateTime createdOn) {
        this.createdOn = createdOn;
    }

    public int getExpires() {
        return expires;
    }

    public void setExpires(int expires) {
        this.expires = expires;
    }

    public int getRuns() {
        return runs;
    }

    public void setRuns(int runs) {
        this.runs = runs;
    }

    public int getCompletedRuns() {
        return completedRuns;
    }

    public void setCompletedRuns(int completedRuns) {
        this.completedRuns = completedRuns;
    }

    public List<RuleSet> getRuleSets() {
        return ruleSets;
    }

    public void setRuleSets(List<RuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    @Override
    public String toString() {
        return "Flow{" +
                "uuid=" + uuid +
                ", name='" + name + '\'' +
                ", archived=" + archived +
                ", labels=" + labels +
                ", createdOn=" + createdOn +
                ", expires=" + expires +
                ", runs=" + runs +
                ", completedRuns=" + completedRuns +
                ", ruleSets=" + ruleSets +
                '}';
    }
}
