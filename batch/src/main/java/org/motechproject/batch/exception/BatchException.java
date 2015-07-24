package org.motechproject.batch.exception;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * Custom exception for the batch module.
 *
 * @author naveen
 *
 */
@SuppressWarnings("serial")
public class BatchException extends Exception {

    /**
     * The error that caused the exception.
     */
    private BatchErrors batchErrors;

    /**
     * Reason for the error.
     */
    private String reason;

    /**
     * @param batchErrors the error that caused the exception
     */
    public BatchException(BatchErrors batchErrors) {
        super(batchErrors.getMessage());
        this.batchErrors = batchErrors;
    }

    /**
     * @param batchErrors the error that caused the exception
     * @param reason reason for the error
     */
    public BatchException(BatchErrors batchErrors, String reason) {
        super(batchErrors.getMessage());
        this.batchErrors = batchErrors;
        this.reason = reason;
    }

    /**
     * @param batchErrors the error that caused the exception
     * @param throwable the underlying exception
     */
    public BatchException(BatchErrors batchErrors, Throwable throwable) {
        super(batchErrors.getMessage(), throwable);
        this.batchErrors = batchErrors;
    }

    /**
     * @param batchErrors the error that caused the exception
     * @param throwable the underlying exception
     * @param reason the reason for this error
     */
    public BatchException(BatchErrors batchErrors, Throwable throwable,
            String reason) {
        super(batchErrors.getMessage(), throwable);
        this.batchErrors = batchErrors;
        this.reason = reason;
    }

    /**
     * Return the error code for the underlying error.
     * @return the error code
     */
    public int getErrorCode() {
        return batchErrors.getCode();
    }

    /**
     * Returns a short description of the error.
     * @return the message representing this error
     */
    public String getErrorMessage() {
        if (StringUtils.isEmpty(reason)) {
            return this.getMessage();
        } else {
            return this.getMessage() + ". Reason: " + reason;
        }
    }

    /**
     * @return the underlying error
     */
    public BatchErrors getError() {
        return batchErrors;
    }

    /**
     * @return the stacktrace for this exception as a string
     */
    public String getErrorMessageDetails() {
        return ExceptionUtils.getStackTrace(this);
    }

    /**
     * @return the reason of this error
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason of this error
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}
