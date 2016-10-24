package org.motechproject.rapidpro.constant;

/**
 * Constant values for event parameters.
 */
public final class EventParameters {

    /*Contact Parameters*/
    public static final String EXTERNAL_ID = "externalId";
    public static final String CONTACT_NAME = "name";
    public static final String LANGUAGE = "language";
    public static final String PHONE = "phone";
    public static final String FIELDS = "fields";
    public static final String BLOCKED = "blocked";
    public static final String FAILED = "failed";
    public static final String CONTACT_UUID = "contactUUID";
    public static final String URNS = "urns";
    public static final String CONTACT_GROUP_UUIDS = "contactGroupUUIDs";
    public static final String MODIFIED_ON = "modifiedOn";

    /*Group Parameters*/
    public static final String GROUP_NAME = "groupName";
    public static final String GROUP_UUID = "groupUUID";

    /*Flow parameters*/
    public static final String FLOW_NAME = "flowName";
    public static final String RESTART_PARTICIPANTS = "restartParticipants";
    public static final String EXTRA = "extra";
    public static final String FLOW_UUID = "flowUUID";
    public static final String FLOW = "flow";
    public static final String RUN = "run";
    public static final String FLOW_CREATED_ON = "createdOn";
    public static final String FLOW_EXPIRES_ON = "expiresOn";
    public static final String FLOW_EXPIRED_ON = "expiredOn";
    public static final String ERROR_MESSAGE = "errorMessage";
    public static final String CONTACT_UUIDS = "contactUUIDs";

    /*web hook parameters*/
    public static final String EVENT = "event";
    public static final String RELAYER = "relayer";
    public static final String RELAYER_PHONE = "relayerPhone";
    public static final String SMS = "sms";
    public static final String TEXT = "text";
    public static final String STATUS = "status";
    public static final String DIRECTION = "direction";
    public static final String TIME = "time";
    public static final String CALL = "call";
    public static final String DURATION = "duration";
    public static final String POWER_LEVEL = "powerLevel";
    public static final String POWER_STATUS = "powerStatus";
    public static final String POWER_SOURCE = "powerSource";
    public static final String NETWORK_TYPE = "networkType";
    public static final String PENDING_MESSAGE_COUNT = "pendingMessageCount";
    public static final String RETRY_MESSAGE_COUNT = "retryMessageCount";
    public static final String LAST_SEEN = "lastSeen";
    public static final String STEP = "step";
    public static final String VALUES = "values";
    public static final String REQUEST_VALUES = "requestValues";
    public static final String CUSTOM_WEB_HOOK_MAP = "customWebHookMap";

    private EventParameters() {
    }
}

