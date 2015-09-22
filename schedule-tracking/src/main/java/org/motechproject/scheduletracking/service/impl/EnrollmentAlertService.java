package org.motechproject.scheduletracking.service.impl;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduletracking.domain.Alert;
import org.motechproject.scheduletracking.domain.AlertWindow;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.Milestone;
import org.motechproject.scheduletracking.domain.MilestoneAlert;
import org.motechproject.scheduletracking.domain.MilestoneWindow;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.events.MilestoneEvent;
import org.motechproject.scheduletracking.events.constants.EventSubjects;
import org.motechproject.scheduletracking.service.MilestoneAlerts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static org.motechproject.commons.date.util.DateUtil.now;

/**
 * Service used for managing alerts.
 */
@Component
public class EnrollmentAlertService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentAlertService.class);

    private MotechSchedulerService schedulerService;

    private EventRelay eventRelay;

    @Autowired
    public EnrollmentAlertService(MotechSchedulerService schedulerService, EventRelay eventRelay) {
        this.schedulerService = schedulerService;
        this.eventRelay = eventRelay;
    }

    /**
     * Schedules jobs for the alerts of the current milestone from the given enrollment.
     *
     * @param enrollment the enrollment for which jobs will be scheduled
     */
    public void scheduleAlertsForCurrentMilestone(Enrollment enrollment) {
        Schedule schedule = enrollment.getSchedule();
        Milestone currentMilestone = schedule.getMilestone(enrollment.getCurrentMilestoneName());
        if (currentMilestone == null) {
            LOGGER.info("Exiting without scheduling Milestone alert as Current Milestone found to be null. ");
            return;
        }

        DateTime currentMilestoneStartDate = enrollment.getCurrentMilestoneStartDate();
        for (MilestoneWindow milestoneWindow : currentMilestone.getMilestoneWindows()) {
            if (currentMilestone.windowElapsed(milestoneWindow.getName(), currentMilestoneStartDate)) {
                continue;
            }

            MilestoneAlert milestoneAlert = MilestoneAlert.fromMilestone(currentMilestone, currentMilestoneStartDate);
            for (Alert alert : milestoneWindow.getAlerts()) {
                LOGGER.info("For MileStone Window {} scheduling a milestone alert with offset {} and interval {}.", milestoneWindow.getName(), alert.getOffset(), alert.getInterval());
                scheduleAlertJob(alert, enrollment, currentMilestone, milestoneWindow, milestoneAlert);
            }
        }
    }

    /**
     * Returns the alerts timings for the current milestone of the given enrollment.
     *
     * @param enrollment the enrollment for which timings will be retrieved
     * @return the current milestone timings.
     */
    public MilestoneAlerts getAlertTimings(Enrollment enrollment) {
        Schedule schedule = enrollment.getSchedule();
        Milestone currentMilestone = schedule.getMilestone(enrollment.getCurrentMilestoneName());
        MilestoneAlerts milestoneAlerts = new MilestoneAlerts();
        if (currentMilestone == null) {
            return milestoneAlerts;
        }

        for (MilestoneWindow milestoneWindow : currentMilestone.getMilestoneWindows()) {
            List<DateTime> alertTimingsForWindow = new ArrayList<>();
            for (Alert alert : milestoneWindow.getAlerts()) {
                AlertWindow alertWindow = createAlertWindowFor(alert, enrollment, currentMilestone, milestoneWindow);
                alertTimingsForWindow.addAll(alertWindow.allPossibleAlerts());
            }
            milestoneAlerts.getAlertTimings().put(milestoneWindow.getName().toString(), alertTimingsForWindow);
        }
        return milestoneAlerts;
    }

    /**
     *  Unschedules the alerts jobs from for the given enrollment.
     *
     * @param enrollment the enrollment for which alerts jobs will be unscheduled
     */
    public void unscheduleAllAlerts(Enrollment enrollment) {
        LOGGER.info("Un-scheduling all jobs for enrollment {}", enrollment.getId());
        schedulerService.safeUnscheduleAllJobs(String.format("%s-%s", EventSubjects.MILESTONE_ALERT, enrollment.getId()));
        LOGGER.info("Un-scheduled all jobs for enrollment {}", enrollment.getId());
    }

    private void scheduleAlertJob(Alert alert, Enrollment enrollment, Milestone currentMilestone, MilestoneWindow milestoneWindow, MilestoneAlert milestoneAlert) {
        MotechEvent event = new MilestoneEvent(enrollment, milestoneAlert, milestoneWindow).toMotechEvent();
        event.getParameters().put(MotechSchedulerService.JOB_ID_KEY, String.format("%s.%d", enrollment.getId(), alert.getIndex()));
        Integer repeatIntervalInSeconds =  alert.getInterval().toStandardSeconds().getSeconds();

        AlertWindow alertWindow = createAlertWindowFor(alert, enrollment, currentMilestone, milestoneWindow);
        int numberOfAlertsToSchedule = alertWindow.numberOfAlertsToSchedule();

        if (numberOfAlertsToSchedule == 1) {
            // if there is only one alert to schedule, a run once job will be scheduled.
            DateTime alertsStartTime = alertWindow.scheduledAlertStartDate();

            if (alertsStartTime.isBefore(now())) {
                LOGGER.info("Sending event {} to trigger Alert  as Alert start time {} already elapsed. ", event, alertsStartTime);
                eventRelay.sendEventMessage(event);
                LOGGER.info("Event {} is sent to trigger Alert  as Alert start time {} already elapsed. ", event, alertsStartTime);
            } else {
                RunOnceSchedulableJob job = new RunOnceSchedulableJob(event, alertsStartTime.toDate());

                schedulerService.safeScheduleRunOnceJob(job);
                LOGGER.info("Scheduled Job to trigger Milestone Alert {} with Start Time {}.", event, alertsStartTime);
            }
        } else if (numberOfAlertsToSchedule > 0) {
            // We take one away from the number to schedule since one is
            // always fired from a RepeatingSchedulableJob
            int numberOfAlertsToFire = numberOfAlertsToSchedule - 1;
            DateTime alertsStartTime = alertWindow.scheduledAlertStartDate();

            // If the first alert should have gone out already, go ahead and raise
            // the event for it and decrease the numberOfAlertsToFire since we've
            // already fired one
            if (alertsStartTime.isBefore(now())) {
                LOGGER.info("Sending event {} to trigger repeating Alert  as Alert start time {} already elapsed. "
                        , event
                        , alertsStartTime);
                alertsStartTime = alertsStartTime.plusSeconds(repeatIntervalInSeconds);
                numberOfAlertsToFire = numberOfAlertsToFire - 1;
                LOGGER.info("Total number of remaining Alerts to fire are {}.", numberOfAlertsToFire);
                eventRelay.sendEventMessage(event);
                LOGGER.info("Event {} is sent to trigger Alert  as Alert start time {} already elapsed. ", event, alertsStartTime);
            }

            // Schedule the repeating job with the scheduler.
            RepeatingSchedulableJob job = new RepeatingSchedulableJob()
                    .setMotechEvent(event)
                    .setStartTime(alertsStartTime.toDate())
                    .setEndTime(null)
                    .setRepeatCount(numberOfAlertsToFire)
                    .setRepeatIntervalInSeconds(repeatIntervalInSeconds)
                    .setIgnorePastFiresAtStart(false);
            LOGGER.info("Scheduling repeatable Job to trigger Milestone Alert {} with Start Time {}.", event, alertsStartTime);

            schedulerService.safeScheduleRepeatingJob(job);
            LOGGER.info("Scheduled repeatable Job to trigger Milestone Alert {} with Start Time {}.", event, alertsStartTime);
        }
    }

    private AlertWindow createAlertWindowFor(Alert alert, Enrollment enrollment, Milestone currentMilestone, MilestoneWindow milestoneWindow) {
        Period windowStart = currentMilestone.getWindowStart(milestoneWindow.getName());
        Period windowEnd = currentMilestone.getWindowEnd(milestoneWindow.getName());

        DateTime currentMilestoneStartDate = enrollment.getCurrentMilestoneStartDate();

        DateTime windowStartDateTime = currentMilestoneStartDate.plus(windowStart);
        DateTime windowEndDateTime = currentMilestoneStartDate.plus(windowEnd);

        return new AlertWindow(windowStartDateTime, windowEndDateTime, enrollment.getEnrolledOn(), enrollment.getPreferredAlertTime(), alert);
    }
}
