package org.motechproject.odk.exception;

/**
 * This exception is thrown when a malformed form field URI is encountered.
 */
public class MalformedUriException extends Exception {

    public MalformedUriException(String message, Throwable cause) {
        super(message, cause);
    }
}
