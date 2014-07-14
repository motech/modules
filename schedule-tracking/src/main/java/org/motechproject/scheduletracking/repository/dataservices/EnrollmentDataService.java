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
 *
 * @see Enrollment
 */
public interface EnrollmentDataService extends MotechDataService<Enrollment> {

    @Lookup
    Enrollment findByExternalIdScheduleNameAndStatus(@LookupField(name="externalId") String externalId,
                                                     @LookupField(name="scheduleName") String scheduleName,
                                                     @LookupField(name="status") EnrollmentStatus status);

    @Lookup
    Enrollment findById(@LookupField(name="id") Long id);

    @Lookup
    List<Enrollment> findByExternalId(@LookupField(name="externalId") String externalId);

    @Lookup
    List<Enrollment> findByStatus(@LookupField(name="status") EnrollmentStatus enrollmentStatus);

    @Lookup
    List<Enrollment> findByMilestoneName(@LookupField(name="currentMilestoneName") String milestoneName);

    @Lookup
    List<Enrollment> findByScheduleName(@LookupField(name="scheduleName") Set<String> scheduleName);

}
