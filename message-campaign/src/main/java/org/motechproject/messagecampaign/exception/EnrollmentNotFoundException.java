package org.motechproject.messagecampaign.exception;

/**
 * Thrown when an enrollment cannot be found.
 */
public class EnrollmentNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 9048246088520612112L;

    public EnrollmentNotFoundException() {
    }

    public EnrollmentNotFoundException(String message) {
        super(message);
    }

    public EnrollmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
