package org.motechproject.appointments.domain;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.CrudEvents;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.event.CrudEventType;

/**
 * AppointmentChangeRecord class to capture the changes in the status of the appointment
 */
@Entity(nonEditable = true)
@CrudEvents(CrudEventType.NONE)
public class AppointmentChangeRecord {

    /**
     * External Id of the patient.
     */
    @Field
    private String externalId;

    /**
     * Appointment id of the appointment being changed.
     */
    @Field
    private String appointmentId;

    /**
     * Date of the appointment creation/change.
     */
    @Field
    private DateTime appointmentDate;

    /**
     * Previous appointment status being changed.
     */
    @Field
    private AppointmentStatus fromStatus;

    /**
     * Current appointment status to change to.
     */
    @Field
    private AppointmentStatus toStatus;

    /**
     * Constructs a new record.
     * @param externalId external Id of the patient
     * @param appointmentId appointment id of the appointment being changed
     * @param appointmentDate date of the appointment creation/change
     * @param fromStatus previous appointment status being changed
     * @param toStatus current appointment status to change to
     */
    public AppointmentChangeRecord(String externalId, String appointmentId, DateTime appointmentDate,
                                   AppointmentStatus fromStatus, AppointmentStatus toStatus) {
        this.externalId = externalId;
        this.appointmentId = appointmentId;
        this.appointmentDate = appointmentDate;
        this.fromStatus = fromStatus;
        this.toStatus = toStatus;
    }

    // Getters & Setters

    /**
     * @return external Id of the patient
     */
    public String getExternalId() { return this.externalId; }

    /**
     * @param externalId external Id of the patient
     */
    public void setExternalId(String externalId) { this.externalId = externalId; }

    /**
     * @return appointment id of the appointment being changed
     */
    public String getAppointmentId() { return this.appointmentId; }

    /**
     * @param appointmentId appointment id of the appointment being changed
     */
    public void setAppointmentId(String appointmentId) { this.appointmentId = appointmentId; }

    /**
     * @return date of the appointment creation/change
     */
    public DateTime getAppointmentDate() { return this.appointmentDate; }

    /**
     * @param appointmentDate date of the appointment creation/change
     */
    public void setAppointmentDate(DateTime appointmentDate) { this.appointmentDate = appointmentDate; }

    /**
     * @return previous appointment status being changed
     */
    public AppointmentStatus getFromStatus() { return this.fromStatus; }

    /**
     * @param fromStatus previous appointment status being changed
     */
    public void setFromStatus(AppointmentStatus fromStatus) { this.fromStatus = fromStatus; }

    /**
     * @return current appointment status to change to
     */
    public AppointmentStatus getToStatus() { return this.toStatus; }

    /**
     * @param toStatus current appointment status to change to
     */
    public void setToStatus(AppointmentStatus toStatus) { this.toStatus = toStatus; }
}
