package org.motechproject.scheduletracking.it;

import ch.lambdaj.Lambda;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.scheduletracking.domain.Enrollment;
import org.motechproject.scheduletracking.domain.EnrollmentBuilder;
import org.motechproject.scheduletracking.domain.EnrollmentStatus;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.repository.AllEnrollments;
import org.motechproject.scheduletracking.repository.dataservices.EnrollmentDataService;
import org.motechproject.scheduletracking.repository.dataservices.ScheduleDataService;
import org.motechproject.scheduletracking.service.EnrollmentRequest;
import org.motechproject.scheduletracking.service.ScheduleTrackingService;
import org.motechproject.scheduletracking.service.impl.EnrollmentServiceImpl;
import org.motechproject.scheduletracking.utility.TestScheduleUtil;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.helper.ServiceRetriever;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.util.List;

import static ch.lambdaj.Lambda.on;
import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.commons.date.util.DateUtil.today;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class AllEnrollmentsIT extends BasePaxIT {

    private AllEnrollments allEnrollments;
    private EnrollmentServiceImpl enrollmentService;

    @Inject
    private ScheduleTrackingService scheduleTrackingService;
    @Inject
    private ScheduleDataService scheduleDataService;
    @Inject
    private EnrollmentDataService enrollmentDataService;
    @Inject
    private BundleContext bundleContext;

    @Before
    public void setUp() {
        for (Schedule schedule : TestScheduleUtil.getTestSchedules(bundleContext, "schedules")) {
            scheduleDataService.create(schedule);
        }

        enrollmentDataService.deleteAll();

        //Set up beans
        WebApplicationContext webAppContext = ServiceRetriever.getWebAppContext(bundleContext, "org.motechproject.schedule-tracking");
        allEnrollments = (AllEnrollments) webAppContext.getBean("allEnrollments");
        enrollmentService = (EnrollmentServiceImpl) webAppContext.getBean("enrollmentServiceImpl");
    }

    @After
    public void tearDown() {
        enrollmentDataService.deleteAll();
        scheduleDataService.deleteAll();
    }

    @Test
    public void shouldCreateEnrollment() {
        Schedule schedule = scheduleDataService.findByName("IPTI Schedule");
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("externalId").withSchedule(schedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentDataService.create(enrollment);

        Enrollment enrollmentFromDb = enrollmentDataService.findById(enrollment.getId());

        assertNotNull(enrollmentFromDb);
        assertEquals(enrollmentFromDb.getSchedule(), schedule);
        assertEquals(EnrollmentStatus.ACTIVE, enrollmentFromDb.getStatus());
    }

    @Test
    public void shouldGetEnrollmentDataServiceWithSchedulePopulatedInThem() {
        Schedule schedule = scheduleDataService.findByName("IPTI Schedule");
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("externalId").withSchedule(schedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());

        List<Enrollment> enrollments = enrollmentDataService.retrieveAll();

        assertEquals(1, enrollments.size());
        Schedule actualSchedule = enrollments.get(0).getSchedule();
        assertEquals(scheduleDataService.findByName("IPTI Schedule"), actualSchedule);
    }

    @Test
    public void shouldFindActiveEnrollmentByExternalIdAndScheduleNameWithSchedulePopulatedInThem() {
        String scheduleName = "IPTI Schedule";
        Schedule schedule = scheduleDataService.findByName(scheduleName);
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(DateUtil.now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollment.setStatus(EnrollmentStatus.UNENROLLED);
        enrollmentDataService.create(enrollment);

        enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(DateUtil.now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentDataService.create(enrollment);

        Enrollment activeEnrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", scheduleName, EnrollmentStatus.ACTIVE);
        assertNotNull(activeEnrollment);
        assertEquals(schedule, activeEnrollment.getSchedule());
    }

    @Test
    public void shouldFindActiveEnrollmentByExternalIdAndScheduleName() {
        String scheduleName = "IPTI Schedule";
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(scheduleDataService.findByName(scheduleName)).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(DateUtil.now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollment.setStatus(EnrollmentStatus.UNENROLLED);
        enrollmentDataService.create(enrollment);

        assertNull(enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", scheduleName, EnrollmentStatus.ACTIVE));
    }

    @Test
    public void shouldConvertTheFulfillmentDateTimeIntoCorrectTimeZoneWhenRetrievingAnEnrollmentWithFulfilledMilestoneFromDatabase() {
        String scheduleName = "IPTI Schedule";
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(scheduleDataService.findByName(scheduleName)).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(DateUtil.now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentDataService.create(enrollment);
        DateTime fulfillmentDateTime = DateTime.now();
        enrollment.fulfillCurrentMilestone(fulfillmentDateTime);
        enrollmentDataService.update(enrollment);

        Enrollment enrollmentFromDatabase = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", scheduleName, EnrollmentStatus.ACTIVE);
        assertEquals(fulfillmentDateTime, enrollmentFromDatabase.getLastFulfilledDate());
    }

    @Test
    public void shouldReturnTheMilestoneStartDateTimeInCorrectTimeZoneForFirstMilestone() {
        DateTime now = DateTime.now();
        String scheduleName = "IPTI Schedule";
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(scheduleDataService.findByName(scheduleName)).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now.minusDays(2)).withEnrolledOn(now).withPreferredAlertTime(new Time(now.toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentDataService.create(enrollment);

        Enrollment enrollmentFromDatabase = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", scheduleName, EnrollmentStatus.ACTIVE);
        assertEquals(now.minusDays(2), enrollmentFromDatabase.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnTheMilestoneStartDateTimeInCorrectTimeZoneForSecondMilestone() {
        Schedule schedule = scheduleDataService.findByName("IPTI Schedule");
        DateTime now = DateTime.now();
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now.minusDays(2)).withEnrolledOn(now).withPreferredAlertTime(new Time(now.toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentDataService.create(enrollment);
        enrollmentService.fulfillCurrentMilestone(enrollment, now);
        enrollmentDataService.update(enrollment);

        Enrollment enrollmentFromDatabase = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", "IPTI Schedule", EnrollmentStatus.ACTIVE);
        assertEquals(now, enrollmentFromDatabase.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnTheMilestoneStartDateTimeInCorrectTimeZoneWhenEnrollingIntoSecondMilestone() {
        Schedule schedule = scheduleDataService.findByName("IPTI Schedule");
        DateTime now = DateTime.now();
        Enrollment enrollment = new EnrollmentBuilder().withExternalId("entity_1").withSchedule(schedule).withCurrentMilestoneName("IPTI 2").withStartOfSchedule(now.minusDays(2)).withEnrolledOn(now).withPreferredAlertTime(new Time(now.toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment();
        enrollmentDataService.create(enrollment);

        Enrollment enrollmentFromDatabase = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", "IPTI Schedule", EnrollmentStatus.ACTIVE);
        assertEquals(now, enrollmentFromDatabase.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnEnrollmentsThatMatchAGivenExternalId() {
        DateTime now = now();
        Schedule iptiSchedule = scheduleDataService.findByName("IPTI Schedule");
        Schedule deliverySchedule = scheduleDataService.findByName("Delivery");
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_1").withSchedule(iptiSchedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_1").withSchedule(deliverySchedule).withCurrentMilestoneName("Default").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_2").withSchedule(iptiSchedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_3").withSchedule(iptiSchedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());

        List<Enrollment> filteredEnrollments = enrollmentDataService.findByExternalId("entity_1");
        assertNotNull(filteredEnrollments.get(0).getSchedule());
        assertEquals(asList(new String[] { "entity_1", "entity_1"}), Lambda.extract(filteredEnrollments, on(Enrollment.class).getExternalId()));
    }

    @Test
    public void shouldFindenrollmentDataServiceThatMatchesGivenScheduleNames() {
        Schedule iptiSchedule = scheduleDataService.findByName("IPTI Schedule");
        Schedule absoluteSchedule = scheduleDataService.findByName("Absolute Schedule");
        Schedule relativeSchedule = scheduleDataService.findByName("Relative Schedule");

        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_1").withSchedule(iptiSchedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(DateUtil.now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_2").withSchedule(absoluteSchedule).withCurrentMilestoneName("milestone1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(DateUtil.now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_3").withSchedule(relativeSchedule).withCurrentMilestoneName("milestone1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(DateUtil.now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_4").withSchedule(relativeSchedule).withCurrentMilestoneName("milestone1").withStartOfSchedule(now()).withEnrolledOn(now()).withPreferredAlertTime(new Time(DateUtil.now().toLocalTime())).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());

        List<Enrollment> filteredEnrollments = allEnrollments.findBySchedule(asList(new String[]{"IPTI Schedule", "Relative Schedule"}));

        assertEquals(3, filteredEnrollments.size());
        assertEquals(asList(new String[] { "entity_1", "entity_3", "entity_4" }), Lambda.extract(filteredEnrollments, on(Enrollment.class).getExternalId()));
        assertEquals(asList(new String[] {"IPTI Schedule", "Relative Schedule", "Relative Schedule"}), Lambda.extract(filteredEnrollments, on(Enrollment.class).getScheduleName()));
    }

    @Test
    public void shouldReturnEnrollmentsThatMatchGivenStatus() {
        DateTime now = now();
        Schedule iptiSchedule = scheduleDataService.findByName("IPTI Schedule");
        Schedule deliverySchedule = scheduleDataService.findByName("Delivery");
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_1").withSchedule(iptiSchedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.COMPLETED).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_2").withSchedule(deliverySchedule).withCurrentMilestoneName("Default").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.DEFAULTED).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_3").withSchedule(iptiSchedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.UNENROLLED).withMetadata(null).toEnrollment());
        enrollmentDataService.create(new EnrollmentBuilder().withExternalId("entity_4").withSchedule(iptiSchedule).withCurrentMilestoneName("IPTI 1").withStartOfSchedule(now).withEnrolledOn(now).withPreferredAlertTime(new Time(8, 10)).withStatus(EnrollmentStatus.ACTIVE).withMetadata(null).toEnrollment());

        List<Enrollment> filteredEnrollments = enrollmentDataService.findByStatus(EnrollmentStatus.ACTIVE);
        assertEquals(asList(new String[] { "entity_4"}), Lambda.extract(filteredEnrollments, on(Enrollment.class).getExternalId()));

        filteredEnrollments = enrollmentDataService.findByStatus(EnrollmentStatus.DEFAULTED);
        assertNotNull(filteredEnrollments.get(0).getSchedule());
        assertEquals(asList(new String[] { "entity_2"}), Lambda.extract(filteredEnrollments, on(Enrollment.class).getExternalId()));
    }

    @Test
    public void shouldReturnEnrollmentsThatWereCompletedDuringTheGivenTimeRange() {
        LocalDate today = today();
        scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("entity_1").setScheduleName("IPTI Schedule").setPreferredAlertTime(new Time(8, 10)).setReferenceDate(today).setReferenceTime(null).setEnrollmentDate(today).setEnrollmentTime(null).setStartingMilestoneName("IPTI 1").setMetadata(null));

        scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("entity_2").setScheduleName("IPTI Schedule").setPreferredAlertTime(new Time(8, 10)).setReferenceDate(today.minusWeeks(2)).setReferenceTime(null).setEnrollmentDate(today.minusWeeks(2)).setEnrollmentTime(null).setStartingMilestoneName("IPTI 1").setMetadata(null));
        scheduleTrackingService.fulfillCurrentMilestone("entity_2", "IPTI Schedule", today.minusDays(2), new Time(0, 0));
        scheduleTrackingService.fulfillCurrentMilestone("entity_2", "IPTI Schedule", today.minusDays(1), new Time(0, 0));

        scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("entity_3").setScheduleName("IPTI Schedule").setPreferredAlertTime(new Time(8, 10)).setReferenceDate(today).setReferenceTime(null).setEnrollmentDate(today).setEnrollmentTime(null).setStartingMilestoneName("IPTI 2").setMetadata(null));
        scheduleTrackingService.fulfillCurrentMilestone("entity_3", "IPTI Schedule", today, new Time(0, 0));

        scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("entity_4").setScheduleName("IPTI Schedule").setPreferredAlertTime(new Time(8, 10)).setReferenceDate(today.minusYears(2)).setReferenceTime(null).setEnrollmentDate(today).setEnrollmentTime(null).setStartingMilestoneName("IPTI 1").setMetadata(null));

        scheduleTrackingService.enroll(new EnrollmentRequest().setExternalId("entity_5").setScheduleName("IPTI Schedule").setPreferredAlertTime(new Time(8, 10)).setReferenceDate(today.minusWeeks(2)).setReferenceTime(null).setEnrollmentDate(today.minusWeeks(2)).setEnrollmentTime(null).setStartingMilestoneName("IPTI 1").setMetadata(null));
        scheduleTrackingService.fulfillCurrentMilestone("entity_5", "IPTI Schedule", today.minusDays(10), new Time(0, 0));
        scheduleTrackingService.fulfillCurrentMilestone("entity_5", "IPTI Schedule", today.minusDays(9), new Time(0, 0));

        DateTime start = newDateTime(today.minusWeeks(1), new Time(0, 0));
        DateTime end = newDateTime(today, new Time(0, 0));
        List<Enrollment> filteredEnrollments = allEnrollments.completedDuring(start, end);
        assertNotNull(filteredEnrollments.get(0).getSchedule());
        assertEquals(asList(new String[] { "entity_2", "entity_3" }), Lambda.extract(filteredEnrollments, on(Enrollment.class).getExternalId()));
    }
}
