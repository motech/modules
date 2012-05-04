package org.motechproject.adherence.contract;

import org.joda.time.DateTime;
import org.motechproject.adherence.domain.AdherenceLog;

import java.util.List;

public class AdherenceSummary {

    private String externalId;
    private String treatmentId;
    private DateTime asOf;
    private int totalDosesTaken;
    private int totalIdealDoses;

    public AdherenceSummary(String externalId, String treatmentId, DateTime asOf, List<AdherenceLog> logs) {
        this.externalId = externalId;
        this.treatmentId = treatmentId;
        this.asOf = asOf;
        this.totalDosesTaken = sumOfDosesTaken(logs);
        this.totalIdealDoses = sumOfIdealDoses(logs);
    }

    public String patientId() {
        return externalId;
    }

    public String treatmentId() {
        return treatmentId;
    }

    public DateTime asOf() {
        return asOf;
    }

    public int totalDosesTaken() {
        return totalDosesTaken;
    }

    public int totalIdealDoses() {
        return totalIdealDoses;
    }

    private int sumOfDosesTaken(List<AdherenceLog> adherenceLogs) {
        int totalDosesTaken = 0;
        for (AdherenceLog adherenceLog : adherenceLogs) {
            totalDosesTaken += adherenceLog.doseTaken();
        }
        return totalDosesTaken;
    }

    private int sumOfIdealDoses(List<AdherenceLog> adherenceLogs) {
        int totalIdealDoses = 0;
        for (AdherenceLog adherenceLog : adherenceLogs) {
            totalIdealDoses += adherenceLog.idealDoses();
        }
        return totalIdealDoses;
    }

}
