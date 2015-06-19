package org.motechproject.openmrs19.resource.model;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

/**
 * Represents a single role. A role is a group of privileges in the OpenMRS system. It's a part of the OpenMRS model.
 */
public class Role {

    private String uuid;
    private String display;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String name) {
        this.display = name;
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link Role} class. It represents the role as its
     * ID.
     */
    public static class RoleSerializer implements JsonSerializer<Role> {
        @Override
        public JsonElement serialize(Role src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
    }
}
