package org.motechproject.hub.exception;

import org.springframework.http.HttpStatus;

/**
 * Custom Exception messages for Rest services
 */
public enum ApplicationErrors implements HubErrors {

    BAD_REQUEST (1001, "One or more input parameter(s) may be wrong", HttpStatus.BAD_REQUEST),
    SUBSCRIPTION_NOT_FOUND (1002, "Subscription not found", HttpStatus.BAD_REQUEST),
    TOPIC_NOT_FOUND (1003, "Topic not found", HttpStatus.BAD_REQUEST);

    private final int code;
    private String message;
    private HttpStatus httpStatus;

    private ApplicationErrors(int code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    /**
     * Gets a message describing the error that occurred.
     *
     * @return a message describing the error
     */
    public String getMessage() {
        return message;
    }

    /**
     * Gets the code of the error that occurred.
     *
     * @return the code of the error
     */
    public int getCode() {
        return code;
    }

    /**
     * Gets the HTTP status appropriate for this error.
     *
     * @return the HTTP status for this error
     */
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}
