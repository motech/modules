package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

/**
 * A type of a {@link CampaignMessage} sent for {@link OffsetCampaign}s.
 * The message is sent once, after a predefined delay.
 */
public class OffsetCampaignMessage extends CampaignMessage {

    /**
     * The time period that must pass, before the message gets sent.
     */
    private Period timeOffset;

    public OffsetCampaignMessage(CampaignMessageRecord messageRecord) {
        super(messageRecord);
        setTimeOffset(messageRecord.getTimeOffset());
    }

    public OffsetCampaignMessage(Time startTime) {
        this(null, null, null, null, startTime, null);
    }

    public OffsetCampaignMessage(Time startTime, Period timeOffset) {
        this(null, null, null, null, startTime, timeOffset);
    }

    public OffsetCampaignMessage(String name, List<String> formats, List<String> languages, String messageKey, Time startTime, Period timeOffset) {
        super(name, formats, languages, messageKey, startTime);
        this.timeOffset = timeOffset;
    }

    public Period getTimeOffset() {
        return timeOffset;
    }

    public final void setTimeOffset(Period timeOffset) {
        this.timeOffset = timeOffset;
    }

    public final void setTimeOffset(String timeOffset) {
        this.timeOffset = new JodaFormatter().parsePeriod(timeOffset);
    }

    /**
     * Ensures that the {@link #startTime} has been provided.
     * @throws CampaignMessageValidationException in case {@link #startTime} has not been set
     */
    @Override
    public void validate() {
        if (getStartTime() == null) {
            throw new CampaignMessageValidationException("StartTime cannot be null in " + OffsetCampaignMessage.class.getName());
        }
    }

}
