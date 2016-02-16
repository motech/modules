package org.motechproject.scheduletracking.service.impl;


import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.scheduler.contract.RepeatingSchedulableJob;
import org.motechproject.scheduler.contract.RunOnceSchedulableJob;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduletracking.domain.Alert;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentBuilder;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Milestone;
import org.motechproject.scheduletracking.domain.MilestoneAlert;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.domain.WindowName;
import org.motechproject.scheduletracking.events.MilestoneEvent;
import org.motechproject.scheduletracking.events.constants.EventSubjects;
import org.motechproject.scheduletracking.service.MilestoneAlerts;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.joda.time.DateTimeConstants.SECONDS_PER_DAY;
import static org.joda.time.DateTimeConstants.SECONDS_PER_HOUR;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.scheduletracking.utility.DateTimeUtil.daysAfter;
import static org.motechproject.scheduletracking.utility.DateTimeUtil.daysAgo;
import static org.motechproject.scheduletracking.utility.DateTimeUtil.weeksAfter;
import static org.motechproject.scheduletracking.utility.DateTimeUtil.weeksAgo;
import static org.motechproject.scheduletracking.utility.PeriodUtil.days;
import static org.motechproject.scheduletracking.utility.PeriodUtil.hours;
import static org.motechproject.scheduletracking.utility.PeriodUtil.months;
import static org.motechproject.scheduletracking.utility.PeriodUtil.weeks;

public class EnrollmentAlertServiceTest {

    private EnrollmentAlertService enrollmentAlertService;

    @Mock
    private MotechSchedulerService schedulerService;
    @Mock
    private EventRelay eventRelay;

    @Before
    public void setup() {
        initMocks(this);

        DateTime now = new DateTime(2012, 3, 16, 8, 15, 0, 0);
        DateTimeUtils.setCurrentMillisFixed(now.getMillis());

        enrollmentAlertService = new EnrollmentAlertService();
        enrollmentAlertService.setSchedulerService(schedulerService);
    }

    @After
    public void tearDown() throws Exception {
        DateTimeUtils.setCurrentMillisSystem();
    }

