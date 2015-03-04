package org.motechproject.csd.adapters;

import org.motechproject.csd.domain.DayOfTheWeek;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlTransient
public class DayOfTheWeekAdapter extends XmlAdapter<String, DayOfTheWeek> {

    @Override
    public DayOfTheWeek unmarshal(String day) {
        return DayOfTheWeek.values()[Integer.valueOf(day)];
    }

    @Override
    public String marshal(DayOfTheWeek day) {
        return String.valueOf(day.getValue());
    }
}
