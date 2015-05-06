package org.motechproject.messagecampaign.domain.campaign;

import org.junit.Test;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;
import org.motechproject.messagecampaign.exception.CampaignValidationException;

import java.util.ArrayList;
import java.util.Arrays;

public class CampaignValidationTest {

    private Campaign campaign;

    @Test(expected = CampaignValidationException.class)
    public void shouldThrowExceptionWhenCampaignNameAndMessagesAreNull() {
        campaign = new AbsoluteCampaign();
        campaign.validate();
    }

    @Test(expected = CampaignValidationException.class)
    public void shouldThrowExceptionWhenDayOfWeekCampaignMaxDurationIsNull() {
        DayOfWeekCampaignMessage message = new DayOfWeekCampaignMessage(new Time(5,30), new ArrayList<DayOfWeek>());
        campaign = new DayOfWeekCampaign("name", Arrays.asList(message), null);
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenAbsoluteCampaignMessageDateIsNull() {
        AbsoluteCampaignMessage message = new AbsoluteCampaignMessage(null, null);
        campaign = new AbsoluteCampaign("name", Arrays.asList(message));
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenCronCampaignMessageCronIsNullOrEmpty() {
        CronBasedCampaignMessage message = new CronBasedCampaignMessage("");
        campaign = new CronBasedCampaign("name", Arrays.asList(message));
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenDayOfWeekCampaignMessageDaysOfWeekIsNull() {
        DayOfWeekCampaignMessage message = new DayOfWeekCampaignMessage(null, null);
        campaign = new DayOfWeekCampaign("name", Arrays.asList(message), new JodaFormatter().parsePeriod("1 Week"));
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenOffsetCampaignMessageStartTimeIsNull() {
        OffsetCampaignMessage message = new OffsetCampaignMessage(null, new JodaFormatter().parsePeriod("1 Week"));
        campaign = new OffsetCampaign("name", Arrays.asList(message));
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenRepeatIntervalCampaignMessageRepeatIntervalIsNull() {
        RepeatIntervalCampaignMessage message = new RepeatIntervalCampaignMessage(null, null);
        campaign = new RepeatIntervalCampaign("name", Arrays.asList(message));
        campaign.validate();
    }

    @Test
    public void shouldNotThrowExceptionWhenAllCampaignsAreValid() {
        AbsoluteCampaignMessage absoluteMessage = new AbsoluteCampaignMessage(new Time(5,30), DateUtil.today());
        campaign = new AbsoluteCampaign("name", Arrays.asList(absoluteMessage));
        campaign.validate();

        CronBasedCampaignMessage cronMessage = new CronBasedCampaignMessage("0 11 11 11 11 ?");
        campaign = new CronBasedCampaign("name", Arrays.asList(cronMessage));
        campaign.validate();

        DayOfWeekCampaignMessage dayOfWeekMessage = new DayOfWeekCampaignMessage(new Time(5,30), new ArrayList<DayOfWeek>());
        campaign = new DayOfWeekCampaign("name", Arrays.asList(dayOfWeekMessage), new JodaFormatter().parsePeriod("1 Week"));
        campaign.validate();

        OffsetCampaignMessage offsetMessage = new OffsetCampaignMessage(new Time(5,30));
        campaign = new OffsetCampaign("name", Arrays.asList(offsetMessage));
        campaign.validate();

        RepeatIntervalCampaignMessage repeatIntervalMessage = new RepeatIntervalCampaignMessage(new Time(5,30), new JodaFormatter().parsePeriod("1 Week"));
        campaign = new RepeatIntervalCampaign("name", Arrays.asList(repeatIntervalMessage));
        campaign.validate();
    }
}
