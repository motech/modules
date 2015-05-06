package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;

import java.util.List;

public class RepeatIntervalCampaign extends Campaign<RepeatIntervalCampaignMessage> {

    public RepeatIntervalCampaign() {

    }

    public RepeatIntervalCampaign(String name, List<RepeatIntervalCampaignMessage> messages) {
        this(name, messages, null);
    }

    public RepeatIntervalCampaign(String name, List<RepeatIntervalCampaignMessage> messages, Period maxDuration) {
        super(name, messages, maxDuration);
    }

    @Override
    public RepeatIntervalCampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new RepeatIntervalCampaignMessage(messageRecord);
    }

}
