package org.motechproject.scheduletracking.domain;

import junit.framework.Assert;
import org.joda.time.DateTime;
import org.junit.Test;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;

import java.util.HashMap;
import java.util.Map;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.scheduletracking.utility.DateTimeUtil.weeksAgo;
import static org.motechproject.scheduletracking.utility.PeriodUtil.weeks;
import static org.motechproject.testing.utils.TimeFaker.fakeNow;
import static org.motechproject.testing.utils.TimeFaker.stopFakingTime;

public class EnrollmentTest  {
    @Test
    public void shouldStartWithFirstMilestoneByDefault() {
        Schedule schedule = new Schedule("Yellow Fever Vaccination");
        Milestone secondMilestone = new Milestone("Second Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        schedule.addMilestones(firstMilestone, secondMilestone);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(5)).withEnrolledOn(weeksAgo(3)).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(firstMilestone.getName(), enrollment.getCurrentMilestoneName());
    }

    @Test
    public void shouldStartWithSecondMilestone() {
        Schedule schedule = new Schedule("Yellow Fever Vaccination");
        Milestone secondMilestone = new Milestone("Second Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        schedule.addMilestones(firstMilestone, secondMilestone);
        Enrollment lateEnrollment = new EnrollmentBuilder().withExternalId("my_entity_1").withSchedule(schedule).withCurrentMilestoneName("Second Shot").withStartOfSchedule(weeksAgo(3)).withEnrolledOn(weeksAgo(3)).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(secondMilestone.getName(), lateEnrollment.getCurrentMilestoneName());
    }

    @Test
    public void shouldReNullWhenNoMilestoneIsFulfilled() {
        Schedule schedule = new Schedule("Yellow Fever Vaccination");
        Milestone secondMilestone = new Milestone("Second Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
        schedule.addMilestones(firstMilestone, secondMilestone);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(5)).withEnrolledOn(weeksAgo(3)).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(null, enrollment.getLastFulfilledDate());
    }

    @Test
    public void shouldReturnTheDateWhenAMilestoneWasLastFulfilled() {
        fakeNow(new DateTime(2012, 2, 20, 8, 15, 0, 0));
        try {
            Schedule schedule = new Schedule("Yellow Fever Vaccination");
            Milestone secondMilestone = new Milestone("Second Shot", weeks(1), weeks(1), weeks(1), weeks(1));
            Milestone firstMilestone = new Milestone("First Shot", weeks(1), weeks(1), weeks(1), weeks(1));
            schedule.addMilestones(firstMilestone, secondMilestone);
            Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(5)).withEnrolledOn(weeksAgo(3)).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
            enrollment.getFulfillments().add(new MilestoneFulfillment("First Shot", weeksAgo(0)));

            assertEquals(weeksAgo(0), enrollment.getLastFulfilledDate());
        } finally {
            stopFakingTime();
        }
    }

    @Test
    public void newEnrollmentShouldBeActive() {
        Schedule schedule = new Schedule("some_schedule");
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("First Shot").withStartOfSchedule(weeksAgo(5)).withEnrolledOn(weeksAgo(3)).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        assertTrue(enrollment.isActive());
    }

    @Test
    public void shouldCopyFromTheGivenEnrollment() {
        Schedule schedule = new Schedule("some_schedule");
        Enrollment originalEnrollment = new EnrollmentBuilder().withExternalId("externalId").withSchedule(schedule).withCurrentMilestoneName("currentMilestoneName").withStartOfSchedule(weeksAgo(3)).withEnrolledOn(weeksAgo(2)).withPreferredAlertTime(new Time(2, 5)).withStatus(EnrollmentStatus.ACTIVE).toEnrollment();
        Map<String, String> metadata = new HashMap<>();
        metadata.put("foo", "bar");
        Enrollment newEnrollment = new EnrollmentBuilder().withExternalId("externalId").withSchedule(schedule).withCurrentMilestoneName("newCurrentMilestoneName").withStartOfSchedule(weeksAgo(2)).withEnrolledOn(now()).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(metadata).toEnrollment();

        Enrollment enrollment = originalEnrollment.copyFrom(newEnrollment);

        assertEquals(newEnrollment.getExternalId(), enrollment.getExternalId());
        assertEquals(newEnrollment.getScheduleName(), enrollment.getScheduleName());
        assertEquals(newEnrollment.getCurrentMilestoneName(), enrollment.getCurrentMilestoneName());
        assertEquals(newEnrollment.getStartOfSchedule(), enrollment.getStartOfSchedule());
        assertEquals(newEnrollment.getEnrolledOn(), enrollment.getEnrolledOn());
        assertEquals(newEnrollment.getPreferredAlertTime(), enrollment.getPreferredAlertTime());
        assertEquals(newEnrollment.getMetadata(), enrollment.getMetadata());
    }

    @Test
    public void shouldReturnReferenceDateWhenCurrentMilestoneIsTheFirstMilestone() {
        String firstMilestoneName = "first milestone";
        DateTime referenceDateTime = weeksAgo(5);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(getMockedSchedule(firstMilestoneName, false)).withCurrentMilestoneName(firstMilestoneName).withStartOfSchedule(referenceDateTime).withEnrolledOn(weeksAgo(3)).withPreferredAlertTime(new Time(8, 20)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(referenceDateTime, enrollment.getCurrentMilestoneStartDate());
    }


    @Test
    public void shouldReturnEnrollmentDateWhenEnrolledIntoSecondMilestoneAndNoMilestonesFulfilled() {
        String firstMilestoneName = "First Shot";
        String secondMilestoneName = "Second Shot";
        DateTime enrollmentDateTime = weeksAgo(3);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(getMockedSchedule(firstMilestoneName, false)).withCurrentMilestoneName(secondMilestoneName).withStartOfSchedule(weeksAgo(5)).withEnrolledOn(enrollmentDateTime).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(enrollmentDateTime, enrollment.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnReferenceDateAsTheMilestoneStartDateOfTheAnyMilestoneWhenTheScheduleIsBasedOnAbsoluteWindows() {
        String firstMilestoneName = "First Milestone";
        String secondMilestoneName = "Second Milestone";
        DateTime referenceDate = weeksAgo(5);

        Enrollment enrollmentIntoFirstMilestone = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(getMockedSchedule(firstMilestoneName, true)).withCurrentMilestoneName(firstMilestoneName).withStartOfSchedule(referenceDate).withEnrolledOn(weeksAgo(3)).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        Enrollment enrollmentIntoSecondMilestone = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(getMockedSchedule(firstMilestoneName, true)).withCurrentMilestoneName(secondMilestoneName).withStartOfSchedule(referenceDate).withEnrolledOn(weeksAgo(3)).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(referenceDate, enrollmentIntoFirstMilestone.getCurrentMilestoneStartDate());
        assertEquals(referenceDate, enrollmentIntoSecondMilestone.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnMilestoneStartForAbsoluteScedule() {
        Schedule schedule = new Schedule("test scedule");
        Milestone milestone1 = new Milestone("milestone 1", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone milestone2 = new Milestone("milestone 2", weeks(1), weeks(1), weeks(1), weeks(1));
        Milestone milestone3 = new Milestone("milestone 3", weeks(1), weeks(1), weeks(1), weeks(1));
        schedule.addMilestones(milestone1, milestone2, milestone3);
        schedule.setBasedOnAbsoluteWindows(true);

        DateTime now = now();
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("milestone 3").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(0, 0)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(now.plusWeeks(8), enrollment.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldFulfillCurrentMilestone() {
        Schedule schedule = new Schedule("some_schedule");
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("externalId").withSchedule(schedule).withCurrentMilestoneName("currentMilestoneName").withStartOfSchedule(weeksAgo(1)).withEnrolledOn(weeksAgo(1)).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        assertEquals(0, enrollment.getFulfillments().size());
        enrollment.fulfillCurrentMilestone(DateUtil.newDateTime(2011, 6, 5, 0, 0, 0));
        assertEquals(1, enrollment.getFulfillments().size());

        MilestoneFulfillment milestoneFulfillment = enrollment.getFulfillments().get(0);
        assertEquals(newDateTime(2011, 6, 5, 0, 0, 0), milestoneFulfillment.getFulfillmentDateTime());
        assertEquals("currentMilestoneName", milestoneFulfillment.getMilestoneName());
    }

    private Schedule getMockedSchedule(String firstMilestoneName, boolean isBasedOnAbsoluteWindows) {
        Schedule schedule = mock(Schedule.class);
        Milestone milestone = mock(Milestone.class);
        when(milestone.getName()).thenReturn(firstMilestoneName);
        when(schedule.getFirstMilestone()).thenReturn(milestone);
        when(schedule.isBasedOnAbsoluteWindows()).thenReturn(isBasedOnAbsoluteWindows);
        return schedule;
    }

    @Test
    public void shouldReturnTheStartOfAGivenWindowForTheCurrentMilestone() {
        Milestone firstMilestone = new Milestone("first_milestone", weeks(1), weeks(1), weeks(1), weeks(1));
        Schedule schedule = new Schedule("my_schedule");
        schedule.addMilestones(firstMilestone);

        DateTime referenceDate = newDateTime(2012, 12, 4, 8, 30, 0);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("ID-074285").withSchedule(schedule).withCurrentMilestoneName("first_milestone").withStartOfSchedule(referenceDate).withEnrolledOn(referenceDate).withPreferredAlertTime(null).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();

        Assert.assertEquals(referenceDate, enrollment.getStartOfWindowForCurrentMilestone(WindowName.earliest));
        Assert.assertEquals(referenceDate.plusWeeks(1), enrollment.getStartOfWindowForCurrentMilestone(WindowName.due));
        Assert.assertEquals(referenceDate.plusWeeks(2), enrollment.getStartOfWindowForCurrentMilestone(WindowName.late));
        Assert.assertEquals(referenceDate.plusWeeks(3), enrollment.getStartOfWindowForCurrentMilestone(WindowName.max));
    }}
