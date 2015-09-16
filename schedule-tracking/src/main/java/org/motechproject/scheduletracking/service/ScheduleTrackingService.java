package org.motechproject.scheduletracking.service;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.service.contract.UpdateCriteria;

import java.util.List;

/**
 * Schedule Tracking Service interface provides methods to enroll an external id into a schedule, fulfill milestones
 * in a schedule and uneroll an external id from a schedule. It also provides querying functionality on Enrollments
 * using various search criteria.
 */
public interface ScheduleTrackingService {

    /**
     * Enrolls a user with external id into a schedule using the details in the EnrollmentRequest and schedules alerts.
     * If the user has already enrolled for the same schedule which is currently active, then we update the existing
     * details and reschedule alerts.
     *
     * @param enrollmentRequest the enrollment details
     * @return the enrollment id
     */
    Long enroll(EnrollmentRequest enrollmentRequest);

    /**
     * Fulfills the current milestone of the enrollment(with fulfillmentDate and time as the the given date and time)
     * which belongs to the given externalId and schedule name.
     *
     * @param externalId the client external id
     * @param scheduleName the schedule name
     * @param fulfillmentDate the fulfillment date
     * @param fulfillmentTime the fulfillment time
     */
    void fulfillCurrentMilestone(String externalId, String scheduleName, LocalDate fulfillmentDate, Time fulfillmentTime);

    /**
     * Fulfills the current milestone of the enrollment(with fulfillmentDate and time as midnight) which belongs to the
     * given externalId and schedule name.
     *
     * @param externalId the client external id
     * @param scheduleName the schedule name
     * @param fulfillmentDate the fulfillment date
     */
    void fulfillCurrentMilestone(String externalId, String scheduleName, LocalDate fulfillmentDate);

    /**
     * Unenrolls / Removes all the scheduled jobs of enrollments which belongs to the given external id and schedule
     * names.
     *
     * @param externalId the client external id
     * @param scheduleNames the schedule names
     */
    void unenroll(String externalId, List<String> scheduleNames);

    /**
     * Returns the enrollment record corresponds to the given external id and schedule name.
     *
     * @param externalId the client external id
     * @param scheduleName the schedule name
     * @return the enrolment
     */
    EnrollmentRecord getEnrollment(String externalId, String scheduleName);

    /**
     * Updates an active enrollment which has the given external id and schedule name.
     *
     * @param updateCriteria states the fields to be updated in the enrollment
     */
    void updateEnrollment(String externalId, String scheduleName, UpdateCriteria updateCriteria);

    /**
     * Searches and returns the enrollment records as per the criteria in the given enrolments query.
     *
     * @param query the enrolments query which contains search criteria
     * @return the enrolments list
     */
    List<EnrollmentRecord> search(EnrollmentsQuery query);

    /**
     * Searches and returns the enrollment records(with all window start dates populated in them) as per the criteria
     * in the given enrollments query.
     *
     * @param query the enrolments query which contains search criteria
     * @return the enrolments list
     */
    List<EnrollmentRecord> searchWithWindowDates(EnrollmentsQuery query);

    /**
     * Gives the alert timings of all the windows in the milestone without actually scheduling the alert jobs
     *
     * @param enrollmentRequest the enrollment request
     * @return the alert timings for all the windows of the milestone
     */
    MilestoneAlerts getAlertTimings(EnrollmentRequest enrollmentRequest);

    /**
     * Saves the given schedule in database.
     *
     * @param scheduleJson the schedule in JSON format
     */
    void add(String scheduleJson);

    /**
     * Returns the schedule with the given name.
     *
     * @param scheduleName the name of the schedule
     * @return schedule
     */
    Schedule getScheduleByName(String scheduleName);

    /**
     * Returns all schedules.
     *
     * @return schedules list
     */
    List<Schedule> getAllSchedules();

    /**
     * Removes the specified schedule from the database.
     *
     * @param scheduleName the name of the schedule to remove
     */
    void remove(String scheduleName);
}
