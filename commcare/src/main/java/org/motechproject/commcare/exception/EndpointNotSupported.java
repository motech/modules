package org.motechproject.commcare.exception;

/**
 * Thrown when data comes for configuration that doesn't support this endpoint.
 */
public class EndpointNotSupported extends Exception {

    public EndpointNotSupported(String message) {
        super(message);
    }
}
