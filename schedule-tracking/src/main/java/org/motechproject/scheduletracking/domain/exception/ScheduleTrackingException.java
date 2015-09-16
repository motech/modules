package org.motechproject.scheduletracking.domain.exception;

/**
 * Thrown when schedule with the given name was not found.
 * @see org.motechproject.scheduletracking.service.ScheduleTrackingService
 */
public class ScheduleTrackingException extends RuntimeException {

    public ScheduleTrackingException(String s, String ... args) {
        super(String.format(s, args));
    }
}
