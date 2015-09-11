package org.motechproject.appointments.service.impl;

import org.motechproject.appointments.domain.AppointmentException;
import org.motechproject.appointments.repository.AppointmentChangeRecordDataService;
import org.motechproject.appointments.repository.AppointmentDataService;
import org.motechproject.appointments.domain.Appointment;
import org.motechproject.appointments.domain.AppointmentChangeRecord;
import org.motechproject.appointments.domain.AppointmentStatus;
import org.motechproject.appointments.reminder.AppointmentReminderService;
import org.motechproject.appointments.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Service implementation for Appointments
 *
 */
@Service("appointmentService")
public class AppointmentServiceImpl implements AppointmentService {

    @Autowired
    private AppointmentDataService appointmentDataService;

    @Autowired
    private AppointmentChangeRecordDataService appointmentChangeRecordDataService;

    @Autowired
    private AppointmentReminderService appointmentReminderService;

    /**
     * Add one instance of an appointment to the store
     * @param appointment Appointment object to add
     * @return Copy of the appointment object from the data store
     */
    @Override
    @Transactional
    public Appointment addAppointment(Appointment appointment) {
        if (appointment.getExternalId() == null) {
            throw new IllegalArgumentException("External id field cannot be null");
        }

        Appointment existing = appointmentDataService.findAppointmentById(appointment.getApptId());

        // Add appoint first, look it up and then add reminders
        Appointment createdOrUpdated;
        if (existing == null) {
            createdOrUpdated = appointmentDataService.create(appointment);
        } else {
            createdOrUpdated = appointmentDataService.update(existing);
        }

        if (createdOrUpdated.getSendReminders()) {
            this.appointmentReminderService.addReminders(createdOrUpdated);
        }

        return createdOrUpdated;
    }

    /**
     * Add appointments for users with external id set in the appointment objects
     * @param appointments List of new appointments to add/track
     */
    @Override
    @Transactional
    public List<Appointment> addAppointments(List<Appointment> appointments) {

        List<Appointment> result = new ArrayList<>(appointments.size());
        for (Appointment current : appointments) {
            result.add(addAppointment(current));
        }
        return result;
    }


    /**
     * Removes the appointment with the fiver appointment Id
     * @param appointmentId Id of the appointment to remove
     * @exception org.motechproject.appointments.domain.AppointmentException if no appointment found to remove with appointmentId
     */
    @Override
    @Transactional
    public void removeAppointment(String appointmentId) throws AppointmentException {
        Appointment lookup = appointmentDataService.findAppointmentById(appointmentId);
        if (lookup == null) {
            throw new AppointmentException("No appointment found with appointmentId " + appointmentId);
        }
        this.appointmentReminderService.removeReminders(lookup);
        appointmentDataService.delete(lookup);
    }

    /**
     * Removes all appointments and reminders for given user (identified by externalId)
     * @param appointments list of appointment objects to remove
     * @exception org.motechproject.appointments.domain.AppointmentException if no appointment found to remove with appointmentId
     */
    @Override
    @Transactional
    public void removeAppointments(List<String> appointments) throws AppointmentException {
        for (String currentAppointment : appointments) {
            removeAppointment(currentAppointment);
        }
    }

    /**
     * Update the the appointment if we can find it. Returns null otherwise.
     * @param toUpdate the latest version of the appointment object to update to
     * @return The updated appointment object if successful, null otherwise
     * @exception org.motechproject.appointments.domain.AppointmentException if the appointment is not found in MDS
     */
    @Override
    @Transactional
    public Appointment updateAppointment(Appointment toUpdate) throws AppointmentException {
        Appointment old = appointmentDataService.findAppointmentById(toUpdate.getApptId());
        if (old == null) {
            throw new AppointmentException("No appointment found with id" + toUpdate.getApptId());
        }

        // if the appt date changed or status changed, create a change record (log)
        if (old.getAppointmentDate() != toUpdate.getAppointmentDate() ||
                old.getStatus() != toUpdate.getStatus()) {
            appointmentChangeRecordDataService.create(new AppointmentChangeRecord(toUpdate.getExternalId(), toUpdate.getApptId(),
                    toUpdate.getAppointmentDate(), old.getStatus(), toUpdate.getStatus()));
        }

        // commit the new version of the appointment
        appointmentDataService.update(toUpdate);

        // remove old reminders and add new ones if needed
        this.appointmentReminderService.removeReminders(old);
        if (toUpdate.getSendReminders()) {
            this.appointmentReminderService.addReminders(toUpdate);
        }

        return toUpdate;
    }

    /**
     * Updates the list of appointments
     * @param appointments List of appointment objects
     * @return Updated list of appointments
     * @exception org.motechproject.appointments.domain.AppointmentException if the appointment in list in not found in MDS
     */
    @Override
    @Transactional
    public List<Appointment> updateAppointments(List<Appointment> appointments) throws AppointmentException {
        List<Appointment> result = new ArrayList<>(appointments.size());

        for (Appointment current : appointments) {
            result.add(updateAppointment(current));
        }

        return result;
    }

    /**
     * Find the list of appointments for a given external id (overloaded)
     * @param externalId external id related to the implementation
     * @return List of appointments that belong to the external id
     */
    @Override
    @Transactional
    public List<Appointment> findAppointmentsByExternalId(String externalId) {
        return appointmentDataService.findAppointmentsByExternalId(externalId);
    }

    /**
     * Find the appointment with the id specified
     * @param appointmentId Appointment to search for
     * @return Appointment object with id
     */
    @Override
    @Transactional
    public Appointment getAppointment(String appointmentId) {
        return appointmentDataService.findAppointmentById(appointmentId);
    }

    /**
     * Find appointments for given user identifier (external id) and appointment status (overloaded)
     * @param externalId external id related to the implementation
     * @param status status of the appointment to filter on
     * @return List of appointments that belong to the external id, filtered by status
     */
    @Override
    @Transactional
    public List<Appointment> findAppointmentsByExternalId(String externalId, AppointmentStatus status) {
        return appointmentDataService.findAppointmentByExternalIdAndStatus(externalId, status);
    }

    /**
     * Toggle the reminders (for all future appointments) for a user with external id. This is the same as passing in an updated
     * Appointment object to the updateAppointments API with the sendReminders flag set
     * @param externalId External id of the user
     * @param sendReminders boolean flag to start or stop reminders based on the appointment interval field
     * @exception org.motechproject.appointments.domain.AppointmentException if the appointment is not found in MDS
     */
    @Override
    @Transactional
    public void toggleReminders(String externalId, boolean sendReminders) throws AppointmentException {
        List<Appointment> result = appointmentDataService.findAppointmentsByExternalId(externalId);

        for (Appointment current : result) {
            if (current.getAppointmentDate() != null && current.getAppointmentDate().isAfterNow()) {
                current.setSendReminders(sendReminders);
                updateAppointment(current);
            }
        }
    }
}
