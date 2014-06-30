package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.domain.message.OffsetCampaignMessage;

import java.util.List;

@Entity
public class OffsetCampaign implements Campaign<OffsetCampaignMessage> {

    private String maxDuration;
    private String name;
    private List<OffsetCampaignMessage> messages;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMessages(List<OffsetCampaignMessage> messages) {
        this.messages = messages;
    }

    @Override
    public List<OffsetCampaignMessage> getMessages() {
        return messages;
    }

    @Override
    public String getName() {
        return name;
    }

    public String maxDuration() {
        return maxDuration;
    }

    public void maxDuration(String maxDuration) {
        this.maxDuration = maxDuration;
    }
}
