package org.motechproject.cmslite.model;

import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;
import java.util.Map;
import java.util.Objects;

/**
 * Represents text content. This class is more suited for storing text content than
 * {@link org.motechproject.cmslite.model.StreamContent}.
 */
@Entity(recordHistory = true)
@Unique(name = "stringLanguageAndName", members = {"language", "name" })
@Access(value = SecurityMode.PERMISSIONS, members = {"manageCMS"})
public class StringContent implements Content {

    /**
     * The content text.
     */
    @Field(required = true, type = "text")
    private String value;

    /**
     * The language for the content.
     */
    @Field(required = true)
    private String language;

    /**
     * The name identifying the content.
     */
    @Field(required = true)
    private String name;

    /**
     * The additional metadata for the content.
     */
    @Field
    private Map<String, String> metadata;

    public StringContent() {
        this(null, null, null);
    }

    /**
     * @param language the language for the content
     * @param name the name identifying the content
     * @param value the content text
     */
    public StringContent(String language, String name, String value) {
        this(language, name, value, null);
    }

    /**
     * @param language the language for the content
     * @param name the name identifying the content
     * @param value the content text
     * @param metadata the additional metadata for the content
     */
    public StringContent(String language, String name, String value, Map<String, String> metadata) {
        this.name = name;
        this.language = language;
        this.metadata = metadata;
        this.value = value;
    }

    /**
     * @return the content text
     */
    public String getValue() {
        return value;
    }

    /**
     * @param value the content text
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * @return the language for the content
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language for the content
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the additional metadata for the content
     */
    public Map<String, String> getMetadata() {
        return metadata;
    }

    /**
     * @param metadata the additional metadata for the content
     */
    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    /**
     * @return the name identifying the content
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name identifying the content
     */
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
