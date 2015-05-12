package org.motechproject.appointments.repository;

import org.motechproject.appointments.domain.Appointment;
import org.motechproject.appointments.domain.AppointmentStatus;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

/**
 * Interface to utilize the MDS CRUD operations for appointment service
 */
public interface AppointmentDataService extends MotechDataService<Appointment> {

    @Lookup
    List<Appointment> findAppointmentByStatus(@LookupField(name = "status") AppointmentStatus status);

    @Lookup
    List<Appointment> findAppointmentsByExternalId(@LookupField(name = "externalId") String externalId);

    @Lookup
    List<Appointment> findAppointmentByExternalIdAndStatus(@LookupField(name = "externalId") String externalId,
                                                           @LookupField(name = "status") AppointmentStatus status);

    @Lookup
    Appointment findAppointmentById(@LookupField(name = "apptId") String apptId);
}
