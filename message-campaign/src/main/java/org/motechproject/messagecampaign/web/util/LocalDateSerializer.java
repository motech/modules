package org.motechproject.messagecampaign.web.util;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.joda.time.LocalDate;

import java.io.IOException;

/**
 * Util used for JSON serialization of {@link LocalDate}.
 */
public class LocalDateSerializer extends JsonSerializer<LocalDate> {

    /**
     * Serializes {@link LocalDate} to JSON format representation.
     *
     * @param localDate value to serialize; not null
     * @param jsonGenerator generator used to output resulting JSON content
     * @param serializerProvider provider that can be used to get serializers
     * @throws IOException in case of input/output problems during serialization
     */
    @Override
    public void serialize(LocalDate localDate, JsonGenerator jsonGenerator, SerializerProvider serializerProvider)
            throws IOException {
        jsonGenerator.writeString(localDate.toString());
    }
}
