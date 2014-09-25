package org.motechproject.ivr.web;

/**
 * Thrown when an error occurs in a controller
 */
public class IvrControllerException extends RuntimeException {
    public IvrControllerException(String message) {
        super(message);
    }

    public IvrControllerException(String message, Throwable cause) {
        super(message, cause);
    }
}
