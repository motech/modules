package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;

import java.util.List;

/**
 * A type of a {@link Campaign}, in which all messages define deliver dates
 * based on a CRON expression.
 *
 * @see CronBasedCampaignMessage
 */
public class CronBasedCampaign extends Campaign<CronBasedCampaignMessage> {

    public CronBasedCampaign() {

    }

    public CronBasedCampaign (String name, List<CronBasedCampaignMessage> messages) {
        this (name, messages, null);
    }

    public CronBasedCampaign (String name, List<CronBasedCampaignMessage> messages, Period maxDuration) {
        super (name, messages, maxDuration);
    }

    @Override
    public CronBasedCampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new CronBasedCampaignMessage(messageRecord);
    }

}
