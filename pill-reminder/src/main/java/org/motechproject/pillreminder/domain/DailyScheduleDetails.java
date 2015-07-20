package org.motechproject.pillreminder.domain;

import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

/**
 * Domain object representing the schedule of sending daily pill reminders.
 * This is tied to a pill regimen.
 */
@Entity
@CrudEvents(CrudEventType.NONE)
public class DailyScheduleDetails {

    /**
     * Valid time range to retry the reminder in-case of subscriber unreachable, in hours.
     */
    @Field(displayName = "Pill Window in Hours")
    private int pillWindowInHours;

    /**
     * Repeat interval for dosage reminder in-case not able reach subscriber, in minutes.
     */
    @Field(displayName = "Repeat Interval in Minutes")
    private int repeatIntervalInMinutes;

    /**
     * The buffer over dosage time in minutes. The first reminder will be fired after this time passes
     * from the dosage time if there was no adherence confirmation.
     */
    @Field(displayName = "Buffer Over-Dosage Time in Minutes")
    private int bufferOverDosageTimeInMinutes;

    /**
     * Constructs an instance without setting any fields.
     */
    public DailyScheduleDetails() {
    }

    /**
     * Constructs an instance of the schedule.
     * @param repeatIntervalInMinutes number of minutes to wait before next retry for the reminder
     * @param pillWindowInHours valid time range to retry the reminder in-case of subscriber unreachable, in hours
     * @param bufferOverDosageTimeInMinutes the time in minutes after the first reminder will be sent, if no adherence confirmation
     */
    public DailyScheduleDetails(int repeatIntervalInMinutes, int pillWindowInHours, int bufferOverDosageTimeInMinutes) {
        this.repeatIntervalInMinutes = repeatIntervalInMinutes;
        this.pillWindowInHours = pillWindowInHours;
        this.bufferOverDosageTimeInMinutes = bufferOverDosageTimeInMinutes;
    }

    /**
     * @return valid time range to retry the reminder in-case of subscriber unreachable, in hours
     */
    public int getPillWindowInHours() {
        return pillWindowInHours;
    }

    /**
     * @param pillWindowInHours valid time range to retry the reminder in-case of subscriber unreachable, in hours
     */
    public void setPillWindowInHours(int pillWindowInHours) {
        this.pillWindowInHours = pillWindowInHours;
    }

    /**
     * @return number of minutes to wait before next retry for the reminder
     */
    public int getRepeatIntervalInMinutes() {
        return repeatIntervalInMinutes;
    }

    /**
     * @param repeatIntervalInMinutes number of minutes to wait before next retry for the reminder
     */
    public void setRepeatIntervalInMinutes(int repeatIntervalInMinutes) {
        this.repeatIntervalInMinutes = repeatIntervalInMinutes;
    }

    /**
     * Returns the buffer over dosage time in minutes. The first reminder will be fired after this time passes
     * from the dosage time if no there was no adherence confirmation.
     * @return the time in minutes after the first reminder will be sent, if no adherence confirmation
     */
    public int getBufferOverDosageTimeInMinutes() {
        return bufferOverDosageTimeInMinutes;
    }

    /**
     * Sets the buffer over dosage time in minutes. The first reminder will be fired after this time passes
     * from the dosage time if no there was no adherence confirmation.
     * @param bufferOverDosageTimeInMinutes the time in minutes after the first reminder will be sent, if no adherence confirmation
     */
    public void setBufferOverDosageTimeInMinutes(int bufferOverDosageTimeInMinutes) {
        this.bufferOverDosageTimeInMinutes = bufferOverDosageTimeInMinutes;
    }

}
