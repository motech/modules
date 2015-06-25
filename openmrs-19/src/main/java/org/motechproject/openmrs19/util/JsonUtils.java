package org.motechproject.openmrs19.util;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.log4j.Logger;
import org.motechproject.commons.api.json.MotechJsonReader;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing the JSON representation of a class into object of that class.
 */
public final class JsonUtils {

    private static final Logger LOGGER = Logger.getLogger(JsonUtils.class);
    private static final MotechJsonReader READER = new MotechJsonReader();
    private static Map<Type, Object> providedAdapters = new HashMap<Type, Object>();

    static {
        providedAdapters.put(Date.class, new OpenMrsDateAdapter());
    }

    /**
     * Utility class, should not be instantiated.
     */
    private JsonUtils() {
    }

    /**
     * Creates an object of type {@code type} from the given {@code String}.
     *
     * @param json  the {@code String} to deserialize
     * @param type  the type of the created object
     * @return object of type {@code type}
     */
    public static Object readJson(String json, Type type) {
        return READER.readFromString(json, type, providedAdapters);
    }

    /**
     * Creates object of type {@code type} from given {@code String} using user-specified adapters.
     *
     * @param json  the {@code String} to deserialize
     * @param type  the type of the created object
     * @param adapters  custom adapters to use for deserialization
     * @return object of type {@code type}
     */
    public static Object readJsonWithAdapters(String json, Type type, Map<Type, Object> adapters) {
        adapters.putAll(providedAdapters);
        return READER.readFromString(json, type, adapters);
    }

    /**
     * Custom adapter for {@link Date} class. It is used to serialize/deserialize object from/to a {@link JsonElement}.
     */
    private static class OpenMrsDateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            Date date = null;
            try {
                date = DateUtil.parseOpenMrsDate(json.getAsString());
            } catch (ParseException e) {
                LOGGER.error("Failed to parse date: " + e.getMessage());
            }
            return date;
        }

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(DateUtil.formatToOpenMrsDate(src));
        }
    }
}
