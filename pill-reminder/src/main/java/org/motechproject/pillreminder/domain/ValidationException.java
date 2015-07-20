package org.motechproject.pillreminder.domain;

/**
 * Signals input validation errors.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 8181307222963733893L;

    public ValidationException(String message) {
        super(message);
    }
}
