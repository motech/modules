package org.motechproject.commcare.domain.report.constants;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;

import java.lang.reflect.Type;

public enum FilterType {
    NUMERIC("numeric"),
    DATE("date"),
    CHOICE_LIST("choice_list"),
    DYNAMIC_CHOICE_LIST("dynamic_choice_list"),
    PRE("pre");

    private final String type;

    FilterType(String type) {
        this.type = type;
    }

    public static class FilterTypeDeserializer implements JsonDeserializer<FilterType> {

        @Override
        public FilterType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
            String value = json.getAsString();
            return FilterType.valueOf(value.toUpperCase());
        }
    }
}
