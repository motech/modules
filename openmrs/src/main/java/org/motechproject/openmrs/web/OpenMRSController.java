package org.motechproject.openmrs.web;

import org.motechproject.openmrs.exception.OpenMRSAuthenticationException;
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
public abstract class OpenMRSController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(OpenMRSAuthenticationException.class)
    @ResponseStatus(value = HttpStatus.UNAUTHORIZED)
    @ResponseBody
    public String handleOpenMRSAuthenticationException(OpenMRSAuthenticationException e) {
        return handleException(e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    @ResponseBody
    public String handleIllegalArgumentException(IllegalArgumentException e) {
        return handleException(e);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public String handleMiscException(Exception e) {
        return handleException(e);
    }

    private String handleException(Exception e) {
        logger.error(e.getMessage(), e);
        return e.getMessage();
    }
}
