package org.motechproject.commcare.parser;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.CommcareFormList;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.domain.MetadataValue;
import org.motechproject.commons.api.json.MotechJsonReader;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Class responsible for deserializing forms from JSON.
 */
public final class FormAdapter {

    private static final MotechJsonReader READER = new MotechJsonReader();
    private static Map<Type, Object> providedAdapters = new HashMap<>();

    static {
        providedAdapters.put(FormValueElement.class, new JsonFormAdapter());
        providedAdapters.put(MetadataValue.class, new MetadataValueAdapter());
    }

    /**
     * Deserializes the given JSON string into an instance of the {@link CommcareForm} class.
     *
     * @param json  the JSON representation of the form
     * @return the deserialized form
     */
    public static CommcareForm readJson(String json) {
        Type type = new TypeToken<CommcareForm>() { } .getType();
        return (CommcareForm) READER.readFromString(json, type, providedAdapters);
    }

    /**
     * Reads a list o forms with metadata from the json file. The result is represented
     * as a {@link CommcareFormList} object.
     * @param json the json to read
     * @return the parsed value
     */
    public static CommcareFormList readListJson(String json) {
        Type type = new TypeToken<CommcareFormList>() {}.getType();
        return (CommcareFormList) READER.readFromString(json, type, providedAdapters);
    }

    /**
     * {@link JsonDeserializer} for the {@link FormValueElement} class.
     */
    private static class JsonFormAdapter implements JsonDeserializer<FormValueElement> {

        @Override
        public FormValueElement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            return recursivelyParse("form", json);
        }
    }

    /**
     * Parses the given {@code jsonElement} into an instance of the {@link FormValueElement} class. The created object
     * will have its name set to the {@code key}.
     *
     * @param key  the name of the element to set
     * @param jsonElement  the JSON element
     * @return the parsed object
     */
    public static FormValueElement recursivelyParse(String key, JsonElement jsonElement) {
        FormValueElement formElement = new FormValueElement();
        formElement.setElementName(key);

        if (jsonElement.isJsonPrimitive()) {
            JsonPrimitive primitive = jsonElement.getAsJsonPrimitive();

            FormValueElement subFormElement = new FormValueElement();
            subFormElement.setElementName(key);
            subFormElement.setValue(primitive.getAsString());
            return subFormElement;

        } else if (jsonElement.isJsonArray()) {
            JsonArray array = jsonElement.getAsJsonArray();
            for (int i = 0; i < array.size(); i++) {
                FormValueElement element = recursivelyParse(key, array.get(i));
                formElement.addFormValueElement(key, element);
            }
            return formElement;
        } else if (jsonElement.isJsonObject()) {
            JsonObject object = jsonElement.getAsJsonObject();
            for (Entry<String, JsonElement> entry : object.entrySet()) {
                if (entry.getValue().isJsonPrimitive()) {
                    JsonPrimitive value = entry.getValue().getAsJsonPrimitive();
                    if (isAttribute(entry.getKey())) {
                        formElement.addAttribute(entry.getKey().substring(1), value.getAsString());
                    } else if (isValue(entry.getKey())) {
                        formElement.setValue(value.getAsString());
                    } else {
                        FormValueElement element = recursivelyParse(entry.getKey(), entry.getValue());
                        formElement.addFormValueElement(element.getElementName(), element);
                    }
                } else {
                    FormValueElement element = recursivelyParse(entry.getKey(), entry.getValue());
                    formElement.addFormValueElement(element.getElementName(), element);
                }

            }
        }
        return formElement;
    }

    /**
     * Checks whether the given {@code key} is an attribute.
     *
     * @param key  the key to be checked
     * @return true if the key is an attribute, false otherwise
     */
    public static boolean isAttribute(String key) {
        return key.startsWith("@");
    }

    /**
     * Checks whether the given {@code key} is a value.
     *
     * @param key  the key to be checked
     * @return true if the key is a value, false otherwise
     */
    public static boolean isValue(String key) {
        return key.startsWith("#");
    }

    /**
     * Utility class, should not be initiated.
     */
    private FormAdapter() { }
}
