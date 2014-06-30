package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.domain.message.RepeatIntervalCampaignMessage;

import java.util.List;

@Entity
public class RepeatIntervalCampaign implements Campaign<RepeatIntervalCampaignMessage> {

    private Period maxDuration;
    private String name;
    private List<RepeatIntervalCampaignMessage> messages;

    public RepeatIntervalCampaign name(String name) {
        setName(name);
        return this;
    }

    public RepeatIntervalCampaign maxDuration(Period maxDuration) {
        this.maxDuration = maxDuration;
        return this;
    }

    public Period maxDuration() {
        return maxDuration;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMessages(List<RepeatIntervalCampaignMessage> messages) {
        this.messages = messages;
    }

    @Override
    public List<RepeatIntervalCampaignMessage> getMessages() {
        return messages;
    }
}
