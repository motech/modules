package org.motechproject.adherence.contract;

import org.joda.time.LocalDate;

import java.util.HashMap;
import java.util.Map;

public class AdherenceData {

    String externalId;
    String treatmentId;
    LocalDate doseDate;
    int status;
    Map<String, Object> meta;

    public AdherenceData(String externalId, String treatmentId, LocalDate doseDate) {
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        this.doseDate = doseDate;
        meta = new HashMap<String, Object>();
    }

    public AdherenceData status(int status) {
        this.status = status;
        return this;
    }

    public Map<String, Object> meta() {
        return meta;
    }

    public AdherenceData meta(Map<String, Object> meta) {
        this.meta = meta;
        return this;
    }

    public AdherenceData addMeta(String key, Object value) {
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