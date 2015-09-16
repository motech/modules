package org.motechproject.scheduletracking.service;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduletracking.domain.Enrollment;

import java.util.Map;

/**
 * Enrollment Service interface provides methods for enroll and unenroll enrollments, fulfill milestones and for retrieving
 * alert timings.
 */
public interface EnrollmentService {

    /**
     * Creates the enrolment from the given parameters and schedule alerts jobs. This method also sends
     * {@link org.motechproject.scheduletracking.events.EnrolledUserEvent} if there is no active enrolment for the
     * external id and the schedule name. If the user has already enrolled for the same schedule which is currently
     * active, then enrollment is updated and alerts are rescheduled.
     *
     * @param externalId the client external id
     * @param scheduleName the name of the schedule
     * @param startingMilestoneName the starting milestone name
     * @param referenceDateTime the reference date and time
     * @param enrollmentDateTime the enrollment date and time
     * @param preferredAlertTime the preferred alert time
     * @param metadata the metadata
     * @return the id of the created enrollment
     */
    Long enroll(String externalId, String scheduleName, String startingMilestoneName, DateTime referenceDateTime,
                DateTime enrollmentDateTime, Time preferredAlertTime, Map<String, String> metadata);

    /**
     * Fulfills the current milestone of the enrollment(with fulfillment date and time as the the given date and time).
     *
     * @param enrollment the enrollment with milestone to fulfills
     * @param fulfillmentDateTime the fulfillment date and time
     */
    void fulfillCurrentMilestone(Enrollment enrollment, DateTime fulfillmentDateTime);

    /**
     * Unenrolls the given enrollment and unschedules all alerts job of the enrollment. This method also sends
     * {@link org.motechproject.scheduletracking.events.UnenrolledUserEvent}.
     *
     * @param enrollment the enrollment to unenroll
     */
    void unenroll(Enrollment enrollment);

    /**
     * Returns the alerts timings for the given parameters. Enrollment is build from the parameters and then alerts timing
     * are retrieved.
     *
     * @param externalId the client external id
     * @param scheduleName the name of the schedule
     * @param milestoneName the current milestone name
     * @param referenceDateTime the reference date and time
     * @param enrollmentDateTime the enrollment date and time
     * @param preferredAlertTime the preferred alert time
     * @return
     */
    MilestoneAlerts getAlertTimings(String externalId, String scheduleName, String milestoneName, DateTime referenceDateTime,
                                    DateTime enrollmentDateTime, Time preferredAlertTime);
}
