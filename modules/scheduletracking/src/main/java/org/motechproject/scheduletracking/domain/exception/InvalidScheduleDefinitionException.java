package org.motechproject.scheduletracking.domain.exception;

public class InvalidScheduleDefinitionException extends RuntimeException {
    public InvalidScheduleDefinitionException(String s, String... args) {
        super(String.format(s, args));
    }
}
