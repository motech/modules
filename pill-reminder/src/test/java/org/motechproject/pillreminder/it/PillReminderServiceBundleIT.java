package org.motechproject.pillreminder.it;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.pillreminder.contract.DailyPillRegimenRequest;
import org.motechproject.pillreminder.contract.DosageRequest;
import org.motechproject.pillreminder.contract.MedicineRequest;
import org.motechproject.pillreminder.dao.PillRegimenDataService;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.Medicine;
import org.motechproject.pillreminder.domain.PillRegimen;
import org.motechproject.pillreminder.service.PillReminderService;
import org.motechproject.scheduler.service.impl.MotechSchedulerServiceImpl;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class PillReminderServiceBundleIT extends BasePaxIT {

    private final Logger logger = getLogger();

    private static final String EXTERNAL_ID = UUID.randomUUID().toString();

    @Inject
    private PillReminderService pillReminderService;
    @Inject
    private PillRegimenDataService pillRegimenDataService;
    @Inject
    private BundleContext bundleContext;

    private Scheduler scheduler;
    private LocalDate startDate;
    private LocalDate endDate;

    @Before
    public void setUp() {
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
        startDate = DateUtil.newDate(2020, 1, 20);
        endDate = DateUtil.newDate(2021, 1, 20);
    }

    @Test
    public void shouldSaveTheDailyPillRegimenAndScheduleJob() throws SchedulerException {
        logger.debug("Running shouldSaveTheDailyPillRegimenAndScheduleJob");

        ArrayList<MedicineRequest> medicineRequests = new ArrayList<>();
        MedicineRequest medicineRequest1 = new MedicineRequest("m1", startDate, endDate);
        medicineRequests.add(medicineRequest1);
        MedicineRequest medicineRequest2 = new MedicineRequest("m2", startDate, startDate.plusDays(5));
        medicineRequests.add(medicineRequest2);

        ArrayList<DosageRequest> dosageContracts = new ArrayList<>();
        dosageContracts.add(new DosageRequest(9, 5, medicineRequests));

        int scheduledJobsNum = scheduler.getTriggerKeys(
                GroupMatcher.triggerGroupEquals(MotechSchedulerServiceImpl.JOB_GROUP_NAME)).size();

        logger.debug("We had {} jobs", scheduledJobsNum);
        pillReminderService.createNew(new DailyPillRegimenRequest(EXTERNAL_ID, 2, 15, 5, dosageContracts));
        logger.debug("We have {} jobs", scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(MotechSchedulerServiceImpl.JOB_GROUP_NAME)).size());

        assertEquals(scheduledJobsNum + 1,
                scheduler.getTriggerKeys(
                        GroupMatcher.triggerGroupEquals(MotechSchedulerServiceImpl.JOB_GROUP_NAME)).size());
    }

    @Test
    public void shouldRenewThePillRegimenAndScheduleJob() throws SchedulerException {
        logger.info("Running shouldRenewThePillRegimenAndScheduleJob");

        ArrayList<MedicineRequest> medicineRequests = new ArrayList<>();
        MedicineRequest medicineRequest1 = new MedicineRequest("m1", startDate, endDate);
        medicineRequests.add(medicineRequest1);
        MedicineRequest medicineRequest2 = new MedicineRequest("m2", startDate, startDate.plusDays(5));
        medicineRequests.add(medicineRequest2);

        ArrayList<DosageRequest> dosageContracts = new ArrayList<>();
        dosageContracts.add(new DosageRequest(9, 5, medicineRequests));

        int scheduledJobsNum = scheduler.getTriggerKeys(
                GroupMatcher.triggerGroupEquals(MotechSchedulerServiceImpl.JOB_GROUP_NAME)).size();

        logger.debug("We had {} jobs", scheduledJobsNum);
        pillReminderService.createNew(new DailyPillRegimenRequest(EXTERNAL_ID, 2, 15, 5, dosageContracts));
        logger.debug("We have {} jobs", scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(MotechSchedulerServiceImpl.JOB_GROUP_NAME)).size());

        ArrayList<DosageRequest> newDosageContracts = new ArrayList<>();
        newDosageContracts.add(new DosageRequest(
                9, 5, Arrays.asList(new MedicineRequest("m1", DateUtil.today(), DateUtil.today().plusDays(100)))));
        newDosageContracts.add(new DosageRequest(
                4, 5, Arrays.asList(new MedicineRequest("m2", DateUtil.today(), DateUtil.today().plusDays(100)))));

        pillReminderService.renew(new DailyPillRegimenRequest(EXTERNAL_ID, 2, 15, 5, newDosageContracts));
        logger.debug("After renewal we have {} jobs",
                scheduler.getTriggerKeys(GroupMatcher.triggerGroupEquals(MotechSchedulerServiceImpl.JOB_GROUP_NAME)).size());

        assertEquals(scheduledJobsNum + 2,
                scheduler.getTriggerKeys(
                        GroupMatcher.triggerGroupEquals(MotechSchedulerServiceImpl.JOB_GROUP_NAME)).size());

        PillRegimen regimen = pillRegimenDataService.findByExternalId(EXTERNAL_ID);
        assertNotNull(regimen);
    }

    @Test
    public void shouldFindAndUpdateDosageCurrentDate() {
        logger.info("Running shouldFindAndUpdateDosageCurrentDate");

        Medicine medicine = new Medicine("m1", startDate, endDate);
        Medicine medicine2 = new Medicine("m2", startDate, startDate.plusMonths(3));
        Set<Medicine> medicines = new HashSet<>();
        medicines.add(medicine);
        medicines.add(medicine2);

        Dosage dosage = new Dosage(new Time(9, 5), medicines);
        Set<Dosage> dosages = new HashSet<>();
        dosages.add(dosage);

        PillRegimen pillRegimen = new PillRegimen(EXTERNAL_ID, dosages, new DailyScheduleDetails(20, 5, 5));

        pillRegimen= pillRegimenDataService.create(pillRegimen);

        Long regimenId = pillRegimen.getId();
        Long dosageId = pillRegimen.getDosages().iterator().next().getId();

        pillReminderService.dosageStatusKnown(regimenId, dosageId, DateUtil.today());

        PillRegimen dbRegimen = pillRegimenDataService.findById(regimenId);
        Dosage dbDosage = dbRegimen.getDosage(dosageId);
        assertEquals(DateUtil.today(), dbDosage.getResponseLastCapturedDate());
    }

    @After
    public void tearDown() {
        pillReminderService.remove(EXTERNAL_ID);
        pillRegimenDataService.deleteAll();
    }
}
