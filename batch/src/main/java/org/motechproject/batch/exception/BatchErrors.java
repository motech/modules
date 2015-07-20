package org.motechproject.batch.exception;

import org.springframework.http.HttpStatus;

/**
 * Interface for custom errors
 *
 * @author naveen
 *
 */
public interface BatchErrors {

    /**
     * @return a short description of the error
     */
    String getMessage();

    /**
     * @return the code associated with this error
     */
    int getCode();

    /**
     * @return the HTTP status that should be used for representing this error
     */
    HttpStatus getHttpStatus();
}
