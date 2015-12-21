package org.motechproject.appointments.reminder.impl;

import org.motechproject.appointments.domain.Appointment;
import org.motechproject.appointments.reminder.AppointmentReminderService;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.contract.RepeatingPeriodSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation for the appointment reminder connector service
 */
@Component
public class AppointmentReminderServiceImpl implements AppointmentReminderService {

    private static final String SUBJECT = "Appointment.Reminder.%s.%s";

    @Autowired
    private MotechSchedulerService schedulerService;

    /**
     * Add reminders for a specific appointment
     * @param appointment Appointment object with the necessary fields populated
     */
    @Override
    @Transactional
    public void addReminders(Appointment appointment) {
        if (appointment.getReminderInterval() != null) {
            RepeatingPeriodSchedulableJob reminder = buildJob(appointment);
            this.schedulerService.safeScheduleRepeatingPeriodJob(reminder);
        }
    }

    /**
     * Remove reminders for a specific appointment
     * @param appointment Appointment object with the necessary fields populated
     */
    @Override
    @Transactional
    public void removeReminders(Appointment appointment) {
        this.schedulerService.safeUnscheduleAllJobs(String.format(SUBJECT, appointment.getExternalId(), appointment.getApptId()));
    }

    /**
     * Helper to create a repeating schedulable job from an appointment
     * @param appointment The appointment object to create the schedulable job for
     * @return a repeating period job for the scheduler
     */
    private RepeatingPeriodSchedulableJob buildJob(Appointment appointment) {
        String eventTitle = String.format(SUBJECT, appointment.getExternalId(), appointment.getApptId());
        MotechEvent event = new MotechEvent(eventTitle);

        return new RepeatingPeriodSchedulableJob(event, appointment.getReminderStartTime().toDate(), appointment.getAppointmentDate().toDate(), appointment.getReminderInterval(), true);
    }
}
