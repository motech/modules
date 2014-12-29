package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.messagecampaign.domain.message.DayOfWeekCampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;
import org.motechproject.messagecampaign.exception.CampaignValidationException;

import java.util.List;

public class DayOfWeekCampaign extends Campaign<DayOfWeekCampaignMessage> {

    public DayOfWeekCampaign() {

    }

    public DayOfWeekCampaign(String name, List<DayOfWeekCampaignMessage> messages, Period maxDuration) {
        super(name, messages, maxDuration);
    }

    @Override
    public DayOfWeekCampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new DayOfWeekCampaignMessage(messageRecord);
    }

    @Override
    public void validate() {
        super.validate();
        if (getMaxDuration() == null) {
            throw new CampaignValidationException("MaxDuration cannot be null in " + getName());
        }
    }

}
