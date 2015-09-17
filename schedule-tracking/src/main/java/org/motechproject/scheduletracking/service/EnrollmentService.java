package org.motechproject.scheduletracking.service;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduletracking.domain.Enrollment;

import java.util.Map;

/**
 * Enrollment Service interface provides methods for enrolling and unenrolling enrollments, fulfilling milestones and for retrieving
 * alert timings.
 */
public interface EnrollmentService {

    /**
     * Creates the enrolment from the given parameters and schedule alerts jobs. This method also sends
     * {@link org.motechproject.scheduletracking.events.EnrolledUserEvent} if there is no active enrolment for the
     * client external id and the schedule name. If the client has already enrolled for the same schedule which is currently
     * active, then enrollment is updated and alerts are rescheduled.
     *
     * @param externalId the client external id
     * @param scheduleName the name of the schedule
     * @param startingMilestoneName the starting milestone name
     * @param referenceDateTime the reference date and time on which the schedule will start
     * @param enrollmentDateTime the enrollment date and time on which the client is enrolled into the schedule
     * @param preferredAlertTime the time of day to send alerts to client
     * @param metadata the metadata
     * @return the id of the created enrollment
     */
    Long enroll(String externalId, String scheduleName, String startingMilestoneName, DateTime referenceDateTime,
                DateTime enrollmentDateTime, Time preferredAlertTime, Map<String, String> metadata);

    /**
     * Fulfills the current milestone of the enrollment(with fulfillment date and time as the the given date and time).
     *
     * @param enrollment the enrollment with milestone to fulfill
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
     * Returns the alert timings for the given parameters. Enrollment is build from the parameters and then alerts timing
     * are retrieved.
     *
     * @param externalId the client external id
     * @param scheduleName the name of the schedule
     * @param milestoneName the current milestone name
     * @param referenceDateTime the reference date and time on which the schedule will start
     * @param enrollmentDateTime the enrollment date and time on which the client is enrolled into the schedule
     * @param preferredAlertTime the time of day to send alerts to client
     * @return the alert timings
     */
    MilestoneAlerts getAlertTimings(String externalId, String scheduleName, String milestoneName, DateTime referenceDateTime,
                                    DateTime enrollmentDateTime, Time preferredAlertTime);
}
