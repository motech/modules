package org.motechproject.pillreminder.contract;

import java.util.List;

/**
 * Daily Pill Regimen Request represents medicine prescription details and reminder configuration.
 * @see DosageRequest
 */
public class DailyPillRegimenRequest {
    private int pillWindowInHours;
    private String externalId;
    private List<DosageRequest> dosageRequests;
    private int reminderRepeatIntervalInMinutes;
    private int bufferOverDosageTimeInMinutes;

    /** Creates Daily Pill prescription for given subscriber id and reminder configuration
     *
     * @param externalId unique id for subscriber
     * @param pillWindowInHours valid time range to retry the reminder in-case of subscriber unreachable, in hours
     * @param reminderRepeatIntervalInMinutes wait time before 2 reminder retries
     * @param bufferTimeForPatientToTakePill additional wait time before sending reminder. (Time to take pill typically 5 min)
     *                                       when set to 5, If pill time is 9:00 am then reminder will be set out at 9:05 am.
     * @param dosageRequests Dosage detail with dose time and medicine list. {@link DosageRequest}
     */
    public DailyPillRegimenRequest(String externalId, int pillWindowInHours, int reminderRepeatIntervalInMinutes, int bufferTimeForPatientToTakePill, List<DosageRequest> dosageRequests) {
        this.externalId = externalId;
        this.reminderRepeatIntervalInMinutes = reminderRepeatIntervalInMinutes;
        this.pillWindowInHours = pillWindowInHours;
        this.bufferOverDosageTimeInMinutes = bufferTimeForPatientToTakePill;
        this.dosageRequests = dosageRequests;
    }

    /**
     * @return valid time range to retry the reminder in-case of subscriber unreachable, in hours
     */
    public int getPillWindowInHours() {
        return pillWindowInHours;
    }

    /**
     * @return unique Id representing subscriber
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * @return all dosage requests scheduled for pill reminder
     */
    public List<DosageRequest> getDosageRequests() {
        return dosageRequests;
    }

    /**
     * Repeat interval for dosage reminder in-case not able reach subscriber.
     * @return number of minutes to wait before next retry for the reminder.
     */
    public int getReminderRepeatIntervalInMinutes() {
        return reminderRepeatIntervalInMinutes;
    }

    /**
     * Returns the buffer over dosage time in minutes. The first reminder will be fired after this time passes
     * from the dosage time if no there was no adherence confirmation.
     * @return the time in minutes after the first reminder will be sent, if no adherence confirmation was received for the regimen
     */
    public int getBufferOverDosageTimeInMinutes() {
        return bufferOverDosageTimeInMinutes;
    }

    /**
     * Sets the buffer over dosage time in minutes. The first reminder will be fired after this time passes
     * from the dosage time if there was no adherence confirmation.
     * @param bufferOverDosageTimeInMinutes the time in minutes after the first reminder will be sent, if no adherence confirmation
     *                                      was received for the regiment
     */
    public void setBufferOverDosageTimeInMinutes(int bufferOverDosageTimeInMinutes) {
        this.bufferOverDosageTimeInMinutes = bufferOverDosageTimeInMinutes;
    }
}
