package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.domain.message.AbsoluteCampaignMessage;

import java.util.List;

@Entity
public class AbsoluteCampaign implements Campaign<AbsoluteCampaignMessage> {
    private String name;
    private List<AbsoluteCampaignMessage> messages;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMessages(List<AbsoluteCampaignMessage> messages) {
        this.messages = messages;
    }

    @Override
    public List<AbsoluteCampaignMessage> getMessages() {
        return messages;
    }

    @Override
    public String getName() {
        return name;
    }
}
