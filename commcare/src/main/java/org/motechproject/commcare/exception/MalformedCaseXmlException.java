package org.motechproject.commcare.exception;

/**
 * Thrown when the given XML is not a valid case.
 */
public class MalformedCaseXmlException extends RuntimeException {

    public MalformedCaseXmlException(String message) {
        super(message);
    }

    public MalformedCaseXmlException(Exception ex, String message) {
        super(message, ex);
    }
}
