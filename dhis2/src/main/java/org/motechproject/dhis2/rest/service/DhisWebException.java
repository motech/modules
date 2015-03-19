package org.motechproject.dhis2.rest.service;

/**
 * Thrown if there is an error with an HTTP request or if there is an error marshalling
 * to or from a data transfer object
 */
public class DhisWebException extends RuntimeException {
    public DhisWebException(String message) {
        super(message);
    }

    public DhisWebException(String message, Throwable cause) {
        super(message, cause);
    }
}
