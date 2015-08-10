package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems while authenticating to the CommCare server. This exception might indicate that the
 * given credentials are invalid.
 */
public class CommcareAuthenticationException extends RuntimeException {

    public CommcareAuthenticationException(String message) {
        super(message);
    }

    public CommcareAuthenticationException(Exception ex, String message) {
        super(message, ex);
    }
}
