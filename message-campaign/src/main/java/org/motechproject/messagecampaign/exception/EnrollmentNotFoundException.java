package org.motechproject.messagecampaign.exception;


public class EnrollmentNotFoundException extends RuntimeException {

    public EnrollmentNotFoundException() {
    }

    public EnrollmentNotFoundException(String message) {
        super(message);
    }

    public EnrollmentNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
