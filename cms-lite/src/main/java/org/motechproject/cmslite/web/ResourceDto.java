package org.motechproject.cmslite.web;

import org.motechproject.cmslite.model.Content;
import org.motechproject.cmslite.model.StreamContent;
import org.motechproject.cmslite.model.StringContent;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static java.util.Collections.addAll;

/**
 * Represent a content from CMS-Lite in the jqgrid. Does not carry any actual content, only
 * information about the name, type and languages of the content.
 */
public class ResourceDto implements Serializable {
    private static final long serialVersionUID = 6728665456455509425L;

    private final Set<String> languages = new TreeSet<>();
    private final String name;
    private final String type;

    /**
     * Constructs an instance for the given content.
     * @param content the content this instance should represent
     */
    public ResourceDto(Content content) {
        this.name = content.getName();
        this.languages.add(content.getLanguage());

        if (content instanceof StringContent) {
            type = "string";
        } else if (content instanceof StreamContent) {
            type = "stream";
        } else {
            type = null;
        }
    }

    /**
     * @param name the name of the content
     * @param type the type of the content, either <b>string</b> or <b>stream</b>
     * @param languages the languages supported by this content
     */
    public ResourceDto(String name, String type, String... languages) {
        this.name = name;
        this.type = type;
        addAll(this.languages, languages);
    }

    /**
     * Adds a language to the set of languages for this content.
     * @param language the language to add
     */
    public void addLanguage(String language) {
        languages.add(language);
    }

    /**
     * @return the languages supported by this content
     */
    public Set<String> getLanguages() {
        return languages;
    }

    /**
     * @return the name of the content
     */
    public String getName() {
        return name;
    }

    /**
     * @return the type of the content, either <b>string</b> or <b>stream</b>
     */
    public String getType() {
        return type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(languages, name, type);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final ResourceDto other = (ResourceDto) obj;

        return Objects.equals(this.languages, other.languages) &&
                Objects.equals(this.name, other.name) &&
                Objects.equals(this.type, other.type);
    }

    @Override
    public String toString() {
        return String.format("ResourceDto{languages=%s, name='%s', type='%s'}", languages, name, type);
    }
}
