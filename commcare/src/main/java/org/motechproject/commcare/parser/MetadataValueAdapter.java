package org.motechproject.commcare.parser;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import org.motechproject.commcare.domain.MetadataValue;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Deserializes {@link MetadataValue} coming from JSON. This metadata values can be both
 * arrays or single strings, hence the parsing here.
 */
public class MetadataValueAdapter implements JsonDeserializer<MetadataValue> {

    @Override
    public MetadataValue deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        if (json.isJsonPrimitive()) {
            return new MetadataValue(json.getAsString());
        } else if (json.isJsonArray()) {
            List<String> values = new ArrayList<>();
            for (JsonElement element : json.getAsJsonArray()) {
                values.add(element.getAsString());
            }
            return new MetadataValue(values);
        } else {
            throw new JsonParseException("Metadata must be either a string or an array of string, instead got " +
                    json);
        }
    }
}
