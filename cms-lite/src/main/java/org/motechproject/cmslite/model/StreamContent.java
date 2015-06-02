package org.motechproject.cmslite.model;

import org.apache.commons.lang.ArrayUtils;
import org.motechproject.mds.annotations.Access;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;
import org.motechproject.mds.annotations.UIDisplayable;
import org.motechproject.mds.util.SecurityMode;

import javax.jdo.annotations.Unique;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a stream content along with checksum. Any file can be stored as a stream content in CMS-Lite.
 */
@Entity(recordHistory = true)
@Unique(name = "streamLanguageAndName", members = {"language", "name" })
@Access(value = SecurityMode.PERMISSIONS, members = {"manageCMS"})
public class StreamContent implements Content {

    /**
     * The actual content - this is the stored file/stream.
     */
    @Field(required =  true)
    private Byte[] content;

    /**
     * The MD5 checksum of the content.
     */
    @Field
    @UIDisplayable
    private String checksum;

    /**
     * The mime type of the content.
     */
    @Field
    @UIDisplayable
    private String contentType;

    /**
     * The language of the content.
     */
    @Field(required = true)
    @UIDisplayable
    private String language;

    /**
     * The name used for identifying the content.
     */
    @Field(required = true)
    @UIDisplayable
    private String name;

    /**
     * The additional metadata for the content.
     */
    @Field
    private Map<String, String> metadata;

    public StreamContent() {
        this(null, null, null, null, null);
    }

    /**
     * @param language the language of the content
     * @param name the name identifying this content
     * @param content the actual can as an array of bytes
     * @param checksum the MD5 checksum for the content
     * @param contentType the mime type of the content
     */
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

    /**
     * @param content the actual can as an array of bytes
     */
    public void setContent(Byte[] content) {
        this.content = content.clone();
    }

    /**
     * @return the actual can as an array of bytes
     */
    public Byte[] getContent() {
        return content.clone();
    }

    /**
     * @return the MD5 checksum for the content
     */
    public String getChecksum() {
        return checksum;
    }

    /**
     * @param checksum the MD5 checksum for the content
     */
    public void setChecksum(String checksum) {
        this.checksum = checksum;
    }

    /**
     * @return the mime type of the content
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType the mime type of the content
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return the language of the content
     */
    public String getLanguage() {
        return language;
    }

    /**
     * @param language the language of the content
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * @return the name identifying this content
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name identifying this content
     */
    public void setName(String name) {
        this.name = name;
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
