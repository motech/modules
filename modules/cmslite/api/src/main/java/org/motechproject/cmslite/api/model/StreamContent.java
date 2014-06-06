package org.motechproject.cmslite.api.model;

import org.apache.commons.lang.ArrayUtils;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;

import java.util.Map;
import java.util.Objects;

/**
 * \ingroup cmslite
 * Represents stream content along with checksum.
 */
@Entity
public class StreamContent implements Content {

    @Field(required =  true)
    private Byte[] content;

    @Field
    @UIDisplayable
    private String checksum;

    @Field
    @UIDisplayable
    private String contentType;

    @Field(required = true)
    @UIDisplayable
    private String language;

    @Field(required = true)
    @UIDisplayable
    private String name;

    @Field
    private Map<String, String> metadata;

    public StreamContent() {
        this(null, null, null, null, null);
    }

    public StreamContent(String language, String name, Byte[] content, String checksum, String contentType) {
        this.name = name;
        this.language = language;

        if (content != null) {
            this.content = content.clone();
        } else {
            this.content = ArrayUtils.EMPTY_BYTE_OBJECT_ARRAY;
        }
        this.checksum = checksum;
        this.contentType = contentType;
    }

    public void setContent(Byte[] content) {
        this.content = content.clone();
    }

    public Byte[] getContent() {
        return content.clone();
    }

    public String getChecksum() {
        return checksum;
    }

    public String getContentType() {
        return contentType;
    }

    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }

    @Override
    public int hashCode() {
        return Objects.hash(checksum, contentType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final StreamContent other = (StreamContent) obj;

        return Objects.equals(this.language, other.language) &&
                Objects.equals(this.name, other.name) &&
                Objects.equals(this.metadata, other.metadata) &&
                Objects.equals(this.checksum, other.checksum) &&
                Objects.equals(this.contentType, other.contentType);
    }

    @Override
    public String toString() {
        return String.format("StreamContent{checksum='%s', contentType='%s'} Content{language='%s', name='%s', metadata=%s}",
                checksum, contentType, language, name, metadata);
    }
}
