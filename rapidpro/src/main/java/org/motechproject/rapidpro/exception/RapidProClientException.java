package org.motechproject.rapidpro.exception;

/**
 * Thrown when there is an error with an HTTP request to Rapidpro.
 */
public class RapidProClientException extends Exception {

    public RapidProClientException(String message) {
        super(message);
    }

    public RapidProClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
