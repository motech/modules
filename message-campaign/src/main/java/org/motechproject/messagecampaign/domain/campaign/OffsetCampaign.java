package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

public class OffsetCampaign extends Campaign {

    private Period timeOffset;

    public OffsetCampaign() {

    }

    public OffsetCampaign(String name, List<CampaignMessage> messages) {
        this(name, messages, null);
    }

    public OffsetCampaign(String name, List<CampaignMessage> messages, Period maxDuration) {
        super(name, messages, maxDuration);
    }

    @Override
    public CampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new CampaignMessage(messageRecord);
    }

    public Period getTimeOffset() {
        return timeOffset;
    }

    @Override
    public void validate2(CampaignMessage cm) {
        if (getStartTime(cm) == null) {
            throw new CampaignMessageValidationException("StartTime cannot be null in " + OffsetCampaign.class.getName());
        }
    }
}
