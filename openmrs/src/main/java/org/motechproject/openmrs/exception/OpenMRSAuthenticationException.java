package org.motechproject.openmrs.exception;

/**
 * Thrown when there were problems while authenticating to the OpenMRS server. This exception might indicate that the
 * given credentials are invalid.
 */
public class OpenMRSAuthenticationException extends RuntimeException {

    public OpenMRSAuthenticationException(String message) {
        super(message);
    }

    public OpenMRSAuthenticationException(Exception ex, String message) {
        super(message, ex);
    }
}
