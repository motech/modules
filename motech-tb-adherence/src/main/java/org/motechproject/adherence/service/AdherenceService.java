package org.motechproject.adherence.service;

import org.joda.time.LocalDate;
import org.motechproject.adherence.domain.DosageLog;
import org.motechproject.adherence.domain.DosageSummary;
import org.motechproject.adherence.repository.AllDosageLogs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AdherenceService {

    private AllDosageLogs allDosageLogs;

    @Autowired
    public AdherenceService(AllDosageLogs allDosageLogs) {
        this.allDosageLogs = allDosageLogs;
    }

    public DosageLog recordAdherence(String patientId, String treatmentCourseId, LocalDate dosageDate, int doseTakenCount, int idealDoseCount, Map<String, String> metaData) {
        DosageLog dosageLog = new DosageLog(patientId, treatmentCourseId, dosageDate, doseTakenCount, idealDoseCount, metaData);
        allDosageLogs.add(dosageLog);
        return dosageLog;
    }

    public List<DosageLog> getDosageLogs(String patientId, String treatmentCourseId, LocalDate fromDate, LocalDate toDate) {
        return allDosageLogs.getAllBy(patientId, treatmentCourseId, fromDate, toDate);
    }

    public List<DosageLog> getDosageLogs(LocalDate fromDate, LocalDate toDate) {
        return allDosageLogs.getAllInDateRange(fromDate, toDate);
    }

    public DosageSummary getPatientDosageSummary(String patientId, String treatmentCourseId, LocalDate fromDate, LocalDate toDate) {
        return allDosageLogs.getPatientDosageSummary(patientId, treatmentCourseId, fromDate, toDate);
    }

}
