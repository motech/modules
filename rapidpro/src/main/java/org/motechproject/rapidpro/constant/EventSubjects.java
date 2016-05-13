package org.motechproject.rapidpro.constant;

/**
 * Constant values for event subjects.
 */
public final class EventSubjects {

    private static final String BASE = "org.motechproject.rapidpro.";

    private EventSubjects() {
    }

    /*Contact related event subjects*/
    public static final String CREATE_CONTACT = BASE + "create-contact";
    public static final String UPDATE_CONTACT = BASE + "update-contact";
    public static final String DELETE_CONTACT = BASE + "delete-contact";
}
