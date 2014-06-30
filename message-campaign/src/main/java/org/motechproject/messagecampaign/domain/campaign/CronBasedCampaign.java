package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.domain.message.CronBasedCampaignMessage;

import java.util.List;

@Entity
public class CronBasedCampaign implements Campaign<CronBasedCampaignMessage> {

    private String maxDuration;
    private String name;
    private List<CronBasedCampaignMessage> messages;

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMessages(List<CronBasedCampaignMessage> messages) {
        this.messages = messages;
    }

    @Override
    public List<CronBasedCampaignMessage> getMessages() {
        return messages;
    }

    @Override
    public String getName() {
        return name;
    }

    public void maxDuration(String maxDuration) {
        this.maxDuration = maxDuration;
    }

    public String maxDuration() {
        return maxDuration;
    }
}
