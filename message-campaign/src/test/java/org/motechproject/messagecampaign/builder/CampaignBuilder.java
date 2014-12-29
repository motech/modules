package org.motechproject.messagecampaign.builder;

import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaign;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaign;
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaign;
import org.motechproject.messagecampaign.domain.message.AbsoluteCampaignMessage;
import org.motechproject.messagecampaign.domain.message.CronBasedCampaignMessage;
import org.motechproject.messagecampaign.domain.message.OffsetCampaignMessage;

import java.util.Arrays;

public final class CampaignBuilder {

    public static AbsoluteCampaign defaultAbsoluteCampaign() {
        AbsoluteCampaignMessage campaignMessage1 = new AbsoluteCampaignMessage("AM1", Arrays.asList("IVR"),
                Arrays.asList("en"), "random-1", new Time(9, 30), DateUtil.today().plusDays(1));

        AbsoluteCampaignMessage campaignMessage2 = new AbsoluteCampaignMessage("AM2", Arrays.asList("IVR"),
                Arrays.asList("en"), "random-2", new Time(9, 30), DateUtil.today().plusDays(2));

        return new AbsoluteCampaign("testCampaign", Arrays.asList(campaignMessage1, campaignMessage2));
    }

    public static CronBasedCampaign defaultCronBasedCampaign() {
        CronBasedCampaignMessage campaignMessage1 = new CronBasedCampaignMessage("CM1", Arrays.asList("IVR"),
                Arrays.asList("en"), "cron-message1", "0 11 11 11 11 ?");

        CronBasedCampaignMessage campaignMessage2 = new CronBasedCampaignMessage("CM2", Arrays.asList("IVR"),
                Arrays.asList("en"), "cron-message2", "0 11 11 11 11 ?");

        CronBasedCampaign campaign = new CronBasedCampaign("testCampaign", Arrays.asList(campaignMessage1, campaignMessage2));
        campaign.setMaxDuration("1 Week");

        return campaign;
    }

    public static OffsetCampaign defaultOffsetCampaign() {
        OffsetCampaignMessage campaignMessage1 = new OffsetCampaignMessage("OM1", Arrays.asList("IVR"),
                Arrays.asList("en"), "child-info-week-1", new Time(9, 30), new JodaFormatter().parsePeriod("1 Week"));

        OffsetCampaignMessage campaignMessage2 = new OffsetCampaignMessage("OM2", Arrays.asList("IVR"),
                Arrays.asList("en"), "child-info-week-1a", new Time(9, 30), new JodaFormatter().parsePeriod("2 Weeks"));

        OffsetCampaignMessage campaignMessage3 = new OffsetCampaignMessage("OM3", Arrays.asList("IVR"),
                Arrays.asList("en"), "child-info-month-1", new Time(9, 30), new JodaFormatter().parsePeriod("1 Month"));

        return new OffsetCampaign("testCampaign", Arrays.asList(campaignMessage1, campaignMessage2, campaignMessage3));
    }
}
