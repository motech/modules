package org.motechproject.hub.service;

import org.motechproject.hub.exception.HubException;
import org.motechproject.hub.model.Modes;

/**
 * This is an interface which provides business logic for a
 * subscription/unsubscription request.
 *
 * @author Anuranjan
 *
 */
public interface SubscriptionService {

    /**
     * This method executes the business logic for subscribing/unsubscribing to
     * a topic. The subscriber provides the callback URL where the update
     * notification should be sent. Returns the id of the IntentVerificationThreadRunnable
     * thread which verify the intent of the subscriber requesting subscription. In case of hubMode
     * is "unsubscribe" thread isn't created and method returns null.
     *
     * @param callbackUrl
     *            - a <code>String</code> representing the subscriber's callback
     *            URL where notifications should be delivered
     * @param hubMode
     *            - a <code>Modes</code> enum representing the mode "subscribe"
     *            or "unsubscribe", depending on the goal of the request
     * @param topic
     *            - a <code>String</code> representing the topic URL that the
     *            subscriber wishes to subscribe to or unsubscribe from
     * @param leaseSeconds
     *            - a <code>String</code> representing the number of seconds for
     *            which the subscriber would like to have the subscription
     *            active. This is an optional parameter
     * @param secret
     *            - a <code>String</code> provided by the subscriber which will
     *            be used to compute an HMAC digest for authorized content
     *            distribution. Currently this is not being consumed by the API
     * @return the id of IntentVerificationThreadRunnable which is created, if thread wasn't created then returns null
     * @throws HubException
     *            - when unsubscribing from a topic that was not subscribed to,
     *            when the topic does not exist or when the topic was not found
     */
    Long subscribe(String callbackUrl, Modes hubMode, String topic,
            String leaseSeconds, String secret) throws HubException;

}
