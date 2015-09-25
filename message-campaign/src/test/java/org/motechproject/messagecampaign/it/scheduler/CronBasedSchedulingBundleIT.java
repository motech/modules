package org.motechproject.messagecampaign.it.scheduler;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.domain.campaign.CampaignMessageRecord;
import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.service.CampaignEnrollmentsQuery;
import org.quartz.SchedulerException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;

public class CronBasedSchedulingBundleIT extends BaseSchedulingIT {

    private static final String EXTERNAL_ID = "entity_1";
    private static final String CAMPAIGN_NAME = "Cron based Message Program";

    @Before
    public void setUp() {
        getMessageCampaignService().unenroll(EXTERNAL_ID, CAMPAIGN_NAME);
    }

    @Test
    public void shouldScheduleMessages() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Cron based Message Program.entity_1.cron-message");
        assertEquals(asList(
                newDateTime(2020, 11, 11, 11, 11, 0),
                newDateTime(2021, 11, 11, 11, 11, 0),
                newDateTime(2022, 11, 11, 11, 11, 0),
                newDateTime(2023, 11, 11, 11, 11, 0),
                newDateTime(2024, 11, 11, 11, 11, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Cron based Message Program.entity_1-runonce");
        assertEquals(asList(newDateTime(2024, 11, 11, 11, 11, 0)), endOfCampaignFireTimes);

        assertNull(getMessageCampaignService().getLatestCampaignMessage(CAMPAIGN_NAME, EXTERNAL_ID));
        assertEquals("cron-message", getMessageCampaignService().getNextCampaignMessage(CAMPAIGN_NAME, EXTERNAL_ID));
    }

    @Test
    public void shouldUnscheduleJobsOnStop() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withExternalId(EXTERNAL_ID)
                .withCampaignName(CAMPAIGN_NAME);
        getMessageCampaignService().stopAll(query);

        assertNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Cron based Message Program.entity_1-runonce"));
        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Cron based Message Program.entity_1.cron-message"));
    }

    @Test
    public void shouldUnscheduleMessageJob() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        assertNotNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Cron based Message Program.entity_1-runonce"));
        assertNotNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Cron based Message Program.entity_1.cron-message"));

        List<CampaignMessageRecord> campaignMessageRecords = getCampaignMessageRecordService().findByNameAndType(CampaignType.CRON, "First");
        assertEquals(1, campaignMessageRecords.size());

        getMessageCampaignService().unscheduleMessageJob(campaignMessageRecords.get(0));

        assertNotNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Cron based Message Program.entity_1-runonce"));
        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Cron based Message Program.entity_1.cron-message"));
    }

    @Test
    public void shouldRescheduleMessageJob() throws InterruptedException, SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Cron based Message Program.entity_1.cron-message");
        assertEquals(asList(
                        newDateTime(2020, 11, 11, 11, 11, 0),
                        newDateTime(2021, 11, 11, 11, 11, 0),
                        newDateTime(2022, 11, 11, 11, 11, 0),
                        newDateTime(2023, 11, 11, 11, 11, 0),
                        newDateTime(2024, 11, 11, 11, 11, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Cron based Message Program.entity_1-runonce");
        assertEquals(asList(newDateTime(2024, 11, 11, 11, 11, 0)), endOfCampaignFireTimes);

        List<CampaignMessageRecord> campaignMessageRecords = getCampaignMessageRecordService().findByNameAndType(CampaignType.CRON, "First");
        assertEquals(1, campaignMessageRecords.size());

        final long campaignMessageRecordId = campaignMessageRecords.get(0).getId();

        getCampaignMessageRecordService().doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                CampaignMessageRecord campaignMessageRecord = getCampaignMessageRecordService().findById(campaignMessageRecordId);
                campaignMessageRecord.setCron("0 12 11 11 11 ?");
                getCampaignMessageRecordService().update(campaignMessageRecord);
            }
        });

        synchronized (lock) {
            lock.wait(4000);
        }

        getMessageCampaignService().rescheduleMessageJob(campaignMessageRecordId);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Cron based Message Program.entity_1.cron-message");
        assertEquals(asList(
                        newDateTime(2020, 11, 11, 11, 12, 0),
                        newDateTime(2021, 11, 11, 11, 12, 0),
                        newDateTime(2022, 11, 11, 11, 12, 0),
                        newDateTime(2023, 11, 11, 11, 12, 0),
                        newDateTime(2024, 11, 11, 11, 12, 0)),
                fireTimes);

        endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Cron based Message Program.entity_1-runonce");
        assertEquals(asList(newDateTime(2024, 11, 11, 11, 12, 0)), endOfCampaignFireTimes);

        getCampaignMessageRecordService().doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                CampaignMessageRecord campaignMessageRecordToUpdate = getCampaignMessageRecordService().findById(campaignMessageRecordId);
                campaignMessageRecordToUpdate.setCron("0 11 11 11 11 ?");
                getCampaignMessageRecordService().update(campaignMessageRecordToUpdate);
            }
        });
    }
}
