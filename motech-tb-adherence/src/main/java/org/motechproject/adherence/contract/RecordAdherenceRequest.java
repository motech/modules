package org.motechproject.adherence.contract;

import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.Map;

public class RecordAdherenceRequest {

    String externalId;
    String treatmentId;
    DateTime asOf;
    int dosesTaken;
    int dosesMissed;
    Map<String, Object> meta;

    public RecordAdherenceRequest(String externalId, String treatmentId, DateTime asOf) {
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        this.asOf = asOf;
        dosesTaken = dosesMissed = 0;
        meta = new HashMap<String, Object>();
    }

    public RecordAdherenceRequest dosesTaken(int dosesTaken) {
        this.dosesTaken = dosesTaken;
        this.dosesMissed = dosesTaken;
        return this;
    }

    public RecordAdherenceRequest dosesMissed(int idealDoses) {
        this.dosesMissed += idealDoses;
        return this;
    }

    public Map<String, Object> meta() {
        return meta;
    }

    public RecordAdherenceRequest meta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public RecordAdherenceRequest addMeta(String key, Object value) {
        meta.put(key, value);
        return this;
    }

    public String externalId() {
        return externalId;
    }

    public String treatmentId() {
        return treatmentId;
    }

    public DateTime asOf() {
        return asOf;
    }

    public int dosesTaken() {
        return dosesTaken;
    }

    public int dosesMissed() {
        return dosesMissed;
    }
}