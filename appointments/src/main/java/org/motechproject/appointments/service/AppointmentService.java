package org.motechproject.appointments.service;

import org.motechproject.appointments.domain.Appointment;
import org.motechproject.appointments.domain.AppointmentException;
import org.motechproject.appointments.domain.AppointmentStatus;

import java.util.List;


/**
 * AppointmentService interface to add/remove/update/find appointments
 */
public interface AppointmentService {

    /**
     * Adds one instance of an appointment
     * @param appointment Appointment object to add
     * @return appointment object that is returned from the data store
     */
    Appointment addAppointment(Appointment appointment);

    /**
     * Add appointments for users with external id set in the appointment objects
     *
     * @param appointments list of appointment objects to add
     */
    List<Appointment> addAppointments(List<Appointment> appointments);

    /**
     * Remove a given appointment from the store
     * @param appointmentId Appointment to remove
     */
    void removeAppointment(String appointmentId) throws AppointmentException;

    /**
     * Removes all appointments and reminders for given user (identified by externalId)
     *
     * @param appointmentId list of appointments to remove
     */
    void removeAppointments(List<String> appointmentId) throws AppointmentException;

    /**
     * Update the appointment object in the store
     * @param appointment Appointment object to update
     * @return updated Appointment object to return
     */
    Appointment updateAppointment(Appointment appointment) throws AppointmentException;
    /**
     * Updates the list of appointments
     * @param appointments List of appointment objects
     * @return Updated list of appointments
     */
    List<Appointment> updateAppointments(List<Appointment> appointments) throws AppointmentException;

    /**
     * Get the appointment for the Appointment Id
     * @param appointmentId Appointment to search for
     * @return Appointment object for id
     */
    Appointment getAppointment(String appointmentId);

    /**
     * Find the list of appointments for a given external id (overloaded)
     * @param externalId external id related to the implementation
     * @return List of appointments that belong to the external id
     */
    List<Appointment> findAppointmentsByExternalId(String externalId);

    /**
     * Find visit for given user identifier (external id) and appointment status (overloaded)
     *
     * @param externalId external id related to the implementation
     * @param status status of the appointment to filter on
     * @return List of appointments that belong to the external id, filtered by status
     */
    List<Appointment> findAppointmentsByExternalId(String externalId, AppointmentStatus status);

    /**
     * Toggle the reminders for a user with external id. True == send reminders for patient (externalId), false == stop reminders for patient
     *
     * @param externalId External id of the user
     * @param sendReminders boolean flag to start or stop reminders based on the appointment interval field
     */
    void toggleReminders(String externalId, boolean sendReminders) throws AppointmentException;
}
