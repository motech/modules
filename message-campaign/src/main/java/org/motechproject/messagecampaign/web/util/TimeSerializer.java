package org.motechproject.messagecampaign.web.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.motechproject.commons.date.model.Time;

import java.io.IOException;

/**
 * Util used for JSON serialization of {@link Time}.
 */
public class TimeSerializer extends JsonSerializer<Time> {

    /**
     * Serializes {@link Time} to JSON format representation.
     *
     * @param time value to serialize; not null
     * @param jsonGenerator generator used to output resulting JSON content
     * @param serializerProvider provider that can be used to get serializers
     * @throws IOException in case of input/output problems during serialization
     */
    @Override
    public void serialize(Time time, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(String.format("%02d:%02d:00", time.getHour(), time.getMinute()));
    }
}
