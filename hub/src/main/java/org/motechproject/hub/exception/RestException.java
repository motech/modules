package org.motechproject.hub.exception;

import org.springframework.http.HttpStatus;

import java.util.List;

/**
 * Exception thrown for Http Requests in case of any exception.
 */
public class RestException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private String reason;

    private HubException hubException;

    /**
     * Creates a new <code>RestException</code> instance. The exception is
     * constructed from {@link org.motechproject.hub.exception.HubException} and
     * list of errors that are the reason of this exception.
     *
     * @param exception the underlying <code>HubException</code> exception
     * @param errors list of errors that will be treated as the reason for this exception
     */
    public RestException(HubException exception, List<String> errors) {
        this(exception, errors.toString());
    }

    /**
     * Creates a new <code>RestException</code> instance. The exception is
     * constructed from {@link org.motechproject.hub.exception.HubException} and
     * a <code>String</code> describing exception cause.
     *
     * @param hubException the underlying <code>HubException</code> exception
     * @param reason the reason of the error
     */
    public RestException(HubException hubException, String reason) {
        super("HttpStatus:" + hubException.getError().getHttpStatus()
                + " reason:" + reason);
        this.reason = reason;
        this.hubException = hubException;
    }

    /**
     * Gets the HTTP status appropriate for the error that caused
     * this exception.
     *
     * @return the HTTP status of the error
     */
    public HttpStatus getHttpStatus() {
        return getHubException().getError().getHttpStatus();
    }

    /**
     * Gets the reason of the error.
     *
     * @return the reason of the error
     */
    public String getReason() {
        return reason;
    }

    /**
     * Gets the underlying <code>HubException</code>.
     *
     * @return the underlying <code>HubException</code>
     */
    public HubException getHubException() {
        return hubException;
    }
}
