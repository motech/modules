package org.motechproject.hub.web;

import org.motechproject.http.agent.service.HttpAgent;
import org.motechproject.http.agent.service.Method;
import org.motechproject.hub.mds.HubSubscription;
import org.motechproject.hub.mds.service.HubSubscriptionMDSService;
import org.motechproject.hub.model.SubscriptionStatusLookup;
import org.motechproject.hub.util.HubConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

/**
 * This class implements <code>Runnable</code> which starts a new thread to
 * verify the intent of the subscriber requesting subscription or
 * unsubscription.
 *
 * @author Anuranjan
 *
 */
public class IntentVerificationThreadRunnable implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(IntentVerificationThreadRunnable.class);

    private static final String INTENT_VERIFICATION_PARAMS = "?hub.mode={mode}&hub.topic={topic}&hub.challenge={challenge}";

    private String callbackUrl;
    private String mode;
    private String topic;
    private Integer topicId;
    private Integer retryCount;
    private Long retryInterval;

    /**
     * Gets the topic id.
     *
     * @return the topic id
     */
    public Integer getTopicId() {
        return topicId;
    }

    /**
     * Sets the topic id.
     *
     * @param topicId the topic id to be set
     */
    public void setTopicId(Integer topicId) {
        this.topicId = topicId;
    }

    /**
     * Gets the retry count.
     *
     * @return the retry count
     */
    public Integer getRetryCount() {
        return retryCount;
    }

    /**
     * Sets the retry count.
     *
     * @param retryCount the retry count to be set
     */
    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    /**
     * Gets interval of intent verification retries.
     *
     * @return the retry interval
     */
    public Long getRetryInterval() {
        return retryInterval;
    }

    /**
     * Sets interval of intent verification retries.
     *
     * @param retryInterval the retry interval to be set
     */
    public void setRetryInterval(Long retryInterval) {
        this.retryInterval = retryInterval;
    }

    /**
     * Gets callback URL. Callback url is the URL at which
     * a subscriber wishes to receive notifications about changes
     * in subscribed topic.
     *
     * @return the callback url
     */
    public String getCallbackUrl() {
        return callbackUrl;
    }

    /**
     * Sets callback URL. Callback url is the URL at which
     * a subscriber wishes to receive notifications about changes
     * in subscribed topic.
     *
     * @param callbackUrl the callback url to be set
     */
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    /**
     * Gets mode of hub request as a <code>String</code>.
     *
     * @return the mode of hub request as a <code>String</code>
     */
    public String getMode() {
        return mode;
    }

    /**
     * Sets mode of hub request. It takes the mode as a <code>String</code>.
     * For valid modes look at {@link org.motechproject.hub.model.Modes}
     *
     * @param mode the mode of hub request to be set
     */
    public void setMode(String mode) {
        this.mode = mode;
    }

    /**
     * Gets the topic. Its the URL of the content that is subscribed.
     *
     * @return the topic URL
     */
    public String getTopic() {
        return topic;
    }

    /**
     * Sets the topic. Topic is the URL of the content to which you want subscribe.
     *
     * @param topic the topic URL
     */
    public void setTopic(String topic) {
        this.topic = topic;
    }

    private HubSubscriptionMDSService hubSubscriptionMDSService;

    private HttpAgent httpAgentImpl;

    /**
     * Creates a new instance of <code>IntentVerificationThreadRunnable</code>, with
     * all fields set to the values specified in the parameters.
     *
     * @param hubSubscriptionMDSService the MDS service for hub subscriptions
     * @param httpAgentImpl the OSGI service for sending http requests
     */
    public IntentVerificationThreadRunnable(
            HubSubscriptionMDSService hubSubscriptionMDSService,
            HttpAgent httpAgentImpl) {
        this.hubSubscriptionMDSService = hubSubscriptionMDSService;
        this.httpAgentImpl = httpAgentImpl;
    }

    /**
     * Sets the <code>HttpAgent</code> which is an OSGI service
     * used for sending http requests.
     *
     * @param httpAgentImpl the http agent to be set
     */
    public void setHttpAgentImpl(HttpAgent httpAgentImpl) {
        this.httpAgentImpl = httpAgentImpl;
    }

    /**
     * In order to prevent an attacker from creating unwanted subscriptions on
     * behalf of a subscriber (or unsubscribing desired ones), a hub must ensure
     * that the subscriber did indeed send the subscription request. The hub
     * verifies a subscription request by sending an HTTP GET request to the
     * subscriber's callback URL as given in the subscription request.
     *
     * For this, hub generates a random string token and sends as param
     * <code>hub.challenge</code> which the subscriber must echo in order to
     * confirm its request. Other parameters are <code>hub.mode</code> and
     * <code>hub.topic</code> coming from the subscription/unsubscription
     * request
     *
     */
    @Override
    @Transactional
    public void run() {

        SubscriptionStatusLookup statusLookup;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);
        // randomly generated UUID
        String uuid = UUID.randomUUID().toString();
        String intentVerificationUrl = callbackUrl + INTENT_VERIFICATION_PARAMS;
        try {
            intentVerificationUrl = replaceParameters(intentVerificationUrl,
                    mode, topic, uuid);
            ResponseEntity<String> response = (ResponseEntity<String>) httpAgentImpl
                    .executeWithReturnTypeSync(intentVerificationUrl, entity,
                            Method.GET, retryCount, retryInterval);

            // Any status code other than 2xx is invalid response. Also, the
            // response body should match the uuid string passed in the GET call
            if (!responseValid(response, uuid)) {
                statusLookup = SubscriptionStatusLookup.INTENT_FAILED;
            } else {
                statusLookup = SubscriptionStatusLookup.INTENT_VERIFIED;
            }
        } catch (RuntimeException e) {
            LOGGER.error("An error occurred during intent verification", e);
            statusLookup = SubscriptionStatusLookup.INTENT_FAILED;
        }

        // fetch the subscription corresponding to the callbackUrl and the
        // topic

        List<HubSubscription> subscriptions = hubSubscriptionMDSService
                .findSubByCallbackUrlAndTopicId(callbackUrl, topicId);
        if (subscriptions == null || subscriptions.size() == 0
                || subscriptions.size() > 1) {
            LOGGER.info("not handled earlier, need to check");
            return;
        }
        HubSubscription subscription = subscriptions.get(0);

        Integer hubSubscriptionStatusId = subscription
                .getHubSubscriptionStatusId();
        Integer currentStatus = -1;
        if (hubSubscriptionStatusId != 0) {
            currentStatus = subscription.getHubSubscriptionStatusId();
        }

        // any failure should not affect the existing subscription status
        if (!SubscriptionStatusLookup.INTENT_VERIFIED.getId().equals(
                currentStatus)) {

            // fetch the HubSubscriptionStatus enum value
            subscription.setHubSubscriptionStatusId(statusLookup.getId());

            // insert a record corresponding to this subscription
            hubSubscriptionMDSService.create(subscription);
        }

    }

    private boolean responseValid(ResponseEntity<String> response, String uuid) {
        if (response == null) {
            return false;
        }
        HttpStatus status = response.getStatusCode();
        if (status.value() / HubConstants.HUNDRED != 2) {
            return false;
        }
        String responseBody = response.getBody();
        if (responseBody == null) {
            return false;
        }
        if (!responseBody.trim().equals(uuid)) {
            return false;
        }
        return true;
    }

    private String replaceParameters(String contentVerificationUrl,
            String modeString, String topic, String uuid) {
        String url = contentVerificationUrl;
        url = url.replace("{mode}", modeString);
        url = url.replace("{topic}", topic);
        url = url.replace("{challenge}", uuid);
        return url;
    }

}
