package org.motechproject.ihe.interop.event;

/**
 * Event subjects.
 */
public final class EventSubjects {

    public static final String BASE_SUBJECT = "org.motechproject.ihe.interop";

    public static final String TEMPLATE_ACTION = BASE_SUBJECT + "template";

    public static final String ALL_TEMPLATE_ACTIONS = TEMPLATE_ACTION + ".*";

    private EventSubjects() { }
}
