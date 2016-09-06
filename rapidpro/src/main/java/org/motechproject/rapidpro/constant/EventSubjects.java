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

    /*Flow related event subjects*/
    public static final String START_FLOW_CONTACT_NAME = BASE + "start-flow-contact-name";
    public static final String START_FLOW_CONTACT_UUID = BASE + "start-flow-contact-uuid";
    public static final String START_FLOW_GROUP_UUID = BASE + "start-flow-group-uuid";
    public static final String START_FLOW_GROUP_NAME = BASE + "start-flow-group-name";
    public static final String FLOW_STARTED_CONTACT = BASE + "flow-started-for-contact";
    public static final String FLOW_STARTED_GROUP = BASE + "flow-started-for-group";
    public static final String FLOW_FAIL_CONTACT_NAME = BASE + "flow-fail-contact-name";
    public static final String FLOW_FAIL_CONTACT_UUID = BASE + "flow-fail-contact-uuid";

    private EventSubjects() {
    }
}
