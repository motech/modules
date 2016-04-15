package org.motechproject.openmrs19.domain;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Represents a single attribute of a user.
 */
public class Attribute {

    private String uuid;
    private String display;
    private String value;
    private String name;
    private String description;
    private String format;
    private AttributeType attributeType;
    private List<Link> links;

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
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }

            if (!(o instanceof Link)) {
                return false;
            }

            Link other = (Link) o;

            return ObjectUtils.equals(uri, other.uri);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(uri).toHashCode();
        }
    }

    /**
     * Represents a single attribute type stored as ID.
     */
    public static class AttributeType {

        private String uuid;

        public String getUuid() {
            return uuid;
        }

        public void setUuid(String uuid) {
            this.uuid = uuid;
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

            return ObjectUtils.equals(uuid, other.uuid);
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(uuid).toHashCode();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof Attribute)) {
            return false;
        }

        Attribute other = (Attribute) o;

        return ObjectUtils.equals(uuid, other.uuid) && ObjectUtils.equals(display, other.display)
                && ObjectUtils.equals(value, other.value) && ObjectUtils.equals(name, other.name)
                && ObjectUtils.equals(description, other.description) && ObjectUtils.equals(format, other.format)
                && ObjectUtils.equals(attributeType, other.attributeType) && ObjectUtils.equals(links, other.links);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(uuid).append(display).append(value).append(name).append(description)
                .append(format).append(attributeType).append(links).toHashCode();
    }
}
