package org.motechproject.dhis2.service;

/**
 * Thrown if there is no corresponding mapped value when attempting to map from a DHIS2
 * id to an external id (or vice versa).
 */
public class TrackedEntityInstanceMappingException extends RuntimeException {
    public TrackedEntityInstanceMappingException(String message) {
        super(message);
    }

    public TrackedEntityInstanceMappingException(String message, Throwable cause) {
        super(message, cause);
    }
}
