package org.motechproject.commcare.exception;

/**
 * Thrown when data comes for configuration that doesn't support this endpoint.
 */
public class EndpointNotSupported extends Exception {

    private static final long serialVersionUID = 9139173107805775194L;

    public EndpointNotSupported(String message) {
        super(message);
    }
}
