package org.motechproject.appointments.reminder;

import org.motechproject.appointments.domain.Appointment;

/**
 * Interface that helps the Appointments module talk to the Scheduler module
 */
public interface AppointmentReminderService {

    /**
     * Add reminders for a given appointment
     * @param appointment Appointment object with the necessary fields populated
     */
    void addReminders(Appointment appointment);

    /**
     * Remove reminders for a given appointment
     * @param appointment Appointment object with the necessary fields populated
     */
    void removeReminders(Appointment appointment);
}
