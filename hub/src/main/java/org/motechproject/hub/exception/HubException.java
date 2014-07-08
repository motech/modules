package org.motechproject.hub.exception;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 * This class is used for custome exception for the Hub Module
 *
 * @author anuranjan
 *
 */
public class HubException extends Exception {

    private static final long serialVersionUID = 6944669766477120363L;

    private HubErrors hubErrors;
    private String reason;

    public HubException(HubErrors hubErrors) {
        super(hubErrors.getMessage());
        this.hubErrors = hubErrors;
    }

    public HubException(HubErrors hubErrors, String reason) {
        super(hubErrors.getMessage());
        this.hubErrors = hubErrors;
        this.reason = reason;
    }

    public HubException(HubErrors hubErrors, Throwable throwable) {
        super(hubErrors.getMessage(), throwable);
        this.hubErrors = hubErrors;
    }

    public HubException(HubErrors hubErrors, Throwable throwable, String reason) {
        super(hubErrors.getMessage(), throwable);
        this.hubErrors = hubErrors;
        this.reason = reason;
    }

    public int getErrorCode() {
        return hubErrors.getCode();
    }

    public String getErrorMessage() {
        if (StringUtils.isEmpty(reason)) {
            return this.getMessage();
        } else {
            return this.getMessage() + ". Reason: " + reason;
        }
    }

    public HubErrors getError() {
        return hubErrors;
    }

    public String getErrorMessageDetails() {

        return getStackTraceString();
    }

    private String getStackTraceString() {
        return ExceptionUtils.getStackTrace(this);
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }
}
