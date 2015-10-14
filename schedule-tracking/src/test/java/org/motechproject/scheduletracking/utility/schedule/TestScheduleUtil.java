package org.motechproject.scheduletracking.utility.schedule;


import org.apache.commons.io.IOUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.motechproject.commons.date.util.DateTimeSourceUtil;
import org.motechproject.commons.date.util.datetime.DateTimeSource;
import org.motechproject.commons.date.util.datetime.DefaultDateTimeSource;
import org.motechproject.scheduletracking.domain.Schedule;
import org.motechproject.scheduletracking.domain.ScheduleFactory;
import org.motechproject.scheduletracking.repository.TrackedSchedulesJsonReaderImpl;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

public class TestScheduleUtil {

    private static final DateTimeSource DATE_TIME_SOURCE = new DefaultDateTimeSource();

    private static final Logger LOGGER = LoggerFactory.getLogger(TestScheduleUtil.class);

    public static String getScheduleJsonFromFile(BundleContext bundleContext, String path, String filename) {
        Enumeration<URL> enumeration = bundleContext.getBundle().findEntries(path, filename, false);

        byte[] byteArr = new byte[0];
        if (enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();

            InputStream in = null;
            try {
                in = url.openStream();
                byteArr = IOUtils.toByteArray(in);
            } catch (IOException e) {
                LOGGER.warn("There was a problem with retrieving json file with schedule.");
            } finally {
                IOUtils.closeQuietly(in);
            }
        }

        return new String(byteArr);
    }

    public static List<Schedule> getTestSchedules(BundleContext bundleContext, String path) {
        List<Schedule> schedules = new ArrayList<>();
        Enumeration<URL> enumeration = bundleContext.getBundle().findEntries(path, "*", false);

        while(enumeration.hasMoreElements()) {
            URL url = enumeration.nextElement();
            byte[] byteArr = new byte[0];

            InputStream in = null;
            try {
                in = url.openStream();
                byteArr = IOUtils.toByteArray(in);
            } catch (IOException e) {
                LOGGER.warn("There was a problem with retrieving json file with schedule.");
            } finally {
                IOUtils.closeQuietly(in);
            }
            String str = new String(byteArr);
            schedules.add(new ScheduleFactory().build(new TrackedSchedulesJsonReaderImpl().getSchedule(str), Locale.ENGLISH));
        }

        return schedules;
    }

    public static void fakeNow(final DateTime dateTime) {
        DateTimeSourceUtil.setSourceInstance(new DateTimeSource() {
            @Override
            public DateTimeZone timeZone() {
                return dateTime.getZone();
            }

            @Override
            public DateTime now() {
                return dateTime;
            }

            @Override
            public LocalDate today() {
                return dateTime.toLocalDate();
            }
        });
    }

    public static void stopFakingTime() {
        DateTimeSourceUtil.setSourceInstance(DATE_TIME_SOURCE);
    }
}
