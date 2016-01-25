package org.motechproject.odk.event.builder;

import org.codehaus.jackson.map.ObjectMapper;

import java.util.Arrays;
import java.util.List;

/**
 * Utility class for {@link org.motechproject.odk.event.builder.EventBuilder}
 */
public final class EventBuilderUtils {

    private static final int DATE_TIME_STRING_LENGTH = 29;
    private static final String GMT_OFFSET = ".000-00:00";

    private EventBuilderUtils() {
    }

    public static List<String> formatStringList(Object value) {
        if (value == null) {
            return null;
        }
        String s = (String) value;
        String[] strings = s.split("\\s+");
        return Arrays.asList(strings);
    }



    public static String formatStringArray(List<String> value) {
        if (value.size() == 0 || value.get(0) == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(value.get(0));
        for (int i = 1; i < value.size(); i++) {
            builder.append("\n");
            builder.append(value.get(i));
        }
        return builder.toString();
    }

    public static String formatDoubleArray(List<Double> value) {
        if (value.size() == 0 || value.get(0) == null) {
            return null;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(value.get(0).toString());
        for (int i = 1; i < value.size(); i++) {
            builder.append("\n");
            builder.append(value.get(i));
        }
        return builder.toString();
    }

    public static String formatAsJson(Object value) {
        if (value == null) {
            return null;
        }
        try {
            return new ObjectMapper().writeValueAsString(value);
        } catch (Exception e) {
            return null;
        }
    }


    public static String formatDateTime(String value) {
        if (value == null) {
            return null;
        }

        if (value.length() != DATE_TIME_STRING_LENGTH) {
            return addGMToffset(value);
        } else {
            return value;
        }
    }

    private static String addGMToffset(String value) {
        return value + GMT_OFFSET;
    }



}
