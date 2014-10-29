package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.domain.message.CronBasedCampaignMessage;

import java.util.List;

@Entity
public class CronBasedCampaign implements Campaign<CronBasedCampaignMessage> {

    private Period maxDuration;
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
        this.maxDuration = new JodaFormatter().parsePeriod(maxDuration);
    }

    public Period maxDuration() {
        return maxDuration;
    }
}
