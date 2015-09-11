package org.motechproject.scheduletracking.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentBuilder;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.events.MilestoneDefaultedEvent;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.scheduletracking.utility.DateTimeUtil.weeksAgo;

public class EndOfMilestoneListenerTest {
    private EndOfMilestoneListener endOfMilestoneListener;

    @Mock
    private EnrollmentDataService enrollmentDataService;

    @Before
    public void setup() {
        initMocks(this);
        endOfMilestoneListener = new EndOfMilestoneListener();
        endOfMilestoneListener.setEnrollmentDataService(enrollmentDataService);
    }

    @Test
    public void shouldDefaultEnrollmentAtTheCurrentMilestoneIfNotFulfilled() {
        Schedule schedule = new Schedule("some_schedule");
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("first_milestone").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(4)).withPreferredAlertTime(new Time(8, 10)).withStatus(null).withMetadata(null).toEnrollment();
        enrollment.setId(1L);
        when(enrollmentDataService.findById(1L)).thenReturn(enrollment);

        MotechEvent event = new MilestoneDefaultedEvent(1L, "job_id", "externalId").toMotechEvent();
        endOfMilestoneListener.handle(event);
        verify(enrollmentDataService).update(enrollment);
    }
}