    @Test
    public void shouldScheduleOneJobIfThereIsOnlyOneAlertInTheMilestone() {
        String externalId = "entity_1";
        String scheduleName = "my_schedule";
        Map<String, String> data = new HashMap<>(1);
        data.put("key", "value");

        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(22));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 3, 0, false));
        milestone.setData(data);
        Schedule schedule = new Schedule(scheduleName);
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(milestone.getName()).withStartOfSchedule(weeksAgo(0)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        ArgumentCaptor<RepeatingSchedulableJob> repeatJobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(schedulerService, times(1)).safeScheduleRepeatingJob(repeatJobCaptor.capture());

        RepeatingSchedulableJob job = repeatJobCaptor.getAllValues().get(0);
        assertJobDetails(job, String.format("%s.0", enrollment.getId()), newDateTime(weeksAfter(0).toLocalDate(), new Time(8, 20)), 2, SECONDS_PER_DAY);
        assertEventDetails(new MilestoneEvent(job.getMotechEvent()), externalId, scheduleName, MilestoneAlert.fromMilestone(milestone, enrollment.getStartOfSchedule()), WindowName.earliest.name(), milestone.getData());
    }

    @Test
    public void shouldScheduleJobsForMilestoneWithWindowsInHours() {
        String externalId = "entity_1";
        String scheduleName = "my_schedule";
        Map<String, String> data = new HashMap<>(1);
        data.put("key", "value");

        Milestone milestone = new Milestone("milestone", hours(3), weeks(1).plus(hours(4)), hours(1), hours(5));
        milestone.addAlert(WindowName.earliest, new Alert(hours(1), hours(1), 2, 0, false));
        milestone.addAlert(WindowName.due, new Alert(weeks(1), hours(2), 2, 1, false));
        milestone.setData(data);
        Schedule schedule = new Schedule(scheduleName);
        schedule.addMilestones(milestone);
        DateTime now = DateUtil.now();

        Enrollment enrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(milestone.getName()).withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        ArgumentCaptor<RepeatingSchedulableJob> repeatJobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(schedulerService, times(2)).safeScheduleRepeatingJob(repeatJobCaptor.capture());

        RepeatingSchedulableJob job = repeatJobCaptor.getAllValues().get(0);
        assertJobDetails(job, String.format("%s.0", enrollment.getId()), now.plusHours(1), 1, SECONDS_PER_HOUR);
        assertEventDetails(new MilestoneEvent(job.getMotechEvent()), externalId, scheduleName, MilestoneAlert.fromMilestone(milestone, enrollment.getStartOfSchedule()), WindowName.earliest.name(), milestone.getData());

        job = repeatJobCaptor.getAllValues().get(1);
        assertJobDetails(job, String.format("%s.1", enrollment.getId()), now.plusHours(3).plusWeeks(1), 1, 2 * SECONDS_PER_HOUR);
        assertEventDetails(new MilestoneEvent(job.getMotechEvent()), externalId, scheduleName, MilestoneAlert.fromMilestone(milestone, enrollment.getStartOfSchedule()), WindowName.due.name(), milestone.getData());
    }

    @Test
    public void shouldScheduleOneRepeatJobForEachAlertInTheFirstMilestone() {
        String externalId = "entity_1";
        String scheduleName = "my_schedule";
        Map<String, String> data = new HashMap<>(1);
        data.put("key", "value");

        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(22));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 3, 0, false));
        milestone.addAlert(WindowName.due, new Alert(days(0), days(3), 2, 1, false));
        milestone.setData(data);
        Schedule schedule = new Schedule(scheduleName);
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(schedule).withCurrentMilestoneName(milestone.getName()).withStartOfSchedule(weeksAgo(0)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        ArgumentCaptor<RepeatingSchedulableJob> repeatJobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(schedulerService, times(2)).safeScheduleRepeatingJob(repeatJobCaptor.capture());

        RepeatingSchedulableJob job = repeatJobCaptor.getAllValues().get(0);
        assertJobDetails(job, String.format("%s.0", enrollment.getId()), newDateTime(weeksAfter(0).toLocalDate(), new Time(8, 20)), 2, SECONDS_PER_DAY);
        assertEventDetails(new MilestoneEvent(job.getMotechEvent()), externalId, scheduleName, MilestoneAlert.fromMilestone(milestone, enrollment.getStartOfSchedule()), WindowName.earliest.name(), milestone.getData());

        job = repeatJobCaptor.getAllValues().get(1);
        assertJobDetails(job, String.format("%s.1", enrollment.getId()), newDateTime(weeksAfter(1).toLocalDate(), new Time(8, 20)), 1, 3 * SECONDS_PER_DAY);
        assertEventDetails(new MilestoneEvent(job.getMotechEvent()), externalId, scheduleName, MilestoneAlert.fromMilestone(milestone, enrollment.getStartOfSchedule()), WindowName.due.name(), milestone.getData());
    }

    @Test
    public void shouldPassMilestoneAlertAsPayloadWhileSchedulingTheJob() {
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(22));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 3, 0, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(weeksAgo(0)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        ArgumentCaptor<RepeatingSchedulableJob> repeatJobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(schedulerService).safeScheduleRepeatingJob(repeatJobCaptor.capture());

        MilestoneAlert milestoneAlert = new MilestoneEvent(repeatJobCaptor.getValue().getMotechEvent()).getMilestoneAlert();
        assertEquals(weeksAfter(0), milestoneAlert.getEarliestDateTime());
        assertEquals(weeksAfter(1), milestoneAlert.getDueDateTime());
        assertEquals(weeksAfter(2), milestoneAlert.getLateDateTime());
        assertEquals(weeksAfter(3), milestoneAlert.getDefaultmentDateTime());
    }

    @Test
    public void shouldNotScheduleJobsForElapsedAlerts() {
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(3), 3, 0, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(daysAgo(4)).withEnrolledOn(daysAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        RunOnceSchedulableJob job = expectAndCaptureRunOneJob();
        assertEquals(newDateTime(daysAfter(2).toLocalDate(), new Time(8, 20)), job.getStartDate());
    }

    @Test
    public void alertIsElapsedTodayIfItIsBeforePreferredAlertTime() {
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(3), 3, 0, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(daysAgo(0)).withEnrolledOn(daysAgo(0)).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        RepeatingSchedulableJob job = expectAndCaptureRepeatingJob();
        assertEquals(newDateTime(daysAfter(3).toLocalDate(), new Time(8, 10)), job.getStartDate());

        assertEquals(1, job.getRepeatCount().intValue());
    }

    @Test
    public void alertIsNotElapsedTodayIfItIsNotBeforePreferredAlertTime() {
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(3), 3, 0, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(daysAgo(0)).withEnrolledOn(daysAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        RepeatingSchedulableJob job = expectAndCaptureRepeatingJob();
        assertEquals(newDateTime(daysAfter(0).toLocalDate(), new Time(8, 20)), job.getStartDate());
        assertEquals(2, job.getRepeatCount().intValue());
    }

    @Test
    public void shouldNotScheduleAnyJobIfAllAlertsHaveElapsed() {
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(3), 1, 0, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(daysAgo(4)).withEnrolledOn(daysAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        verify(schedulerService, never()).safeScheduleRepeatingJob(Matchers.<RepeatingSchedulableJob>any());
    }

    @Test
    public void shouldScheduleAlertJobWithOffset() {
        Milestone milestone = new Milestone("milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.due, new Alert(days(3), days(1), 3, 0, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(weeksAgo(0)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        RepeatingSchedulableJob job = expectAndCaptureRepeatingJob();
        assertEquals(newDateTime(daysAfter(10).toLocalDate(), new Time(8, 20)), job.getStartDate());
    }

    @Test
    public void shouldSceduleJobForAbsoluteSchedule() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone secondMilestone = new Milestone("milestone_2", weeks(1), weeks(1), weeks(1), weeks(1));
        secondMilestone.addAlert(WindowName.due, new Alert(days(0), days(1), 1, 0, false));

        Schedule schedule = new Schedule("my_schedule");
        schedule.setBasedOnAbsoluteWindows(true);
        schedule.addMilestones(firstMilestone, secondMilestone);

        Enrollment enrollmentIntoSecondMilestone = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_2").withStartOfSchedule(weeksAgo(0)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(0, 0)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollmentIntoSecondMilestone);

        RunOnceSchedulableJob job = expectAndCaptureRunOneJob();
        assertEquals(newDateTime(weeksAfter(5).toLocalDate(), new Time(0, 0)), job.getStartDate());
    }

    @Test
    public void shouldScheduleJobForFloatingAlerts() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        Alert alert = new Alert(days(0), days(1), 7, 0, true);
        firstMilestone.addAlert(WindowName.due, alert);

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(daysAgo(12)).withEnrolledOn(DateUtil.now()).withPreferredAlertTime(new Time(8, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        ArgumentCaptor<RepeatingSchedulableJob> repeatJobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(schedulerService, times(1)).safeScheduleRepeatingJob(repeatJobCaptor.capture());

        assertEquals(DateUtil.now(), repeatJobCaptor.getValue().getStartDate());
        assertEquals(1, repeatJobCaptor.getValue().getRepeatCount().intValue());
    }

    @Test
    public void shouldScheduleJobForFloatingAlerts_WithPreferredTimeEarlierThanCurrentTime() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        Alert alert = new Alert(days(0), days(3), 7, 0, true);
        firstMilestone.addAlert(WindowName.due, alert);

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(daysAgo(12)).withEnrolledOn(DateUtil.now()).withPreferredAlertTime(new Time(6, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        ArgumentCaptor<RunOnceSchedulableJob> runOneJobCaptor = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
        verify(schedulerService, times(1)).safeScheduleRunOnceJob(runOneJobCaptor.capture());

        assertEquals(newDateTime(DateUtil.now().plusDays(1).toLocalDate(), new Time(6, 15)), runOneJobCaptor.getValue().getStartDate());
    }

    @Test
    public void shouldConsiderZeroOffsetForBackDatedFloatingAlerts() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), months(10), weeks(1), weeks(1));
        Alert alert = new Alert(days(-5), days(1), 7, 0, true);
        firstMilestone.addAlert(WindowName.due, alert);

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(daysAgo(30)).withEnrolledOn(DateUtil.now()).withPreferredAlertTime(new Time(8, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        ArgumentCaptor<RepeatingSchedulableJob> repeatJobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(schedulerService, times(1)).safeScheduleRepeatingJob(repeatJobCaptor.capture());

        assertEquals(DateUtil.now(), repeatJobCaptor.getValue().getStartDate());
        assertEquals(6, repeatJobCaptor.getValue().getRepeatCount().intValue());
    }

    @Test
    public void shouldConsiderZeroOffsetForBackDatedFloatingAlertsWhenEnrolledInADifferentWindow() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(4), months(10), weeks(0), weeks(0));
        Alert alert = new Alert(weeks(-2), days(1), 1, 0, true);
        firstMilestone.addAlert(WindowName.due, alert);

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(1)).withPreferredAlertTime(new Time(8, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        ArgumentCaptor<RunOnceSchedulableJob> runOneJobCaptor = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
        verify(schedulerService, times(1)).safeScheduleRunOnceJob(runOneJobCaptor.capture());

        assertEquals(DateUtil.now(), runOneJobCaptor.getValue().getStartDate());
    }

    @Test
    public void shouldReturnAlertTimingsForTheGivenMilestone() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        firstMilestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 2, 0, false));
        firstMilestone.addAlert(WindowName.earliest, new Alert(days(2), days(1), 1, 0, false));
        firstMilestone.addAlert(WindowName.due, new Alert(days(1), days(1), 7, 0, false));
        firstMilestone.addAlert(WindowName.max, new Alert(days(2), days(2), 7, 0, false));

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(8, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        MilestoneAlerts milestoneAlerts = enrollmentAlertService.getAlertTimings(enrollment);

        List<DateTime> earliestWindowAlertTimings = milestoneAlerts.getEarliestWindowAlertTimings();
        assertEquals(3, earliestWindowAlertTimings.size());
        assertEquals(new DateTime(2012, 3, 16, 8, 15, 0, 0), earliestWindowAlertTimings.get(0));
        assertEquals(new DateTime(2012, 3, 17, 8, 15, 0, 0), earliestWindowAlertTimings.get(1));
        assertEquals(new DateTime(2012, 3, 18, 8, 15, 0, 0), earliestWindowAlertTimings.get(2));

        List<DateTime> dueWindowAlertTimings = milestoneAlerts.getDueWindowAlertTimings();
        assertEquals(7, dueWindowAlertTimings.size());
        assertEquals(new DateTime(2012, 3, 24, 8, 15, 0, 0), dueWindowAlertTimings.get(0));
        assertEquals(new DateTime(2012, 3, 30, 8, 15, 0, 0), dueWindowAlertTimings.get(6));

        List<DateTime> maxWindowAlertTimings = milestoneAlerts.getMaxWindowAlertTimings();
        assertEquals(7, maxWindowAlertTimings.size());
        assertEquals(new DateTime(2012, 4, 8, 8, 15, 0, 0), maxWindowAlertTimings.get(0));
        assertEquals(new DateTime(2012, 4, 20, 8, 15, 0, 0), maxWindowAlertTimings.get(6));
    }

    @Test
    public void shouldReturnAlertTimingsAsNullForTheGivenNonExistingMilestone() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        firstMilestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 2, 0, false));

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_non_existent").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(8, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        MilestoneAlerts milestoneAlerts = enrollmentAlertService.getAlertTimings(enrollment);

        List<DateTime> earliestWindowAlertTimings = milestoneAlerts.getEarliestWindowAlertTimings();
        assertNull(earliestWindowAlertTimings);
    }

    @Test
    public void shouldReturnAlertTimingsForElapsedAlertsAlsoInTheMilestoneWindow() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        firstMilestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 5, 0, false));

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(daysAgo(3)).withEnrolledOn(daysAgo(3)).withPreferredAlertTime(new Time(8, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        MilestoneAlerts milestoneAlerts = enrollmentAlertService.getAlertTimings(enrollment);

        List<DateTime> earliestWindowAlertTimings = milestoneAlerts.getEarliestWindowAlertTimings();
        assertEquals(5, earliestWindowAlertTimings.size());
        assertEquals(new DateTime(2012, 3, 13, 8, 15, 0, 0), earliestWindowAlertTimings.get(0));
        assertEquals(new DateTime(2012, 3, 14, 8, 15, 0, 0), earliestWindowAlertTimings.get(1));
    }

    @Test
    public void shouldReturnAlertTimingsForElapsedWindowAlsoInTheMilestone() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        firstMilestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 5, 0, false));

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(weeksAgo(1)).withEnrolledOn(weeksAgo(1)).withPreferredAlertTime(new Time(8, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        MilestoneAlerts milestoneAlerts = enrollmentAlertService.getAlertTimings(enrollment);

        List<DateTime> earliestWindowAlertTimings = milestoneAlerts.getEarliestWindowAlertTimings();
        assertEquals(5, earliestWindowAlertTimings.size());
        assertEquals(new DateTime(2012, 3, 9, 8, 15, 0, 0), earliestWindowAlertTimings.get(0));
        assertEquals(new DateTime(2012, 3, 10, 8, 15, 0, 0), earliestWindowAlertTimings.get(1));
    }

    @Test
    public void shouldReturnAlertTimingsForAbsoluteSchedule() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone secondMilestone = new Milestone("milestone_2", weeks(1), weeks(1), weeks(1), weeks(1));
        secondMilestone.addAlert(WindowName.due, new Alert(days(0), days(1), 3, 0, false));

        Schedule schedule = new Schedule("my_schedule");
        schedule.setBasedOnAbsoluteWindows(true);
        schedule.addMilestones(firstMilestone, secondMilestone);

        Enrollment enrollmentIntoSecondMilestone = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_2").withStartOfSchedule(weeksAgo(0)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(0, 0)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        MilestoneAlerts milestoneAlerts = enrollmentAlertService.getAlertTimings(enrollmentIntoSecondMilestone);

        List<DateTime> dueWindowAlertTimings = milestoneAlerts.getDueWindowAlertTimings();
        assertEquals(3, dueWindowAlertTimings.size());
        assertEquals(new DateTime(2012, 4, 20, 0, 0, 0, 0), dueWindowAlertTimings.get(0));
    }

    @Test
    public void shouldReturnAlertTimingsForFloatingAlerts() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        firstMilestone.addAlert(WindowName.due, new Alert(days(0), days(1), 7, 0, true));

        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("some_id").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(daysAgo(12)).withEnrolledOn(DateUtil.now()).withPreferredAlertTime(new Time(8, 15)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        MilestoneAlerts milestoneAlerts = enrollmentAlertService.getAlertTimings(enrollment);

        List<DateTime> dueWindowAlertTimings = milestoneAlerts.getDueWindowAlertTimings();
        assertEquals(7, dueWindowAlertTimings.size());
        assertEquals(now(), dueWindowAlertTimings.get(0));
    }

    @Test
    public void shouldNotScheduleJobsForFutureMilestones() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone secondMilestone = new Milestone("milestone_2", weeks(1), weeks(1), weeks(1), weeks(1));
        secondMilestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 3, 0, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone, secondMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(weeksAgo(0)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        verify(schedulerService, times(0)).safeScheduleRepeatingJob(Matchers.<RepeatingSchedulableJob>any());
    }

    @Test
    public void shouldNotScheduleJobsForPassedWindowInTheFirstMilestone() {
        Milestone milestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        milestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 4, 0, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(milestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone_1").withStartOfSchedule(weeksAgo(1)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        verify(schedulerService, times(0)).scheduleRepeatingJob(Matchers.<RepeatingSchedulableJob>any());
    }

    @Test
    public void shouldNotScheduleJobsForPassedMilestones() {
        Milestone firstMilestone = new Milestone("milestone_1", weeks(1), weeks(1), weeks(1), weeks(1));
        firstMilestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 4, 0, false));
        Milestone secondMilestone = new Milestone("milestone_2", weeks(1), weeks(1), weeks(1), weeks(1));
        secondMilestone.addAlert(WindowName.earliest, new Alert(days(0), days(1), 2, 1, false));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone, secondMilestone);

        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone_2").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(0)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentAlertService.scheduleAlertsForCurrentMilestone(enrollment);

        RepeatingSchedulableJob job = expectAndCaptureRepeatingJob();
        assertEquals(String.format("%s.1", enrollment.getId()), job.getMotechEvent().getParameters().get(MotechSchedulerService.JOB_ID_KEY));
        assertEquals(newDateTime(weeksAgo(0).toLocalDate(), new Time(8, 20)), job.getStartDate());
        assertRepeatIntervalValue(SECONDS_PER_DAY, job.getRepeatIntervalInSeconds());
        assertEquals(1, job.getRepeatCount().intValue());
    }

    @Test
    public void shouldUnenrollEntityFromTheSchedule() {
        Schedule schedule = new Schedule("schedule_test");
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("milestone").withStartOfSchedule(weeksAgo(4)).withEnrolledOn(weeksAgo(4)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollment.setId(1L);
        enrollmentAlertService.unscheduleAllAlerts(enrollment);

        verify(schedulerService).safeUnscheduleAllJobs(String.format("%s-%s", EventSubjects.MILESTONE_ALERT, 1L));
    }

    private void assertRepeatIntervalValue(int expected, int actual) {
        assertTrue(actual > 0);
        assertEquals(expected, actual);
    }

    private void assertJobDetails(RepeatingSchedulableJob job, String jobIdKey, DateTime startTime, int repeatCount, int repeatInterval) {
        assertEquals(jobIdKey, job.getMotechEvent().getParameters().get(MotechSchedulerService.JOB_ID_KEY));
        assertEquals(startTime, job.getStartDate());
        assertEquals(repeatCount, job.getRepeatCount().intValue());
        assertRepeatIntervalValue(repeatInterval, job.getRepeatIntervalInSeconds());
    }

    private void assertEventDetails(MilestoneEvent event, String externalId, String scheduleName, MilestoneAlert milestoneAlert, String windowName, Map<String, String> data) {
        assertEquals(externalId, event.getExternalId());
        assertEquals(scheduleName, event.getScheduleName());
        assertEquals(milestoneAlert, event.getMilestoneAlert());
        assertEquals(windowName, event.getWindowName());

        Map<String, String> eventData = event.getMilestoneData();

        if (data == null) {
            assertNull(eventData);
        } else {
            assertNotNull(eventData);
            assertEquals(data.size(), eventData.size());

            for (Map.Entry<String, String> entry : data.entrySet()) {
                assertTrue(eventData.containsKey(entry.getKey()));
                assertEquals(entry.getValue(), eventData.get(entry.getKey()));
            }
        }
    }

    private RepeatingSchedulableJob expectAndCaptureRepeatingJob() {
        ArgumentCaptor<RepeatingSchedulableJob> repeatJobCaptor = ArgumentCaptor.forClass(RepeatingSchedulableJob.class);
        verify(schedulerService).safeScheduleRepeatingJob(repeatJobCaptor.capture());

        return repeatJobCaptor.getValue();
    }

    private RunOnceSchedulableJob expectAndCaptureRunOneJob() {
        ArgumentCaptor<RunOnceSchedulableJob> runOneJobCaptor = ArgumentCaptor.forClass(RunOnceSchedulableJob.class);
        verify(schedulerService).safeScheduleRunOnceJob(runOneJobCaptor.capture());

        return runOneJobCaptor.getValue();
    }
}
