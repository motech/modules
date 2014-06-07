package org.motechproject.sms.alert;

/**
 * Helper class - Uses StatusMessageService to send system Alerts
 */
public interface MotechStatusMessage {

    void alert(String message);
}
