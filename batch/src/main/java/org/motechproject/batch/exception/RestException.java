package org.motechproject.batch.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Exception thrown for Http Requests in case of any exception.
 *
 * @author naveen
 *
 */
public class RestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * The reason for the exception.
     */
    private String reason;

    /**
     * The underlying batch exception.
     */
    private BatchException batchException;

    /**
     * Builds this exception from the provided batch exception and list of errors.
     * @param exception the underlying exception
     * @param errors list of errors that will be treated as the reason for this exception
     */
    public RestException(BatchException exception, List<String> errors) {
        this(exception, errors.toString());
    }

    /**
     * Builds this exception given an exception and a reason for it.
     * @param batchException the underlying exception
     * @param reason the reason of the error
     */
    public RestException(BatchException batchException, String reason) {
        super("HttpStatus:" + batchException.getError().getHttpStatus()
                + " reason:" + reason);
        this.reason = reason;
        this.batchException = batchException;
    }

    /**
     * @return the HTTP status that should be returned for this exception
     */
    public HttpStatus getHttpStatus() {
        return getBatchException().getError().getHttpStatus();
    }

    /**
     * @return the reason of the error
     */
    public String getReason() {
        return reason;
    }

    /**
     * @return the underlying batch exception
     */
    public BatchException getBatchException() {
        return batchException;
    }
}
