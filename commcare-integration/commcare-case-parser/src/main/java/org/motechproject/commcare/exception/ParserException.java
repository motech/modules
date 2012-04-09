package org.motechproject.commcare.exception;

public class ParserException extends RuntimeException{
    public ParserException(String message) {
        super(message);
    }

    public ParserException(Exception ex, String message) {
        super(message, ex);
    }
}
