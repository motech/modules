package org.motechproject.scheduletracking.domain.exception;

/**
 * Thrown when the enrollment which should be updated does not exist or is not active.
 */
public class InvalidEnrollmentException extends RuntimeException {

    public InvalidEnrollmentException(String message) {
        super(message);
    }
}
