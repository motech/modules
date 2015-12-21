package org.motechproject.messagecampaign.it.scheduler;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateTimeSourceUtil;
import org.motechproject.commons.date.util.datetime.DateTimeSource;
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

public class RepeatCampaignSchedulingBundleIT extends BaseSchedulingIT {

    private static final String EXTERNAL_ID = "entity_1";
    private static final String CAMPAIGN_NAME = "WeeklyCampaign";
    private static final String HOURLY_CAMPAIGN_NAME = "HourlyCampaign";

    @Before
    public void setUp() {
        getMessageCampaignService().unenroll(EXTERNAL_ID, CAMPAIGN_NAME);
        getMessageCampaignService().unenroll(EXTERNAL_ID, HOURLY_CAMPAIGN_NAME);
    }

    @Test
    public void shouldScheduleAllMessagesOfCampaignAtMessageStartTime() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 10, 30, 0),
                newDateTime(2020, 7, 17, 10, 30, 0),
                newDateTime(2020, 7, 24, 10, 30, 0)),
                fireTimes);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_2-repeat");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 8, 30, 0),
                newDateTime(2020, 7, 20, 8, 30, 0),
                newDateTime(2020, 7, 30, 8, 30, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.WeeklyCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 30, 8, 30, 0)), endOfCampaignFireTimes);

        assertNull(getMessageCampaignService().getLatestCampaignMessage(CAMPAIGN_NAME, EXTERNAL_ID));
        assertEquals("message_key_2", getMessageCampaignService().getNextCampaignMessage(CAMPAIGN_NAME, EXTERNAL_ID));
    }

    @Test
    public void shouldScheduleWeeklyMessagesAtUserSpecifiedTime() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), new Time(15, 20));
        getMessageCampaignService().enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 15, 20, 0),
                newDateTime(2020, 7, 17, 15, 20, 0),
                newDateTime(2020, 7, 24, 15, 20, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.WeeklyCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 30, 15, 20, 0)), endOfCampaignFireTimes);
    }

    @Test
    public void shouldNotScheduleMessagesInPastForDelayedEnrollment() throws SchedulerException {
        try {
            DateTimeSourceUtil.setSourceInstance(new DateTimeSource() {
                private DateTime dateTime = new DateTime(2020, 7, 15, 0, 0);

                @Override
                public DateTimeZone timeZone() {
                    return dateTime.getZone();
                }

                @Override
                public DateTime now() {
                    return dateTime;
                }

                @Override
                public LocalDate today() {
                    return dateTime.toLocalDate();
                }
            });

            CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
            getMessageCampaignService().enroll(campaignRequest);
            List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat");
            assertEquals(asList(
                    newDateTime(2020, 7, 17, 10, 30, 0),
                    newDateTime(2020, 7, 24, 10, 30, 0)),
                    fireTimes);


            List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.WeeklyCampaign.entity_1-runonce");
            assertEquals(asList(newDateTime(2020, 7, 30, 8, 30, 0)), endOfCampaignFireTimes);
        } finally {
            DateTimeSourceUtil.setSourceInstance(DATE_TIME_SOURCE);
        }
    }

    @Test
    public void shouldScheduleMessagesEvery12Hours() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, HOURLY_CAMPAIGN_NAME, new LocalDate(2020, 7, 10), new Time(4, 30));
        getMessageCampaignService().enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.HourlyCampaign.entity_1.message_key_1-repeat");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 4, 30, 0),
                newDateTime(2020, 7, 10, 16, 30, 0),
                newDateTime(2020, 7, 11, 4, 30, 0),
                newDateTime(2020, 7, 11, 16, 30, 0)),
                fireTimes);


        List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.HourlyCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 11, 16, 30, 0)), endOfCampaignFireTimes);
    }

    @Test
    public void shouldUnscheduleJobsOnStop() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withExternalId(EXTERNAL_ID).withCampaignName(CAMPAIGN_NAME);
        getMessageCampaignService().stopAll(query);

        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat"));
        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_2-repeat"));
        assertNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.WeeklyCampaign.entity_1-runonce"));
    }

    @Test
    public void shouldUnscheduleMessageJob() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        assertNotNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat"));
        assertNotNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_2-repeat"));
        assertNotNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.WeeklyCampaign.entity_1-runonce"));

        List<CampaignMessageRecord> campaignMessageRecords = getCampaignMessageRecordService().findByNameAndType(CampaignType.REPEAT_INTERVAL, "message2");
        assertEquals(1, campaignMessageRecords.size());

        getMessageCampaignService().unscheduleMessageJob(campaignMessageRecords.get(0));

        assertNotNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat"));
        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_2-repeat"));
        assertNotNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.WeeklyCampaign.entity_1-runonce"));
    }

    @Test
    public void shouldRescheduleMessageJob() throws InterruptedException, SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat");
        assertEquals(asList(
                        newDateTime(2020, 7, 10, 10, 30, 0),
                        newDateTime(2020, 7, 17, 10, 30, 0),
                        newDateTime(2020, 7, 24, 10, 30, 0)),
                fireTimes);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_2-repeat");
        assertEquals(asList(
                        newDateTime(2020, 7, 10, 8, 30, 0),
                        newDateTime(2020, 7, 20, 8, 30, 0),
                        newDateTime(2020, 7, 30, 8, 30, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.WeeklyCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 30, 8, 30, 0)), endOfCampaignFireTimes);

        List<CampaignMessageRecord> campaignMessageRecords = getCampaignMessageRecordService().findByNameAndType(CampaignType.REPEAT_INTERVAL, "message2");
        assertEquals(1, campaignMessageRecords.size());

        final long campaignMessageRecordId = campaignMessageRecords.get(0).getId();

        getCampaignMessageRecordService().doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                CampaignMessageRecord campaignMessageRecord = getCampaignMessageRecordService().findById(campaignMessageRecordId);
                campaignMessageRecord.setRepeatEvery("6 days");
                getCampaignMessageRecordService().update(campaignMessageRecord);
            }
        });

        synchronized (lock) {
            lock.wait(4000);
        }

        getMessageCampaignService().rescheduleMessageJob(campaignMessageRecordId);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_1-repeat");
        assertEquals(asList(
                        newDateTime(2020, 7, 10, 10, 30, 0),
                        newDateTime(2020, 7, 17, 10, 30, 0),
                        newDateTime(2020, 7, 24, 10, 30, 0)),
                fireTimes);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.WeeklyCampaign.entity_1.message_key_2-repeat");
        assertEquals(asList(
                        newDateTime(2020, 7, 10, 8, 30, 0),
                        newDateTime(2020, 7, 16, 8, 30, 0),
                        newDateTime(2020, 7, 22, 8, 30, 0),
                        newDateTime(2020, 7, 28, 8, 30, 0)),
                fireTimes);

        endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.WeeklyCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 28, 8, 30, 0)), endOfCampaignFireTimes);

        getCampaignMessageRecordService().doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                CampaignMessageRecord campaignMessageRecordToUpdate = getCampaignMessageRecordService().findById(campaignMessageRecordId);
                campaignMessageRecordToUpdate.setRepeatEvery("10 days");
                getCampaignMessageRecordService().update(campaignMessageRecordToUpdate);
            }
        });
    }
}
