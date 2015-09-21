package org.motechproject.dhis2.exception;

/**
 * The <code>DataElementNotFoundException</code> exception signals a situation when
 * a dataElement with a given name or id cannot be found.
 */
public class DataElementNotFoundException extends RuntimeException {

    public DataElementNotFoundException(String message) {
        super(message);
    }

    public DataElementNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
