package org.motechproject.adherence.contract;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

public class RecordAdherenceRequest {

    String externalId;
    String treatmentId;
    LocalDate doseDate;
    int status;
    Map<String, Object> meta;

    public RecordAdherenceRequest(String externalId, String treatmentId, LocalDate doseDate) {
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        this.doseDate = doseDate;
        meta = new HashMap<String, Object>();
    }

    public RecordAdherenceRequest status(int status) {
        this.status = status;
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

    public int status() {
        return status;
    }

}