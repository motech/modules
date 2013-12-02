package org.motechproject.sms.audit;

/**
 * Cross-provider delivery statuses
 */
public enum DeliveryStatus {
    /**
     * We just received a message and will either try to send it directly or schedule it for later delivery
     */
    PENDING,
    /**
     * There was a problem delivering a message to the provider, we're retrying
     */
    RETRYING,
    /**
     * The maximum number of retries has occurred, so we've given up trying
     */
    ABORTED,
    /**
     * This message was added to the MOTECH schedule
     */
    SCHEDULED,
    /**
     * SMS was successfully sent to the provider
     */
    DISPATCHED,
    /**
     * Received confirmation from the provider that the message was delivered
     */
    DELIVERY_CONFIRMED,
    /**
     * Received confirmation from the provider that the message was not delivered
     */
    FAILURE_CONFIRMED,
    /**
     * Incoming SMS
     */
    RECEIVED
}
