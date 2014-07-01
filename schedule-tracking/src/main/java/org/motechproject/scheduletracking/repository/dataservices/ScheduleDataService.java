package org.motechproject.scheduletracking.repository.dataservices;


import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.scheduletracking.domain.Schedule;

/**
 * Motech Data Service interface class for Schedule entity. The implementation
 * is provided by the Motech Data Servives module.
 *
 * @see Schedule
 */
public interface ScheduleDataService extends MotechDataService<Schedule> {

    @Lookup
    Schedule findByName(@LookupField(name = "name") String name);
}
