package org.motechproject.messagecampaign.domain.campaign;

import org.joda.time.Period;
import org.motechproject.commons.date.model.Time;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.commons.date.util.JodaFormatter;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

/**
 * A type of a {@link CampaignMessage} sent for {@link RepeatIntervalCampaign}s.
 * The message is sent periodically, with provided delays between each message.
 */
public class RepeatIntervalCampaignMessage extends CampaignMessage {

    /**
     * The time interval between messages.
     */
    private Period repeatInterval;

    public RepeatIntervalCampaignMessage(CampaignMessageRecord messageRecord) {
        super(messageRecord);
        setRepeatInterval(messageRecord.getRepeatEvery());
    }

    public RepeatIntervalCampaignMessage(Time startTime, Period repeatInterval) {
        this(null, null, null, null, startTime, repeatInterval);
    }

    public RepeatIntervalCampaignMessage(String name, List<String> formats, List<String> languages, String messageKey, Time startTime, Period repeatInterval) {
        super(name, formats, languages, messageKey, startTime);
        this.repeatInterval = repeatInterval;
    }

    public long getRepeatIntervalInMillis() {
        return repeatInterval.toDurationFrom(DateUtil.now()).getMillis();
    }

    public Integer getRepeatIntervalInSeconds() {
        return (int) repeatInterval.toDurationFrom(DateUtil.now()).getStandardSeconds();
    }

    public Period getRepeatInterval() {
        return repeatInterval;
    }

    public final void setRepeatInterval(Period repeatInterval) {
        this.repeatInterval = repeatInterval;
    }

    public final void setRepeatInterval(String repeatInterval) {
        this.repeatInterval = new JodaFormatter().parsePeriod(repeatInterval);
    }

    /**
     * Ensures that both {@link #repeatInterval} and {@link #startTime} are not null.
     * @throws CampaignMessageValidationException in case repeatInterval or startTime has not been set
     */
    @Override
    public void validate() {
        if (repeatInterval == null) {
            throw new CampaignMessageValidationException("RepeatInterval cannot be null in " + RepeatIntervalCampaignMessage.class.getName());
        } else if (getStartTime() == null) {
            throw new CampaignMessageValidationException("StartTime cannot be null in " + RepeatIntervalCampaignMessage.class.getName());
        }
    }
}
