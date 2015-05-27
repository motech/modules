package org.motechproject.messagecampaign.domain.message;

import org.motechproject.commons.date.model.Time;
import org.motechproject.messagecampaign.exception.CampaignMessageValidationException;

import java.util.ArrayList;
import java.util.List;

public class DayOfWeekCampaignMessage extends CampaignMessage0 {

    private List<DayOfWeek> daysOfWeek;

    public DayOfWeekCampaignMessage(CampaignMessageRecord messageRecord) {
        super(messageRecord);
        this.daysOfWeek = retrieveDaysOfWeek(messageRecord.getRepeatOn());
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

    private List<DayOfWeek> retrieveDaysOfWeek(List<String> days) {
        List<DayOfWeek> daysList = new ArrayList<>();
        for (String day : days) {
            daysList.add(DayOfWeek.parse(day));
        }
        return daysList;
    }

    @Override
    public void validate() {
        if (daysOfWeek == null) {
            throw new CampaignMessageValidationException("DaysOfWeek cannot be null in " + DayOfWeekCampaignMessage.class.getName());
        } else if (getStartTime() == null) {
            throw new CampaignMessageValidationException("StartTime cannot be null in " + DayOfWeekCampaignMessage.class.getName());
        }
    }
}
