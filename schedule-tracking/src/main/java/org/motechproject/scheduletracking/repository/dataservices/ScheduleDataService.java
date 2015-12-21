package org.motechproject.scheduletracking.repository.dataservices;


import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.scheduletracking.domain.Schedule;

/**
 * Motech Data Service interface class for Schedule entity. The implementation
 * is provided by the Motech Data Servives module.
 * @see org.motechproject.scheduletracking.domain.Schedule
 */
public interface ScheduleDataService extends MotechDataService<Schedule> {

    /**
     * Returns the schedule for the given schedule name.
     *
     * @param name the name of the schedule
     * @return the schedule
     */
    @Lookup
    Schedule findByName(@LookupField(name = "name") String name);
}
