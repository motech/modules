package org.motechproject.commcare.exception;

/**
 * Thrown when the given XML is not a valid case.
 */
public class MalformedCaseXmlException extends RuntimeException {

    private static final long serialVersionUID = -397389909438204204L;

    public MalformedCaseXmlException(String message) {
        super(message);
    }

    public MalformedCaseXmlException(Exception ex, String message) {
        super(message, ex);
    }
}
