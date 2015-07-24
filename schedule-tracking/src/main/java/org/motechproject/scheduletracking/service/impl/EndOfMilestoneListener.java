package org.motechproject.scheduletracking.service.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.events.MilestoneDefaultedEvent;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.scheduletracking.domain.EnrollmentStatus.DEFAULTED;
import static org.motechproject.scheduletracking.events.constants.EventSubjects.MILESTONE_DEFAULTED;

@Component
public class EndOfMilestoneListener {
    private EnrollmentDataService enrollmentDataService;
    private static final Logger LOGGER = LoggerFactory.getLogger(EndOfMilestoneListener.class);

    @Autowired
    public EndOfMilestoneListener(EnrollmentDataService enrollmentDataService) {
        this.enrollmentDataService = enrollmentDataService;
    }

    @MotechListener(subjects = MILESTONE_DEFAULTED)
    public void handle(MotechEvent motechEvent) {
        LOGGER.info("Handling {} Event : {}.", MILESTONE_DEFAULTED, motechEvent);
        MilestoneDefaultedEvent event = new MilestoneDefaultedEvent(motechEvent);
        Enrollment enrollment = enrollmentDataService.findById(event.getEnrollmentId());
        enrollment.setStatus(DEFAULTED);
        LOGGER.info("Defaulting enrollment with id {}.", enrollment.getId());
        enrollmentDataService.update(enrollment);
        LOGGER.info("Enrollment with id {} is defaulted.", enrollment.getId());
    }
}
