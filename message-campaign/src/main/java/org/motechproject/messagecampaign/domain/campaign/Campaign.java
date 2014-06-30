package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.messagecampaign.domain.message.CampaignMessage;

import java.util.List;

public interface Campaign<T extends CampaignMessage> {
    String getName();

    void setName(String name);

    void setMessages(List<T> messages);

    List<T> getMessages();
}
