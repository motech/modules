package org.motechproject.csd.adapters;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlTransient
public class DateAdapter extends XmlAdapter<String, DateTime> {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd");

    @Override
    public DateTime unmarshal(String date) {
        return DateTime.parse(date);
    }

    @Override
    public String marshal(DateTime date) {
        return date.toString(DATE_TIME_FORMATTER);
    }
}
