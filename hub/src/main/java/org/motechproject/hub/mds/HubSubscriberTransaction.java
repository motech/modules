package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Stores the transaction in which the hub distributes the content to its
 * subscribers.
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

    /**
     * Gets distribution status id.
     *
     * @return the id of distribution status
     */
    public Integer getHubDistributionStatusId() {
        return hubDistributionStatusId;
    }

    /**
     * Sets distribution status id.
     *
     * @param hubDistributionStatusId the distribution status id to be set
     */
    public void setHubDistributionStatusId(Integer hubDistributionStatusId) {
        this.hubDistributionStatusId = hubDistributionStatusId;
    }

    /**
     * Gets subscription id.
     *
     * @return the id of subscription
     */
    public Integer getHubSubscriptionId() {
        return hubSubscriptionId;
    }

    /**
     * Sets subscription id.
     *
     * @param hubSubscriptionId the subscription id to be set
     */
    public void setHubSubscriptionId(Integer hubSubscriptionId) {
        this.hubSubscriptionId = hubSubscriptionId;
    }

    /**
     * Gets content id.
     *
     * @return the id of content
     */
    public Integer getContentId() {
        return contentId;
    }

    /**
     * Sets content id.
     *
     * @param contentId the content id to be set
     */
    public void setContentId(Integer contentId) {
        this.contentId = contentId;
    }

    /**
     * Creates a new instance of <code>HubSubscriberTransaction</code>, with
     * all fields set to null.
     */
    public HubSubscriberTransaction() {
    }

    /**
     * Creates a new instance of <code>HubSubscriberTransaction</code>, with
     * all fields set to the values specified in the parameters.
     *
     * @param hubDistributionStatusId the id of distribution status
     * @param hubSubscriptionId the id of subscription
     * @param contentId the id of content
     */
    public HubSubscriberTransaction(Integer hubDistributionStatusId,
            Integer hubSubscriptionId, Integer contentId) {
        this.hubDistributionStatusId = hubDistributionStatusId;
        this.hubSubscriptionId = hubSubscriptionId;
        this.contentId = contentId;
    }

}
