package org.motechproject.ivr.service;

/**
 * Thrown when an error occurs while trying to initiate an outgoing call
 */
public class CallInitiationException extends RuntimeException {
    public CallInitiationException(String message) {
        super(message);
    }

    public CallInitiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
