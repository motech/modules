package org.motechproject.adherence.service;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.motechproject.util.datetime.DateTimeSource;
import org.motechproject.util.datetime.DefaultDateTimeSource;

import java.util.Calendar;
import java.util.TimeZone;

public class MockedDateTimeSource implements DateTimeSource {

    private DateTimeZone timeZone;
    private DateTime now;

    @Override
    public DateTimeZone timeZone() {
        TimeZone tz = Calendar.getInstance().getTimeZone();
        this.timeZone = DateTimeZone.forTimeZone(tz);
        return this.timeZone;
    }

    @Override
    public DateTime now() {
        return now == null ? new DefaultDateTimeSource().now() : now;
    }

    @Override
    public LocalDate today() {
        return now == null ? new DefaultDateTimeSource().today() : now.toLocalDate();
    }

    public void setNow(DateTime now) {
        this.now = now;
    }

    public void reset() {
        setNow(null);
    }
}
