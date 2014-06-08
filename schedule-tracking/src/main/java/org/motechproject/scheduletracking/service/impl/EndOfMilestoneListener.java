package org.motechproject.scheduletracking.service.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.events.DefaultmentCaptureEvent;
import org.motechproject.scheduletracking.repository.AllEnrollments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static org.motechproject.scheduletracking.domain.EnrollmentStatus.DEFAULTED;
import static org.motechproject.scheduletracking.events.constants.EventSubjects.DEFAULTMENT_CAPTURE;

@Component
public class EndOfMilestoneListener {
    private AllEnrollments allEnrollments;

    @Autowired
    public EndOfMilestoneListener(AllEnrollments allEnrollments) {
        this.allEnrollments = allEnrollments;
    }

    @MotechListener(subjects = DEFAULTMENT_CAPTURE)
    public void handle(MotechEvent motechEvent) {
        DefaultmentCaptureEvent event = new DefaultmentCaptureEvent(motechEvent);
        Enrollment enrollment = allEnrollments.get(event.getEnrollmentId());
        enrollment.setStatus(DEFAULTED);
        allEnrollments.update(enrollment);
    }
}
