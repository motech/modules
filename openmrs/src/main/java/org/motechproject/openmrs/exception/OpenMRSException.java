package org.motechproject.openmrs.exception;

/**
 * Thrown when there were problems while fetching data from OpenMRS server.
 */
public class OpenMRSException extends RuntimeException {

    public OpenMRSException(Throwable e) {
        super(e);
    }

    public OpenMRSException(String message) {
        super(message);
    }

    public OpenMRSException(String message, Throwable cause) {
        super(message, cause);
    }
}
