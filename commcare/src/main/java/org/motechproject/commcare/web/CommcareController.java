package org.motechproject.commcare.web;

import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;
import org.motechproject.commcare.exception.ConfigurationNotFoundException;
import org.motechproject.commcare.exception.EndpointNotSupported;
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
    public String handleNotFound(Exception e) {
        return handleException(e);
    }

    @ExceptionHandler(EndpointNotSupported.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleBadRequest(Exception e) {
        return handleException(e);
    }

    @ExceptionHandler(CommcareAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleCommcareAuthenticationException(CommcareAuthenticationException e) {
        return handleException(e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return handleException(e);
    }

    private String handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return e.getMessage();
    }

}
