package org.motechproject.appointments.reminder.impl;

import org.joda.time.Period;
import org.motechproject.appointments.domain.Appointment;
import org.motechproject.appointments.reminder.AppointmentReminderService;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Implementation for the appointment reminder connector service
 */
@Component
public class AppointmentReminderServiceImpl implements AppointmentReminderService {

    private static final int MILLISECONDS = 1000;
    private static final int SECONDS = 60;
    private static final int MINUTES = 60;
    private static final int HOURS = 24;
    private static final int DAYS = 7;

    private static final String SUBJECT = "Appointment.Reminder.%s.%s";

    @Autowired
    private MotechSchedulerService schedulerService;

    /**
     * Add reminders for a specific appointment
     * @param appointment Appointment object with the necessary fields populated
     */
    @Override
    public void addReminders(Appointment appointment) {
        if (appointment.getReminderInterval() != null) {
            RepeatingSchedulableJob reminder = buildJob(appointment);
            this.schedulerService.safeScheduleRepeatingJob(reminder);
        }
    }

    /**
     * Remove reminders for a specific appointment
     * @param appointment Appointment object with the necessary fields populated
     */
    @Override
    public void removeReminders(Appointment appointment) {
        this.schedulerService.safeUnscheduleAllJobs(String.format(SUBJECT, appointment.getExternalId(), appointment.getApptId()));
    }

    /**
     * Helper to create a repeating schedulable job from an appointment
     * @param appointment The appointment object to create the schedulable job for
     * @return a repeating job for the scheduler
     */
    private RepeatingSchedulableJob buildJob(Appointment appointment) {
        String eventTitle = String.format(SUBJECT, appointment.getExternalId(), appointment.getApptId());
        MotechEvent event = new MotechEvent(eventTitle);

        // temp workaround for lack of period support in scheduler, ignoring anything bigger than weeks for now. Tracking MOTECH-1121
        Period interval = appointment.getReminderInterval();
        long reminderInterval = interval.getWeeks() * DAYS * HOURS * MINUTES * SECONDS * MILLISECONDS + interval.getDays() * HOURS * MINUTES * SECONDS * MILLISECONDS +
                interval.getHours() * MINUTES * SECONDS * MILLISECONDS + interval.getMinutes() * SECONDS * MILLISECONDS + interval.getSeconds() * MILLISECONDS + interval.getMillis();

        return new RepeatingSchedulableJob(event, appointment.getReminderStartTime().toDate(), appointment.getAppointmentDate().toDate(), reminderInterval, true);
    }
}
