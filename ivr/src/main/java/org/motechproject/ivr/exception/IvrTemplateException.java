package org.motechproject.ivr.exception;

import org.springframework.http.HttpStatus;

/**
 * The <code>IvrTemplateException</code> is a custom exception that
 * can be thrown from templates. Using this exception you can control
 * the error code in HTTP responses.
 */
public class IvrTemplateException extends RuntimeException {
    private static final long serialVersionUID = -4264065037359936514L;

    private HttpStatus errorCode;

    public IvrTemplateException(String message) {
        this(message, null);
    }

    public IvrTemplateException(String message, HttpStatus errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
