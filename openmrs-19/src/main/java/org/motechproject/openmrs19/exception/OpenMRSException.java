package org.motechproject.openmrs19.exception;

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
