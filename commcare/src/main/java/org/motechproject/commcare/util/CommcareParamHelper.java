package org.motechproject.commcare.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

/**
 * Utility for handling params in Commcare urls.
 */
public final class CommcareParamHelper {

    private static final DateTimeFormatter FMT = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Prints a datetime in the format expected by the Commcare server. The current time zone of the provided
     * date time is used. The format is <code>yyyy-MM-dd'T'HH:mm:ss</code>.
     * @param dt the datetime to print
     * @return the datetime as string that can be used as query param in Commcare
     */
    public static String printDateTime(DateTime dt) {
        return FMT.print(dt);
    }

    /**
     * Prints the passed object as a datetime. Supports {@link String} and {@link DateTime} objects.
     * @param object the object to print
     * @return the object printed as a datetime, or null if the object was null
     * @throws IllegalArgumentException if the object is not a supported class
     */
    public static String printObjectAsDateTime(Object object) {
        if (object instanceof DateTime) {
            return printDateTime((DateTime) object);
        } else if (object instanceof String) {
            return (String) object;
        } else if (object == null) {
            return null;
        } else {
            throw new IllegalArgumentException("Cannot print a datetime from " + object.getClass().getName());
        }
    }

    /**
     * Calculates the offset parameter based on the page size and page number.
     * @param pageSize the page size
     * @param pageNumber the page number
     * @return the offset that should be sent to Commcare
     */
    public static int toOffset(Integer pageSize, Integer pageNumber) {
        if (pageNumber == null || pageNumber == 0) {
            return  0;
        } else {
            return (pageNumber - 1) * (pageSize != null && pageSize >= 0 ? pageSize : 0);
        }
    }

    private CommcareParamHelper() {
    }
}
