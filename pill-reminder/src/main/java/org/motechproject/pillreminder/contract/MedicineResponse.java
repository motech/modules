package org.motechproject.pillreminder.contract;

import org.joda.time.LocalDate;

/**
 * Medicine details returned from the pill-reminder module.
 * @see DosageResponse
 */
public class MedicineResponse {
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;

    /**
     * Constructs a medicine response.
     * @param name the name of the medicine
     * @param startDate the start date for taking this medicine
     * @param endDate the end date for taking this medicine
     */
    public MedicineResponse(String name, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    /**
     * @return the name of the medicine
     */
    public String getName() {
        return name;
    }

    /**
     * @return the start date for taking this medicine
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @return the end date for taking this medicine
     */
    public LocalDate getEndDate() {
        return endDate;
    }
}
