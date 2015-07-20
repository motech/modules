package org.motechproject.pillreminder.contract;

import java.util.List;

/**
 * PillRegimenResponse represents subscribers dosage prescription and reminder configuration.
 * @see org.motechproject.pillreminder.service.PillReminderService#getPillRegimen(String)
 */
public class PillRegimenResponse {

    /**
     * Unique identifier of the pill regimen.
     */
    private Long pillRegimenId;

    /**
     * The external ID associated with this regimen, can represent a patient.
     */
    private String externalId;

    /**
     * The time window in hours during which reminders for this regimen will be sent.
     * No reminders should be sent after this time passes from the start of sending reminders.
     */
    private int reminderRepeatWindowInHours;

    /**
     * The interval between sending subsequent reminders in minutes.
     */
    private int reminderRepeatIntervalInMinutes;

    /**
     * The time in minutes after the first reminder will be sent, if no adherence confirmation was received for the regimen.
     */
    private int bufferOverDosageTimeInMinutes;

    /**
     * Medicine dosages in this pill regimen.
     */
    private List<DosageResponse> dosages;

    /**
     * Constructs a pill regimen response.
     * @param pillRegimenId unique identifier of the pill regimen
     * @param externalId the external ID associated with this regimen, can represent a patient
     * @param reminderRepeatWindowInHours the number of hours during which reminders will be sent
     * @param reminderRepeatIntervalInMinutes the interval between sending subsequent reminders in minutes
     * @param bufferOverDosageTimeInMinutes the time in minutes after the first reminder will be sent, if no adherence confirmation was received for the regimen
     * @param dosages medicine dosages in this pill regimen
     */
    public PillRegimenResponse(Long pillRegimenId, String externalId, int reminderRepeatWindowInHours, int reminderRepeatIntervalInMinutes, int bufferOverDosageTimeInMinutes, List<DosageResponse> dosages) {
        this.pillRegimenId = pillRegimenId;
        this.externalId = externalId;
        this.reminderRepeatWindowInHours = reminderRepeatWindowInHours;
        this.reminderRepeatIntervalInMinutes = reminderRepeatIntervalInMinutes;
        this.bufferOverDosageTimeInMinutes = bufferOverDosageTimeInMinutes;
        this.dosages = dosages;
    }

    /**
     * @return unique identifier of the pill regimen
     */
    public Long getPillRegimenId() {
        return pillRegimenId;
    }

    /**
     * @return the external ID associated with this regimen, can represent a patient
     */
    public String getExternalId() {
        return externalId;
    }

    /**
     * Returns the time window in hours during which reminders for this regimen will be sent.
     * No reminders should be sent after this time passes from the start of sending reminders.
     * @return the number of hours during which reminders will be sent
     */
    public int getReminderRepeatWindowInHours() {
        return reminderRepeatWindowInHours;
    }

    /**
     * @return the interval between sending subsequent reminders in minutes
     */
    public int getReminderRepeatIntervalInMinutes() {
        return reminderRepeatIntervalInMinutes;
    }

    /**
     * @return medicine dosages in this pill regimen
     */
    public List<DosageResponse> getDosages() {
        return dosages;
    }

    /**
     * Returns the buffer over dosage time in minutes. The first reminder will be fired after this time passes
     * from the dosage time if there was no adherence confirmation.
     * @return the time in minutes after the first reminder will be sent, if no adherence confirmation
     */
    public int getBufferOverDosageTimeInMinutes() {
        return bufferOverDosageTimeInMinutes;
    }

    /**
     * Sets the buffer over dosage time in minutes. The first reminder will be fired after this time passes
     * from the dosage time if there was no adherence confirmation.
     * @param bufferOverDosageTimeInMinutes the time in minutes after the first reminder will be sent, if no adherence confirmation
     */
    public void setBufferOverDosageTimeInMinutes(int bufferOverDosageTimeInMinutes) {
        this.bufferOverDosageTimeInMinutes = bufferOverDosageTimeInMinutes;
    }
}
