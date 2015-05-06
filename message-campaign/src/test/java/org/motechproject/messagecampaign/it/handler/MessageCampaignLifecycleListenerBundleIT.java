package org.motechproject.messagecampaign.it.handler;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.dao.CampaignMessageRecordService;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessageRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.scheduler.service.MotechSchedulerService;
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
import org.quartz.TriggerKey;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.quartz.TriggerKey.triggerKey;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MessageCampaignLifecycleListenerBundleIT extends BasePaxIT {

    @Inject
    MessageCampaignService messageCampaignService;

    @Inject
    private CampaignRecordService campaignRecordService;

    @Inject
    private CampaignMessageRecordService campaignMessageRecordService;

    @Inject
    private CampaignEnrollmentDataService campaignEnrollmentDataService;

    @Inject
    private BundleContext bundleContext;

    @Inject
    private MotechSchedulerService schedulerService;

    Scheduler scheduler;

    final Object lock = new Object();

    @Before
    public void setup() {
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
        schedulerService.unscheduleAllJobs("org.motechproject.messagecampaign");
    }

    @After
    public void teardown() {
        schedulerService.unscheduleAllJobs("org.motechproject.messagecampaign");
    }

    @Test
    public void shouldUpdateEnrollments() throws InterruptedException, SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_4", "DayOfWeekCampaign", new LocalDate(2020, 7, 10), null);
        messageCampaignService.enroll(campaignRequest);

        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_4.message_key_1");
        assertEquals(asList(
                        newDateTime(2020, 7, 10, 10, 30, 0),
                        newDateTime(2020, 7, 13, 10, 30, 0),
                        newDateTime(2020, 7, 17, 10, 30, 0),
                        newDateTime(2020, 7, 20, 10, 30, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes =
                getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_4-runonce");
        assertEquals(asList(newDateTime(2020, 7, 20, 10, 30, 0)), endOfCampaignFireTimes);

        CampaignRecord campaignRecord = campaignRecordService.findByName("DayOfWeekCampaign");
        campaignRecord.setMaxDuration("3 Weeks");

        synchronized (lock) {
            campaignRecordService.update(campaignRecord);
            lock.wait(4000);
        }

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_4.message_key_1");
        assertEquals(asList(
                        newDateTime(2020, 7, 10, 10, 30, 0),
                        newDateTime(2020, 7, 13, 10, 30, 0),
                        newDateTime(2020, 7, 17, 10, 30, 0),
                        newDateTime(2020, 7, 20, 10, 30, 0),
                        newDateTime(2020, 7, 24, 10, 30, 0),
                        newDateTime(2020, 7, 27, 10, 30, 0)),
                fireTimes);

        endOfCampaignFireTimes =
                getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_4-runonce");
        assertEquals(asList(newDateTime(2020, 7, 27, 10, 30, 0)), endOfCampaignFireTimes);

        campaignRecord.setMaxDuration("2 weeks");
        campaignRecordService.update(campaignRecord);
    }

    @Test
    public void shouldDeleteCampaignEnrollments() throws InterruptedException, SchedulerException, IOException {
        CampaignMessageRecord campaignMessageRecord = new CampaignMessageRecord();
        campaignMessageRecord.setName("NewMessage");
        campaignMessageRecord.setMessageKey("new_key_11");
        campaignMessageRecord.setMessageType(CampaignType.OFFSET);
        campaignMessageRecord.setTimeOffset("1 Week");
        campaignMessageRecord.setStartTime("10:30");

        CampaignRecord campaignRecord = new CampaignRecord();
        campaignRecord.setName("NewCampaign");
        campaignRecord.setCampaignType(CampaignType.OFFSET);
        campaignRecord.setMessages(Arrays.asList(campaignMessageRecord));

        campaignRecordService.create(campaignRecord);

        CampaignRequest campaignRequest = new CampaignRequest("entity_5", "NewCampaign", new LocalDate(2020, 7, 10), null);
        messageCampaignService.enroll(campaignRequest);

        TriggerKey triggerKey = triggerKey("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.NewCampaign.entity_5.new_key_11-runonce");
        assertTrue(scheduler.checkExists(triggerKey));

        campaignRecord = campaignRecordService.findByName("NewCampaign");
        CampaignEnrollment campaignEnrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName("entity_5", "NewCampaign");
        assertNotNull(campaignEnrollment);

        synchronized (lock) {
            campaignRecordService.delete(campaignRecord);
            lock.wait(4000);
        }

        assertFalse(scheduler.checkExists(triggerKey));
        campaignEnrollment = campaignEnrollmentDataService.findByExternalIdAndCampaignName("entity_5", "DayOfWeekCampaign");
        assertNull(campaignEnrollment);
    }

    @Test
    public void shouldRescheduleMessageJob() throws InterruptedException, SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_4", "Cron based Message Program", new LocalDate(2020, 7, 10), null);
        messageCampaignService.enroll(campaignRequest);

        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Cron based Message Program.entity_4.cron-message");
        assertEquals(asList(
                        newDateTime(2020, 11, 11, 11, 11, 0),
                        newDateTime(2021, 11, 11, 11, 11, 0),
                        newDateTime(2022, 11, 11, 11, 11, 0),
                        newDateTime(2023, 11, 11, 11, 11, 0),
                        newDateTime(2024, 11, 11, 11, 11, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Cron based Message Program.entity_4-runonce");
        assertEquals(asList(newDateTime(2024, 11, 11, 11, 11, 0)), endOfCampaignFireTimes);

        List<CampaignMessageRecord> campaignMessageRecords = campaignMessageRecordService.findByNameAndType(CampaignType.CRON, "First");
        assertEquals(1, campaignMessageRecords.size());
        CampaignMessageRecord campaignMessageRecord = campaignMessageRecords.get(0);
        campaignMessageRecord.setCron("0 12 11 11 11 ?");

        synchronized (lock) {
            campaignMessageRecordService.update(campaignMessageRecord);
            lock.wait(4000);
        }

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Cron based Message Program.entity_4.cron-message");
        assertEquals(asList(
                        newDateTime(2020, 11, 11, 11, 12, 0),
                        newDateTime(2021, 11, 11, 11, 12, 0),
                        newDateTime(2022, 11, 11, 11, 12, 0),
                        newDateTime(2023, 11, 11, 11, 12, 0),
                        newDateTime(2024, 11, 11, 11, 12, 0)),
                fireTimes);

        endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Cron based Message Program.entity_4-runonce");
        assertEquals(asList(newDateTime(2024, 11, 11, 11, 12, 0)), endOfCampaignFireTimes);

        campaignMessageRecord.setCron("0 11 11 11 11 ?");
        campaignMessageRecordService.update(campaignMessageRecord);
    }

    @Test
    public void shouldDeleteCampaignMessage() throws InterruptedException, SchedulerException, IOException {
        CampaignMessageRecord campaignMessageRecord = new CampaignMessageRecord();
        campaignMessageRecord.setName("NewMessage");
        campaignMessageRecord.setMessageKey("new_key_11");
        campaignMessageRecord.setMessageType(CampaignType.OFFSET);
        campaignMessageRecord.setTimeOffset("1 Week");
        campaignMessageRecord.setStartTime("10:30");

        CampaignRecord campaignRecord = new CampaignRecord();
        campaignRecord.setName("NewCampaign");
        campaignRecord.setCampaignType(CampaignType.OFFSET);
        campaignRecord.setMessages(Arrays.asList(campaignMessageRecord));

        campaignRecordService.create(campaignRecord);

        CampaignRequest campaignRequest = new CampaignRequest("entity_5", "NewCampaign", new LocalDate(2020, 7, 10), null);
        messageCampaignService.enroll(campaignRequest);

        TriggerKey triggerKey = triggerKey("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.NewCampaign.entity_5.new_key_11-runonce");
        assertTrue(scheduler.checkExists(triggerKey));

        List<CampaignMessageRecord> campaignMessageRecords = campaignMessageRecordService.findByNameAndType(CampaignType.OFFSET, "NewMessage");
        assertEquals(1, campaignMessageRecords.size());
        campaignMessageRecord = campaignMessageRecords.get(0);

        synchronized (lock) {
            campaignMessageRecordService.delete(campaignMessageRecord);
            lock.wait(4000);
        }

        assertFalse(scheduler.checkExists(triggerKey));

        campaignRecordService.delete(campaignRecord);
    }

    private List<DateTime> getFireTimes(String triggerKey) throws SchedulerException {
        Trigger trigger = scheduler.getTrigger(triggerKey(triggerKey, "default"));
        List<DateTime> fireTimes = new ArrayList<>();
        Date nextFireTime = trigger.getNextFireTime();
        while (nextFireTime != null) {
            fireTimes.add(newDateTime(nextFireTime));
            nextFireTime = trigger.getFireTimeAfter(nextFireTime);
        }
        return fireTimes;
    }
}
