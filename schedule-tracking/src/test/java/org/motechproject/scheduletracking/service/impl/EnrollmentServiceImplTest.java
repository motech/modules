package org.motechproject.scheduletracking.service.impl;

import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduletracking.domain.Alert;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentBuilder;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Milestone;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.domain.WindowName;
import org.motechproject.scheduletracking.domain.exception.NoMoreMilestonesToFulfillException;
import org.motechproject.scheduletracking.events.EnrolledUserEvent;
import org.motechproject.scheduletracking.events.UnenrolledUserEvent;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.motechproject.scheduletracking.service.MilestoneAlerts;
import org.motechproject.scheduletracking.repository.dataservices.ScheduleDataService;
import org.motechproject.testing.utils.BaseUnitTest;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.scheduletracking.domain.EnrollmentStatus.ACTIVE;
import static org.motechproject.scheduletracking.utility.DateTimeUtil.weeksAgo;
import static org.motechproject.scheduletracking.utility.PeriodUtil.days;
import static org.motechproject.scheduletracking.utility.PeriodUtil.weeks;

public class EnrollmentServiceImplTest extends BaseUnitTest {
    private EnrollmentServiceImpl enrollmentService;

    @Mock
    private EnrollmentDataService enrollmentDataService;
    @Mock
    private ScheduleDataService scheduleDataService;
    @Mock
    private EnrollmentAlertService enrollmentAlertService;
    @Mock
    private EnrollmentDefaultmentService enrollmentDefaultmentService;
    @Mock
    private EventRelay eventRelay;

    @Before
    public void setup() {
        initMocks(this);
        enrollmentService = new EnrollmentServiceImpl(scheduleDataService, enrollmentDataService, enrollmentAlertService, enrollmentDefaultmentService, eventRelay);
    }

