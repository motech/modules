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

public class CronBasedSchedulingBundleIT extends BaseSchedulingIT {

    @Test
    public void shouldScheduleMessage() throws SchedulerException {
        CampaignRequest campaignRequest = new CampaignRequest("entity_1", "Cron based Message Program", new LocalDate(2020, 7, 10), null);
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
    }
}
