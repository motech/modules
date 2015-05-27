package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;

import java.util.List;

public class AbsoluteCampaign extends Campaign {

    public AbsoluteCampaign() {

    }

    public AbsoluteCampaign(String name, List<CampaignMessage> messages) {
        super(name, messages);
    }

    @Override
    public CampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new CampaignMessage(messageRecord);
    }

    public LocalDate getDate(CampaignMessage cm) {
        // TODO: etc: campaign.getCampaignRecurrence().getMaxDuration();
        throw new RuntimeException("TODO: Not implemented");
    }
    public Time getStartTime(CampaignMessage cm) {
        throw new RuntimeException("TODO: Not implemented");
    }
}
