package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems while connecting to the Commcare server.
 */
public class CommcareConnectionFailureException extends Exception {

    public CommcareConnectionFailureException(String message) {
        super(message);
    }

    public CommcareConnectionFailureException(Exception ex, String message) {
        super(message, ex);
    }
}
