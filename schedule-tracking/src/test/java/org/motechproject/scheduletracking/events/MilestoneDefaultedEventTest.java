package org.motechproject.scheduletracking.events;

import org.junit.Test;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduletracking.events.constants.EventDataKeys;
import org.motechproject.scheduletracking.events.constants.EventSubjects;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class MilestoneDefaultedEventTest {
    @Test
    public void shouldCreateMotechEvent() {
        Long enrollmentId = 567L;
        String jobId = "jobId";
        final String externalId = "externalId";

        MilestoneDefaultedEvent milestoneDefaultedEvent = new MilestoneDefaultedEvent(enrollmentId, jobId, externalId);
        MotechEvent motechEvent = milestoneDefaultedEvent.toMotechEvent();

        assertEquals(EventSubjects.MILESTONE_DEFAULTED, motechEvent.getSubject());
        Map<String, Object> parameters = motechEvent.getParameters();
        assertEquals(enrollmentId, parameters.get(EventDataKeys.ENROLLMENT_ID));
        assertEquals(jobId, parameters.get(MotechSchedulerService.JOB_ID_KEY));
        assertEquals(externalId, parameters.get(EventDataKeys.EXTERNAL_ID));
    }
}
