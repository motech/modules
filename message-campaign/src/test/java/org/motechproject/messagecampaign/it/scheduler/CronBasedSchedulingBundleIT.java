package org.motechproject.messagecampaign.it.scheduler;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.service.CampaignEnrollmentsQuery;
import org.quartz.SchedulerException;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;

public class CronBasedSchedulingBundleIT extends BaseSchedulingIT {

    private static final String EXTERNAL_ID = "entity_1";
    private static final String CAMPAIGN_NAME = "Cron based Message Program";

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
}
