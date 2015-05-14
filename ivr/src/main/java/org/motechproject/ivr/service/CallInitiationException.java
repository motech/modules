package org.motechproject.ivr.service;

/**
 * Thrown when an error occurs while trying to initiate an outgoing call
 */
public class CallInitiationException extends RuntimeException {

    private static final long serialVersionUID = -4482581556851958471L;

    public CallInitiationException(String message) {
        super(message);
    }

    public CallInitiationException(String message, Throwable cause) {
        super(message, cause);
    }
}
