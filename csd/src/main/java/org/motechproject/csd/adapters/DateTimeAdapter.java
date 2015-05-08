package org.motechproject.csd.adapters;

import org.joda.time.DateTime;

import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.adapters.XmlAdapter;

@XmlTransient
public class DateTimeAdapter extends XmlAdapter<String, DateTime> {

    @Override
    public DateTime unmarshal(String date) {
        return DateTime.parse(date);
    }

    @Override
    public String marshal(DateTime date) {
        return date.toString();
    }
}
