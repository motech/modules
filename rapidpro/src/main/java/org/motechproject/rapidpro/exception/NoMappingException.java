package org.motechproject.rapidpro.exception;

/**
 * Thrown when there is no mapping from an external ID to a Rapidpro UUID present.
 */
public class NoMappingException extends Exception {

    private static final String NO_MAPPING = "No mapping exists for External ID: ";

    public NoMappingException(String externalId) {
        super(NO_MAPPING + externalId);
    }
}
