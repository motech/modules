package org.motechproject.openlmis.rest.service;

/**
 * Thrown if there is an error with an HTTP request or if there is an error marshalling
 * to or from a data transfer object
 */
public class OpenlmisWebException extends RuntimeException {
    /**
     * 
     */
    private static final long serialVersionUID = -1284054460384833359L;

    /**
     * 
     */

    public OpenlmisWebException(String message) {
        super(message);
    }

    public OpenlmisWebException(String message, Throwable cause) {
        super(message, cause);
    }
}
