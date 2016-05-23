package org.motechproject.openmrs.exception;

/**
 * Thrown when concept with given name already exists.
 */
public class ConceptNameAlreadyInUseException extends Exception {

    public ConceptNameAlreadyInUseException(String message) {
        super(message);
    }

}
