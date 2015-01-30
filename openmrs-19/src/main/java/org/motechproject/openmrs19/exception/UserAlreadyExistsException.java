package org.motechproject.openmrs19.exception;

/**
 * Thrown when trying to create an existing user.
 */
public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
