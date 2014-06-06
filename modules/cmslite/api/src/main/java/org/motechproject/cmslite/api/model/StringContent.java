package org.motechproject.cmslite.api.model;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

import java.util.Map;
import java.util.Objects;

/**
 * \ingroup cmslite
 * Represents Text Content.
 */
@Entity
public class StringContent implements Content {

    @Field(required = true)
    private String value;

    @Field(required = true)
    private String language;

    @Field(required = true)
    private String name;

    @Field
    private Map<String, String> metadata;

    public StringContent() {
        this(null, null, null);
    }

    public StringContent(String language, String name, String value) {
        this(language, name, value, null);
    }

    public StringContent(String language, String name, String value, Map<String, String> metadata) {
        this.name = name;
        this.language = language;
        this.metadata = metadata;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final StringContent other = (StringContent) obj;

        return  Objects.equals(this.language, other.language) &&
                Objects.equals(this.name, other.name) &&
                Objects.equals(this.metadata, other.metadata) &&
                Objects.equals(this.value, other.value);
    }

    @Override
    public String toString() {
        return String.format("StringContent{value='%s'} Content{language='%s', name='%s', metadata=%s}", value, language, name, metadata);
    }
}
