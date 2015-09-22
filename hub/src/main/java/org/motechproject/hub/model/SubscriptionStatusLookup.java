package org.motechproject.hub.model;

/**
 * This enum class defines the valid status codes for subscription status.
 *
 * @author anuranjan
 *
 */
public enum SubscriptionStatusLookup {

    /**
     * Subscription is accepted and should be intent verified
     */
    ACCEPTED(1, "accepted"),
    /**
     * Subscription did not pass intent verification
     */
    INTENT_FAILED(2, "intent_failed"),
    /**
     * Subscription successfully passed intent verification
     */
    INTENT_VERIFIED(3, "intent_verified");

    private final String status;
    private final Integer id;

    /**
     * Gets the id of subscription status.
     *
     * @return the id of subscription status
     */
    public Integer getId() {
        return id;
    }

    private SubscriptionStatusLookup(Integer id, String status) {
        this.status = status;
        this.id = id;
    }

    @Override
    public String toString() {
        return this.status;
    }

}
