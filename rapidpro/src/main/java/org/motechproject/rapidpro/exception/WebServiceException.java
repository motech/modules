package org.motechproject.rapidpro.exception;

/**
 * Thrown when an error is encountered at the Webservice layer.
 */
public class WebServiceException extends Exception {

    public WebServiceException(String message) {
        super(message);
    }

    public WebServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
