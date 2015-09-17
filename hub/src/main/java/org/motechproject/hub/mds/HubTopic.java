package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Stores the topic URLs. Topic URL is the URL with the content that can be subscribed.
 */
@Entity
public class HubTopic implements java.io.Serializable {

    private static final long serialVersionUID = -5048963496204264339L;

    @Field(required = true)
    private String topicUrl;

    /**
     * Creates a new <code>HubTopic</code> instance with topic URL set to null.
     */
    public HubTopic() {
        this(null);
    }

    /**
     * Creates a new <code>HubTopic</code> instance with topic URL set to the value
     * passed as a parameter.
     *
     * @param topicUrl the topic URL to be set
     */
    public HubTopic(String topicUrl) {
        this.topicUrl = topicUrl;
    }

    /**
     * Gets the URL of topic.
     *
     * @return the topic URL
     */
    public String getTopicUrl() {
        return this.topicUrl;
    }

    /**
     * Sets the URL of topic.
     *
     * @param topicUrl the topic URL to be set
     */
    public void setTopicUrl(String topicUrl) {
        this.topicUrl = topicUrl;
    }

}
