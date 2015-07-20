package org.motechproject.pillreminder.domain;

import org.joda.time.LocalDate;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

/**
 * The domain object representing a single medicine in a dosage that is part of a pill regimen.
 */
@Entity
@CrudEvents(CrudEventType.NONE)
public class Medicine {

    /**
     * Error message for medicine validation.
     */
    public static final String MEDICINE_END_DATE_CANNOT_BE_BEFORE_START_DATE =
            "Medicine end-date cannot be before start-date";

    /**
     * The name of the medicine.
     */
    @Field(displayName = "Name")
    private String name;

    /**
     * The start date of taking the medicine.
     */
    @Field(displayName = "Start Date")
    private LocalDate startDate;

    /**
     * The end date for this medicine, null mean no end.
     */
    @Field(displayName = "End Date")
    private LocalDate endDate;

    /**
     * Constructs an instance without setting any fields.
     */
    public Medicine() {
    }

    /**
     * Creates a medicine instance using the provided data.
     * @param name the name of the medicine
     * @param startDate the start date for taking the medicine
     * @param endDate the end date for taking this medicine, null means never-ending
     */
    public Medicine(String name, LocalDate startDate, LocalDate endDate) {
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
     * @param name the name of the medicine
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the start date for taking the medicine
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the start date for taking the medicine
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the end date for taking the medicine, null means never-ending
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the end date for taking the medicine, null means never-ending
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * Validates this medicine, by checking if the start date is not after the end date.
     * @throws ValidationException if the start date is afeter the end date
     */
    public void validate() {
        if (getEndDate() != null && getStartDate().isAfter(getEndDate())) {
             throw new ValidationException(MEDICINE_END_DATE_CANNOT_BE_BEFORE_START_DATE);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicine)) {
            return false;
        }

        Medicine medicine = (Medicine) o;

        return name.equals(medicine.name);
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }
}
