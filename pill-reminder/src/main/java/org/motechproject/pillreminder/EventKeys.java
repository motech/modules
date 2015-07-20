package org.motechproject.pillreminder;

/**
 * Event keys and subjects used by the module.
 */
public final class EventKeys {

    // Keys

    /**
     * The key for Dosage ID key.
     */
    public static final String DOSAGE_ID_KEY = "DosageID";

    /**
     * The key for External ID key.
     */
    public static final String EXTERNAL_ID_KEY = "ExternalID";

    /**
     * The Pill Regimen ID key.
     */
    public static final String PILL_REGIMEN_ID = "PillRegimenID";

    /**
     * The key for the last capture date of the dosage.
     */
    public static final String LAST_CAPTURE_DATE = "LastCaptureDate";

    /**
     * The key for the number of pill reminders already sent for a dosage.
     */
    public static final String PILLREMINDER_TIMES_SENT = "times-reminders-sent";

    /**
     * The key for the total number of pill reminders that can be sent for a dosage.
     */
    public static final String PILLREMINDER_TOTAL_TIMES_TO_SEND = "times-reminders-to-be-sent";

    /**
     * They key for the retry interval (in minutes) for repeating reminders for this dosage.
     */
    public static final String PILLREMINDER_RETRY_INTERVAL = "retry-interval";

    // Subjects

    /**
     * The base for Pill-Reminder subjects.
     */
    public static final String BASE_SUBJECT = "org.motechproject.pillreminder.";

    /**
     * The subject used internally by pill reminder. Used for scheduler jobs.
     */
    public static final String PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER = BASE_SUBJECT + "scheduler-reminder";

    /**
     * This subject signals that a pill reminder should be sent to the patient.
     */
    public static final String PILLREMINDER_REMINDER_EVENT_SUBJECT = BASE_SUBJECT + "pill-reminder";

    // Action subject

    /**
     * The subject the pill reminder listens for. Sending an event with this subject will update the last capture
     * date for a dosage. This subject is used by a task action from the pill-reminder channel.
     */
    public static final String PILLREMINDER_REMINDER_EVENT_SUBJECT_DOSAGE_STATUS_KNOWN = BASE_SUBJECT + "dosage-status-known";

    /**
     * The subject the pill reminder listens for. Sending an event with this subject will unsubscribe a patient from
     * the reminder service. This subject is used by a task action from the pill-reminder channel.
     */
    public static final String PILLREMINDER_REMINDER_EVENT_SUBJECT_UNSUBSCRIBE = BASE_SUBJECT + "unsubscribe";

    private EventKeys() {
    }
}
