package org.motechproject.odk.exception;

/**
 * This exception is thrown if a method is unable to generate a basic authentication header.
 */
public class BasicAuthException extends RuntimeException {

    public BasicAuthException(Throwable cause) {
        super(cause);
    }
}
