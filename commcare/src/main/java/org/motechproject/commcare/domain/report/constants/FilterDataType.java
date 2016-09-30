package org.motechproject.commcare.domain.report.constants;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public enum FilterDataType {
    STRING("string"),
    INTEGER("integer"),
    DECIMAL("decimal");

    private final String datatype;

    FilterDataType(String datatype) {
        this.datatype = datatype;
    }

    public static class FilterDataTypeDeserializer implements JsonDeserializer<FilterDataType> {

        @Override
        public FilterDataType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String value = json.getAsString();
            return FilterDataType.valueOf(value.toUpperCase());
        }
    }
}