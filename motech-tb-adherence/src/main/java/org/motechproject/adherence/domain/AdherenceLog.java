package org.motechproject.adherence.domain;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.LocalDate;
import org.motechproject.model.MotechBaseDataObject;

import java.util.Map;

@TypeDiscriminator("doc.type === 'AdherenceLog'")
public class AdherenceLog extends MotechBaseDataObject {

    @JsonProperty
    private String externalId;
    @JsonProperty
    private String treatmentId;
    @JsonProperty
    private LocalDate doseDate;
    @JsonProperty
    private int doseTaken;
    @JsonProperty
    private int idealDoses;
    @JsonProperty
    private Map<String, Object> meta;

    public AdherenceLog() {
        super();
    }

    public AdherenceLog(String externalId, String treatmentId, LocalDate doseDate) {
        super();
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        this.doseDate = doseDate;
    }

    public AdherenceLog doseCounts(int dosesTaken, int dosesMissed) {
        this.doseTaken = dosesTaken;
        this.idealDoses = dosesTaken + dosesMissed;
        return this;
    }

    public AdherenceLog meta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public String externalId() {
        return externalId;
    }

    public String treatmentId() {
        return treatmentId;
    }

    public LocalDate doseDate() {
        return doseDate;
    }

    public int doseTaken() {
        return doseTaken;
    }

    public int doseMissed() {
        return idealDoses - doseTaken;
    }

    public int idealDoses() {
        return idealDoses;
    }

    public Map<String, Object> meta() {
        return meta;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AdherenceLog that = (AdherenceLog) o;

        if (doseDate != null ? !doseDate.equals(that.doseDate) : that.doseDate != null) return false;
        if (externalId != null ? !externalId.equals(that.externalId) : that.externalId != null) return false;
        if (treatmentId != null ? !treatmentId.equals(that.treatmentId) : that.treatmentId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = externalId != null ? externalId.hashCode() : 0;
        result = 31 * result + (treatmentId != null ? treatmentId.hashCode() : 0);
        result = 31 * result + (doseDate != null ? doseDate.hashCode() : 0);
        return result;
    }
}
