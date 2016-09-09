package org.motechproject.rapidpro.constant;

/**
 * Constant values for all RapidPro web hook event types.
 */
public final class WebHookTypes {

    /*An incoming message was received by the Android phone*/
    public static final String WEB_HOOK_MO_SMS = "mo_sms";

    /*An outgoing message has been confirmed as sent by the Android phone*/
    public static final String WEB_HOOK_MT_SENT = "mt_sent";

    /*The network has reported that an outgoing message was delivered to the final recipient*/
    public static final String WEB_HOOK_MT_DLVD = "mt_dlvd";

    /*An incoming call was missed on the Android phone*/
    public static final String WEB_HOOK_MO_MISS = "mo_miss";

    /*An incoming call was picked up on the Android phone*/
    public static final String WEB_HOOK_MO_CALL = "mo_call";

    /*An outgoing call was made on the Android phone*/
    public static final String WEB_HOOK_MT_CALL = "mt_call";

    /*An outgoing call was made but never picked up by the recipient*/
    public static final String WEB_HOOK_MT_MISS = "mt_miss";

    /*Your Android phone has either lost contact with RapidPro or is reporting a very low battery*/
    public static final String WEB_HOOK_ALARM = "alarm";

    /*A user has reached an API action in one of your flows*/
    public static final String WEB_HOOK_FLOW = "flow";

    private WebHookTypes() {
    }
}
