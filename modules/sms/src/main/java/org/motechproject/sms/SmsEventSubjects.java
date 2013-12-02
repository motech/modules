package org.motechproject.sms;

/**
 * Event subjects, mirrors DeliveryStatus
 */
public final class SmsEventSubjects {

    private SmsEventSubjects() { }

    public static final String PENDING = "outbound_sms_pending";
    public static final String RETRYING = "outbound_sms_retrying";
    public static final String ABORTED = "outbound_sms_aborted";
    public static final String SCHEDULED = "outbound_sms_scheduled";
    public static final String DISPATCHED = "outbound_sms_dispatched";
    public static final String DELIVERY_CONFIRMED = "outbound_sms_delivery_confirmed";
    public static final String FAILURE_CONFIRMED = "outbound_sms_failure_confirmed";
    public static final String SEND_SMS = "send_sms";
    public static final String INBOUND_SMS = "inbound_sms";
}
