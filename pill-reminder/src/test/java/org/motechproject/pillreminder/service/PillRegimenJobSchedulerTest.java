package org.motechproject.pillreminder.service;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.scheduler.service.MotechSchedulerService;
import org.motechproject.scheduler.contract.CronSchedulableJob;
import org.motechproject.pillreminder.EventKeys;
import org.motechproject.pillreminder.domain.DailyScheduleDetails;
import org.motechproject.pillreminder.domain.Dosage;
import org.motechproject.pillreminder.domain.Medicine;
import org.motechproject.pillreminder.domain.PillRegimen;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashSet;

import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(PowerMockRunner.class)
@PrepareForTest(DateUtil.class)
public class PillRegimenJobSchedulerTest {

    private PillRegimen pillRegimen;

    @Mock
    private MotechSchedulerService schedulerService;

    private PillRegimenJobScheduler jobScheduler;
    private Long pillRegimenId;
    private String externalId;
    private HashSet<Dosage> dosages;

    @Before
    public void setUp() {
        initMocks(this);

        pillRegimenId = 361988L;
        externalId = "externalId";

        final HashSet<Medicine> medicines = new HashSet<Medicine>() {{
            add(new Medicine("med1", DateUtil.today(), null));
        }};

        dosages = new HashSet<Dosage>() {{
            final Dosage dosage1 = new Dosage(new Time(10, 5), medicines);
            dosage1.setId(1L);
            final Dosage dosage2 = new Dosage(new Time(20, 5), medicines);
            dosage2.setId(2L);
            add(dosage1);
            add(dosage2);
        }};


        jobScheduler = new PillRegimenJobScheduler(schedulerService);
    }

    @Test
    public void shouldScheduleDailyJob() {
        pillRegimen = new PillRegimen(externalId, dosages, new DailyScheduleDetails(15, 2, 5));
        pillRegimen.setId(pillRegimenId);
        jobScheduler.scheduleDailyJob(pillRegimen);
        verify(schedulerService, times(2)).safeScheduleJob(any(CronSchedulableJob.class));
    }

    @Test
    public void shouldUnscheduleJob() {
        pillRegimen = new PillRegimen(externalId, dosages, new DailyScheduleDetails(15, 2, 5));
        pillRegimen.setId(pillRegimenId);
        jobScheduler.unscheduleJobs(pillRegimen);
        verify(schedulerService, times(1)).safeUnscheduleJob(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER, "1");
        verify(schedulerService, times(1)).safeUnscheduleRepeatingJob(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER, "1");
        verify(schedulerService, times(1)).safeUnscheduleJob(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER, "2");
        verify(schedulerService, times(1)).safeUnscheduleRepeatingJob(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER, "2");
    }

    @Test
    public void shouldSetUpCorrectCronExpressionForDailyJob() {
        final LocalDate today = DateUtil.today();
        pillRegimen = new PillRegimen(externalId, dosages, new DailyScheduleDetails(15, 2, 5));
        pillRegimen.setId(pillRegimenId);
        final HashSet<Medicine> medicines = new HashSet<Medicine>() {{
            add(new Medicine("med1", today.minusDays(1), null));
        }};
        final Dosage dosage1 = new Dosage(new Time(10, 15), medicines);
        dosage1.setId(88L);

        final CronSchedulableJob schedulableJob = jobScheduler.getSchedulableDailyJob(pillRegimen, dosage1);
        assertEquals(String.format("0 %d %d * * ?", 20, 10), schedulableJob.getCronExpression());
        assertEquals(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER, schedulableJob.getMotechEvent().getSubject());
        assertTrue(schedulableJob.getStartDate().isAfter(today.minusDays(1).toDateTimeAtStartOfDay()));
    }

    @Test
    public void shouldSetUpCorrectCronExpressionForDailyJob_WhenTimeSpillsOverToTheNextDay() {
        final LocalDate today = DateUtil.today();
        pillRegimen = new PillRegimen(externalId, dosages, new DailyScheduleDetails(15, 2, 5));
        pillRegimen.setId(pillRegimenId);
        final HashSet<Medicine> medicines = new HashSet<Medicine>() {{
            add(new Medicine("med1", today.minusDays(1), null));
        }};
        final Dosage dosage1 = new Dosage(new Time(23, 58), medicines);
        dosage1.setId(17L);

        final CronSchedulableJob schedulableJob = jobScheduler.getSchedulableDailyJob(pillRegimen, dosage1);
        assertEquals(String.format("0 %d %d * * ?", 3, 0), schedulableJob.getCronExpression());
        assertEquals(EventKeys.PILLREMINDER_REMINDER_EVENT_SUBJECT_SCHEDULER, schedulableJob.getMotechEvent().getSubject());
        assertTrue(new LocalDate(schedulableJob.getStartDate()).isEqual(today));
    }
}


