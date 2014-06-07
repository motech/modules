package org.motechproject.sms;

/**
 * Possible Sms Event payloads (ie: params)
 */
public final class SmsEventParams {

    private SmsEventParams() { }

    /**
     * Config that was used for this message
     */
    public static final String CONFIG = "config";
    /**
     * list of recipients (phone numbers)
     */
    public static final String RECIPIENTS = "recipients";
    /**
     * incoming SMS recipient (phone number)
     */
    public static final String RECIPIENT = "recipient";
    /**
     * incoming SMS sender  (phone number)
     */
    public static final String SENDER = "sender";
    /**
     * time at which this SMS should be sent
     */
    public static final String DELIVERY_TIME = "delivery_time";
    /**
     * date & time when this event happened
     */
    public static final String TIMESTAMP = "timestamp";
    /**
     * date & time when this event happened
     */
    public static final String TIMESTAMP_DATETIME = "timestamp_datetime";
    /**
     * UTC time when this event happened
     */
    public static final String TIMESTAMP_TIME = "timestamp_time";
    /**
     * the text content of the SMS message
     */
    public static final String MESSAGE = "message";
    /**
     * internal SMS failure counter
     */
    public static final String FAILURE_COUNT = "failure_count";
    /**
     * MOTECH unique message id
     */
    public static final String MOTECH_ID = "motech_id";
    /**
     * provider unique message id
     */
    public static final String PROVIDER_MESSAGE_ID = "provider_message_id";
    /**
     * provider provided SMS delivery status, sometimes holds more information than what MOTECH models
     */
    public static final String PROVIDER_STATUS = "provider_status";
}
