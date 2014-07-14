package org.motechproject.scheduletracking.it;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.model.Time;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.motechproject.scheduletracking.service.EnrollmentRecord;
import org.motechproject.scheduletracking.service.EnrollmentRequest;
import org.motechproject.scheduletracking.service.EnrollmentsQuery;
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

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.now;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class ScheduleTrackingServiceIT extends BasePaxIT {

    @Inject
    private ScheduleTrackingService scheduleTrackingService;
    @Inject
    private ScheduleDataService scheduleDataService;
    @Inject
    private EnrollmentDataService enrollmentDataService;
    @Inject
    private BundleContext bundleContext;

    @Before
    public void setUp() throws IOException {
        for (Schedule schedule : TestScheduleUtil.getTestSchedules(bundleContext, "schedules")) {
            scheduleDataService.create(schedule);
        }
    }

    @After
    public void tearDown() {
        enrollmentDataService.deleteAll();
        scheduleDataService.deleteAll();
    }

    @Test
    public void shouldUpdateEnrollmentIfAnActiveEnrollmentAlreadyExists() {
        Enrollment activeEnrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus("externalId", "IPTI Schedule", EnrollmentStatus.ACTIVE);
        assertNull("Active enrollment present", activeEnrollment);

        Time originalPreferredAlertTime = new Time(8, 10);
        DateTime now = now();
        Long enrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("externalId").setScheduleName("IPTI Schedule").setPreferredAlertTime(originalPreferredAlertTime).setReferenceDate(now.toLocalDate()).setReferenceTime(null).setEnrollmentDate(null).setEnrollmentTime(null).setStartingMilestoneName(null).setMetadata(null));
        assertNotNull("EnrollmentId is null", enrollmentId);

        activeEnrollment = enrollmentDataService.findById(enrollmentId);
        assertNotNull("No active enrollment present", activeEnrollment);
        assertEquals(originalPreferredAlertTime, activeEnrollment.getPreferredAlertTime());
        assertEquals(newDateTime(now.toLocalDate(), new Time(0, 0)), activeEnrollment.getStartOfSchedule());

        Time updatedPreferredAlertTime = new Time(2, 5);
        DateTime updatedReferenceDate = now.minusDays(1);
        Long updatedEnrollmentId = scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("externalId").setScheduleName("IPTI Schedule").setPreferredAlertTime(updatedPreferredAlertTime).setReferenceDate(updatedReferenceDate.toLocalDate()).setReferenceTime(null).setEnrollmentDate(null).setEnrollmentTime(null).setStartingMilestoneName(null));
        assertEquals(enrollmentId, updatedEnrollmentId);

        activeEnrollment = enrollmentDataService.findById(updatedEnrollmentId);
        assertNotNull("No active enrollment present", activeEnrollment);
        assertEquals(updatedPreferredAlertTime, activeEnrollment.getPreferredAlertTime());
        assertEquals(newDateTime(updatedReferenceDate.toLocalDate(), new Time(0, 0)), activeEnrollment.getStartOfSchedule());
    }

    @Test
    public void fulfillMilestoneShouldBeIdempotent() {
        scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("entity_1").setScheduleName("IPTI Schedule").setPreferredAlertTime(null).setReferenceDate(LocalDate.now()).setReferenceTime(null).setEnrollmentDate(LocalDate.now()).setEnrollmentTime(null).setStartingMilestoneName(null).setMetadata(null));
        scheduleTrackingService.fulfillCurrentMilestone("entity_1", "IPTI Schedule", LocalDate.now(), new Time(8, 20));
        scheduleTrackingService.fulfillCurrentMilestone("entity_1", "IPTI Schedule", LocalDate.now(), new Time(8, 20));

        List<EnrollmentRecord> enrollment = scheduleTrackingService.search(new EnrollmentsQuery().havingExternalId("entity_1").havingSchedule("IPTI Schedule"));
        assertEquals("IPTI 2", enrollment.get(0).getCurrentMilestoneName());
    }

    @Test
    public void shouldReturnScheduleFromDb() {
        Schedule schedule = scheduleTrackingService.getScheduleByName("IPTI Schedule");

        assertNotNull(schedule);
        assertEquals("IPTI 1", schedule.getFirstMilestone().getName());
        assertEquals(2, schedule.getMilestones().size());
        assertNotNull(schedule.getMilestone("IPTI 1"));
        assertEquals("IPTI Schedule", schedule.getName());
        assertEquals("IPTI 2", schedule.getNextMilestoneName("IPTI 1"));
        assertNull(schedule.getNextMilestoneName("IPTI 2"));
    }

    @Test
    public void shouldNotReturnScheduleThatDoesNotExist() {
        Schedule schedule = scheduleTrackingService.getScheduleByName("Fake Schedule");
        assertNull(schedule);
    }

    @Test
    public void shouldReturnAllSchedules() {
        List<Schedule> schedules = scheduleTrackingService.getAllSchedules();
        assertEquals(10, schedules.size());
    }


}
