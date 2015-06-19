package org.motechproject.openmrs19.resource.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Represents a single identifier type. It's a part of the OpenMRS model.
 */
public class IdentifierType {

    private String uuid;
    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link IdentifierType} class. It represents the
     * identifier type as its ID.
     */
    public static class IdentifierTypeSerializer implements JsonSerializer<IdentifierType> {

        @Override
        public JsonElement serialize(IdentifierType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
    }
}
