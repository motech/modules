package org.motechproject.hub.exception;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * This class is used for custom exception for the Hub Module
 *
 * @author anuranjan
 *
 */
public class HubException extends Exception {

    private static final long serialVersionUID = 6944669766477120363L;

    private HubErrors hubErrors;
    private String reason;

    /**
     * Creates a new <code>HubException</code> instance from
     * {@link org.motechproject.hub.exception.HubErrors} passed as a parameter.
     *
     * @param hubErrors the error that caused the exception
     */
    public HubException(HubErrors hubErrors) {
        super(hubErrors.getMessage());
        this.hubErrors = hubErrors;
    }

    /**
     * Creates a new <code>HubException</code> instance from
     * {@link org.motechproject.hub.exception.HubErrors} and
     * a <code>String</code> describing error cause passed as
     * a parameters.
     *
     * @param hubErrors the error that caused the exception
     * @param reason reason for the error
     */
    public HubException(HubErrors hubErrors, String reason) {
        super(hubErrors.getMessage());
        this.hubErrors = hubErrors;
        this.reason = reason;
    }

    /**
     * Creates a new <code>HubException</code> instance from
     * {@link org.motechproject.hub.exception.HubErrors} and
     * <code>Throwable</code> that caused this exception passed as
     * a parameters.
     *
     * @param hubErrors the error that caused the exception
     * @param throwable the underlying exception
     */
    public HubException(HubErrors hubErrors, Throwable throwable) {
        super(hubErrors.getMessage(), throwable);
        this.hubErrors = hubErrors;
    }

    /**
     * Creates a new <code>HubException</code> instance from
     * {@link org.motechproject.hub.exception.HubErrors},
     * <code>Throwable</code> that caused this exception and
     * a <code>String</code> describing error cause passed as
     * a parameters.
     *
     * @param hubErrors the error that caused the exception
     * @param throwable the underlying exception
     * @param reason the reason for this error
     */
    public HubException(HubErrors hubErrors, Throwable throwable, String reason) {
        super(hubErrors.getMessage(), throwable);
        this.hubErrors = hubErrors;
        this.reason = reason;
    }

    /**
     * Gets the error code for the underlying error.
     *
     * @return the error code
     */
    public int getErrorCode() {
        return hubErrors.getCode();
    }

    /**
     * Gets a short description of the error.
     *
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
     * Gets the underlying {@link org.motechproject.hub.exception.HubErrors} error.
     *
     * @return the underlying {@link org.motechproject.hub.exception.HubErrors} error
     */
    public HubErrors getError() {
        return hubErrors;
    }

    /**
     * Gets the stacktrace of this exception as a <code>String</code>.
     *
     * @return the stacktrace for this exception as a <code>String</code>
     */
    public String getErrorMessageDetails() {

        return getStackTraceString();
    }


    private String getStackTraceString() {
        return ExceptionUtils.getStackTrace(this);
    }

    /**
     * Gets a <code>String</code> describing cause of this error.
     *
     * @return the reason of this error
     */
    public String getReason() {
        return reason;
    }

    /**
     * Sets a <code>String</code> describing cause of this error.
     *
     * @param reason the reason of this error
     */
    public void setReason(String reason) {
        this.reason = reason;
    }
}
