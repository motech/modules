package org.motechproject.messagecampaign.domain.campaign;

import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.List;

/**
 * A type of a {@link CampaignMessage} sent for {@link DayOfWeekCampaign}s.
 * The message is sent at the specified days of a week.
 */
public class DayOfWeekCampaignMessage extends CampaignMessage {

    /**
     * The list containing days of a week, during which the message wil be delivered.
     */
    private List<DayOfWeek> daysOfWeek;

    public DayOfWeekCampaignMessage(CampaignMessageRecord messageRecord) {
        super(messageRecord);
        this.daysOfWeek = messageRecord.getRepeatOn();
    }

    public DayOfWeekCampaignMessage(Time startTime, List<DayOfWeek> daysOfWeek) {
        this(null, null, null, null, startTime, daysOfWeek);
    }

    public DayOfWeekCampaignMessage(String name, List<String> formats, List<String> languages, String messageKey, Time startTime, List<DayOfWeek> daysOfWeek) {
        super(name, formats, languages, messageKey, startTime);
        this.daysOfWeek = daysOfWeek;
    }

    public List<DayOfWeek> getDaysOfWeek() {
        return daysOfWeek;
    }

    public void setDaysOfWeek(List<DayOfWeek> daysOfWeek) {
        this.daysOfWeek = daysOfWeek;
    }

    /**
     * Ensures that both {@link #daysOfWeek} and {@link #startTime} are not null.
     * @throws CampaignMessageValidationException in case daysOfWeek or startTime has not been set
     */
    @Override
    public void validate() {
        if (daysOfWeek == null) {
            throw new CampaignMessageValidationException("DaysOfWeek cannot be null in " + DayOfWeekCampaignMessage.class.getName());
        } else if (getStartTime() == null) {
            throw new CampaignMessageValidationException("StartTime cannot be null in " + DayOfWeekCampaignMessage.class.getName());
        }
    }
}
