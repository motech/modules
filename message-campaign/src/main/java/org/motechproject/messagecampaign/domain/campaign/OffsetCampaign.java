package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.messagecampaign.domain.message.OffsetCampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import java.util.List;

public class OffsetCampaign extends Campaign<OffsetCampaignMessage> {

    public OffsetCampaign() {

    }

    public OffsetCampaign(String name, List<OffsetCampaignMessage> messages) {
        this(name, messages, null);
    }

    public OffsetCampaign(String name, List<OffsetCampaignMessage> messages, Period maxDuration) {
        super(name, messages, maxDuration);
    }

    @Override
    public OffsetCampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new OffsetCampaignMessage(messageRecord);
    }

}
