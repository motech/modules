package org.motechproject.commcare.exception;

/**
 * Thrown when there were problems while parsing {@link org.motechproject.commcare.response.OpenRosaResponse}
 * CommCare server response.
 */
public class OpenRosaParserException extends Exception {

    private static final long serialVersionUID = 4646041644642523712L;

    public OpenRosaParserException(String message) {
        super(message);
    }

    public OpenRosaParserException(Exception ex, String message) {
        super(message, ex);
    }
}
