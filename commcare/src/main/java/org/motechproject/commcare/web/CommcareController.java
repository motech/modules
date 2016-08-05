package org.motechproject.commcare.web;

import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;
import org.motechproject.commcare.exception.ConfigurationNotFoundException;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commons.api.json.MotechMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Abstract controller that serves as a base for all controllers in this module. It's purpose is to provide exception
 * handlers for deriving controllers.
 */
public abstract class CommcareController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler({ConfigurationNotFoundException.class, CommcareConnectionFailureException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public MotechMessage handleNotFound(Exception e) {
        return new MotechMessage(e.getMessage());
    }

    @ExceptionHandler(EndpointNotSupported.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public MotechMessage handleBadRequest(Exception e) {
        return new MotechMessage(e.getMessage());
    }

    @ExceptionHandler(CommcareAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public MotechMessage handleCommcareAuthenticationException(CommcareAuthenticationException e) {
        return new MotechMessage(e.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public MotechMessage handleIllegalArgumentException(IllegalArgumentException e) {
        return new MotechMessage(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public MotechMessage handleMiscException(Exception e) {
        return new MotechMessage(e.getMessage());
    }

}
