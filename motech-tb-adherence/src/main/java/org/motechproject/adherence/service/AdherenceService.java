package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.contract.AdherenceRecords;
import org.motechproject.adherence.domain.AdherenceAuditLog;
import org.motechproject.adherence.domain.AdherenceLog;
import org.motechproject.adherence.repository.AllAdherenceAuditLogs;
import org.motechproject.adherence.repository.AllAdherenceLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdherenceService {

    private AllAdherenceLogs allAdherenceLogs;
    private AllAdherenceAuditLogs allAdherenceAuditLogs;

    @Autowired
    public AdherenceService(AllAdherenceLogs allAdherenceLogs, AllAdherenceAuditLogs allAdherenceAuditLogs) {
        this.allAdherenceLogs = allAdherenceLogs;
        this.allAdherenceAuditLogs = allAdherenceAuditLogs;
    }

    public void recordAdherence(String user, String source, AdherenceData... datas) {
        for (AdherenceData data : datas) {
            AdherenceLog adherenceLog = new AdherenceLog(data.externalId(), data.treatmentId(), data.doseDate());
            adherenceLog.status(data.status());
            adherenceLog.meta(data.meta());
            addWithAuditLog(adherenceLog, user, source);
        }
    }

    private void addWithAuditLog(AdherenceLog adherenceLog, String user, String source) {
        AdherenceLog existingLog = allAdherenceLogs.findLogBy(adherenceLog.externalId(), adherenceLog.treatmentId(), adherenceLog.doseDate());

        allAdherenceLogs.add(adherenceLog);

        if (adherenceUpdated(adherenceLog, existingLog)) {
            allAdherenceAuditLogs.add(new AdherenceAuditLog(adherenceLog, user, source));
        }
    }

    private boolean adherenceUpdated(AdherenceLog adherenceLog, AdherenceLog existingLog) {
        return (existingLog == null) || (existingLog != null && existingLog.status() != adherenceLog.status());
    }

    public AdherenceRecords adherenceRecords(String externalId, String treatmentId, LocalDate asOf) {
        List<AdherenceLog> adherenceLogs = allAdherenceLogs.findLogsBy(externalId, treatmentId, asOf);
        return new AdherenceRecords(externalId, treatmentId, adherenceLogs);
    }

    public AdherenceRecords adherenceRecords(String externalId, String treatmentId, LocalDate fromDate, LocalDate toDate) {
        List<AdherenceLog> adherenceLogs = allAdherenceLogs.findLogsBy(externalId, treatmentId, fromDate, toDate);
        return new AdherenceRecords(externalId, treatmentId, adherenceLogs);
    }

    public List<AdherenceData> adherenceLogs(LocalDate asOf, int pageNumber, int pageSize) {
        return allAdherenceLogs.findLogsAsOf(asOf, pageNumber, pageSize);
    }
}
