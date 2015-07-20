package org.motechproject.pillreminder.domain;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Cascade;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Set;

/**
 * The domain object representing a pill regimen. This is the top level entity in this module.
 */
@Entity
public class PillRegimen {

    /**
     * The unique identifier of the pill regimen.
     */
    @Field
    private Long id;

    /**
     * The external ID tied to this pill regimen, used for tying patients with the regimen.
     */
    @Field(displayName = "External ID")
    private String externalId;

    /**
     * The schedule details for this pill regimen.
     */
    @Field(displayName = "Schedule Details")
    @Cascade(delete = true)
    private DailyScheduleDetails scheduleDetails;

    /**
     * Medicine dosages belonging to this pill regimen. Each dosage can have multiple medicines.
     */
    @Field(displayName = "Dosages")
    @Cascade(delete = true)
    private Set<Dosage> dosages;

    /**
     * Constructs a pill regimen without setting any fields.
     */
    public PillRegimen() {
    }

    /**
     * Constructs a pill regimen.
     * @param externalId the external ID tied to this pill regimen, used for tying patients with the regimen.
     * @param dosages medicine dosages belonging to this pill regimen
     * @param scheduleDetails the schedule details for this pill regimen
     */
    public PillRegimen(String externalId, Set<Dosage> dosages, DailyScheduleDetails scheduleDetails) {
        this.externalId = externalId;
        this.dosages = dosages;
        this.scheduleDetails = scheduleDetails;
    }

    /**
     * @return the external ID tied to this pill regimen, used for tying patients with the regimen
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * @param externalId the external ID tied to this pill regimen, used for tying patients with the regimen
     */
    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    /**
     * Gets medicine dosages belonging to this pill regimen. Each dosage can have multiple medicines.
     * @return medicine dosages belonging to this pill regimen
     */
    public Set<Dosage> getDosages() {
        return dosages;
    }

    /**
     * Sets medicine dosages belonging to this pill regimen. Each dosage can have multiple medicines.
     * @param dosages medicine dosages belonging to this pill regimen
     */
    public void setDosages(Set<Dosage> dosages) {
        this.dosages = dosages;
    }

    /**
     * @return the schedule details for this pill regimen
     */
    public DailyScheduleDetails getScheduleDetails() {
        return scheduleDetails;
    }

    /**
     * @param scheduleDetails the schedule details for this pill regimen
     */
    public void setScheduleDetails(DailyScheduleDetails scheduleDetails) {
        this.scheduleDetails = scheduleDetails;
    }

    /**
     * @return the unique identifier of the pill regimen
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id the unique identifier of the pill regimen
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Validates the dosages for this regimen. Will call {@link Dosage#validate()} on
     * every medicine dosage in this regimen.
     * @throws ValidationException if any of the dosages fails validation
     */
    public void validate() {
        for (Dosage dosage : dosages) {
            dosage.validate();
        }
    }

    /**
     * Retrieves the dosage with given ID belonging to this regimen.
     * @param dosageId the id of the dosage to retrieve
     * @return the matching dosage, or null if this regimen does not have a dosage with such an ID
     */
    public Dosage getDosage(Long dosageId) {
        for (Dosage dosage : dosages) {
            if (dosage.getId().equals(dosageId)) {
                return dosage;
            }
        }
        return null;
    }

    /**
     * Checks if there were no reminders yet set for the given dosage.
     * @param dosage the dosage to check
     * @return true if no reminder was yet set for this dosage, false otherwise
     */
    public boolean isFirstReminderFor(Dosage dosage) {
        return numberOfTimesPillRemindersSentFor(dosage) == 0;
    }

    /**
     * Returns the number of times a pill reminder was set for the given dosage. This is done using only a calculation
     * based on the schedule for this regimen and the details from the dosage.
     * @param dosage the dosage to check
     * @return the number of time a pill reminder was sent for the dosage
     */
    public int numberOfTimesPillRemindersSentFor(Dosage dosage) {
        DailyScheduleDetails details = getScheduleDetails();
        Time dosageStartTime = dosage.getDosageTime();
        int minsSinceDosage = Math.min(getOffsetOfCurrentTimeFromDosageStartTime(dosageStartTime, DateUtil.now()), details.getPillWindowInHours() * 60);
        return (minsSinceDosage / details.getRepeatIntervalInMinutes());
    }

    /**
     * Calculates the total number of times the pill reminder for this regimen will be sent, in case the patient
     * does not provide an adherence response.
     * @return the maximum number of reminders that will be sent for this regimen
     */
    public int timesPillRemainderWillBeSent() {
        DailyScheduleDetails details = getScheduleDetails();
        return (details.getPillWindowInHours() * 60) / details.getRepeatIntervalInMinutes();
    }

    private int getOffsetOfCurrentTimeFromDosageStartTime(Time dosageStartTime, DateTime now) {
        int hourDiff = now.getHourOfDay() - dosageStartTime.getHour();
        if (hourDiff < 0) {
            hourDiff += 24;
        }
        return hourDiff * 60 + now.getMinuteOfHour() - dosageStartTime.getMinute();
    }
}
