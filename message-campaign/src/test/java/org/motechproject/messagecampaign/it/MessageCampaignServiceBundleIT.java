package org.motechproject.messagecampaign.it;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignEnrollment;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.messagecampaign.service.MessageCampaignService;
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
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;
import static org.quartz.TriggerKey.triggerKey;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MessageCampaignServiceBundleIT extends BasePaxIT {

    @Inject
    MessageCampaignService messageCampaignService;

    @Inject
    private CampaignRecordService campaignRecordService;

    @Inject
    private BundleContext bundleContext;

    Scheduler scheduler;

    final Object lock = new Object();

    @Before
    public void setup() {
        scheduler = (Scheduler) getQuartzScheduler(bundleContext);
    }

    @Test
    public void shouldUnscheduleMessageJobsWhenCampaignIsStopped() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "PREGNANCY", new LocalDate(2020, 7, 10), null);

        TriggerKey triggerKey = triggerKey("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.PREGNANCY.entity_1.PREGNANCY", "default");

        messageCampaignService.enroll(campaignRequest);
        assertTrue(scheduler.checkExists(triggerKey));

        messageCampaignService.unenroll(campaignRequest.externalId(), campaignRequest.campaignName());
        assertFalse(scheduler.checkExists(triggerKey));
    }

    @Test
    public void shouldUpdateEnrollments() throws InterruptedException, SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "DayOfWeekCampaign", new LocalDate(2020, 7, 10), null);
        messageCampaignService.enroll(campaignRequest);

        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1");
        assertEquals(asList(
                        newDateTime(2020, 7, 10, 10, 30, 0),
                        newDateTime(2020, 7, 13, 10, 30, 0),
                        newDateTime(2020, 7, 17, 10, 30, 0),
                        newDateTime(2020, 7, 20, 10, 30, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes =
                getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 20, 10, 30, 0)), endOfCampaignFireTimes);

        campaignRecordService.doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                CampaignRecord campaignRecord = campaignRecordService.findByName("DayOfWeekCampaign");
                campaignRecord.setMaxDuration("3 Weeks");
                campaignRecordService.update(campaignRecord);
            }
        });

        synchronized (lock) {
            lock.wait(4000);
        }

        messageCampaignService.updateEnrollments(campaignRecordService.findByName("DayOfWeekCampaign").getId());

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1");
        assertEquals(asList(
                        newDateTime(2020, 7, 10, 10, 30, 0),
                        newDateTime(2020, 7, 13, 10, 30, 0),
                        newDateTime(2020, 7, 17, 10, 30, 0),
                        newDateTime(2020, 7, 20, 10, 30, 0),
                        newDateTime(2020, 7, 24, 10, 30, 0),
                        newDateTime(2020, 7, 27, 10, 30, 0)),
                fireTimes);

        endOfCampaignFireTimes =
                getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 27, 10, 30, 0)), endOfCampaignFireTimes);

        campaignRecordService.doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                CampaignRecord campaignRecordToUpdate = campaignRecordService.findByName("DayOfWeekCampaign");
                campaignRecordToUpdate.setMaxDuration("2 weeks");
                campaignRecordService.update(campaignRecordToUpdate);
            }
        });
    }

    @Test
    public void shouldScheduleAndUnscheduleJobsForEnrollment() throws SchedulerException {
        CampaignEnrollment enrollment = new CampaignEnrollment("enrollId", "DayOfWeekCampaign");
        enrollment.setReferenceDate(new LocalDate(2020, 7, 10));

        TriggerKey messageTriggerKey =
                triggerKey("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.enrollId.message_key_1", "default");

        TriggerKey endOfCampaignTriggerKey =
                triggerKey("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.enrollId-runonce", "default");

        messageCampaignService.scheduleJobsForEnrollment(enrollment);

        assertTrue(scheduler.checkExists(messageTriggerKey));
        assertTrue(scheduler.checkExists(endOfCampaignTriggerKey));

        messageCampaignService.unscheduleJobsForEnrollment(enrollment);

        assertFalse(scheduler.checkExists(messageTriggerKey));
        assertFalse(scheduler.checkExists(endOfCampaignTriggerKey));
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
