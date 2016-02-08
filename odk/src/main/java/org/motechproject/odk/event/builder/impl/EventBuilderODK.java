package org.motechproject.odk.event.builder.impl;

import org.codehaus.jackson.map.ObjectMapper;
import org.motechproject.odk.constant.FieldTypeConstants;
import org.motechproject.odk.domain.OdkJsonFormPublication;
import org.motechproject.odk.exception.EventBuilderException;
import org.motechproject.odk.event.builder.AbstractEventBuilder;
import org.motechproject.odk.event.builder.EventBuilder;
import org.motechproject.odk.util.EventBuilderUtils;

import java.io.IOException;
import java.util.Map;

public class EventBuilderODK extends AbstractEventBuilder implements EventBuilder {

    private static final String URL = "url";
    private static final int TIME_STRING_SIZE = 5;


    protected Object formatValue(String type, Object value) throws EventBuilderException {

        switch (type) {

            case FieldTypeConstants.BINARY:
                return formatUrl((Map<String, String>) value);

            case FieldTypeConstants.REPEAT_GROUP:
                return EventBuilderUtils.formatAsJson(value);

            case FieldTypeConstants.TIME:
                return formatDate((String) value);

            default:
                return value;
        }
    }

    private String formatDate(String value) {
        return value.substring(0, TIME_STRING_SIZE);
    }


    private String formatUrl(Map<String, String> value) {
        if (value == null) {
            return null;
        }
        return value.get(URL);
    }

    @Override
    protected Map<String, Object> getData(String json) throws EventBuilderException {
        try {
            OdkJsonFormPublication publication = new ObjectMapper().readValue(json, OdkJsonFormPublication.class);
            return publication.getData()[0];
        } catch (IOException e) {
            throw new EventBuilderException(e);
        }

    }
}

