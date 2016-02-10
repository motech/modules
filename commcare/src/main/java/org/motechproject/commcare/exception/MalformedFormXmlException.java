package org.motechproject.commcare.exception;

/**
 * Thrown in case of problems while parsing Commcare Forms to/from XML.
 */
public class MalformedFormXmlException extends RuntimeException {

    private static final long serialVersionUID = 3110456366793246561L;

    public MalformedFormXmlException(String message) {
        super(message);
    }

    public MalformedFormXmlException(Exception ex, String message) {
        super(message, ex);
    }
}
