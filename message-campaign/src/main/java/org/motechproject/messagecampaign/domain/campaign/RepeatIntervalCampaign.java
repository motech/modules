package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;

import java.util.List;

/**
 * A type of a {@link Campaign}, in which all messages are repeated
 * periodically every specified amount of time.
 *
 * @see RepeatIntervalCampaignMessage
 */
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
