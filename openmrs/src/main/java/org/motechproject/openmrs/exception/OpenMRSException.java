package org.motechproject.openmrs.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.HttpClientErrorException;

/**
 * Thrown when there were problems while fetching data from OpenMRS server.
 */
public class OpenMRSException extends RuntimeException {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSException.class);

    public OpenMRSException(Throwable e) {
        super(e);
    }

    public OpenMRSException(String message) {
        super(message);
    }

    public OpenMRSException(String message, Throwable cause) {
        super(message, cause);

        if (cause instanceof HttpClientErrorException) {
            HttpClientErrorException httpCause = (HttpClientErrorException) cause;
            LOGGER.debug("{} {} {}", message, httpCause.getMessage(), httpCause.getResponseBodyAsString());
        }
    }
}
