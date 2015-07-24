package org.motechproject.scheduletracking.service.impl;

import org.joda.time.DateTime;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduletracking.service.EnrollmentRequest;
import org.motechproject.scheduletracking.service.ScheduleTrackingService;
import org.motechproject.scheduletracking.service.ScheduletrackingTasksActionFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;


public class ScheduletrackingTasksActionFacadeImpl implements ScheduletrackingTasksActionFacade {

    @Autowired
    private ScheduleTrackingService scheduleTrackingService;

    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduletrackingTasksActionFacadeImpl.class);

    public void enroll(String externalId, String scheduleName, // NO CHECKSTYLE ParameterNumber
                       String preferredAlertTime, DateTime referenceDate, String referenceTime,
                       DateTime enrolmentDate, String enrollmentTime, String startingMilestoneName) {
        EnrollmentRequest enrollmentRequest = new EnrollmentRequest()
                .setExternalId(externalId)
                .setScheduleName(scheduleName)
                .setPreferredAlertTime(new Time(preferredAlertTime))
                .setReferenceDate(referenceDate.toLocalDate())
                .setReferenceTime(new Time(referenceTime))
                .setEnrollmentDate(enrolmentDate.toLocalDate())
                .setEnrollmentTime(new Time(enrollmentTime))
                .setStartingMilestoneName(startingMilestoneName);
        LOGGER.info("Enrolling client with case id {} for schedule {}.", enrollmentRequest.getExternalId(), enrollmentRequest.getScheduleName());
        scheduleTrackingService.enroll(enrollmentRequest);
        LOGGER.info("Client with case id {} for Schedule {} is enrolled.", enrollmentRequest.getExternalId(), enrollmentRequest.getScheduleName());

    }

    public void unenroll(String externalId, String scheduleName) {
        LOGGER.info("Un-enrolling client with case id {} for schedule {}.", externalId, scheduleName);
        scheduleTrackingService.unenroll(externalId, Arrays.asList(scheduleName));
        LOGGER.info("Client with case id {} for schedule/schedules {} is un-enrolled.", externalId, scheduleName);
    }
}
