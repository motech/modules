package org.motechproject.hub.mds;

import org.motechproject.mds.annotations.Entity;
import org.motechproject.mds.annotations.Field;

/**
 * Stores the subscription details of a topic.
 */
@Entity
public class HubSubscription implements java.io.Serializable {

    private static final long serialVersionUID = 811889421861700076L;

    @Field(required = true)
    private Integer hubTopicId;

    @Field(required = true)
    private Integer hubSubscriptionStatusId;

    @Field(required = true)
    private String callbackUrl;

    @Field
    private Integer leaseSeconds;

    @Field
    private String secret;

    /**
     * Gets the id of topic.
     *
     * @return the id of topic
     */
    public Integer getHubTopicId() {
        return hubTopicId;
    }

    /**
     * Sets the id of topic.
     *
     * @param hubTopicId the topic id to be set
     */
    public void setHubTopicId(Integer hubTopicId) {
        this.hubTopicId = hubTopicId;
    }

    /**
     * Gets the id of subscription status.
     *
     * @return the id of subscription status
     */
    public Integer getHubSubscriptionStatusId() {
        return hubSubscriptionStatusId;
    }

    /**
     * Sets the id of subscription status.
     *
     * @param hubSubscriptionStatusId the subscription status id to be set
     */
    public void setHubSubscriptionStatusId(Integer hubSubscriptionStatusId) {
        this.hubSubscriptionStatusId = hubSubscriptionStatusId;
    }

    /**
     * Gets the callback url.
     *
     * @return the callback url
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * Sets the callback url.
     *
     * @param callbackUrl the callback url to be set
     */
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    /**
     * Gets lease seconds - number of seconds for which the subscriber
     * will have his subscription active.
     *
     * @return the amount of lease seconds
     */
    public Integer getLeaseSeconds() {
        return leaseSeconds;
    }

    /**
     * Sets lease seconds - number of seconds for which the subscriber
     * will have his subscription active.
     *
     * @param leaseSeconds amount of lease seconds to be set
     */
    public void setLeaseSeconds(Integer leaseSeconds) {
        this.leaseSeconds = leaseSeconds;
    }

    /**
     * Gets the secret value. Secret is a value used for computing an HMAC digest for
     * authorized content distribution.
     *
     * @return the secret value
     */
    public String getSecret() {
        return secret;
    }

    /**
     * Sets the secret value. Secret is a value used for computing an HMAN digest for
     * authorized content distribution. It must be less than 200 bytes in length.
     *
     * @param secret the secret value to be set
     */
    public void setSecret(String secret) {
        this.secret = secret;
    }

    /**
     * Creates a new instance of <code>HubSubscription</code>, with
     * all fields set to null.
     */
    public HubSubscription() {
    }

    /**
     * Creates a new instance of <code>HubSubscription</code>, with
     * all fields set to the values specified in the parameters.
     *
     * @param hubTopicId the id of a topic
     * @param hubSubscriptionStatusId the id of a subscription status
     * @param callbackUrl the callback url
     */
    public HubSubscription(Integer hubTopicId, Integer hubSubscriptionStatusId,
            String callbackUrl) {
        this.hubTopicId = hubTopicId;
        this.hubSubscriptionStatusId = hubSubscriptionStatusId;
        this.callbackUrl = callbackUrl;
    }

    /**
     * Creates a new instance of <code>HubSubscription</code>, with
     * all fields set to the values specified in the parameters.
     *
     * @param hubTopicId the id of a topic
     * @param hubSubscriptionStatusId the id of a subscription status
     * @param callbackUrl the callback url
     * @param leaseSeconds the time in seconds for which the subscription will be active
     * @param secret the secret value for HMAC digest computing
     */
    public HubSubscription(Integer hubTopicId, Integer hubSubscriptionStatusId,
            String callbackUrl, Integer leaseSeconds, String secret) {
        this.hubTopicId = hubTopicId;
        this.hubSubscriptionStatusId = hubSubscriptionStatusId;
        this.callbackUrl = callbackUrl;
        this.leaseSeconds = leaseSeconds;
        this.secret = secret;
    }
}
