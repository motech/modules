package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems while connecting to the Commcare server.
 */
public class CommcareConnectionFailureException extends Exception {

    private static final long serialVersionUID = -6819822778837941939L;

    public CommcareConnectionFailureException(String message) {
        super(message);
    }

    public CommcareConnectionFailureException(Exception ex, String message) {
        super(message, ex);
    }
}
