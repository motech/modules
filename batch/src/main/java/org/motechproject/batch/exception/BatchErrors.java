package org.motechproject.batch.exception;

import org.springframework.http.HttpStatus;

/**
 * Interface for custom errors
 *
 * @author naveen
 *
 */
public interface BatchErrors {

    String getMessage();

    int getCode();

    HttpStatus getHttpStatus();
}