    @Test
    public void shouldEnrollEntityIntoSchedule() {
        String externalId = "entity_1";
        String scheduleName = "my_schedule";
        DateTime referenceDate = weeksAgo(0);
        DateTime enrollmentDate = weeksAgo(0);
        Time preferredAlertTime = new Time(8, 10);

        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 3, 0, false));
        milestone.addAlert(WindowName.due, new Alert(days(0), weeks(1), 2, 1, false));
        Schedule schedule = new Schedule(scheduleName);
        schedule.addMilestones(milestone);
        when(scheduleDataService.findByName(scheduleName)).thenReturn(schedule);
        when(enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName, EnrollmentStatus.ACTIVE)).thenReturn(null);

        Enrollment dummyEnrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(milestone.getName()).withStartOfSchedule(referenceDate).withEnrolledOn(enrollmentDate).withPreferredAlertTime(preferredAlertTime).withStatus(ACTIVE).withMetadata(null).toEnrollment();
        dummyEnrollment.setId(1L);

        Map<String, String> metadata = new HashMap<String, String>();
        enrollmentService.enroll(externalId, scheduleName, milestone.getName(), referenceDate, enrollmentDate, preferredAlertTime, metadata);

        verify(enrollmentDataService, times(0)).update(Matchers.<Enrollment>any());
        ArgumentCaptor<Enrollment> enrollmentArgumentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentDataService, times(1)).create(enrollmentArgumentCaptor.capture());

        Enrollment enrollment = enrollmentArgumentCaptor.getValue();

        assertEnrollment(enrollment, externalId, scheduleName, milestone, ACTIVE, schedule, metadata);

        MotechEvent event = new EnrolledUserEvent(enrollment.getExternalId(), enrollment.getScheduleName(), enrollment.getPreferredAlertTime(), referenceDate, enrollmentDate, enrollment.getCurrentMilestoneName()).toMotechEvent();
        verify(eventRelay).sendEventMessage(event);

        verify(enrollmentAlertService).scheduleAlertsForCurrentMilestone(enrollmentArgumentCaptor.capture());
        assertEnrollment(enrollment, externalId, scheduleName, milestone, ACTIVE, schedule, metadata);

        verify(enrollmentDefaultmentService).scheduleJobToCaptureDefaultment(enrollment);
        assertEnrollment(enrollment, externalId, scheduleName, milestone, ACTIVE, schedule, metadata);

        verify(enrollmentAlertService, times(0)).unscheduleAllAlerts(Matchers.<Enrollment>any());
        verify(enrollmentDefaultmentService, times(0)).unscheduleMilestoneDefaultedJob(Matchers.<Enrollment>any());
    }

    @Test
    public void shouldUnenrollOldEntityIfTheSameEntityIsEnrolledAgain() {
        String externalId = "entity_1";
        String scheduleName = "my_schedule";
        DateTime referenceDate = weeksAgo(0);
        DateTime enrollmentDate = weeksAgo(0);
        Time preferredAlertTime = new Time(8, 10);

        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 3, 0, false));
        milestone.addAlert(WindowName.due, new Alert(days(0), weeks(1), 2, 1, false));
        Schedule schedule = new Schedule(scheduleName);
        schedule.addMilestones(milestone);
        when(scheduleDataService.findByName(scheduleName)).thenReturn(schedule);
        Enrollment dummyEnrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(milestone.getName()).withStartOfSchedule(referenceDate).withEnrolledOn(enrollmentDate).withPreferredAlertTime(preferredAlertTime).withStatus(ACTIVE).withMetadata(null).toEnrollment();
        Enrollment oldEnrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(null).withEnrolledOn(null).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        when(enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName, EnrollmentStatus.ACTIVE)).thenReturn(oldEnrollment);

        dummyEnrollment.setId(1L);

        Map<String, String> metadata = new HashMap<String, String>();
        enrollmentService.enroll(externalId, scheduleName, milestone.getName(), referenceDate, enrollmentDate, preferredAlertTime, metadata);

        ArgumentCaptor<Enrollment> enrollmentArgumentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentDataService, times(1)).update(enrollmentArgumentCaptor.capture());
        assertEnrollment(enrollmentArgumentCaptor.getValue(), externalId, scheduleName, milestone, ACTIVE, schedule, metadata);
        verify(enrollmentDataService, times(0)).create(Matchers.<Enrollment>any());


        verify(enrollmentAlertService).unscheduleAllAlerts(enrollmentArgumentCaptor.capture());
        assertEnrollment(enrollmentArgumentCaptor.getValue(), externalId, scheduleName, new Milestone("milestone", null, null, null, null), EnrollmentStatus.ACTIVE, schedule, metadata);

        verify(enrollmentDefaultmentService).unscheduleMilestoneDefaultedJob(enrollmentArgumentCaptor.capture());
        assertEnrollment(enrollmentArgumentCaptor.getValue(), externalId, scheduleName, new Milestone("milestone", null, null, null, null), EnrollmentStatus.ACTIVE, schedule, metadata);

        verify(enrollmentAlertService, times(1)).scheduleAlertsForCurrentMilestone(enrollmentArgumentCaptor.capture());
        assertEnrollment(enrollmentArgumentCaptor.getValue(), externalId, scheduleName, milestone, ACTIVE, schedule, metadata);

        verify(enrollmentDefaultmentService, times(1)).scheduleJobToCaptureDefaultment(enrollmentArgumentCaptor.getValue());
        assertEnrollment(enrollmentArgumentCaptor.getValue(), externalId, scheduleName, milestone, ACTIVE, schedule, metadata);

    }

    @Test
    public void shouldEnrollEntityAsDefaultedOneIfMilestoneDurationHasExpired() {
        String externalId = "entity_1";
        String externalId2 = "entity_2";
        String externalId3 = "entity_3";
        String scheduleName = "my_schedule";
        DateTime now = newDateTime(2012, 4, 4, 3, 3, 59);
        DateTime referenceDateTime = now.minusDays(29);
        DateTime enrollmentDateTime = now;
        Time preferredAlertTime = new Time(8, 10);
        EnrollmentStatus enrollmentStatus = EnrollmentStatus.DEFAULTED;

        mockCurrentDate(now);
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone milestone2 = new Milestone("milestone2", weeks(4), weeks(1), weeks(1), weeks(0));
        Schedule schedule = new Schedule(scheduleName);
        schedule.addMilestones(milestone, milestone2);
        when(scheduleDataService.findByName(scheduleName)).thenReturn(schedule);
        Enrollment dummyEnrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(milestone.getName()).withStartOfSchedule(referenceDateTime).withEnrolledOn(enrollmentDateTime).withPreferredAlertTime(preferredAlertTime).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        dummyEnrollment.setId(1L);
        when(enrollmentDataService.findByExternalIdScheduleNameAndStatus(externalId, scheduleName, EnrollmentStatus.ACTIVE)).thenReturn(null);

        enrollmentService.enroll(externalId, scheduleName, milestone.getName(), referenceDateTime, enrollmentDateTime, preferredAlertTime, null);
        DateTime enrollmentDateTimeBeforeMilestone2 = now.minusWeeks(6).minusSeconds(1);
        enrollmentService.enroll(externalId2, scheduleName, milestone2.getName(), null, enrollmentDateTimeBeforeMilestone2, preferredAlertTime, null);
        DateTime enrollmentDateTimeInMilestone2Start = now.minusWeeks(6);
        enrollmentService.enroll(externalId3, scheduleName, milestone2.getName(), null, enrollmentDateTimeInMilestone2Start, preferredAlertTime, null);

        verify(enrollmentDataService, times(0)).update(Matchers.<Enrollment>any());

        ArgumentCaptor<Enrollment> enrollmentArgumentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentDataService, times(3)).create(enrollmentArgumentCaptor.capture());
        assertEnrollment(enrollmentArgumentCaptor.getAllValues().get(0), externalId, scheduleName, milestone, enrollmentStatus, schedule, null);
        assertEnrollment(enrollmentArgumentCaptor.getAllValues().get(1), externalId2, scheduleName, milestone2, EnrollmentStatus.DEFAULTED, schedule, null);
        assertEnrollment(enrollmentArgumentCaptor.getAllValues().get(2), externalId3, scheduleName, milestone2, EnrollmentStatus.ACTIVE, schedule, null);
    }

    private void assertEnrollment(Enrollment enrollment, String externalId, String scheduleName, Milestone milestone, EnrollmentStatus enrollmentStatus, Schedule schedule, Map<String, String> metadata) {
        assertEquals(externalId, enrollment.getExternalId());
        assertEquals(scheduleName, enrollment.getScheduleName());
        assertEquals(milestone.getName(), enrollment.getCurrentMilestoneName());
        assertEquals(enrollmentStatus, enrollment.getStatus());
        assertEquals(schedule, enrollment.getSchedule());
        assertEquals(metadata, enrollment.getMetadata());
    }

    @Test
    public void shouldFulfillCurrentMilestoneInEnrollment() {
        Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone secondMilestone = new Milestone("Second Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        secondMilestone.addAlert(WindowName.earliest, new Alert(days(0), weeks(1), 3, 0, false));
        Schedule schedule = new Schedule("Yellow Fever Vaccination");
        schedule.addMilestones(firstMilestone, secondMilestone);
        when(scheduleDataService.findByName("Yellow Fever Vaccination")).thenReturn(schedule);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(4)).withPreferredAlertTime(new Time(8, 20)).withStatus(ACTIVE).withMetadata(null).toEnrollment();
        DateTime now = now();
        enrollmentService.fulfillCurrentMilestone(enrollment, now);

        assertEquals("Second Shot", enrollment.getCurrentMilestoneName());
        assertEquals(now, enrollment.getLastFulfilledDate());

        verify(enrollmentDataService).update(enrollment);
    }

    @Test
    public void shouldScheduleJobsForNextMilestoneWhenCurrentMilestoneIsFulfilled() {
        Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone secondMilestone = new Milestone("Second Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        secondMilestone.addAlert(WindowName.earliest, new Alert(days(0), weeks(1), 3, 0, false));
        Schedule schedule = new Schedule("Yellow Fever Vaccination");
        schedule.addMilestones(firstMilestone, secondMilestone);
        when(scheduleDataService.findByName("Yellow Fever Vaccination")).thenReturn(schedule);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(4)).withPreferredAlertTime(new Time(8, 20)).withStatus(ACTIVE).withMetadata(null).toEnrollment();
        enrollment.setId(1L);
        enrollmentService.fulfillCurrentMilestone(enrollment, null);

        verify(enrollmentAlertService).unscheduleAllAlerts(enrollment);
        verify(enrollmentDefaultmentService).unscheduleMilestoneDefaultedJob(enrollment);

        ArgumentCaptor<Enrollment> updatedEnrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentAlertService).scheduleAlertsForCurrentMilestone(updatedEnrollmentCaptor.capture());
        assertEquals("Second Shot", updatedEnrollmentCaptor.getValue().getCurrentMilestoneName());

        verify(enrollmentDefaultmentService).scheduleJobToCaptureDefaultment(updatedEnrollmentCaptor.capture());
        assertEquals("Second Shot", updatedEnrollmentCaptor.getValue().getCurrentMilestoneName());

        verify(enrollmentDataService).update(enrollment);
    }

    @Test(expected = NoMoreMilestonesToFulfillException.class)
    public void shouldThrowExceptionIfAllMilestonesAreFulfilled() {
        Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone secondMilestone = new Milestone("Second Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Schedule schedule = new Schedule("Yellow Fever Vaccination");
        schedule.addMilestones(firstMilestone, secondMilestone);
        when(scheduleDataService.findByName("Yellow Fever Vaccination")).thenReturn(schedule);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(4)).withPreferredAlertTime(new Time(8, 20)).withStatus(ACTIVE).withMetadata(null).toEnrollment();
        enrollmentService.fulfillCurrentMilestone(enrollment, null);
        enrollmentService.fulfillCurrentMilestone(enrollment, null);
        enrollmentService.fulfillCurrentMilestone(enrollment, null);

        assertEquals(null, enrollment.getCurrentMilestoneName());
    }

    @Test
    public void shouldCompleteEnrollmentWhenAllMilestonesAreFulfilled() {
        Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone secondMilestone = new Milestone("Second Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        String scheduleName = "Yellow Fever Vaccination";
        Schedule schedule = new Schedule(scheduleName);
        schedule.addMilestones(firstMilestone, secondMilestone);
        when(scheduleDataService.findByName(scheduleName)).thenReturn(schedule);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(4)).withPreferredAlertTime(new Time(8, 20)).withStatus(ACTIVE).withMetadata(null).toEnrollment();
        enrollmentService.fulfillCurrentMilestone(enrollment, null);
        enrollmentService.fulfillCurrentMilestone(enrollment, null);

        assertEquals(true, enrollment.isCompleted());
        verify(enrollmentDataService, times(2)).update(enrollment);
    }

    @Test
    public void shouldNotHaveAnyJobsScheduledAfterEnrollmentIsComplete() {
        Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Schedule schedule = new Schedule("Yellow Fever Vaccination");
        schedule.addMilestones(firstMilestone);
        when(scheduleDataService.findByName("Yellow Fever Vaccination")).thenReturn(schedule);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(4)).withPreferredAlertTime(new Time(8, 20)).withStatus(ACTIVE).withMetadata(null).toEnrollment();
        enrollmentService.fulfillCurrentMilestone(enrollment, null);

        verify(enrollmentAlertService).unscheduleAllAlerts(enrollment);
        verify(enrollmentDefaultmentService).unscheduleMilestoneDefaultedJob(enrollment);
        verify(enrollmentAlertService, times(0)).scheduleAlertsForCurrentMilestone(enrollment);
        verify(enrollmentDefaultmentService, times(0)).scheduleJobToCaptureDefaultment(enrollment);
    }

    @Test
    public void shouldUnenrollEntityFromTheSchedule() {
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 3, 0, false));
        String scheduleName = "my_schedule";
        Schedule schedule = new Schedule(scheduleName);
        schedule.addMilestones(milestone);
        when(scheduleDataService.findByName(scheduleName)).thenReturn(schedule);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(4)).withPreferredAlertTime(new Time(8, 10)).withStatus(ACTIVE).withMetadata(null).toEnrollment();
        enrollment.setId(1L);
        enrollmentService.unenroll(enrollment);

        ArgumentCaptor<Enrollment> enrollmentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentDataService).update(enrollmentCaptor.capture());

        enrollment = enrollmentCaptor.getValue();

        MotechEvent event = new UnenrolledUserEvent(enrollment.getExternalId(), enrollment.getScheduleName()).toMotechEvent();
        verify(eventRelay).sendEventMessage(event);

        Assert.assertEquals("entity_1", enrollment.getExternalId());
        Assert.assertEquals(scheduleName, enrollment.getScheduleName());
        assertEquals(false, enrollment.isActive());

        verify(enrollmentAlertService).unscheduleAllAlerts(enrollment);
        verify(enrollmentDefaultmentService).unscheduleMilestoneDefaultedJob(enrollment);
    }

    @Test
    public void shouldReturnReferenceDateWhenCurrentMilestoneIsTheFirstMilestone() {
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(milestone);
        when(scheduleDataService.findByName("my_schedule")).thenReturn(schedule);

        DateTime startOfSchedule = weeksAgo(5);
        DateTime enrolledOn = weeksAgo(3);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(startOfSchedule).withEnrolledOn(enrolledOn).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        assertEquals(startOfSchedule, enrollment.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnEnrollmentDateWhenEnrolledIntoSecondMilestoneAndNoMilestonesFulfilled() {
        Milestone firstMilestone = new Milestone("first_milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone secondMilestone = new Milestone("second_milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone, secondMilestone);
        when(scheduleDataService.findByName("my_schedule")).thenReturn(schedule);

        DateTime expected = weeksAgo(3);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("second_milestone").withStartOfSchedule(weeksAgo(5)).withEnrolledOn(expected).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        assertEquals(expected, enrollment.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnTheEndOfAGivenWindowForTheCurrentMilestone() {
        Milestone firstMilestone = new Milestone("first_milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);
        when(scheduleDataService.findByName("my_schedule")).thenReturn(schedule);

        DateTime referenceDate = newDateTime(2012, 12, 4, 8, 30, 0);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("first_milestone").withStartOfSchedule(referenceDate).withEnrolledOn(referenceDate).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(referenceDate.plusWeeks(1), enrollment.getEndOfWindowForCurrentMilestone(WindowName.earliest));
        assertEquals(referenceDate.plusWeeks(2), enrollment.getEndOfWindowForCurrentMilestone(WindowName.due));
        assertEquals(referenceDate.plusWeeks(3), enrollment.getEndOfWindowForCurrentMilestone(WindowName.late));
        assertEquals(referenceDate.plusWeeks(4), enrollment.getEndOfWindowForCurrentMilestone(WindowName.max));
    }

    @Test
    public void ShouldInvokeEnrollmentAlertServiceToGetAlertTimings() {
        DateTime now = now();
        Schedule schedule = new Schedule("my_schedule");
        when(scheduleDataService.findByName("my_schedule")).thenReturn(schedule);
        MilestoneAlerts mockedMilestoneAlerts = mock(MilestoneAlerts.class);
        when(enrollmentAlertService.getAlertTimings(any(Enrollment.class))).thenReturn(mockedMilestoneAlerts);

        MilestoneAlerts milestoneAlerts = enrollmentService.getAlertTimings("external_id", "my_schedule", "milestone_1", now, now, new Time(8, 15));

        ArgumentCaptor<Enrollment> enrollmentArgumentCaptor = ArgumentCaptor.forClass(Enrollment.class);
        verify(enrollmentAlertService).getAlertTimings(enrollmentArgumentCaptor.capture());
        Enrollment enrollment = enrollmentArgumentCaptor.getValue();
        assertEquals("external_id", enrollment.getExternalId());
        assertEquals("my_schedule", enrollment.getScheduleName());
        assertEquals("milestone_1", enrollment.getCurrentMilestoneName());
        assertEquals(now, enrollment.getStartOfSchedule());
        assertEquals(now, enrollment.getEnrolledOn());
        assertEquals(new Time(8, 15), enrollment.getPreferredAlertTime());

        assertEquals(mockedMilestoneAlerts, milestoneAlerts);
    }
}
