package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;
import org.motechproject.messagecampaign.exception.CampaignValidationException;

import java.util.ArrayList;
import java.util.List;

public abstract class Campaign<T extends CampaignMessage> {
    private String name;
    private List<T> messages;
    private Period maxDuration;

    public Campaign () {

    }

    public Campaign (String name, List<T> messages) {
        this(name, messages, null);
    }

    protected Campaign(String name, List<T> messages, Period maxDuration) {
        this.name = name;
        this.messages = messages;
        this.maxDuration = maxDuration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<T> getMessages() {
        return messages;
    }

    public void setMessages(List<T> messages) {
        this.messages = messages;
    }

    public Period getMaxDuration() {
        return maxDuration;
    }

    public void setMaxDuration(Period maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setMaxDuration(String maxDuration) {
        if (maxDuration != null) {
            this.maxDuration = new JodaFormatter().parsePeriod(maxDuration);
        }
    }

    public void setMessageRecords(List<CampaignMessageRecord> messageRecords) {
        List<T> campaignMessages = new ArrayList<>();
        for (CampaignMessageRecord messageRecord : messageRecords) {
            T campaignMessage = getCampaignMessage(messageRecord);
            campaignMessages.add(campaignMessage);
        }
        setMessages(campaignMessages);
    }

    public abstract T getCampaignMessage(CampaignMessageRecord messageRecord);

    public void validate() {
        if (name == null) {
            throw new CampaignValidationException("Name cannot be null in " + getClass().getName());
        } else if (messages == null || messages.isEmpty()) {
            throw new CampaignValidationException("Messages cannot be null or empty in " + name);
        }
        for (T campaignMessage : getMessages() ) {
            campaignMessage.validate();
        }
    }
}
