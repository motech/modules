package org.motechproject.batch.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom Exception messages for Rest services
 *
 * @author naveen
 *
 */
public enum ApplicationErrors implements BatchErrors {

    BAD_REQUEST(1001, "One or more input parameter(s) may be wrong",
            HttpStatus.BAD_REQUEST),

    JOB_NOT_FOUND(1002, "Job not found",
            HttpStatus.BAD_REQUEST),

    DUPLICATE_JOB(1003, "Duplicate Job", HttpStatus.BAD_REQUEST),

    DATABASE_OPERATION_FAILED(3003,
            "Error in querying database", HttpStatus.INTERNAL_SERVER_ERROR),

    FILE_READING_WRTING_FAILED(3002, "Error while reading from or writing to file", HttpStatus.INTERNAL_SERVER_ERROR),

    JOB_TRIGGER_FAILED(3001, "Error in starting job", HttpStatus.INTERNAL_SERVER_ERROR),

    UNSCHEDULE_JOB_FAILED(3004, "Error in unscheduling job", HttpStatus.INTERNAL_SERVER_ERROR);

    /**
     * The error code.
     */
    private final int code;

    /**
     * A short message representing the error.
     */
    private String message;

    /**
     * The HTTP Status that should be used for representing this error.
     */
    private HttpStatus httpStatus;

    ApplicationErrors(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * @return a short message representing the error
     */
    public String getMessage() {
        return message;
    }

    /**
     * @return the code of this error
     */
    public int getCode() {
        return code;
    }

    /**
     * @return the HTTP status appropriate for this error
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
