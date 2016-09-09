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
    private String display;

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

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display);
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

        return Objects.equals(this.uuid, other.uuid) && Objects.equals(this.display, other.display);
    }
}
