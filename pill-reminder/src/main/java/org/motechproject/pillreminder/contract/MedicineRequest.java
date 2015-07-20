package org.motechproject.pillreminder.contract;

import org.joda.time.LocalDate;

/**
 * MedicineRequest specifies medicine name and prescription effective date range.
 */
public class MedicineRequest {

    /**
     * The name of the medicine.
     */
    private String name;

    /**
     * The start date for taking this medicine.
     */
    private LocalDate startDate;

    /**
     * The end date for taking this medicine.
     */
    private LocalDate endDate;

    /**
     * Constructs a medicine request.
     * @param name the name of the medicine
     * @param startDate the start date for taking this medicine
     * @param endDate the end date for taking this medicine
     */
    public MedicineRequest(String name, LocalDate startDate, LocalDate endDate) {
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
