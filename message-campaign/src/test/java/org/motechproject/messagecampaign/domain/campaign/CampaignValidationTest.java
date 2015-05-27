package org.motechproject.messagecampaign.domain.campaign;

import org.junit.Test;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.DayOfWeek;
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
        CampaignMessage message = new CampaignMessage(new Time(5,30), new ArrayList<DayOfWeek>());
        campaign = new DayOfWeekCampaign("name", Arrays.asList(message), null);
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenAbsoluteCampaignMessageDateIsNull() {
        CampaignMessage message = new CampaignMessage(null, null);
        campaign = new AbsoluteCampaign("name", Arrays.asList(message));
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenCronCampaignMessageCronIsNullOrEmpty() {
        CampaignMessage message = new CampaignMessage("");
        campaign = new CronBasedCampaign("name", Arrays.asList(message));
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenDayOfWeekCampaignMessageDaysOfWeekIsNull() {
        CampaignMessage message = new CampaignMessage(null, null);
        campaign = new DayOfWeekCampaign("name", Arrays.asList(message), new JodaFormatter().parsePeriod("1 Week"));
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenOffsetCampaignMessageStartTimeIsNull() {
        CampaignMessage message = new CampaignMessage(null, new JodaFormatter().parsePeriod("1 Week"));
        campaign = new OffsetCampaign("name", Arrays.asList(message));
        campaign.validate();
    }

    @Test(expected = CampaignMessageValidationException.class)
    public void shouldThrowExceptionWhenRepeatIntervalCampaignMessageRepeatIntervalIsNull() {
        CampaignMessage message = new CampaignMessage(null, null);
        campaign = new RepeatIntervalCampaign("name", Arrays.asList(message));
        campaign.validate();
    }

    @Test
    public void shouldNotThrowExceptionWhenAllCampaignsAreValid() {
        CampaignMessage absoluteMessage = new CampaignMessage(new Time(5,30), DateUtil.today());
        campaign = new AbsoluteCampaign("name", Arrays.asList(absoluteMessage));
        campaign.validate();

        CampaignMessage cronMessage = new CampaignMessage("0 11 11 11 11 ?");
        campaign = new CronBasedCampaign("name", Arrays.asList(cronMessage));
        campaign.validate();

        CampaignMessage dayOfWeekMessage = new CampaignMessage(new Time(5,30), new ArrayList<DayOfWeek>());
        campaign = new DayOfWeekCampaign("name", Arrays.asList(dayOfWeekMessage), new JodaFormatter().parsePeriod("1 Week"));
        campaign.validate();

        CampaignMessage offsetMessage = new CampaignMessage(new Time(5,30));
        campaign = new OffsetCampaign("name", Arrays.asList(offsetMessage));
        campaign.validate();

        CampaignMessage repeatIntervalMessage = new CampaignMessage(new Time(5,30), new JodaFormatter().parsePeriod("1 Week"));
        campaign = new RepeatIntervalCampaign("name", Arrays.asList(repeatIntervalMessage));
        campaign.validate();
    }
}
