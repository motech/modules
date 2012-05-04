package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.adherence.contract.AdherenceSummary;
import org.motechproject.adherence.contract.RecordAdherenceRequest;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherenceService {

    private AllAdherenceLogs allAdherenceLogs;

    @Autowired
    public AdherenceService(AllAdherenceLogs allAdherenceLogs) {
        this.allAdherenceLogs = allAdherenceLogs;
    }

    public void recordAdherence(RecordAdherenceRequest... requests) {
        for (RecordAdherenceRequest request : requests) {
            AdherenceLog adherenceLog = new AdherenceLog(request.externalId(), request.treatmentId(), request.doseDate());
            adherenceLog.doseCounts(request.dosesTaken(), request.dosesMissed());
            adherenceLog.meta(request.meta());
            allAdherenceLogs.add(adherenceLog);
        }
    }

    public AdherenceSummary adherenceAsOf(String externalId, String treatmentId, LocalDate asOf) {
        List<AdherenceLog> adherenceLogs = allAdherenceLogs.findLogsBy(externalId, treatmentId, asOf);
        return new AdherenceSummary(externalId, treatmentId, asOf, adherenceLogs);
    }

    public AdherenceRecords adherenceRecords(String externalId, String treatmentId, LocalDate asOf) {
        List<AdherenceLog> adherenceLogs = allAdherenceLogs.findLogsBy(externalId, treatmentId, asOf);
        return new AdherenceRecords(externalId, treatmentId, adherenceLogs);
    }

    public AdherenceRecords adherenceRecords(String externalId, String treatmentId, LocalDate fromDate, LocalDate toDate) {
        List<AdherenceLog> adherenceLogs = allAdherenceLogs.findLogsBy(externalId, treatmentId, fromDate, toDate);
        return new AdherenceRecords(externalId, treatmentId, adherenceLogs);
    }
}
