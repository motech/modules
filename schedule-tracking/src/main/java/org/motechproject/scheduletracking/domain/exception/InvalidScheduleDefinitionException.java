package org.motechproject.scheduletracking.domain.exception;

/**
 * Thrown when adding floating alerts for the absolute schedules.
 */
public class InvalidScheduleDefinitionException extends RuntimeException {

    public InvalidScheduleDefinitionException(String s, String... args) {
        super(String.format(s, args));
    }
}
