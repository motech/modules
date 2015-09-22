package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Stores the content and content type received from the publisher when there is
 * an update in a topic.
 */
@Entity
public class HubDistributionContent implements java.io.Serializable {

    private static final long serialVersionUID = -5048963496204264339L;

    @Field(required = true)
    private String content;

    @Field(required = true)
    private String contentType;

    /**
     * Creates a new instance of <code>HubDistributionContent</code>, with
     * all fields set to null.
     */
    public HubDistributionContent() {
        this(null, null);
    }

    /**
     * Creates a new instance of <code>HubDistributionContent</code>, with
     * all fields set to the values specified in the parameters.
     *
     * @param content the content which will be stored
     * @param contentType the type of stored content
     */
    public HubDistributionContent(String content, String contentType) {
        this.content = content;
        this.contentType = contentType;
    }

    /**
     * Gets the type of stored content.
     *
     * @return the type of stored content
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * Sets the type of stored content to the value passed as a parameter.
     *
     * @param contentType the type to be set
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * Gets the stored content.
     *
     * @return the stored content
     */
    public String getContent() {
        return this.content;
    }

    /**
     * Sets the content to store.
     *
     * @param content the content to be set
     */
    public void setContent(String content) {
        this.content = content;
    }

}
