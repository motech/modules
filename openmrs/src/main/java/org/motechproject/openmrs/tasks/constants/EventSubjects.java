package org.motechproject.openmrs.tasks.constants;

/**
 * Utility class for storing event subjects.
 */
public final class EventSubjects {

    private static final String BASE_SUBJECT = "org.motechproject.openmrs.";

    public static final String CONFIG_CHANGE_EVENT = BASE_SUBJECT + "configChange";
    public static final String GET_COHORT_QUERY_MEMBER_EVENT = BASE_SUBJECT + "getCohortQueryMember";

    /**
     * Utility class, should not be initiated.
     */
    private EventSubjects() {
    }
}
