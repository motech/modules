package org.motechproject.pillreminder.event;

public final class EventKeys {

    private EventKeys() {

    }

    public static final String DOSAGE_ID_KEY = "DosageID";
    public static final String EXTERNAL_ID_KEY = "ExternalID";
    public static final String PILL_REGIMEN_ID = "PillRegimenID";
    public static final String LAST_CAPTURE_DATE = "LastCaptureDate";
    public static final String BASE_SUBJECT = "org.motechproject.pillreminder.";

    public static final String PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER = BASE_SUBJECT + "scheduler-reminder";
    public static final String PILLREMINDER_REMINDER_EVENT_SUBJECT = BASE_SUBJECT + "pill-reminder";
    public static final String PILLREMINDER_REMINDER_EVENT_SUBJECT_DOSAGE_STATUS_KNOWN = BASE_SUBJECT + "dosage-status-known";
    public static final String PILLREMINDER_REMINDER_EVENT_SUBJECT_UNSUBSCRIBE = BASE_SUBJECT + "unsubscribe";
    public static final String PILLREMINDER_TIMES_SENT = "times-reminders-sent";
    public static final String PILLREMINDER_TOTAL_TIMES_TO_SEND = "times-reminders-to-be-sent";
    public static final String PILLREMINDER_RETRY_INTERVAL = "retry-interval";
}
