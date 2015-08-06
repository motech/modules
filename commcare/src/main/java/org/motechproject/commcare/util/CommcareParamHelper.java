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
     * Calculates the offset parameter based on the page size and page number.
     * @param pageSize the page size
     * @param pageNumber the page number
     * @return the offset that should be sent to Commcare
     */
    public static int toOffset(Integer pageSize, Integer pageNumber) {
        if (pageNumber == null) {
            return  0;
        } else {
            return (pageNumber - 1) * (pageSize != null && pageSize >= 0 ? pageSize : 0);
        }
    }

    private CommcareParamHelper() {
    }
}
