package org.motechproject.messagecampaign.domain.campaign;

import java.util.List;

/**
 * A type of a {@link Campaign}, in which all messages have got predefined
 * delivery dates.
 *
 * @see AbsoluteCampaignMessage
 */
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
