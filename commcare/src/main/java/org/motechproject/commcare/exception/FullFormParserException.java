package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems while parsing an XML form.
 */
public class FullFormParserException extends Exception {

    private static final long serialVersionUID = 2532247749044974760L;

    public FullFormParserException(String message) {
        super(message);
    }

    public FullFormParserException(Exception ex, String message) {
        super(message, ex);
    }
}
