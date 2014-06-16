package org.motechproject.appointments.repository;

import org.joda.time.DateTime;
import org.motechproject.appointments.domain.AppointmentChangeRecord;
import org.motechproject.appointments.domain.AppointmentStatus;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * Interface to utilize the Seuss CRUD operations for appointment record service (logging & reporting)
 */
public interface AppointmentChangeRecordDataService extends MotechDataService<AppointmentChangeRecord> {

    @Lookup
    List<AppointmentChangeRecord> findByCriteria(@LookupField(name = "externalId") String externalId,
                                           @LookupField(name = "appointmentId") String appointmentId,
                                           @LookupField(name = "appointmentDate") DateTime appointmentDate,
                                           @LookupField(name = "fromStatus") AppointmentStatus fromStatus,
                                           @LookupField(name = "toStatus") AppointmentStatus toStatus);
}
