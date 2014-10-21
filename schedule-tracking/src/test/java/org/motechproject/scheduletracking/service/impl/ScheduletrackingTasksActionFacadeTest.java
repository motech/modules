package org.motechproject.scheduletracking.service.impl;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduletracking.service.ScheduletrackingTasksActionFacade;
import org.motechproject.scheduletracking.service.EnrollmentRequest;
import org.motechproject.scheduletracking.service.ScheduleTrackingService;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;

public class ScheduletrackingTasksActionFacadeTest {

    @InjectMocks
    private ScheduletrackingTasksActionFacade scheduletrackingTasksActionFacade = new ScheduletrackingTasksActionFacadeImpl();

    @Mock
    ScheduleTrackingService scheduleTrackingService;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
    }

    @Test
    public void shouldEnrollFromEvent() {
        String externalId = "externalId";
        String scheduleName = "scheduleName";
        String preferredAlertTime = "12:20";
        DateTime referenceDate = DateTime.now();
        String referenceTime = "16:10";
        DateTime enrollmentDate = DateTime.now();
        String enrollmentTime = "20:30";
        String startingMilestoneName = "startingMilestoneName";

        scheduletrackingTasksActionFacade.enroll(externalId, scheduleName, preferredAlertTime, referenceDate, referenceTime, enrollmentDate, enrollmentTime, startingMilestoneName);

        ArgumentCaptor<EnrollmentRequest> captor = ArgumentCaptor.forClass(EnrollmentRequest.class);

        verify(scheduleTrackingService).enroll(captor.capture());

        EnrollmentRequest enrollmentRequest = captor.getValue();

        assertEquals(externalId, enrollmentRequest.getExternalId());
        assertEquals(scheduleName, enrollmentRequest.getScheduleName());
        assertEquals(new Time(preferredAlertTime), enrollmentRequest.getPreferredAlertTime());
        assertEquals(referenceDate.toLocalDate(), enrollmentRequest.getReferenceDate());
        assertEquals(new Time(referenceTime), enrollmentRequest.getReferenceTime());
        assertEquals(newDateTime(enrollmentDate.toLocalDate(), new Time(enrollmentTime)), enrollmentRequest.getEnrollmentDateTime());
        assertEquals(startingMilestoneName, enrollmentRequest.getStartingMilestoneName());

    }

    @Test
    public void shouldUnenrollFromEvent() {
        String externalId = "externalId";
        String scheduleName = "scheduleName";

        scheduletrackingTasksActionFacade.unenroll(externalId, scheduleName);

        verify(scheduleTrackingService).unenroll(externalId, Arrays.asList(scheduleName));
    }
}