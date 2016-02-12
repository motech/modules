package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems while parsing case.
 */
public class CaseParserException extends Exception {

    private static final long serialVersionUID = 8007314406269145206L;

    public CaseParserException(String message) {
        super(message);
    }

    public CaseParserException(Exception ex, String message) {
        super(message, ex);
    }
}
