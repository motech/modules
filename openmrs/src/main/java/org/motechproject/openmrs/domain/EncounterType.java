package org.motechproject.openmrs.domain;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Class representing a type of the encounter.
 */
public class EncounterType {

    private String uuid;
    private String name;
    private String description;

    public EncounterType(String name, String uuid) {
        this.name = name;
        this.uuid = uuid;
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the
     * {@link EncounterType} class. It represents the encounter
     * as its uuid.
     */
    public static class EncounterTypeSerializer implements JsonSerializer<EncounterType> {

        @Override
        public JsonElement serialize(EncounterType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
    }

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, name, description);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof EncounterType)) {
            return false;
        }

        EncounterType other = (EncounterType) o;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.name, other.name) &&
                Objects.equals(this.description, other.description);
    }
}
