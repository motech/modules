package org.motechproject.sms.audit.constants;

/**
 * Utility class for storing delivery statuses.
 */
public final class DeliveryStatuses {

    public static final String RECEIVED = "RECEIVED";
    public static final String RETRYING = "RETRYING";
    public static final String ABORTED = "ABORTED";
    public static final String DISPATCHED = "DISPATCHED";
    public static final String FAILURE_CONFIRMED = "FAILURE_CONFIRMED";
    public static final String SCHEDULED = "SCHEDULED";
    public static final String PENDING = "PENDING";

    private DeliveryStatuses() {

    }
}
