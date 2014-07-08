package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Stores the transaction in which the hub distributes the content to its
 * subscribers
 */
@Entity
public class HubSubscriberTransaction implements java.io.Serializable {

    private static final long serialVersionUID = -2823908898058704053L;

    @Field(required = true)
    private Integer hubDistributionStatusId;

    @Field(required = true)
    private Integer hubSubscriptionId;

    @Field
    private Integer contentId;

    public Integer getHubDistributionStatusId() {
        return hubDistributionStatusId;
    }

    public void setHubDistributionStatusId(Integer hubDistributionStatusId) {
        this.hubDistributionStatusId = hubDistributionStatusId;
    }

    public Integer getHubSubscriptionId() {
        return hubSubscriptionId;
    }

    public void setHubSubscriptionId(Integer hubSubscriptionId) {
        this.hubSubscriptionId = hubSubscriptionId;
    }

    public Integer getContentId() {
        return contentId;
    }

    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    public HubSubscriberTransaction() {
    }

    public HubSubscriberTransaction(Integer hubDistributionStatusId,
            Integer hubSubscriptionId, Integer contentId) {
        this.hubDistributionStatusId = hubDistributionStatusId;
        this.hubSubscriptionId = hubSubscriptionId;
        this.contentId = contentId;
    }

}
