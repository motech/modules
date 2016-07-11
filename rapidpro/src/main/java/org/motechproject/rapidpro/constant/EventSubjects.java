package org.motechproject.rapidpro.constant;

/**
 * Constant values for event subjects.
 */
public final class EventSubjects {

    private static final String BASE = "org.motechproject.rapidpro.";

    /*Contact related event subjects*/
    public static final String CREATE_CONTACT = BASE + "create-contact";
    public static final String UPDATE_CONTACT = BASE + "update-contact";
    public static final String DELETE_CONTACT = BASE + "delete-contact";

    /*Group related event subjects*/
    public static final String ADD_TO_GROUP = BASE + "add-contact-to-group";
    public static final String REMOVE_FROM_GROUP = BASE + "remove-contact-from-group";
    public static final String ADDED_TO_GROUP = BASE + "contact-added-to-group";
    public static final String REMOVED_FROM_GROUP = BASE + "contact-removed-from-group";

    private EventSubjects() {
    }
}
