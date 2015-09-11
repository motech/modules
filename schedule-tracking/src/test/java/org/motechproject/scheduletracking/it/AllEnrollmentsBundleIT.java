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
import org.motechproject.scheduletracking.service.EnrollmentService;
import org.motechproject.scheduletracking.service.ScheduleTrackingService;
import org.motechproject.scheduletracking.utility.TestScheduleUtil;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.helper.ServiceRetriever;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;

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
public class AllEnrollmentsBundleIT extends BasePaxIT {

    private AllEnrollments allEnrollments;

    @Inject
    private EnrollmentService enrollmentService;
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
    }

    @After
    public void tearDown() {
        enrollmentDataService.deleteAll();
        scheduleDataService.deleteAll();
    }

    @Test
    public void shouldCreateEnrollment() {
        createEnrollment("externalId", "IPTI Schedule", "IPTI 1", now(), now(), new Time(now().toLocalTime()), EnrollmentStatus.ACTIVE, null);

        Enrollment enrollmentFromDb = enrollmentDataService.retrieveAll().get(0);

        assertNotNull(enrollmentFromDb);
        assertEquals(enrollmentFromDb.getSchedule(), scheduleDataService.findByName("IPTI Schedule"));
        assertEquals(EnrollmentStatus.ACTIVE, enrollmentFromDb.getStatus());
    }

    @Test
    public void shouldGetEnrollmentDataServiceWithSchedulePopulatedInThem() {
        createEnrollment("externalId", "IPTI Schedule", "IPTI 1", now(), now(), new Time(now().toLocalTime()), EnrollmentStatus.ACTIVE, null);

        List<Enrollment> enrollments = enrollmentDataService.retrieveAll();

        assertEquals(1, enrollments.size());
        Schedule actualSchedule = enrollments.get(0).getSchedule();
        assertEquals(scheduleDataService.findByName("IPTI Schedule"), actualSchedule);
    }

    @Test
    public void shouldFindActiveEnrollmentByExternalIdAndScheduleNameWithSchedulePopulatedInThem() {
        createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now(), now(), new Time(now().toLocalTime()), EnrollmentStatus.UNENROLLED, null);
        createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now(), now(), new Time(now().toLocalTime()), EnrollmentStatus.ACTIVE, null);

        Enrollment activeEnrollment = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", "IPTI Schedule", EnrollmentStatus.ACTIVE);
        assertNotNull(activeEnrollment);
        assertEquals(scheduleDataService.findByName("IPTI Schedule"), activeEnrollment.getSchedule());
    }

    @Test
    public void shouldFindActiveEnrollmentByExternalIdAndScheduleName() {
        createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now(), now(), new Time(now().toLocalTime()), EnrollmentStatus.UNENROLLED, null);

        assertNull(enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", "IPTI Schedule", EnrollmentStatus.ACTIVE));
    }

    @Test
    public void shouldConvertTheFulfillmentDateTimeIntoCorrectTimeZoneWhenRetrievingAnEnrollmentWithFulfilledMilestoneFromDatabase() {
        final Enrollment enrollment  = createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now(), now(), new Time(now().toLocalTime()), EnrollmentStatus.ACTIVE, null);
        final DateTime fulfillmentDateTime = DateTime.now();

        enrollmentDataService.doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Enrollment enrollmentToUpdate = enrollmentDataService.findById(enrollment.getId());
                enrollmentToUpdate.fulfillCurrentMilestone(fulfillmentDateTime);
                enrollmentDataService.update(enrollmentToUpdate);
            }
        });

        Enrollment enrollmentFromDatabase = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", "IPTI Schedule", EnrollmentStatus.ACTIVE);
        assertEquals(fulfillmentDateTime, enrollmentFromDatabase.getLastFulfilledDate());
    }

    @Test
    public void shouldReturnTheMilestoneStartDateTimeInCorrectTimeZoneForFirstMilestone() {
        DateTime now = DateTime.now();

        createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now.minusDays(2), now(), new Time(now().toLocalTime()), EnrollmentStatus.ACTIVE, null);

        Enrollment enrollmentFromDatabase = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", "IPTI Schedule", EnrollmentStatus.ACTIVE);
        assertEquals(now.minusDays(2), enrollmentFromDatabase.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnTheMilestoneStartDateTimeInCorrectTimeZoneForSecondMilestone() {
        final DateTime now = DateTime.now();
        final Enrollment enrollment = createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now.minusDays(2), now(), new Time(now().toLocalTime()), EnrollmentStatus.ACTIVE, null);

        enrollmentDataService.doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                Enrollment enrollmentToUpdate = enrollmentDataService.findById(enrollment.getId());
                enrollmentService.fulfillCurrentMilestone(enrollmentToUpdate, now);
                enrollmentDataService.update(enrollmentToUpdate);
            }
        });

        Enrollment enrollmentFromDatabase = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", "IPTI Schedule", EnrollmentStatus.ACTIVE);
        assertEquals(now, enrollmentFromDatabase.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnTheMilestoneStartDateTimeInCorrectTimeZoneWhenEnrollingIntoSecondMilestone() {
        DateTime now = DateTime.now();

        createEnrollment("entity_1", "IPTI Schedule", "IPTI 2", now.minusDays(2), now(), new Time(now().toLocalTime()), EnrollmentStatus.ACTIVE, null);

        Enrollment enrollmentFromDatabase = enrollmentDataService.findByExternalIdScheduleNameAndStatus("entity_1", "IPTI Schedule", EnrollmentStatus.ACTIVE);
        assertEquals(now, enrollmentFromDatabase.getCurrentMilestoneStartDate());
    }

    @Test
    public void shouldReturnEnrollmentsThatMatchAGivenExternalId() {
        DateTime now = now();

        createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now, now(), new Time(8, 10), EnrollmentStatus.ACTIVE, null);
        createEnrollment("entity_1", "Delivery", "Default", now, now(), new Time(8, 10), EnrollmentStatus.ACTIVE, null);
        createEnrollment("entity_2", "IPTI Schedule", "IPTI 1", now, now(), new Time(8, 10), EnrollmentStatus.ACTIVE, null);
        createEnrollment("entity_3", "IPTI Schedule", "IPTI 1", now, now(), new Time(8, 10), EnrollmentStatus.ACTIVE, null);

        List<Enrollment> filteredEnrollments = enrollmentDataService.findByExternalId("entity_1");
        assertNotNull(filteredEnrollments.get(0).getSchedule());
        assertEquals(asList(new String[] { "entity_1", "entity_1"}), Lambda.extract(filteredEnrollments, on(Enrollment.class).getExternalId()));
    }

    @Test
    public void shouldFindenrollmentDataServiceThatMatchesGivenScheduleNames() {
        createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now(), now(), new Time(DateUtil.now().toLocalTime()), EnrollmentStatus.ACTIVE, null);
        createEnrollment("entity_2", "Absolute Schedule", "milestone1", now(), now(), new Time(DateUtil.now().toLocalTime()), EnrollmentStatus.ACTIVE, null);
        createEnrollment("entity_3", "Relative Schedule", "milestone1", now(), now(), new Time(DateUtil.now().toLocalTime()), EnrollmentStatus.ACTIVE, null);
        createEnrollment("entity_4", "Relative Schedule", "milestone1", now(), now(), new Time(DateUtil.now().toLocalTime()), EnrollmentStatus.ACTIVE, null);

        List<Enrollment> filteredEnrollments = allEnrollments.findBySchedule(asList(new String[]{"IPTI Schedule", "Relative Schedule"}));

        assertEquals(3, filteredEnrollments.size());
        assertEquals(asList(new String[]{"entity_1", "entity_3", "entity_4"}), Lambda.extract(filteredEnrollments, on(Enrollment.class).getExternalId()));
        assertEquals(asList(new String[] {"IPTI Schedule", "Relative Schedule", "Relative Schedule"}), Lambda.extract(filteredEnrollments, on(Enrollment.class).getScheduleName()));
    }

    @Test
    public void shouldReturnEnrollmentsThatMatchGivenStatus() {
        DateTime now = now();

        createEnrollment("entity_1", "IPTI Schedule", "IPTI 1", now, now(), new Time(8, 10), EnrollmentStatus.COMPLETED, null);
        createEnrollment("entity_2", "Delivery", "Default", now, now(), new Time(8, 10), EnrollmentStatus.DEFAULTED, null);
        createEnrollment("entity_3", "IPTI Schedule", "IPTI 1", now, now(), new Time(8, 10), EnrollmentStatus.UNENROLLED, null);
        createEnrollment("entity_4", "IPTI Schedule", "IPTI 1", now, now(), new Time(8, 10), EnrollmentStatus.ACTIVE, null);

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

    private Enrollment createEnrollment(final String externalId, final String scheduleName, final String currentMilestoneName, final DateTime referenceDateTime, final DateTime enrollmentDateTime, final Time preferredAlertTime, final EnrollmentStatus enrollmentStatus, final Map<String,String> metadata) {
        return enrollmentDataService.doInTransaction(new TransactionCallback<Enrollment>() {
            @Override
            public Enrollment doInTransaction(TransactionStatus transactionStatus) {
                Enrollment enrollment = new EnrollmentBuilder().withExternalId(externalId).withSchedule(scheduleDataService.findByName(scheduleName)).withCurrentMilestoneName(currentMilestoneName).withStartOfSchedule(referenceDateTime).withEnrolledOn(enrollmentDateTime).withPreferredAlertTime(preferredAlertTime).withStatus(enrollmentStatus).withMetadata(metadata).toEnrollment();
                return enrollmentDataService.create(enrollment);
            }
        });
    }
}
