package org.motechproject.scheduletracking.events.constants;

/**
 * Event subjects published by the schedule tracking module
 */
public final class EventSubjects {

    private static final String BASE_SUBJECT = "org.motechproject.scheduletracking.";

    public static final String MILESTONE_ALERT = BASE_SUBJECT + "milestone.alert";
    public static final String MILESTONE_DEFAULTED = BASE_SUBJECT + "milestone.defaulted";
    public static final String USER_ENROLLED = BASE_SUBJECT + "user.enrolled";
    public static final String USER_UNENROLLED = BASE_SUBJECT + "user.unenrolled";

    private EventSubjects() {
    }
}
