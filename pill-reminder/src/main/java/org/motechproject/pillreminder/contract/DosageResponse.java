package org.motechproject.pillreminder.contract;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;

import java.util.List;

/**
 * Dosage details returned from pill reminder module, represents single dosage detail (morning or evening)
 * Also contains information about last dosage confirmation.
 */
public class DosageResponse {
    private Long dosageId;
    private int dosageHour;
    private int dosageMinute;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate responseLastCapturedDate;
    private List<MedicineResponse> medicines;

    /**
     * Constructs a dosage response.
     * @param dosageId unique dosage identifier
     * @param dosageTime the time of the dosage
     * @param startDate dosage start date, if there are multiple medicines with different start date then first start date will be returned
     * @param endDate dosage end date, if there are multiple medicines with different end date then last ending will be returned
     * @param responseLastCapturedDate last dosage confirmation from subscriber for this dose
     * @param medicines medicines prescribed
     */
    public DosageResponse(Long dosageId, Time dosageTime, LocalDate startDate, LocalDate endDate, LocalDate responseLastCapturedDate, List<MedicineResponse> medicines) {
        this.dosageId = dosageId;
        this.dosageHour = dosageTime.getHour();
        this.dosageMinute = dosageTime.getMinute();
        this.startDate = startDate;
        this.endDate = endDate;
        this.responseLastCapturedDate = responseLastCapturedDate;
        this.medicines = medicines;
    }

    /** Unique dosage identifier */
    public Long getDosageId() {
        return dosageId;
    }

    /** Dosage time -- hour component */
    public int getDosageHour() {
        return dosageHour;
    }

    /** Dosage time -- minute component */
    public int getDosageMinute() {
        return dosageMinute;
    }

    /** Dosage start date, if there are multiple medicines with different start date then first start date will be returned */
    public LocalDate getStartDate() {
        return startDate;
    }

    /** Dosage end date, if there are multiple medicines with different end date then last ending will be returned */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @return Last dosage confirmation from subscriber for this dose.
     */
    public LocalDate getResponseLastCapturedDate() {
        return responseLastCapturedDate;
    }

    /**
     * @return Medicines prescribed
     */
    public List<MedicineResponse> getMedicines() {
        return medicines;
    }
}
