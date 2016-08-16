package org.motechproject.openmrs.domain;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.motechproject.openmrs.util.JsonUtils;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single attribute of a user.
 */
public class Attribute {

    private static final String VALUE_KEY = "value";

    private String uuid;
    private String display;
    private String value;
    private String name;
    private String description;
    private String format;
    private AttributeType attributeType;
    private List<Link> links;
    private String hydratedObject;

    /**
     * Represents a single link.
     */
    public static class Link {

        private String uri;

        public String getUri() {
            return uri;
        }

        public void setUri(String uri) {
            this.uri = uri;
        }

        @Override
        public int hashCode() {
            return Objects.hash(uri);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Link)) {
                return false;
            }

            Link other = (Link) o;

            return Objects.equals(uri, other.uri);
        }
    }

    /**
     * Represents a single attribute type stored as ID.
     */
    public static class AttributeType {

        private String display;

        private String uuid;

        private String format;

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

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }

        @Override
        public int hashCode() {
            return Objects.hash(uuid);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof AttributeType)) {
                return false;
            }

            AttributeType other = (AttributeType) o;

            return Objects.equals(uuid, other.uuid);
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} interface for the
     * {@link AttributeType} class.
     */
    public static class AttributeTypeSerializer implements JsonSerializer<AttributeType> {

        @Override
        public JsonElement serialize(AttributeType src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(src.getUuid());
        }
    }

    /**
     * Implementation of the {@link JsonSerializer} and {@link JsonDeserializer} interfaces for the
     * {@link Attribute} class.
     */
    public static class AttributeSerializer implements JsonSerializer<Attribute>, JsonDeserializer<Attribute> {

        @Override
        public JsonElement serialize(Attribute src, Type typeOfSrc, JsonSerializationContext context) {
            JsonObject attribute = new JsonObject();

            if (src.getAttributeType() != null && src.getAttributeType().getUuid() != null) {
                attribute.addProperty("attributeType", src.getAttributeType().getUuid());
            }

            if (src.getValue() != null) {
                attribute.addProperty(VALUE_KEY, src.getValue());
            } else if (src.getHydratedObject() != null) {
                attribute.addProperty("hydratedObject", src.getHydratedObject());
            }

            return attribute;
        }

        @Override
        public Attribute deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject attribute = json.getAsJsonObject();

            JsonElement value = attribute.get(VALUE_KEY);
            if (value != null && value.isJsonObject()) {
                JsonObject valueObject = value.getAsJsonObject();
                String attributeValue = valueObject.get("uuid").toString().replaceAll("\"", "");
                attribute.addProperty(VALUE_KEY, attributeValue);
            }

            return (Attribute) JsonUtils.readJson(attribute.toString(), Attribute.class);
        }
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public AttributeType getAttributeType() {
        return attributeType;
    }

    public void setAttributeType(AttributeType attributeType) {
        this.attributeType = attributeType;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getHydratedObject() {
        return hydratedObject;
    }

    public void setHydratedObject(String hydratedObject) {
        this.hydratedObject = hydratedObject;
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, display, value, name, description, format, attributeType, links, hydratedObject);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Attribute)) {
            return false;
        }

        Attribute other = (Attribute) o;

        return Objects.equals(uuid, other.uuid) && Objects.equals(display, other.display)
                && Objects.equals(value, other.value) && Objects.equals(name, other.name)
                && Objects.equals(description, other.description) && Objects.equals(format, other.format)
                && Objects.equals(attributeType, other.attributeType) && Objects.equals(links, other.links);
    }
}
