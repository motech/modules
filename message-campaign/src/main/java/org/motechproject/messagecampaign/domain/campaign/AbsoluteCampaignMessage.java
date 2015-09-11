package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.LocalDate;
import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

/**
 * A type of a {@link CampaignMessage} sent for {@link AbsoluteCampaign}s.
 * The message is sent once, at a predefined date.
 */
public class AbsoluteCampaignMessage extends CampaignMessage {

    /**
     * The date to send the message on.
     */
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

    /**
     * Ensures that both {@link #date} and {@link #startTime} are not null.
     * @throws CampaignMessageValidationException in case date or startTime are not set
     */
    @Override
    public void validate() {
        if (date == null) {
            throw new CampaignMessageValidationException("Date cannot be null in " + AbsoluteCampaignMessage.class.getName());
        } else if (getStartTime() == null) {
            throw new CampaignMessageValidationException("StartTime cannot be null in " + AbsoluteCampaignMessage.class.getName());
        }
    }

}
