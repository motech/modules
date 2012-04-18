package org.motechproject.adherence.domain;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.model.MotechBaseDataObject;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * Captures Adherence over a time period
 * </p>
 * <p>
 * Adherence over given period is considered to be uniform. ie, adherence is the same at any point of
 * time over the time period.
 * </p>
 */
@TypeDiscriminator("doc.type === 'AdherenceLog'")
public class AdherenceLog extends MotechBaseDataObject {

    /*The id of the entity for whom adherence is recorded*/
    @JsonProperty
    protected String externalId;
    /*The id of the phenomenon against which adherence is recorded*/
    @JsonProperty
    protected String referenceId;

    @JsonProperty
    protected DateTime from;
    @JsonProperty
    protected DateTime to;

    @JsonProperty
    protected int dosesTaken;
    @JsonProperty
    protected int totalDoses;

    @JsonProperty
    protected List<String> tags;

    private AdherenceLog() {
        this.dosesTaken = 0;
        this.totalDoses = 0;
        this.tags = new ArrayList<String>();
    }

    public AdherenceLog(String externalId, DateTime from, DateTime to) {
        this();
        this.externalId = externalId;
        this.from = from.withSecondOfMinute(0).withMillisOfSecond(0);
        this.to = to.withSecondOfMinute(0).withMillisOfSecond(0);
    }

    public String externalId() {
        return externalId;
    }

    public String referenceId() {
        return referenceId;
    }

    @JsonIgnore
    public boolean isAdherenceOf(String externalId, String referenceId) {
        return this.externalId.equals(externalId) && this.referenceId.equals(referenceId);
    }

    @JsonIgnore
    public boolean covers(DateTime pointInTime) {
        return !this.from.isAfter(pointInTime) && this.to.isBefore(pointInTime);
    }

    public int dosesTaken() {
        return dosesTaken;
    }

    public int totalDoses() {
        return totalDoses;
    }

    public boolean encloses(AdherenceLog otherLog) {
        return !this.to.isBefore(otherLog.to);
    }

    public AdherenceLog alignWith(AdherenceLog log) {
        this.from = log.to.plusMinutes(1);
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdherenceLog that = (AdherenceLog) o;

        if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;
        if (from != null ? !from.equals(that.from) : that.from != null) return false;
        if (referenceId != null ? !referenceId.equals(that.referenceId) : that.referenceId != null) return false;
        if (to != null ? !to.equals(that.to) : that.to != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = externalId != null ? externalId.hashCode() : 0;
        result = 31 * result + (referenceId != null ? referenceId.hashCode() : 0);
        result = 31 * result + (from != null ? from.hashCode() : 0);
        result = 31 * result + (to != null ? to.hashCode() : 0);
        return result;
    }
}