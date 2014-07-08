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

    public Integer getHubTopicId() {
        return hubTopicId;
    }

    public void setHubTopicId(Integer hubTopicId) {
        this.hubTopicId = hubTopicId;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public DateTime getNotificationTime() {
        return notificationTime;
    }

    public void setNotificationTime(DateTime notificationTime) {
        this.notificationTime = notificationTime;
    }

    public HubPublisherTransaction() {
    }

    public HubPublisherTransaction(Integer hubTopicId, Integer contentId,
            DateTime notificationTime) {
        this.hubTopicId = hubTopicId;
        this.contentId = contentId;
        this.notificationTime = notificationTime;
    }
}
