package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;

import java.util.List;

/**
 * A type of a {@link Campaign}, in which all messages have got a defined
 * delay.
 *
 * @see OffsetCampaignMessage
 */
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
