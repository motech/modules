package org.motechproject.rapidpro.constant;

/**
 * Constant values for event subjects.
 */
public final class EventSubjects {

    private static final String BASE = "org.motechproject.rapidpro.";
    private static final String WEB_HOOK = BASE + "web-hook.";

    /*Contact related event subjects*/
    public static final String CREATE_CONTACT = BASE + "create-contact";
    public static final String UPDATE_CONTACT = BASE + "update-contact";
    public static final String DELETE_CONTACT = BASE + "delete-contact";
    public static final String CONTACT_CREATED = BASE + "contact-created";
    public static final String CONTACT_UPDATED = BASE + "contact-updated";
    public static final String CONTACT_DELETED = BASE + "contact-deleted";

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

    /*Web hook related event subjects*/
    public static final String WEB_HOOK_RECEIVED_SMS = WEB_HOOK + "received-SMS";
    public static final String WEB_HOOK_SENT_SMS = WEB_HOOK + "sent-SMS";
    public static final String WEB_HOOK_DELIVERED_SMS = WEB_HOOK + "delivered-SMS";
    public static final String WEB_HOOK_INCOMING_CALL = WEB_HOOK + "incoming-call";
    public static final String WEB_HOOK_MISSED_CALL = WEB_HOOK + "missed-call";
    public static final String WEB_HOOK_CALL_CONNECTED = WEB_HOOK + "call-connected";
    public static final String WEB_HOOK_CALL_NOT_CONNECTED = WEB_HOOK + "call-not-connected";
    public static final String WEB_HOOK_ALARM = WEB_HOOK + "alarm";
    public static final String WEB_HOOK_FLOW = WEB_HOOK + "flow";
    public static final String WEB_HOOK_FAIL = WEB_HOOK + "fail";
    public static final String WEB_HOOK_CUSTOM = WEB_HOOK + "custom";

    private EventSubjects() {
    }
}
