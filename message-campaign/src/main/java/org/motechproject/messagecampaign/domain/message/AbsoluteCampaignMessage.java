package org.motechproject.messagecampaign.domain.message;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

public class AbsoluteCampaignMessage extends CampaignMessage {

    private LocalDate date;

    public AbsoluteCampaignMessage(CampaignMessageRecord messageRecord) {
        super(messageRecord);
        this.date = messageRecord.getDate();
    }

    public AbsoluteCampaignMessage(Time startTime, LocalDate date) {
        this(null, null, null, null, startTime, date);
    }

    public AbsoluteCampaignMessage(String name, List<String> formats, List<String> languages, String messageKey, Time startTime, LocalDate date) {
        super(name, formats, languages, messageKey, startTime);
        this.date = date;
    }

    public LocalDate getDate() {
        return this.date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public void validate() {
        if (date == null) {
            throw new CampaignMessageValidationException("Date cannot be null in " + AbsoluteCampaignMessage.class.getName());
        } else if (getStartTime() == null) {
            throw new CampaignMessageValidationException("StartTime cannot be null in " + AbsoluteCampaignMessage.class.getName());
        }
    }

}
