package org.motechproject.scheduletracking.service.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentBuilder;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.domain.exception.NoMoreMilestonesToFulfillException;
import org.motechproject.scheduletracking.events.EnrolledUserEvent;
import org.motechproject.scheduletracking.events.UnenrolledUserEvent;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.motechproject.scheduletracking.repository.dataservices.ScheduleDataService;
import org.motechproject.scheduletracking.service.EnrollmentService;
import org.motechproject.scheduletracking.service.MilestoneAlerts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.scheduletracking.domain.EnrollmentStatus.COMPLETED;
import static org.motechproject.scheduletracking.domain.EnrollmentStatus.UNENROLLED;

/**
 * Implementation of the {@link org.motechproject.scheduletracking.service.EnrollmentService}.
 */
@Component
public class EnrollmentServiceImpl implements EnrollmentService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EnrollmentServiceImpl.class);
    private ScheduleDataService scheduleDataService;
    private EnrollmentDataService enrollmentDataService;
    private EnrollmentAlertService enrollmentAlertService;
    private EnrollmentDefaultmentService enrollmentDefaultmentService;
    private EventRelay eventRelay;

    @Autowired
    public EnrollmentServiceImpl(ScheduleDataService scheduleDataService, EnrollmentDataService enrollmentDataService,
                                 EnrollmentAlertService enrollmentAlertService,
                                 EnrollmentDefaultmentService enrollmentDefaultmentService, EventRelay eventRelay) {
        this.scheduleDataService = scheduleDataService;
        this.enrollmentDataService = enrollmentDataService;
        this.enrollmentAlertService = enrollmentAlertService;
        this.enrollmentDefaultmentService = enrollmentDefaultmentService;
        this.eventRelay = eventRelay;
    }

    @Override
    public Long enroll(String externalId, String scheduleName, String startingMilestoneName, DateTime referenceDateTime, DateTime enrollmentDateTime, Time preferredAlertTime, Map<String, String> metadata) {
        LOGGER.info("Finding by Schedule Name {}", scheduleName);
        Schedule schedule = scheduleDataService.findByName(scheduleName);
        LOGGER.info("Found Schedule {} ", schedule.getName());
        LOGGER.info("Creating Enrollment for MileStone {} for client with case id {} ", startingMilestoneName, externalId);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(startingMilestoneName).withStartOfSchedule(referenceDateTime).withEnrolledOn(enrollmentDateTime).withPreferredAlertTime(preferredAlertTime).withStatus(EnrollmentStatus.ACTIVE).withMetadata(metadata).toEnrollment();
        LOGGER.info("Verifying Milestone {} is not expired.", startingMilestoneName);
        if (schedule.hasExpiredSince(enrollment.getCurrentMilestoneStartDate(), startingMilestoneName)) {
            enrollment.setStatus(EnrollmentStatus.DEFAULTED);
        }
        LOGGER.info("Finding active enrollment for client with case id {} and schedule name {}", externalId, scheduleName);
        Enrollment activeEnrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName, EnrollmentStatus.ACTIVE);
        if (activeEnrollment == null) {
            LOGGER.info("No active enrollment with Milestone {} found.", enrollment.getCurrentMilestoneName());
            LOGGER.info("Persisting new enrollment.");
            enrollmentDataService.create(enrollment);
            MotechEvent event = new EnrolledUserEvent(enrollment.getExternalId(),
                    enrollment.getScheduleName(), enrollment.getPreferredAlertTime(), referenceDateTime,
                    enrollmentDateTime, enrollment.getCurrentMilestoneName()).toMotechEvent();
            LOGGER.info("Raising Enrolled User Event {}.", event);
            eventRelay.sendEventMessage(event);
        } else {
            LOGGER.info("Found active enrollment {} with Milestone {}. ", activeEnrollment.getId(), enrollment.getCurrentMilestoneName());
            LOGGER.info("Un scheduling both defaulted as well as milestone job for enrollment id {}."
                    , activeEnrollment.getId());
            unscheduleJobs(activeEnrollment);
            enrollment = activeEnrollment.copyFrom(enrollment);
            LOGGER.info("Updating enrollment ");
            enrollmentDataService.update(enrollment);
        }
        scheduleJobs(enrollment);
        return enrollment.getId();
    }

    @Override
    public void fulfillCurrentMilestone(Enrollment enrollment, DateTime fulfillmentDateTime) {
        LOGGER.info("Finding by Schedule Name {}", enrollment.getScheduleName());
        Schedule schedule = scheduleDataService.findByName(enrollment.getScheduleName());
        if (StringUtils.isBlank(enrollment.getCurrentMilestoneName())) {
            throw new NoMoreMilestonesToFulfillException();
        }
        LOGGER.info("Fulfilling Milestone {} for enrollment with id {}", enrollment.getCurrentMilestoneName(), enrollment.getId());
        LOGGER.info("Un scheduling both defaulted as well as milestone job for enrollment id {}.", enrollment.getId());
        unscheduleJobs(enrollment);

        enrollment.fulfillCurrentMilestone(fulfillmentDateTime);
        String nextMilestoneName = schedule.getNextMilestoneName(enrollment.getCurrentMilestoneName());
        enrollment.setCurrentMilestoneName(nextMilestoneName);
        if (nextMilestoneName == null) {
            LOGGER.info("Since there is no next milestone, changing enrollment ({}) status as completed.", enrollment.getId());
            enrollment.setStatus(COMPLETED);
        } else {
            LOGGER.info("Since there is a next milestone {}, scheduling defaulted and milestone job for enrollment ({}).", nextMilestoneName, enrollment.getId());
            scheduleJobs(enrollment);
        }
        LOGGER.info("Updating Enrollment with id {}. ", enrollment.getId());
        enrollmentDataService.update(enrollment);
    }

    @Override
    public void unenroll(Enrollment enrollment) {
        unscheduleJobs(enrollment);
        enrollment.setStatus(UNENROLLED);
        enrollmentDataService.update(enrollment);
        eventRelay.sendEventMessage(new UnenrolledUserEvent(enrollment.getExternalId(), enrollment.getScheduleName()).toMotechEvent());
    }

    private void scheduleJobs(Enrollment enrollment) {
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);
        enrollmentDefaultmentService.scheduleJobToCaptureDefaultment(enrollment);
    }

    private void unscheduleJobs(Enrollment enrollment) {
        enrollmentAlertService.unscheduleAllAlerts(enrollment);
        enrollmentDefaultmentService.unscheduleMilestoneDefaultedJob(enrollment);
    }

    @Override
    public MilestoneAlerts getAlertTimings(String externalId, String scheduleName, String milestoneName, DateTime referenceDateTime,
                                           DateTime enrollmentDateTime, Time preferredAlertTime) {
        Schedule schedule = scheduleDataService.findByName(scheduleName);
        return enrollmentAlertService.getAlertTimings(new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).
                withCurrentMilestoneName(milestoneName).withStartOfSchedule(referenceDateTime).withEnrolledOn(enrollmentDateTime).
                withPreferredAlertTime(preferredAlertTime).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
    }
}
