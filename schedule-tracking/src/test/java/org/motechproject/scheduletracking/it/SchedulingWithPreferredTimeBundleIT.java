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
import org.motechproject.scheduletracking.utility.schedule.TestScheduleUtil;
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
public class SchedulingWithPreferredTimeBundleIT extends BasePaxIT {

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
    public void shouldScheduleAlertsAtPreferredAlertTime() throws SchedulerException, URISyntaxException, IOException {
        addSchedule("schedulingIT", "schedule.json");

        Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("abcde").setScheduleName("schedule").setPreferredAlertTime(new Time(8, 20)).setReferenceDate(newDate(2050, 5, 10)).setReferenceTime(new Time(0, 0)).setEnrollmentDate(newDate(2050, 5, 10)).setEnrollmentTime(new Time(0, 0)).setStartingMilestoneName("milestone1").setMetadata(null));

        List<DateTime> fireTimes = getFireTimes(format("org.motechproject.scheduletracking.milestone.alert-%s.0-repeat", enrollmentId)) ;
        assertEquals(asList(
                newDateTime(2050, 5, 15, 8, 20, 0),
                newDateTime(2050, 5, 16, 8, 20, 0),
                newDateTime(2050, 5, 17, 8, 20, 0),
                newDateTime(2050, 5, 18, 8, 20, 0),
                newDateTime(2050, 5, 19, 8, 20, 0)),
                fireTimes);
    }

    @Test
    public void shouldNotScheduleAlertsInThePastForDelayedEnrollment() throws SchedulerException, URISyntaxException, IOException {
        addSchedule("schedulingIT", "schedule.json");

        try {
            TestScheduleUtil.fakeNow(newDateTime(2050, 5, 17, 9, 0, 0));

            Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("abcde").setScheduleName("schedule").setPreferredAlertTime(new Time(10, 0)).setReferenceDate(newDate(2050, 5, 10)).setReferenceTime(new Time(0, 0)).setEnrollmentDate(newDate(2050, 5, 17)).setEnrollmentTime(new Time(0, 0)).setStartingMilestoneName("milestone1").setMetadata(null));

            List<DateTime> fireTimes = getFireTimes(format("org.motechproject.scheduletracking.milestone.alert-%s.0-repeat", enrollmentId)) ;
            assertEquals(asList(
                    newDateTime(2050, 5, 17, 10, 0, 0),
                    newDateTime(2050, 5, 18, 10, 0, 0),
                    newDateTime(2050, 5, 19, 10, 0, 0)),
                    fireTimes);
        } finally {
            TestScheduleUtil.stopFakingTime();
        }
    }

    @Test
    public void shouldNotScheduleAlertForTheDayIfPreferredTimeIsPassed() throws SchedulerException, URISyntaxException, IOException {
        addSchedule("schedulingIT", "schedule.json");

        try {
            TestScheduleUtil.fakeNow(newDateTime(2050, 5, 17, 11, 0, 0));
                Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("abcde").setScheduleName("schedule").setPreferredAlertTime(new Time(8, 0)).setReferenceDate(newDate(2050, 5, 10)).setReferenceTime(new Time(0, 0)).setEnrollmentDate(newDate(2050, 5, 17)).setEnrollmentTime(new Time(0, 0)).setStartingMilestoneName("milestone1").setMetadata(null));

            List<DateTime> fireTimes = getFireTimes(format("org.motechproject.scheduletracking.milestone.alert-%s.0-repeat", enrollmentId)) ;
            assertEquals(asList(
                    newDateTime(2050, 5, 18, 8, 0, 0),
                    newDateTime(2050, 5, 19, 8, 0, 0)),
                    fireTimes);
        } finally {
            TestScheduleUtil.stopFakingTime();
        }
    }

    @Test
    public void shouldScheduleSecondMilestoneAlerts() throws IOException, URISyntaxException, SchedulerException {
        addSchedule("schedulingIT", "schedule.json");

        Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("abcde").setScheduleName("schedule").setPreferredAlertTime(new Time(10, 0)).setReferenceDate(newDate(2050, 5, 10)).setReferenceTime(new Time(0, 0)).setEnrollmentDate(newDate(2050, 5, 10)).setEnrollmentTime(new Time(0, 0)).setStartingMilestoneName("milestone1").setMetadata(null));
        scheduleTrackingService.fulfillCurrentMilestone("abcde", "schedule", newDate(2050, 5, 20), new Time(9, 0));

        List<DateTime> fireTimes = getFireTimes(format("org.motechproject.scheduletracking.milestone.alert-%s.1-repeat", enrollmentId)) ;
        assertEquals(asList(
                newDateTime(2050, 5, 20, 10, 0, 0),
                newDateTime(2050, 5, 21, 10, 0, 0),
                newDateTime(2050, 5, 22, 10, 0, 0)),
                fireTimes);
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
