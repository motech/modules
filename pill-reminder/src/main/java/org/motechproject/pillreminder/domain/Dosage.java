package org.motechproject.pillreminder.domain;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.Ignore;
import org.motechproject.mds.event.CrudEventType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The domain object representing a medicine dosage within a pill regimen.
 */
@Entity
@CrudEvents(CrudEventType.NONE)
public class Dosage {

    /**
     * The unique dosage ID.
     */
    @Field
    private Long id;

    /**
     * The time of day at which the dosage should be taken.
     */
    @Field(displayName = "Dosage Time")
    private Time dosageTime;

    /**
     * The last captured date when the patient reported taking this dosage.
     */
    @Field(displayName = "Last Response Captured Date")
    private LocalDate responseLastCapturedDate;

    /**
     * All the medicines that should be taken as part of this dosage.
     */
    @Field(displayName = "Medicines")
    @Cascade(delete = true)
    private Set<Medicine> medicines;

    /**
     * Constructs an instance without setting any fields.
     */
    public Dosage() {
    }

    /**
     * Creates a dosage using a medicine and a time of day for taking the medicines.
     * @param dosageTime the time of day at which this dosage should be taken
     * @param medicines all medicines that are part of this dosage
     */
    public Dosage(Time dosageTime, Set<Medicine> medicines) {
        this.dosageTime = dosageTime;
        this.medicines = medicines;
    }

    /**
     * @return all medicines that are part of this dosage
     */
    public Set<Medicine> getMedicines() {
        return medicines;
    }

    /**
     * @param medicines all medicines that are part of this dosage
     */
    public void setMedicines(Set<Medicine> medicines) {
        this.medicines = medicines;
    }

    /**
     * @return the time of day at which this dosage should be taken
     */
    public Time getDosageTime() {
        return dosageTime;
    }

    /**
     * @param dosageTime the time of day at which this dosage should be taken
     */
    public void setDosageTime(Time dosageTime) {
        this.dosageTime = dosageTime;
    }

    /**
     * @return the unique dosage identifier
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the unique dosage identifier
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the last captured date when the patient reported taking this dosage
     */
    public LocalDate getResponseLastCapturedDate() {
        return responseLastCapturedDate;
    }

    /**
     * @param responseLastCapturedDate the last captured date when the patient reported taking this dosage
     */
    public void setResponseLastCapturedDate(LocalDate responseLastCapturedDate) {
        this.responseLastCapturedDate = responseLastCapturedDate;
    }

    /**
     * Update this dosage with a new response date from the patient.
     * If the provided date is null or before the currently set last capture date, it will be ignored.
     * @param lastCapturedDate the date on which the response was captured
     */
    public void updateResponseLastCapturedDate(LocalDate lastCapturedDate) {
        if (responseLastCapturedDate == null || responseLastCapturedDate.isBefore(lastCapturedDate)) {
            responseLastCapturedDate = lastCapturedDate;
        }
    }

    /**
     * Checks if there is a patient response captured for the last dosage time.
     * This will check if the response was made today or yesterday, provided it's still before today's dosage time.
     * @return true if the response was captured, false otherwise
     */
    @Ignore
    public boolean isTodaysDosageResponseCaptured() {
        LocalDate today = DateUtil.today();
        LocalDate yesterday = today.minusDays(1);
        LocalTime localNow = DateUtil.now().toLocalTime();

        if (responseLastCapturedDate == null) {
            return false;
        } else if (responseLastCapturedDate.equals(today)) {
            return true;
        }

        return responseLastCapturedDate.equals(yesterday) && new Time(localNow.getHourOfDay(), localNow.getMinuteOfHour()).isBefore(dosageTime);
    }

    /**
     * Gets the start date for taking this dosage. It will check all the medicines for the earliest start date.
     * @return the start date for taking this dosage
     */
    @Ignore
    public LocalDate getStartDate() {
        List<Medicine> sortedList = new ArrayList<>(medicines);
        Collections.sort(sortedList, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                return o1.getStartDate().compareTo(o2.getStartDate());
            }
        });
        return sortedList.isEmpty() ? null : sortedList.get(0).getStartDate();
    }

    /**
     * Gets the end date for taking this dosage. It will check all the medicines for the latest end date.
     * @return the end date for taking this dosage, or null if there are no medicines with end dates
     */
    @Ignore
    public LocalDate getEndDate() {
        Set<Medicine> medicinesWithNonNullEndDate = getMedicinesWithNonNullEndDate();
        if (medicinesWithNonNullEndDate.isEmpty()) {
            return null;
        }

        List<Medicine> sortedList = new ArrayList<>(medicinesWithNonNullEndDate);
        Collections.sort(sortedList, new Comparator<Medicine>() {
            @Override
            public int compare(Medicine o1, Medicine o2) {
                return o2.getEndDate().compareTo(o1.getEndDate());
            }
        });
        return sortedList.isEmpty() ? null : sortedList.get(0).getEndDate();
    }

    /**
     * Returns medicines with non null end dates.
     * @return a set with medicines with end dates, never null
     */
    @Ignore
    private Set<Medicine> getMedicinesWithNonNullEndDate() {
        Set<Medicine> medicinesWithNonNullEndDate = new HashSet<>();
        for (Medicine medicine : medicines) {
            if (medicine.getEndDate() != null) {
                medicinesWithNonNullEndDate.add(medicine);
            }
        }
        return medicinesWithNonNullEndDate;
    }

    /**
     * Validates this dosage by calling {@link Medicine#validate()} on all medicines for this dosage.
     * @throws ValidationException if one of the medicines fails validation
     */
    public void validate() {
        for (Medicine medicine : getMedicines()) {
            medicine.validate();
        }
    }

    /**
     * Returns a datetime for the dosage that should be taken today, by combining the today date and the dosage
     * time.
     * @return a datetime for the dosage of today
     */
    public DateTime todaysDosageTime() {
        return DateUtil.newDateTime(DateUtil.today(), dosageTime);
    }
}
