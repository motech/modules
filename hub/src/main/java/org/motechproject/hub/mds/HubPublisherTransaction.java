package org.motechproject.hub.mds;

import org.joda.time.DateTime;
import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Stores the details of the transaction in which the publisher notifies the hub
 * about an update in a topic
 */
@Entity
public class HubPublisherTransaction implements java.io.Serializable {

    private static final long serialVersionUID = -8125937765866045050L;

    @Field(required = true)
    private Integer hubTopicId;

    @Field(required = true)
    private Integer contentId;

    @Field
    private DateTime notificationTime;

    /**
     * Gets the topic id.
     *
     * @return the topic id
     */
    public Integer getHubTopicId() {
        return hubTopicId;
    }

    /**
     * Sets the topic id.
     *
     * @param hubTopicId the topic id to be set
     */
    public void setHubTopicId(Integer hubTopicId) {
        this.hubTopicId = hubTopicId;
    }

    /**
     * Gets the content id.
     *
     * @return the content id
     */
    public Integer getContentId() {
        return contentId;
    }

    /**
     * Sets the content id.
     *
     * @param contentId content id to be set
     */
    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    /**
     * Gets date and time of notification about update.
     *
     * @return date/time of topic update
     */
    public DateTime getNotificationTime() {
        return notificationTime;
    }

    /**
     * Sets date and time of notification about update.
     *
     * @param notificationTime date/time to be set
     */
    public void setNotificationTime(DateTime notificationTime) {
        this.notificationTime = notificationTime;
    }

    /**
     * Creates a new instance of <code>HubPublisherTransaction</code>, with
     * all fields set to null.
     */
    public HubPublisherTransaction() {
    }

    /**
     * Creates a new instance of <code>HubPublisherTransaction</code>, with
     * all fields set to the values specified in the parameters.
     *
     * @param hubTopicId the id of topic
     * @param contentId the id of content
     * @param notificationTime date/time of the notification
     */
    public HubPublisherTransaction(Integer hubTopicId, Integer contentId,
            DateTime notificationTime) {
        this.hubTopicId = hubTopicId;
        this.contentId = contentId;
        this.notificationTime = notificationTime;
    }
}
