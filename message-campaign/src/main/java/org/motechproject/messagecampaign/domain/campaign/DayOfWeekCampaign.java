package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.domain.message.DayOfWeekCampaignMessage;

import java.util.List;

@Entity
public class DayOfWeekCampaign implements Campaign<DayOfWeekCampaignMessage> {

    private Period maxDuration;
    private String name;
    private List<DayOfWeekCampaignMessage> messages;

    public DayOfWeekCampaign name(String name) {
        setName(name);
        return this;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setMessages(List<DayOfWeekCampaignMessage> messages) {
        this.messages = messages;
    }

    @Override
    public List<DayOfWeekCampaignMessage> getMessages() {
        return messages;
    }

    @Override
    public String getName() {
        return name;
    }

    public DayOfWeekCampaign maxDuration(Period maxDuration) {
        this.maxDuration = maxDuration;
        return this;
    }

    public Period maxDuration() {
        return maxDuration;
    }
}
