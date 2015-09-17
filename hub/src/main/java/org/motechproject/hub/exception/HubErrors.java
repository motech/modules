package org.motechproject.hub.exception;

import org.springframework.http.HttpStatus;

/**
 * An interface for custom Hub errors.
 */
public interface HubErrors {

    /**
     * Gets a message describing the error that occurred.
     *
     * @return a message describing the error
     */
    String getMessage();

    /**
     * Gets the code of the error that occurred.
     *
     * @return the code of the error
     */
    int getCode();

    /**
     * Gets the HTTP status appropriate for this error.
     *
     * @return the HTTP status for this error
     */
    HttpStatus getHttpStatus();
}
