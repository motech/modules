package org.motechproject.commcare.domain.report.constants;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public enum ColumnType {
    FIELD("field"),
    EXPANDED("expanded");

    private final String type;

    ColumnType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static ColumnType getColumnTypeFromTypeValue(String typeValue) {
        ColumnType[] columnTypes = ColumnType.values();
        for (ColumnType columnType : columnTypes) {
            if (columnType.getType().equals(typeValue)) {
                return columnType;
            }
        }
        throw new IllegalArgumentException("Invalid column type value: " + typeValue);
    }

    public static class ColumnTypeDeserializer implements JsonDeserializer<ColumnType> {

        @Override
        public ColumnType deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            String value = json.getAsString();
            return ColumnType.getColumnTypeFromTypeValue(value);
        }
    }
}
