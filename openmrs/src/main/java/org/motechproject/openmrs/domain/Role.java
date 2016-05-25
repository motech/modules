package org.motechproject.openmrs.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Represents a single role. A role is a group of privileges in the OpenMRS system.
 */
public class Role {

    private String uuid;
    private String display;
    private String name;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Role other = (Role) obj;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display) &&
                Objects.equals(this.name, other.name);
    }
}
