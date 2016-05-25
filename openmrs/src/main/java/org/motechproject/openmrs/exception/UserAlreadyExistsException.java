package org.motechproject.openmrs.exception;

/**
 * Thrown when trying to create an existing user.
 */
public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
