package org.motechproject.sms.util;

/**
 * Class holding constant String values related to permissions and roles.
 */
public final class Constants {

    public static final String MANAGE_SMS_PERMISSION = "manageSMS";
    public static final String VIEW_SMS_LOGS_PERMISSION = "viewSMSLogs";

    public static final String HAS_MANAGE_SMS_ROLE = "hasRole('" + MANAGE_SMS_PERMISSION + "')";
    public static final String HAS_VIEW_SMS_LOGS_ROLE = "hasRole('" + VIEW_SMS_LOGS_PERMISSION + "')";
    public static final String UI_CONFIG = "custom-ui.js";

    private Constants() {
    }

}
