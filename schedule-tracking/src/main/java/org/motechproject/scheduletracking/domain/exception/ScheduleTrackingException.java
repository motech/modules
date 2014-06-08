package org.motechproject.scheduletracking.domain.exception;

public class ScheduleTrackingException extends RuntimeException {
    public ScheduleTrackingException(String s, String ... args) {
        super(String.format(s, args));
    }
}
