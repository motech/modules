package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import java.util.List;

public class CronBasedCampaign extends Campaign<CampaignMessage> {

    public CronBasedCampaign() {

    }

    public CronBasedCampaign (String name, List<CampaignMessage> messages) {
        this (name, messages, null);
    }

    public CronBasedCampaign (String name, List<CampaignMessage> messages, Period maxDuration) {
        super (name, messages, maxDuration);
    }

    @Override
    public CampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new CampaignMessage(messageRecord);
    }

}
