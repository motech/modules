package org.motechproject.scheduletracking.service.impl;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.Milestone;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.events.MilestoneDefaultedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.scheduletracking.events.constants.EventSubjects.MILESTONE_DEFAULTED;

/**
 * Component to manage defaulted enrollments.
 */
@Component
public class EnrollmentDefaultmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentDefaultmentService.class);

    private MotechSchedulerService schedulerService;

    @Autowired
    public EnrollmentDefaultmentService(MotechSchedulerService schedulerService) {
        this.schedulerService = schedulerService;
    }

    /**
     * Schedules job to trigger defaultment alert for the given enrollment. Job is created when the current milestone
     * is not expired.
     *
     * @param enrollment the enrollment to mark as defaultment
     */
    public void scheduleJobToCaptureDefaultment(Enrollment enrollment) {
        Schedule schedule = enrollment.getSchedule();
        Milestone currentMilestone = schedule.getMilestone(enrollment.getCurrentMilestoneName());
        if (currentMilestone == null) {
            LOGGER.info("Exiting without scheduling defaultment alert as Current Milestone found to be null. ");
            return;
        }

        DateTime currentMilestoneStartDate = enrollment.getCurrentMilestoneStartDate();
        DateTime milestoneEndDateTime = currentMilestoneStartDate.plus(currentMilestone.getMaximumDuration());

        if (milestoneEndDateTime.isBefore(now())) {
            LOGGER.info("Exiting without scheduling defaultment alert as Current Milestone already expired.");
            return;
        }

        MotechEvent event = new MilestoneDefaultedEvent(enrollment.getId(), enrollment.getId().toString(), enrollment.getExternalId()).toMotechEvent();
        LOGGER.info("Scheduling job to trigger defaultment alert for enrollment with id {} ", enrollment.getId());
        schedulerService.safeScheduleRunOnceJob(new RunOnceSchedulableJob(event, milestoneEndDateTime.toDate()));
    }

    /**
     * Unschedules all the defaultment alert jobs for the given enrollment.
     *
     * @param enrollment the enrollment for which all defaultment jobs will be unscheduled.
     */
    public void unscheduleMilestoneDefaultedJob(Enrollment enrollment) {
        LOGGER.info("Un-scheduling all jobs for enrollment {}", enrollment.getId());
        schedulerService.safeUnscheduleAllJobs(String.format("%s-%s", MILESTONE_DEFAULTED, enrollment.getId()));
        LOGGER.info("Un-scheduled all jobs for enrollment {}", enrollment.getId());
    }
}
