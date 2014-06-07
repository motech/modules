package org.motechproject.scheduletracking.domain.search;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Milestone;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.domain.WindowName;
import org.motechproject.scheduletracking.repository.AllEnrollments;
import org.motechproject.scheduletracking.repository.AllSchedules;
import org.motechproject.scheduletracking.service.impl.EnrollmentServiceImpl;

import java.util.ArrayList;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.scheduletracking.utility.PeriodUtil.weeks;
import static org.powermock.api.mockito.PowerMockito.when;

public class InWindowCriterionTest {

    @Mock
    EnrollmentServiceImpl enrollmentService;
    @Mock
    private AllEnrollments allEnrollments;
    @Mock
    private AllSchedules allSchedules;

    @Before
    public void setup() {
        initMocks(this);
    }

    @Test
    public void shouldFilterByWindowName() {
        List<Enrollment> enrollments = new ArrayList<Enrollment>();
        Enrollment enrollment1 = mock(Enrollment.class);
        Enrollment enrollment2 = mock(Enrollment.class);
        Enrollment enrollment3 = mock(Enrollment.class);
        enrollments.addAll(asList(enrollment1, enrollment2, enrollment3));

        when(allEnrollments.getAll()).thenReturn(enrollments);

        when(enrollment1.getCurrentWindowAsOf(Matchers.<DateTime>any())).thenReturn(WindowName.earliest);
        when(enrollment2.getCurrentWindowAsOf(Matchers.<DateTime>any())).thenReturn(WindowName.due);
        when(enrollment3.getCurrentWindowAsOf(Matchers.<DateTime>any())).thenReturn(WindowName.earliest);

        assertEquals(asList(enrollment1, enrollment3), new InWindowCriterion((asList(WindowName.earliest))).fetch(allEnrollments));
        assertEquals(asList(enrollment1, enrollment3), new InWindowCriterion((asList(WindowName.earliest))).filter(enrollments));

        assertEquals(asList(enrollment2), new InWindowCriterion((asList(WindowName.due))).fetch(allEnrollments));
        assertEquals(asList(enrollment2), new InWindowCriterion((asList(WindowName.due))).filter(enrollments));
    }

    @Test
    public void shouldFilterPerformingAnOrOperationOverTheValues() {
        List<Enrollment> enrollments = new ArrayList<Enrollment>();
        Enrollment enrollment1 = mock(Enrollment.class);
        Enrollment enrollment2 = mock(Enrollment.class);
        Enrollment enrollment3 = mock(Enrollment.class);
        enrollments.addAll(asList(enrollment1, enrollment2, enrollment3));

        when(allEnrollments.getAll()).thenReturn(enrollments);

        when(enrollment1.getCurrentWindowAsOf(Matchers.<DateTime>any())).thenReturn(WindowName.earliest);
        when(enrollment2.getCurrentWindowAsOf(Matchers.<DateTime>any())).thenReturn(WindowName.due);
        when(enrollment3.getCurrentWindowAsOf(Matchers.<DateTime>any())).thenReturn(WindowName.late);

        assertEquals(asList(enrollment1, enrollment3), new InWindowCriterion((asList(WindowName.earliest, WindowName.late))).fetch(allEnrollments));
        assertEquals(asList(enrollment1, enrollment3), new InWindowCriterion((asList(WindowName.earliest, WindowName.late))).filter(enrollments));
    }

    @Test
    public void shouldReturnTheWindowAnEnrollmentIsInForTheCurrentMilestone() {
        Milestone firstMilestone = new Milestone("first_milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);
        Mockito.when(allSchedules.getByName("my_schedule")).thenReturn(schedule);

        DateTime referenceDate = newDateTime(2012, 12, 4, 8, 30, 0);
        Enrollment enrollment = new Enrollment().setExternalId("ID-074285").setSchedule(schedule).setCurrentMilestoneName("first_milestone").setStartOfSchedule(referenceDate).setEnrolledOn(referenceDate).setPreferredAlertTime(null).setStatus(EnrollmentStatus.ACTIVE).setMetadata(null);

        assertEquals(WindowName.earliest, enrollment.getCurrentWindowAsOf(newDateTime(2012, 12, 4, 8, 30, 0)));
        assertEquals(WindowName.earliest, enrollment.getCurrentWindowAsOf(newDateTime(2012, 12, 4, 8, 30, 1)));
        assertEquals(WindowName.due, enrollment.getCurrentWindowAsOf(newDateTime(2012, 12, 11, 9, 30, 0)));
        assertEquals(WindowName.due, enrollment.getCurrentWindowAsOf(newDateTime(2012, 12, 11, 8, 30, 1)));
        assertEquals(WindowName.late, enrollment.getCurrentWindowAsOf(newDateTime(2012, 12, 18, 9, 30, 0)));
        assertEquals(WindowName.max, enrollment.getCurrentWindowAsOf(newDateTime(2012, 12, 28, 9, 30, 0)));
        assertEquals(null, enrollment.getCurrentWindowAsOf(newDateTime(2013, 1, 28, 9, 30, 0)));
    }
}
