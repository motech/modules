package org.motechproject.messagecampaign.builder;

import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.domain.campaign.AbsoluteCampaign;
import org.motechproject.messagecampaign.domain.campaign.CronBasedCampaign;
import org.motechproject.messagecampaign.domain.campaign.OffsetCampaign;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;

import java.util.Arrays;

public final class CampaignBuilder {

    public static AbsoluteCampaign defaultAbsoluteCampaign() {
        CampaignMessage campaignMessage1 = new CampaignMessage("AM1", Arrays.asList("IVR"),
                Arrays.asList("en"), "random-1", new Time(9, 30), DateUtil.today().plusDays(1));

        CampaignMessage campaignMessage2 = new CampaignMessage("AM2", Arrays.asList("IVR"),
                Arrays.asList("en"), "random-2", new Time(9, 30), DateUtil.today().plusDays(2));

        return new AbsoluteCampaign("testCampaign", Arrays.asList(campaignMessage1, campaignMessage2));
    }

    public static CronBasedCampaign defaultCronBasedCampaign() {
        CampaignMessage campaignMessage1 = new CampaignMessage("CM1", Arrays.asList("IVR"),
                Arrays.asList("en"), "cron-message1", "0 11 11 11 11 ?");

        CampaignMessage campaignMessage2 = new CampaignMessage("CM2", Arrays.asList("IVR"),
                Arrays.asList("en"), "cron-message2", "0 11 11 11 11 ?");

        CronBasedCampaign campaign = new CronBasedCampaign("testCampaign", Arrays.asList(campaignMessage1, campaignMessage2));
        campaign.setMaxDuration("1 Week");

        return campaign;
    }

    public static OffsetCampaign defaultOffsetCampaign() {
        CampaignMessage campaignMessage1 = new CampaignMessage("OM1", Arrays.asList("IVR"),
                Arrays.asList("en"), "child-info-week-1", new Time(9, 30), new JodaFormatter().parsePeriod("1 Week"));

        CampaignMessage campaignMessage2 = new CampaignMessage("OM2", Arrays.asList("IVR"),
                Arrays.asList("en"), "child-info-week-1a", new Time(9, 30), new JodaFormatter().parsePeriod("2 Weeks"));

        CampaignMessage campaignMessage3 = new CampaignMessage("OM3", Arrays.asList("IVR"),
                Arrays.asList("en"), "child-info-month-1", new Time(9, 30), new JodaFormatter().parsePeriod("1 Month"));

        return new OffsetCampaign("testCampaign", Arrays.asList(campaignMessage1, campaignMessage2, campaignMessage3));
    }
}
