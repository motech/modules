package org.motechproject.scheduletracking.repository;

import org.joda.time.DateTime;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.List;

/**
 * The DAO that utilizes a MDS back-end for retrieving instances.
 */
@Repository
public class AllEnrollments {

    @Autowired
    private EnrollmentDataService enrollmentDataService;

    /**
     * Returns all enrollments stored in the database.
     *
     * @return the list of the enrollments
     */
    public List<Enrollment> retrieveAll() {
        return enrollmentDataService.retrieveAll();
    }

    /**
     * Returns the list of enrollments with the given external id.
     *
     * @param externalId the external id
     * @return the list of the enrollments which satisfy the search conditions
     */
    public List<Enrollment> findByExternalId(String externalId) {
        return enrollmentDataService.findByExternalId(externalId);
    }

    /**
     * Returns the list of enrollments with the given current milestone name.
     *
     * @param currentMilestone the current milestone name
     * @return the list of the enrollments which satisfy the search conditions
     */
    public List<Enrollment> findByCurrentMilestone(String currentMilestone) {
        return enrollmentDataService.findByMilestoneName(currentMilestone);
    }

    /**
     * Returns the list of enrollments with the given status.
     *
     * @param status the enrollment status
     * @return the list of the enrollments which satisfy the search conditions
     */
    public List<Enrollment> findByStatus(EnrollmentStatus status) {
        return enrollmentDataService.findByStatus(status);
    }

    /**
     * Returns these enrollments, that have got a given key-value entry in their metadata.
     *
     * @param property the metadata key
     * @param value the metadata value
     * @return the list of the enrollments which satisfy the search conditions
     */
    public List<Enrollment> findByMetadataProperty(String property, String value) {
        return enrollmentDataService.executeQuery(new MapQueryExecution(property, value));
    }

    /**
     * Returns the list of enrollments with the given schedule names.
     *
     * @param scheduleNames the names of the schedule
     * @return the list of the enrollments which satisfy the search conditions
     */
    public List<Enrollment> findBySchedule(List<String> scheduleNames) {
        return enrollmentDataService.findByScheduleName(new HashSet<>(scheduleNames));
    }

    /**
     * Return all enrollments that have been completed during the given time range.
     *
     * @param start the start date and time
     * @param end the end date end time
     * @return the list of the enrollments which satisfy the search conditions
     */
    public List<Enrollment> completedDuring(DateTime start, DateTime end) {
        return enrollmentDataService.executeQuery(new MilestoneRangeQueryExecution(start, end));
    }
}
