package org.motechproject.ivr.domain;

/**
 * Information about the status of an IVR call
 */
public enum CallStatus {
    /**
     * Motech received an request to initiate an outbound call
     */
    MOTECH_INITIATED,
    /**
     * A call was initiated at the IVR provider
     */
    INITIATED,
    /**
     * A call is in progress
     */
    IN_PROGRESS,
    /**
     * A call was successfully answered & terminated
     */
    ANSWERED,
    /**
     * A call was unsuccessful because the line was busy
     */
    BUSY,
    /**
     * A call was unsuccessful for some other reason than BUSY or NO_ANSWER
     */
    FAILED,
    /**
     * A call was unsuccessful because the recipient did not answer
     */
    NO_ANSWER,
    /**
     * I don't know what happened
     */
    UNKNOWN
}
