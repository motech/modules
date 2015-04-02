package org.motechproject.openmrs19.exception;

/**
 * Thrown when there was a problem while deleting user.
 */
public class UserDeleteException extends Exception {

    public UserDeleteException(String message, Throwable e) {
        super(message, e);
    }

}
