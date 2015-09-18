package org.motechproject.scheduletracking.domain.exception;

/**
 * Thrown when the enrollment which should be updated does not exist or is not active.
 */
public class InvalidEnrollmentException extends RuntimeException {

    private static final long serialVersionUID = -4937023030220818008L;

    public InvalidEnrollmentException(String message) {
        super(message);
    }
}
