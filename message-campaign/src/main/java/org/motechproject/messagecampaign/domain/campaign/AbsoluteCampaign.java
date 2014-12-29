package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.messagecampaign.domain.message.AbsoluteCampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import java.util.List;

public class AbsoluteCampaign extends Campaign<AbsoluteCampaignMessage> {

    public AbsoluteCampaign() {

    }

    public AbsoluteCampaign(String name, List<AbsoluteCampaignMessage> messages) {
        super(name, messages);
    }

    @Override
    public AbsoluteCampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new AbsoluteCampaignMessage(messageRecord);
    }

}
