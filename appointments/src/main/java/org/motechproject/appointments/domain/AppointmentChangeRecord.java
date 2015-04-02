package org.motechproject.appointments.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

/**
 * AppointmentChangeRecord class to capture the changes in the status of the appointment
 */
@Entity
@CrudEvents(CrudEventType.NONE)
public class AppointmentChangeRecord {

    // external Id of the patient
    @Field
    private String externalId;

    // Appointment id being changed
    @Field
    private String appointmentId;

    // date of the Appointment creation/change
    @Field
    private DateTime appointmentDate;

    // Previous appointment status being changed
    @Field
    private AppointmentStatus fromStatus;

    // Current appointment status to change to
    @Field
    private AppointmentStatus toStatus;

    public AppointmentChangeRecord(String externalId, String appointmentId, DateTime appointmentDate,
                                   AppointmentStatus fromStatus, AppointmentStatus toStatus) {
        this.externalId = externalId;
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    // Getters & Setters
    public String getExternalId() { return this.externalId; }

    public void setExternalId(String externalId) { this.externalId = externalId; }

    public String getAppointmentId() { return this.appointmentId; }

    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    public DateTime getAppointmentDate() { return this.appointmentDate; }

    public void setAppointmentDate(DateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    public AppointmentStatus getFromStatus() { return this.fromStatus; }

    public void setFromStatus(AppointmentStatus fromStatus) { this.fromStatus = fromStatus; }

    public AppointmentStatus getToStatus() { return this.toStatus; }

    public void setToStatus(AppointmentStatus toStatus) { this.toStatus = toStatus; }
}
