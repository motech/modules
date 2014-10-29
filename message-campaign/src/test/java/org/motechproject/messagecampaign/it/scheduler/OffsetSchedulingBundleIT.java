package org.motechproject.messagecampaign.it.scheduler;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.junit.Test;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.quartz.SchedulerException;

import java.util.List;

import static java.util.Arrays.asList;
import static junit.framework.Assert.assertEquals;
import static org.motechproject.commons.date.util.DateUtil.newDateTime;

public class OffsetSchedulingBundleIT extends BaseSchedulingIT {

    @Test
    public void shouldScheduleMessage() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "Relative Dates Message Program", new LocalDate(2020, 7, 10), null);
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
}
