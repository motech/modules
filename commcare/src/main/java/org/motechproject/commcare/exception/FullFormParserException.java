package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems while parsing an XML form.
 */
public class FullFormParserException extends Exception {

    public FullFormParserException(String message) {
        super(message);
    }

    public FullFormParserException(Exception ex, String message) {
        super(message, ex);
    }
}
