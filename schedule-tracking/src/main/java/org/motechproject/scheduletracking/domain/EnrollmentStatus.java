package org.motechproject.scheduletracking.domain;

/**
 * The <code>EnrollmentStatus</code> Enum contains the possible states for
 * an enrollment.
 */
public enum EnrollmentStatus {

    /**
     * Enrollment is active, alerts will be raised.
     */
    ACTIVE,

    /**
     * Enrollment is defaulted, all of the configured alerts will not be raised. If the milestone has not been fulfilled
     * by the last day of the milestone, then that enrollment is marked as defaulted. It also cannot be moved to the
     * active state.
     */
    DEFAULTED,

    /**
     * Enrollment is completed when the last milestone is fulfilled.
     */
    COMPLETED,

    /**
     * Enrollment will have unenrolled state when unenroll was performed.
     */
    UNENROLLED;
}
