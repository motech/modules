package org.motechproject.scheduletracking.service;

import org.joda.time.DateTime;

/**
 * Facade used to expose methods as a task actions.
 * @see org.motechproject.scheduletracking.domain.Enrollment
 * @see org.motechproject.scheduletracking.service.EnrollmentRecord
 */
public interface ScheduletrackingTasksActionFacade {

    /**
     * Creates enrollment from the given parameters and schedules alerts. If the user has already enrolled for the same
     * schedule which is currently active, then we update the existing details and reschedule alerts.
     *
     * @param externalId the client external id
     * @param scheduleName the schedule name
     * @param preferredAlertTime the preferred alert time
     * @param referenceDate the reference date
     * @param referenceTime the reference time
     * @param enrolmentDate the enrolment date
     * @param enrollmentTime the enrolment time
     * @param startingMilestoneName the starting milestone name
     */
    void enroll(String externalId, String scheduleName, String preferredAlertTime, // NO CHECKSTYLE ParameterNumber
                DateTime referenceDate, String referenceTime, DateTime enrolmentDate, String enrollmentTime,
                String startingMilestoneName);

    /**
     * Unenrolls all enrollments with the given external id and schedule name. Also removes all the scheduled jobs of
     * enrollments.
     *
     * @param externalId the client external id
     * @param scheduleName the name of the schedule
     */
    void unenroll(String externalId, String scheduleName);
}
