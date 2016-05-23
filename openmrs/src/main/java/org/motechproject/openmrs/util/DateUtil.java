package org.motechproject.openmrs.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Utility class for parsing {@link Date} to {@link String} and vice versa.
 */
public final class DateUtil {

    /**
     * Utility class, should not be instantiated.
     */
    private DateUtil() {
    }

    /**
     * Parses the given {@link Date} to the {@link String} representation of that date in the format used by the OpenMRS.
     *
     * @param date  the date to be parsed
     * @return  the string representation of the given date
     */
    public static String formatToOpenMrsDate(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return dateFormat.format(date.getTime());
    }

    /**
     * Parses the given string in date format used by OpenMRS to an instance of the {@link Date} class.
     *
     * @param date  the string representation of the date
     * @return the date parsed from the string
     * @throws ParseException if there were problems while parsing the given string
     */
    public static Date parseOpenMrsDate(String date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return dateFormat.parse(date);
    }

}
