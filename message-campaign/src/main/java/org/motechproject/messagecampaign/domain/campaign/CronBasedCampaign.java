package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.messagecampaign.domain.message.CronBasedCampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import java.util.List;

public class CronBasedCampaign extends Campaign<CronBasedCampaignMessage> {

    public CronBasedCampaign() {

    }

    public CronBasedCampaign (String name, List<CronBasedCampaignMessage> messages) {
        this (name, messages, null);
    }

    public CronBasedCampaign (String name, List<CronBasedCampaignMessage> messages, Period maxDuration) {
        super (name, messages, maxDuration);
    }

    @Override
    public CronBasedCampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new CronBasedCampaignMessage(messageRecord);
    }

}
