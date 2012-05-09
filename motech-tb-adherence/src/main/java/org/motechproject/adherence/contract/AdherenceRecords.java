package org.motechproject.adherence.contract;

import org.joda.time.LocalDate;
import org.motechproject.adherence.domain.AdherenceLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AdherenceRecords {

    String externalId;
    String treatmentId;
    List<AdherenceRecord> adherenceRecords;

    public AdherenceRecords(String externalId, String treatmentId, List<AdherenceLog> logs) {
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        adherenceRecords(logs);
    }

    private void adherenceRecords(List<AdherenceLog> logs) {
        this.adherenceRecords = new ArrayList<AdherenceRecord>();
        for (AdherenceLog log : logs) {
            adherenceRecords.add(new AdherenceRecord(log.doseDate(), log.status(), log.meta()));
        }
    }

    public List<AdherenceRecord> adherenceRecords() {
        return adherenceRecords;
    }

    public int size() {
        return adherenceRecords.size();
    }

    public static class AdherenceRecord {

        LocalDate recordDate;
        int status;
        Map<String, Object> meta;

        public AdherenceRecord(LocalDate recordDate, int status, Map<String, Object> meta) {
            this.recordDate = recordDate;
            this.status = status;
            this.meta = meta;
        }

        public LocalDate recordDate() {
            return recordDate;
        }

        public int status() {
            return status;
        }

        public Map<String, Object> meta() {
            return meta;
        }

        public Object meta(String key) {
            return meta.get(key);
        }
    }
}
