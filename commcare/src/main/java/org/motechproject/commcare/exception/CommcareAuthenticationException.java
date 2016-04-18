package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems while authenticating to the CommCare server. This exception might indicate that the
 * given credentials are invalid.
 */
public class CommcareAuthenticationException extends RuntimeException {

    private static final long serialVersionUID = -3913685825652130949L;

    public CommcareAuthenticationException() {
        super("Motech was unable to authenticate to CommCareHQ. Please verify your account settings.");
    }

    public CommcareAuthenticationException(Exception ex, String message) {
        super(message, ex);
    }
}
