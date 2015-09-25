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
import org.motechproject.messagecampaign.domain.campaign.DayOfWeek;
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


public class DayOfWeekCampaignSchedulingBundleIT extends BaseSchedulingIT {

    private static final String EXTERNAL_ID = "entity_1";
    private static final String CAMPAIGN_NAME = "DayOfWeekCampaign";

    @Before
    public void setUp() {
        getMessageCampaignService().unenroll(EXTERNAL_ID, CAMPAIGN_NAME);
    }

    @Test
    public void shouldScheduleMessageAtItsStartTime() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null); // Friday
        getMessageCampaignService().enroll(campaignRequest);
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

        assertEquals("message_key_1", getMessageCampaignService().getNextCampaignMessage(CAMPAIGN_NAME, EXTERNAL_ID));
        assertNull(getMessageCampaignService().getLatestCampaignMessage(CAMPAIGN_NAME, EXTERNAL_ID));
    }

    @Test
    public void shouldScheduleMessageAtUserPreferredTime() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), new Time(8, 20)); // Friday
        getMessageCampaignService().enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1");
        assertEquals(asList(
                newDateTime(2020, 7, 10, 8, 20, 0),
                newDateTime(2020, 7, 13, 8, 20, 0),
                newDateTime(2020, 7, 17, 8, 20, 0),
                newDateTime(2020, 7, 20, 8, 20, 0)),
                fireTimes);

        List<DateTime> endOfCampaignFireTimes =
                getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 20, 8, 20, 0)), endOfCampaignFireTimes);
    }

    @Test
    public void shouldNotScheduleMessagesInThePastForDelayedEnrollment() throws SchedulerException {
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

            CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null); // Friday
            getMessageCampaignService().enroll(campaignRequest);
            List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1");
            assertEquals(asList(
                    newDateTime(2020, 7, 17, 10, 30, 0),
                    newDateTime(2020, 7, 20, 10, 30, 0)),
                    fireTimes);

            List<DateTime> endOfCampaignFireTimes =
                    getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_1-runonce");
            assertEquals(asList(newDateTime(2020, 7, 20, 10, 30, 0)), endOfCampaignFireTimes);
        } finally {
            DateTimeSourceUtil.setSourceInstance(DATE_TIME_SOURCE);
        }
    }

    @Test
    public void shouldUnscheduleJobsOnStop() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withExternalId(EXTERNAL_ID).withCampaignName(CAMPAIGN_NAME);
        getMessageCampaignService().stopAll(query);

        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1"));
        assertNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_1-runonce"));
    }

    @Test
    public void shouldUnscheduleMessageJob() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        assertNotNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1"));
        assertNotNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_1-runonce"));

        List<CampaignMessageRecord> campaignMessageRecords = getCampaignMessageRecordService().findByNameAndType(CampaignType.DAY_OF_WEEK, "message1");
        assertEquals(1, campaignMessageRecords.size());

        getMessageCampaignService().unscheduleMessageJob(campaignMessageRecords.get(0));

        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1"));
        assertNotNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_1-runonce"));
    }

    @Test
    public void shouldRescheduleMessageJob() throws InterruptedException, SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

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

        List<CampaignMessageRecord> campaignMessageRecords = getCampaignMessageRecordService().findByNameAndType(CampaignType.DAY_OF_WEEK, "message1");
        assertEquals(1, campaignMessageRecords.size());

        final long campaignMessageRecordId = campaignMessageRecords.get(0).getId();

        getCampaignMessageRecordService().doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                CampaignMessageRecord campaignMessageRecord = getCampaignMessageRecordService().findById(campaignMessageRecordId);
                campaignMessageRecord.setRepeatOn(asList(DayOfWeek.Thursday));
                getCampaignMessageRecordService().update(campaignMessageRecord);
            }
        });

        synchronized (lock) {
            lock.wait(4000);
        }

        getMessageCampaignService().rescheduleMessageJob(campaignMessageRecordId);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.DayOfWeekCampaign.entity_1.message_key_1");
        assertEquals(asList(
                        newDateTime(2020, 7, 16, 10, 30, 0),
                        newDateTime(2020, 7, 23, 10, 30, 0)),
                fireTimes);

        endOfCampaignFireTimes =
                getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.DayOfWeekCampaign.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 23, 10, 30, 0)), endOfCampaignFireTimes);

        getCampaignMessageRecordService().doInTransaction(new TransactionCallbackWithoutResult() {
            @Override
            protected void doInTransactionWithoutResult(TransactionStatus transactionStatus) {
                CampaignMessageRecord campaignMessageRecordToUpdate = getCampaignMessageRecordService().findById(campaignMessageRecordId);
                campaignMessageRecordToUpdate.setRepeatOn(asList(DayOfWeek.Monday, DayOfWeek.Friday));
                getCampaignMessageRecordService().update(campaignMessageRecordToUpdate);
            }
        });
    }
}
