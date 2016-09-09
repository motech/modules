package org.motechproject.rapidpro.exception;

/**
 * Thrown when an error is encountered while parsing a web hook request.
 */
public class WebHookParserException extends Exception {

    public WebHookParserException(String message) {
        super(message);
    }

    public WebHookParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
