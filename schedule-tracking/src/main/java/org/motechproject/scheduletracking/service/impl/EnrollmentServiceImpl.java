package org.motechproject.scheduletracking.service.impl;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentBuilder;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.domain.exception.NoMoreMilestonesToFulfillException;
import org.motechproject.scheduletracking.events.EnrolledUserEvent;
import org.motechproject.scheduletracking.events.UnenrolledUserEvent;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.motechproject.scheduletracking.service.EnrollmentService;
import org.motechproject.scheduletracking.service.MilestoneAlerts;
import org.motechproject.scheduletracking.repository.dataservices.ScheduleDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

import static org.motechproject.scheduletracking.domain.EnrollmentStatus.COMPLETED;
import static org.motechproject.scheduletracking.domain.EnrollmentStatus.UNENROLLED;

@Component
public class EnrollmentServiceImpl implements EnrollmentService {

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
        Schedule schedule = scheduleDataService.findByName(scheduleName);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(startingMilestoneName).withStartOfSchedule(referenceDateTime).withEnrolledOn(enrollmentDateTime).withPreferredAlertTime(preferredAlertTime).withStatus(EnrollmentStatus.ACTIVE).withMetadata(metadata).toEnrollment();

        if (schedule.hasExpiredSince(enrollment.getCurrentMilestoneStartDate(), startingMilestoneName)) {
            enrollment.setStatus(EnrollmentStatus.DEFAULTED);
        }

        Enrollment activeEnrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName, EnrollmentStatus.ACTIVE);
        if (activeEnrollment == null) {
            enrollmentDataService.create(enrollment);
            eventRelay.sendEventMessage(new EnrolledUserEvent(enrollment.getExternalId(),
                    enrollment.getScheduleName(), enrollment.getPreferredAlertTime(), referenceDateTime,
                    enrollmentDateTime, enrollment.getCurrentMilestoneName()).toMotechEvent());
        } else {
            unscheduleJobs(activeEnrollment);
            enrollment = activeEnrollment.copyFrom(enrollment);
            enrollmentDataService.update(enrollment);
        }

        scheduleJobs(enrollment);
        return enrollment.getId();
    }

    @Override
    public void fulfillCurrentMilestone(Enrollment enrollment, DateTime fulfillmentDateTime) {
        Schedule schedule = scheduleDataService.findByName(enrollment.getScheduleName());
        if (StringUtils.isBlank(enrollment.getCurrentMilestoneName())) {
            throw new NoMoreMilestonesToFulfillException();
        }

        unscheduleJobs(enrollment);

        enrollment.fulfillCurrentMilestone(fulfillmentDateTime);
        String nextMilestoneName = schedule.getNextMilestoneName(enrollment.getCurrentMilestoneName());
        enrollment.setCurrentMilestoneName(nextMilestoneName);
        if (nextMilestoneName == null) {
            enrollment.setStatus(COMPLETED);
        } else {
            scheduleJobs(enrollment);
        }

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
    public MilestoneAlerts getAlertTimings(String externalId, String scheduleName, String milestoneName, DateTime referenceDateTime, DateTime enrollmentDateTime, Time preferredAlertTime) {
        Schedule schedule = scheduleDataService.findByName(scheduleName);
        return enrollmentAlertService.getAlertTimings(new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(milestoneName).withStartOfSchedule(referenceDateTime).withEnrolledOn(enrollmentDateTime).withPreferredAlertTime(preferredAlertTime).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
    }
}
