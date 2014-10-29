package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.messagecampaign.domain.message.OffsetCampaignMessage;

import java.util.List;

@Entity
public class OffsetCampaign implements Campaign<OffsetCampaignMessage> {

    private Period maxDuration;
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

    public Period maxDuration() {
        return maxDuration;
    }

    public void maxDuration(String maxDuration) {
        this.maxDuration = new JodaFormatter().parsePeriod(maxDuration);
    }
}
