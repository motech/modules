package org.motechproject.adherence.contract;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

public class RecordAdherenceRequest {

    String externalId;
    String treatmentId;
    LocalDate doseDate;
    int dosesTaken;
    int dosesMissed;
    Map<String, Object> meta;

    public RecordAdherenceRequest(String externalId, String treatmentId, LocalDate doseDate) {
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        this.doseDate = doseDate;
        dosesTaken = dosesMissed = 0;
        meta = new HashMap<String, Object>();
    }

    public RecordAdherenceRequest dosesTaken(int dosesTaken) {
        this.dosesTaken = dosesTaken;
        return this;
    }

    public RecordAdherenceRequest dosesMissed(int dosesMissed) {
        this.dosesMissed = dosesMissed;
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

    public LocalDate doseDate() {
        return doseDate;
    }

    public int dosesTaken() {
        return dosesTaken;
    }

    public int dosesMissed() {
        return dosesMissed;
    }
}