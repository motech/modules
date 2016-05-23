package org.motechproject.openmrs.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Represents a single identifier type.
 */
public class IdentifierType {

    private String uuid;
    private String display;
    private String name;

    public IdentifierType() {
    }

    public IdentifierType(String name) {
        this.name = name;
    }

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
     * Implementation of the {@link JsonSerializer} interface for the {@link IdentifierType} class. It represents the
     * identifier type as its ID.
     */
    public static class IdentifierTypeSerializer implements JsonSerializer<IdentifierType> {

        @Override
        public JsonElement serialize(IdentifierType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof IdentifierType)) {
            return false;
        }

        IdentifierType other = (IdentifierType) o;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display) &&
                Objects.equals(this.name, other.name);
    }
}
