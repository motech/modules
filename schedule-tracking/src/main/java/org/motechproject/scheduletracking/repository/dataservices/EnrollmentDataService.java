package org.motechproject.scheduletracking.repository.dataservices;

import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;

import java.util.List;
import java.util.Set;

/**
 * Motech Data Service interface class for Enrollment entity. The implementation
 * is provided by the Motech Data Services module.
 * @see org.motechproject.scheduletracking.domain.Enrollment
 */
public interface EnrollmentDataService extends MotechDataService<Enrollment> {

    /**
     * Returns the enrollment with the given external id, schedule name and status.
     *
     * @param externalId the external id
     * @param scheduleName the name of the schedule
     * @param status the enrollment status
     * @return the enrollment
     */
    @Lookup
    Enrollment findByExternalIdScheduleNameAndStatus(@LookupField(name = "externalId") String externalId,
                                                     @LookupField(name = "scheduleName") String scheduleName,
                                                     @LookupField(name = "status") EnrollmentStatus status);

    /**
     * Returns the enrollment with the given id.
     *
     * @param id id of the instance
     * @return the enrollment
     */
    @Lookup
    Enrollment findById(@LookupField(name = "id") Long id);

    /**
     * Returns the list of enrollments with the given external id.
     *
     * @param externalId the external id
     * @return the list of the enrollments
     */
    @Lookup
    List<Enrollment> findByExternalId(@LookupField(name = "externalId") String externalId);

    /**
     * Returns the list of enrollments with the given status.
     *
     * @param enrollmentStatus the enrollment status
     * @return the list of the enrollments which satisfy the search conditions
     */
    @Lookup
    List<Enrollment> findByStatus(@LookupField(name = "status") EnrollmentStatus enrollmentStatus);

    /**
     * Returns the list of enrollments with the given current milestone name.
     *
     * @param milestoneName the current milestone name
     * @return the list of the enrollments which satisfy the search conditions
     */
    @Lookup
    List<Enrollment> findByMilestoneName(@LookupField(name = "currentMilestoneName") String milestoneName);

    /**
     * Returns the list of enrollments with the given schedule names.
     *
     * @param scheduleName the names of the schedule
     * @return the list of the enrollments which satisfy the search conditions
     */
    @Lookup
    List<Enrollment> findByScheduleName(@LookupField(name = "scheduleName") Set<String> scheduleName);

}
