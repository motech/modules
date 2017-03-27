package org.motechproject.openmrs.tasks.constants;

/**
 * Utility class for storing event subjects.
 */
public final class EventSubjects {

    private static final String BASE_SUBJECT = "org.motechproject.openmrs.";

    public static final String CONFIG_CHANGE_EVENT = BASE_SUBJECT + "configChange";
    public static final String GET_COHORT_QUERY_MEMBER_EVENT = BASE_SUBJECT + "getCohortQueryMember.";

    public static final String FETCH_PATIENT_MESSAGE = BASE_SUBJECT + "fetch.patient";
    public static final String FETCH_ENCOUNTER_MESSAGE = BASE_SUBJECT + "fetch.encounter";
    public static final String READ_MESSAGE = BASE_SUBJECT + "read";
    public static final String FEED_CHANGE_MESSAGE = BASE_SUBJECT + "feedchange.";
    public static final String RESCHEDULE_FETCH_JOB = BASE_SUBJECT + "reschedulefetchjob";

    public static final String PATIENT_FEED_CHANGE_MESSAGE = FEED_CHANGE_MESSAGE + "patient";
    public static final String ENCOUNTER_FEED_CHANGE_MESSAGE = FEED_CHANGE_MESSAGE + "encounter";
    /**
     * Utility class, should not be initiated.
     */
    private EventSubjects() {
    }
}
