package org.motechproject.messagecampaign.builder;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.domain.message.AbsoluteCampaignMessage;
import org.motechproject.messagecampaign.domain.message.CronBasedCampaignMessage;
import org.motechproject.messagecampaign.domain.message.OffsetCampaignMessage;

import java.util.Arrays;

public class CampaignMessageBuilder {

    public AbsoluteCampaignMessage absoluteCampaignMessage(String name, LocalDate date, String messageKey, Time startTime) {
        AbsoluteCampaignMessage absoluteCampaignMessage = new AbsoluteCampaignMessage();
        absoluteCampaignMessage.setName(name);
        absoluteCampaignMessage.setDate(date);
        absoluteCampaignMessage.setMessageKey(messageKey);
        absoluteCampaignMessage.setStartTime(startTime);
        absoluteCampaignMessage.setFormats(Arrays.asList("IVR"));
        absoluteCampaignMessage.setLanguages(Arrays.asList("en"));
        return absoluteCampaignMessage;
    }

    public CronBasedCampaignMessage cronBasedCampaignMessage(String name, String cron, String messageKey) {
        CronBasedCampaignMessage cronBasedCampaignMessage = new CronBasedCampaignMessage();
        cronBasedCampaignMessage.setName(name);
        cronBasedCampaignMessage.setCron(cron);
        cronBasedCampaignMessage.setMessageKey(messageKey);
        return cronBasedCampaignMessage;
    }

    public OffsetCampaignMessage offsetCampaignMessage(String name, String timeOffset, String messageKey, Time startTime) {
        OffsetCampaignMessage offsetCampaignMessage = new OffsetCampaignMessage();
        offsetCampaignMessage.setName(name);
        offsetCampaignMessage.setTimeOffset(new JodaFormatter().parsePeriod(timeOffset));
        offsetCampaignMessage.setMessageKey(messageKey);
        offsetCampaignMessage.setStartTime(startTime);
        return offsetCampaignMessage;
    }
}
