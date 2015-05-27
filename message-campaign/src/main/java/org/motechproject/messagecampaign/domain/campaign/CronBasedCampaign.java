package org.motechproject.messagecampaign.domain.campaign;

import org.apache.commons.lang.StringUtils;
import org.joda.time.Period;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

public class CronBasedCampaign extends Campaign {

    private String cron;

    public CronBasedCampaign() {

    }

    public CronBasedCampaign (String name, List<CampaignMessage> messages) {
        this (name, messages, null);
    }

    public CronBasedCampaign (String name, List<CampaignMessage> messages, Period maxDuration) {
        super(name, messages, maxDuration);
    }

    @Override
    public CampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new CampaignMessage(messageRecord);
    }

    public String getCron(CampaignMessage cm) {
        return this.cron;
    }

    @Override
    public void validate2(CampaignMessage cm) {
        if (StringUtils.isBlank(cron)) {
            throw new CampaignMessageValidationException("Cron cannot be null or empty in " + CronBasedCampaign.class.getName());
        }
    }
}
