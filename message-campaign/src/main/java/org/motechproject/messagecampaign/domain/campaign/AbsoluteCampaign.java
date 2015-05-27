package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.domain.message.CampaignMessage;
import org.motechproject.messagecampaign.domain.message.CampaignMessageRecord;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

public class AbsoluteCampaign extends Campaign {

    private LocalDate date;

    public AbsoluteCampaign() {

    }

    public AbsoluteCampaign(String name, List<CampaignMessage> messages) {
        super(name, messages);
    }

    @Override
    public CampaignMessage getCampaignMessage(CampaignMessageRecord messageRecord) {
        return new CampaignMessage(messageRecord);
    }

    public LocalDate getDate(CampaignMessage cm) {
        return this.date;
    }

    @Override
    public void validate2(CampaignMessage cm) {
        if (date == null) {
            throw new CampaignMessageValidationException("Date cannot be null in " + AbsoluteCampaign.class.getName());
        } else if (getStartTime(cm) == null) {
            throw new CampaignMessageValidationException("StartTime cannot be null in " + AbsoluteCampaign.class.getName());
        }
    }
}
