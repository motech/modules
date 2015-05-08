package org.motechproject.csd.adapters;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlTransient
public class TimeAdapter extends XmlAdapter<String, DateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("HH:mm:ss");

    @Override
    public DateTime unmarshal(String date) {
        return DateTime.parse(date, DATE_TIME_FORMATTER);
    }

    @Override
    public String marshal(DateTime date) {
        return date.toString(DATE_TIME_FORMATTER);
    }
}
