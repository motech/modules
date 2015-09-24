package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Stores the status of a subscription. Valid statuses are defined
 * in {@link org.motechproject.hub.model.SubscriptionStatusLookup}.
 */
@Entity
public class HubSubscriptionStatus implements java.io.Serializable {

    private static final long serialVersionUID = 8995781005450591068L;

    @Field(required = true)
    private Integer subscriptionStatusId;
    @Field(required = true)
    private String subscriptionStatusCode;

    /**
     * Creates a new instance of <code>HubSubscriptionStatus</code>, with
     * all fields set to null.
     */
    public HubSubscriptionStatus() {
    }

    /**
     * Creates a new instance of <code>HubSubscriptionStatus</code>, with
     * all fields set to the values specified in the parameters.
     *
     * @param subscriptionStatusId the id of subscription status
     * @param subscriptionStatusCode the code of subscription status
     */
    public HubSubscriptionStatus(Integer subscriptionStatusId,
            String subscriptionStatusCode) {
        this.subscriptionStatusId = subscriptionStatusId;
        this.subscriptionStatusCode = subscriptionStatusCode;
    }

    /**
     * Gets the id of subscription status.
     *
     * @return the subscription status id
     */
    public Integer getSubscriptionStatusId() {
        return subscriptionStatusId;
    }

    /**
     * Sets the id of subscription status.
     *
     * @param subscriptionStatusId the subscription status id to be set
     */
    public void setSubscriptionStatusId(Integer subscriptionStatusId) {
        this.subscriptionStatusId = subscriptionStatusId;
    }

    /**
     * Gets the code of subscription status.
     *
     * @return the subscription status code
     */
    public String getSubscriptionStatusCode() {
        return subscriptionStatusCode;
    }

    /**
     * Sets the code of subscription status.
     *
     * @param subscriptionStatusCode the subscription status code to be set
     */
    public void setSubscriptionStatusCode(String subscriptionStatusCode) {
        this.subscriptionStatusCode = subscriptionStatusCode;
    }

}
