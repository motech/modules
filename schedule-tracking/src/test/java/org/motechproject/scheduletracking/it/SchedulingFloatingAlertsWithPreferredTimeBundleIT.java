package org.motechproject.scheduletracking.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.motechproject.scheduletracking.service.EnrollmentRequest;
import org.motechproject.scheduletracking.repository.dataservices.ScheduleDataService;
import org.motechproject.scheduletracking.service.ScheduleTrackingService;
import org.motechproject.scheduletracking.utility.TestScheduleUtil;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;

import javax.inject.Inject;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.commons.date.util.DateUtil.newDate;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.quartz.TriggerKey.triggerKey;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class SchedulingFloatingAlertsWithPreferredTimeBundleIT extends BasePaxIT {

    @Inject
    private ScheduleTrackingService scheduleTrackingService;

    @Inject
    private MotechSchedulerService schedulerService;

    @Inject
    private ScheduleDataService scheduleDataService;

    @Inject
    private EnrollmentDataService enrollmentDataService;

    @Inject
    private BundleContext bundleContext;

    private Scheduler scheduler;

    @Before
    public void setup() {
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
    }

    @After
    public void teardown() {
        schedulerService.unscheduleAllJobs("org.motechproject.scheduletracking");
        enrollmentDataService.deleteAll();
        scheduleDataService.deleteAll();
    }

    @Test
    public void shouldScheduleFloatingAlertsAtPreferredAlertTime() throws SchedulerException, URISyntaxException, IOException {
        addSchedule("schedulingIT", "schedule_with_floating_alerts.json");

        Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("abcde").setScheduleName("schedule_with_floating_alerts").setPreferredAlertTime(new Time(8, 20)).setReferenceDate(newDate(2050, 5, 10)).setReferenceTime(new Time(9, 0)).setEnrollmentDate(newDate(2050, 5, 10)).setEnrollmentTime(new Time(9, 0)).setStartingMilestoneName("milestone1").setMetadata(null));

        List<DateTime> fireTimes = getFireTimes(format("org.motechproject.scheduletracking.milestone.alert-%s.0-repeat", enrollmentId)) ;
        assertEquals(asList(
                newDateTime(2050, 5, 17, 8, 20, 0),
                newDateTime(2050, 5, 18, 8, 20, 0),
                newDateTime(2050, 5, 19, 8, 20, 0),
                newDateTime(2050, 5, 20, 8, 20, 0)),
                fireTimes);
    }

    @Test
    public void shouldFloatTheAlertsForDelayedEnrollment() throws SchedulerException, URISyntaxException, IOException {
        addSchedule("schedulingIT", "schedule_with_floating_alerts.json");
        try {
            TestScheduleUtil.fakeNow(newDateTime(2050, 5, 10, 10, 0, 0));
            Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("abcde").setScheduleName("schedule_with_floating_alerts").setPreferredAlertTime(new Time(9, 0)).setReferenceDate(newDate(2050, 5, 2)).setReferenceTime(new Time(11, 0)).setEnrollmentDate(newDate(2050, 5, 2)).setEnrollmentTime(new Time(11, 0)).setStartingMilestoneName("milestone1").setMetadata(null));

            List<DateTime> fireTimes = getFireTimes(format("org.motechproject.scheduletracking.milestone.alert-%s.0-repeat", enrollmentId)) ;
            assertEquals(asList(
                    newDateTime(2050, 5, 11, 9, 0, 0),
                    newDateTime(2050, 5, 12, 9, 0, 0),
                    newDateTime(2050, 5, 13, 9, 0, 0),
                    newDateTime(2050, 5, 14, 9, 0, 0)),
                    fireTimes);
        } finally {
            TestScheduleUtil.stopFakingTime();
        }
    }

    @Test
    public void shouldFloatTheAlertsForDelayedEnrollmentInTheGivenSpaceLeft() throws SchedulerException, URISyntaxException, IOException {
        addSchedule("schedulingIT", "schedule_with_floating_alerts.json");

        try {
            TestScheduleUtil.fakeNow(newDateTime(2050, 5, 20, 10, 0, 0));
            Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest()
                .setExternalId("abcde")
                .setScheduleName("schedule_with_floating_alerts")
                .setPreferredAlertTime(new Time(8, 0))
                .setReferenceDate(newDate(2050, 5, 9))
                .setReferenceTime(new Time(12, 0))
                .setEnrollmentDate(newDate(2050, 5, 9))
                .setEnrollmentTime(new Time(12, 0))
                .setStartingMilestoneName("milestone1")
                .setMetadata(null));

            List<DateTime> fireTimes = getFireTimes(format("org.motechproject.scheduletracking.milestone.alert-%s.0-repeat", enrollmentId)) ;
            assertEquals(asList(
                    newDateTime(2050, 5, 21, 8, 0, 0),
                    newDateTime(2050, 5, 22, 8, 0, 0),
                    newDateTime(2050, 5, 23, 8, 0, 0)),
                    fireTimes);
        } finally {
            TestScheduleUtil.stopFakingTime();
        }
    }

    @Test
    public void shouldScheduleSecondMilestoneAlertsForToday() throws IOException, URISyntaxException, SchedulerException {
        addSchedule("schedulingIT", "schedule_with_floating_alerts.json");

        try {
            TestScheduleUtil.fakeNow(newDateTime(2050, 5, 20, 10, 0, 0));
            Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("abcde").setScheduleName("schedule_with_floating_alerts").setPreferredAlertTime(new Time(9, 0)).setReferenceDate(newDate(2050, 5, 9)).setReferenceTime(new Time(11, 0)).setEnrollmentDate(newDate(2050, 5, 9)).setEnrollmentTime(new Time(11, 0)).setStartingMilestoneName("milestone1").setMetadata(null));
            scheduleTrackingService.fulfillCurrentMilestone("abcde", "schedule_with_floating_alerts", newDate(2050, 5, 18), new Time(11, 0));

            List<DateTime> fireTimes = getFireTimes(format("org.motechproject.scheduletracking.milestone.alert-%s.1-repeat", enrollmentId)) ;
            assertEquals(asList(
                    newDateTime(2050, 5, 21, 9, 0, 0),
                    newDateTime(2050, 5, 22, 9, 0, 0),
                    newDateTime(2050, 5, 23, 9, 0, 0)),
                    fireTimes);
        } finally {
            TestScheduleUtil.stopFakingTime();
        }
    }

    private void addSchedule(String path, String filename) throws URISyntaxException, IOException {
        String scheduleJson = TestScheduleUtil.getScheduleJsonFromFile(bundleContext, path, filename);
        scheduleTrackingService.add(scheduleJson);
    }

    private List<DateTime> getFireTimes(String key) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(triggerKey(key, "default"));
        List<DateTime> fireTimes = new ArrayList<>();
        Date nextFireTime = trigger.getNextFireTime();
        while (nextFireTime != null) {
            fireTimes.add(newDateTime(nextFireTime));
            nextFireTime = trigger.getFireTimeAfter(nextFireTime);
        }
        return fireTimes;
    }
}
