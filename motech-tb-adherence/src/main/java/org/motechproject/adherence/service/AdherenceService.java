package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.AdherenceLogMapper;
import org.motechproject.adherence.contract.AdherenceData;
import org.motechproject.adherence.contract.AdherenceRecords;
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

    public void saveOrUpdateAdherence(List<AdherenceData> data) {
        for (AdherenceData adherenceData : data) {
            AdherenceLog adherenceLog = new AdherenceLog(adherenceData.externalId(), adherenceData.treatmentId(), adherenceData.doseDate());
            adherenceLog.status(adherenceData.status());
            adherenceLog.meta(adherenceData.meta());
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

    public List<AdherenceData> adherenceLogs(LocalDate asOf, int pageNumber, int pageSize) {
        return allAdherenceLogs.findLogsAsOf(asOf, pageNumber, pageSize);
    }

    public void addOrUpdateLogsByDoseDate(List<AdherenceData> adherenceData, String externalId) {
        List<AdherenceLog> adherenceLogs = new AdherenceLogMapper().map(adherenceData);
        allAdherenceLogs.addOrUpdateLogsForExternalIdByDoseDate(adherenceLogs,externalId);
    }

    public List<AdherenceData> findLogsInRange(String externalId, String treatmentId, LocalDate startDate, LocalDate endDate) {
        return allAdherenceLogs.findLogsInRange(externalId,treatmentId,startDate,endDate);
    }
}
