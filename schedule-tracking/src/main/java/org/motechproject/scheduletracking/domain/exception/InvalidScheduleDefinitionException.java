package org.motechproject.scheduletracking.domain.exception;

/**
 * Thrown when adding floating alerts for the absolute schedules.
 */
public class InvalidScheduleDefinitionException extends RuntimeException {

    private static final long serialVersionUID = -8635795766665154050L;

    public InvalidScheduleDefinitionException(String s, String... args) {
        super(String.format(s, args));
    }
}
