package org.motechproject.scheduletracking.domain.exception;

/**
 * Thrown when fulfillCurrentMilestone was invoked but all milestones in the schedule have been fulfilled.
 * @see org.motechproject.scheduletracking.service.EnrollmentService
 */
public class NoMoreMilestonesToFulfillException extends RuntimeException {

    private static final long serialVersionUID = 5265794275742790287L;

    public NoMoreMilestonesToFulfillException() {
        super("All milestones in the schedule have been fulfilled.");
    }
}
