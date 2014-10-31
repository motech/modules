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

public class OffsetSchedulingBundleIT extends BaseSchedulingIT {

    private static final String EXTERNAL_ID = "entity_1";
    private static final String CAMPAIGN_NAME = "Relative Dates Message Program";

    @Test
    public void shouldScheduleMessages() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);
        List<DateTime> fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Relative Dates Message Program.entity_1.child-info-week-1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 17, 10, 30, 0)), fireTimes);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Relative Dates Message Program.entity_1.child-info-week-1a-runonce");
        assertEquals(asList(newDateTime(2020, 7, 17, 10, 30, 0)), fireTimes);

        fireTimes = getFireTimes("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Relative Dates Message Program.entity_1.child-info-week-1b-runonce");
        assertEquals(asList(newDateTime(2020, 7, 19, 10, 30, 0)), fireTimes);

        List<DateTime> endOfCampaignFireTimes = getFireTimes("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Relative Dates Message Program.entity_1-runonce");
        assertEquals(asList(newDateTime(2020, 7, 19, 10, 30, 0)), endOfCampaignFireTimes);
    }

    @Test
    public void shouldUnscheduleJobsOnStop() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest(EXTERNAL_ID, CAMPAIGN_NAME, new LocalDate(2020, 7, 10), null);
        getMessageCampaignService().enroll(campaignRequest);

        CampaignEnrollmentsQuery query = new CampaignEnrollmentsQuery().withExternalId(EXTERNAL_ID).withCampaignName(CAMPAIGN_NAME);
        getMessageCampaignService().stopAll(query);

        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Relative Dates Message Program.entity_1.child-info-week-1-runonce"));
        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Relative Dates Message Program.entity_1.child-info-week-1a-runonce"));
        assertNull(getTrigger("org.motechproject.messagecampaign.fired-campaign-message-MessageJob.Relative Dates Message Program.entity_1.child-info-week-1b-runonce"));
        assertNull(getTrigger("org.motechproject.messagecampaign.campaign-completed-EndOfCampaignJob.Relative Dates Message Program.entity_1-runonce"));
    }
}
