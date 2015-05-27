package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import java.util.List;

public class AbsoluteCampaign extends Campaign<CampaignMessage> {

    public AbsoluteCampaign() {

    }

    public AbsoluteCampaign(String name, List<CampaignMessage> messages) {
        super(name, messages);
    }

    @Override
    public CampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new CampaignMessage(messageRecord);
    }

}
