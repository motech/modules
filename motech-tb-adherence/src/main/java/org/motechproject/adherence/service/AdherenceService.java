package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.adherence.contract.AdherenceData;
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

    public void recordAdherence(AdherenceData... datas) {
        for (AdherenceData data : datas) {
            AdherenceLog adherenceLog = new AdherenceLog(data.externalId(), data.treatmentId(), data.doseDate());
            adherenceLog.status(data.status());
            adherenceLog.meta(data.meta());
            allAdherenceLogs.add(adherenceLog);
        }
    }

    public AdherenceRecords adherenceRecords(String externalId, String treatmentId, LocalDate asOf) {
        List<AdherenceLog> adherenceLogs = allAdherenceLogs.findLogsBy(externalId, treatmentId, asOf);
        return new AdherenceRecords(externalId, treatmentId, adherenceLogs);
    }

    public AdherenceRecords adherenceRecords(String externalId, String treatmentId, LocalDate fromDate, LocalDate toDate) {
        List<AdherenceLog> adherenceLogs = allAdherenceLogs.findLogsBy(externalId, treatmentId, fromDate, toDate);
        return new AdherenceRecords(externalId, treatmentId, adherenceLogs);
    }

    public List<AdherenceLog> adherenceLogs(LocalDate asOf) {
        return allAdherenceLogs.findLogsAsOf(asOf);
    }
}
