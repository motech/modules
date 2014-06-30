package org.motechproject.messagecampaign.domain.message;

import org.motechproject.commons.date.model.Time;

import java.util.List;

public interface CampaignMessage {
    String getName();

    List<String> getFormats();

    List<String> getLanguages();

    String getMessageKey();

    void setName(String name);

    void setFormats(List<String> formats);

    void setLanguages(List<String> languages);

    void setMessageKey(String messageKey);

    Time getStartTime();

    void setStartTime(Time startTime);

    void setStartTime(int hour, int minute);
}
