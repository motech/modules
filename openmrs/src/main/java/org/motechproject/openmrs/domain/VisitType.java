package org.motechproject.openmrs.domain;


import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Objects;

/**
 * Class representing a type of the visit.
 */
public class VisitType {

    private String uuid;
    private String name;
    private String description;

    /**
     * Default constructor.
     */
    public VisitType() {
    }

    public VisitType(String uuid) {
        this.uuid = uuid;
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the {@link VisitType} class. It represents the visit type
     * as its ID.
     */
    public static class VisitTypeSerializer implements JsonSerializer<VisitType> {

        @Override
        public JsonElement serialize(VisitType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
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

        if (!(o instanceof VisitType)) {
            return false;
        }

        VisitType other = (VisitType) o;

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.name, other.name)
                && Objects.equals(this.description, other.description);
    }
}
