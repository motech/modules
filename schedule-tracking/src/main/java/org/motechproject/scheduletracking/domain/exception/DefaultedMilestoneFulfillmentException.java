package org.motechproject.scheduletracking.domain.exception;

public class DefaultedMilestoneFulfillmentException extends RuntimeException {
    public DefaultedMilestoneFulfillmentException() {
        super("Cannot fulfill milestone for a defaulted enrollment.");
    }
}
