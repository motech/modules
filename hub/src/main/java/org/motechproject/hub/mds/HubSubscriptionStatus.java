package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Stores the status of a subscription
 */
@Entity
public class HubSubscriptionStatus implements java.io.Serializable {

    private static final long serialVersionUID = 8995781005450591068L;

    @Field(required = true)
    private Integer subscriptionStatusId;
    @Field(required = true)
    private String subscriptionStatusCode;

    public HubSubscriptionStatus() {
    }

    public HubSubscriptionStatus(Integer subscriptionStatusId,
            String subscriptionStatusCode) {
        this.subscriptionStatusId = subscriptionStatusId;
        this.subscriptionStatusCode = subscriptionStatusCode;
    }

    public Integer getSubscriptionStatusId() {
        return subscriptionStatusId;
    }

    public void setSubscriptionStatusId(Integer subscriptionStatusId) {
        this.subscriptionStatusId = subscriptionStatusId;
    }

    public String getSubscriptionStatusCode() {
        return subscriptionStatusCode;
    }

    public void setSubscriptionStatusCode(String subscriptionStatusCode) {
        this.subscriptionStatusCode = subscriptionStatusCode;
    }

}
