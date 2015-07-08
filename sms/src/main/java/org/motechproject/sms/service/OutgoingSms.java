package org.motechproject.sms.service;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.sms.util.SmsEventParams;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.apache.commons.lang.StringUtils.isNotBlank;

/**
 * Represents an outgoing SMS
 */
public class OutgoingSms {

    /**
     * One or more recipients
     */
    private List<String> recipients;
    /**
     * The content of the message to send
     */
    private String message;
    /**
     * If specified, use this config to send the SMS, otherwise use the default config
     */
    private String config;
    /**
     * If specified will schedule the message for future delivery using the MOTECH scheduler
     */
    private DateTime deliveryTime;
    /**
     * MOTECH specific message GUID
     */
    private String motechId;
    /**
     * Provider specific message id
     */
    private String providerId;
    /**
     * Internal failure counter
     */
    private Integer failureCount = 0;
    /**
     * Map of custom parameters
     */
    private Map<String, String> customParams;

    /**
     * Constructs a new instance without setting any fields.
     */
    public OutgoingSms() {
    }

    /**
     * Constructs an instance using the field map from the provided event.
     * @param event the event to parse into the SMS instance
     * @see SmsEventParams
     */
    public OutgoingSms(MotechEvent event) {
        Map<String, Object> params = event.getParameters();
        config = (String) params.get(SmsEventParams.CONFIG);
        recipients = (List<String>) params.get(SmsEventParams.RECIPIENTS);
        message = (String) params.get(SmsEventParams.MESSAGE);
        deliveryTime = (DateTime) params.get(SmsEventParams.DELIVERY_TIME);
        if (params.containsKey(SmsEventParams.FAILURE_COUNT)) {
            failureCount = (Integer) params.get(SmsEventParams.FAILURE_COUNT);
        }
        motechId = (String) params.get(SmsEventParams.MOTECH_ID);
        providerId = (String) params.get(SmsEventParams.PROVIDER_MESSAGE_ID);
        customParams = (Map<String, String>) params.get(SmsEventParams.CUSTOM_PARAMS);
    }

    /**
     * Creates a new instance using the provided parameters.
     * @param config use this config to send the SMS, otherwise use the default config
     * @param recipients one or more recipients
     * @param message the message to send
     * @param deliveryTime the expected delivery time of the sms
     */
    public OutgoingSms(String config, List<String> recipients, String message, DateTime deliveryTime) {
        this.recipients = recipients;
        this.message = message;
        this.config = config;
        this.deliveryTime = deliveryTime;
    }

    /**
     * Creates a new instance using the provided parameters.
     * @param config use this config to send the SMS, otherwise use the default config
     * @param recipient the recipient of the SMS
     * @param message the message to send
     * @param deliveryTime the expected delivery time of the sms
     */
    public OutgoingSms(String config, String recipient, String message, DateTime deliveryTime) {
        this(config, Collections.singletonList(recipient), message, deliveryTime);
    }

    /**
     * Creates a new instance using the provided parameters.
     * @param config use this config to send the SMS, otherwise use the default config
     * @param recipients the recipients of the SMS
     * @param message the message to send
     */
    public OutgoingSms(String config, List<String> recipients, String message) {
        this.recipients = recipients;
        this.message = message;
        this.config = config;
    }

    public OutgoingSms(String config, String recipient, String message) {
        this(config, Collections.singletonList(recipient), message);
    }

    /**
     * Creates a new instance using the provided parameters.
     * @param recipients the recipients of the SMS
     * @param message the message to send
     * @param deliveryTime the expected delivery time of the sms
     */
    public OutgoingSms(List<String> recipients, String message, DateTime deliveryTime) {
        this.recipients = recipients;
        this.message = message;
        this.deliveryTime = deliveryTime;
    }

    /**
     * Creates a new instance using the provided parameters.
     * @param recipient the recipient of the SMS
     * @param message the message to send
     * @param deliveryTime the expected delivery time of the sms
     */
    public OutgoingSms(String recipient, String message, DateTime deliveryTime) {
        this(Collections.singletonList(recipient), message, deliveryTime);
    }

    /**
     * Creates a new instance using the provided parameters.
     * @param recipients the recipients of the SMS
     * @param message the message to send
     */
    public OutgoingSms(List<String> recipients, String message) {
        this.recipients = recipients;
        this.message = message;
    }

    /**
     * Creates a new instance using the provided parameters.
     * @param recipient the recipient of the SMS
     * @param message the message to send
     */
    public OutgoingSms(String recipient, String message) {
        this(Collections.singletonList(recipient), message);
    }

    /**
     * If specified, use this config to send the SMS, otherwise use the default config.
     * @return the config that will be used for sending this SMS
     */
    public String getConfig() {
        return config;
    }

    /**
     * Checks if this SMS specifies a config for sending the SMS, or whether the default
     * configuration should be used.
     * @return true if there's a config specified for this SMS, false if the default config is to be used
     */
    public Boolean hasConfig() {
        return isNotBlank(config);
    }

    /**
     * If specified, use this config to send the SMS, otherwise use the default config.
     * @param config the config that will be used for sending this SMS
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * Gets the recipients for this message. An SMS can have one or more recipients.
     * @return the recipients fo the SMS
     */
    public List<String> getRecipients() {
        return recipients;
    }

    /**
     * Gets the recipients for this message. An SMS can have one or more recipients.
     * @param recipients the recipients fo the SMS
     */
    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    /**
     * @return the content of the message to send
     */
    public String getMessage() {
        return message;
    }

    /**
     * @param message the content of the message to send
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Gets the delivery time for this SMS. If specified, will schedule
     * the message for future delivery using the MOTECH scheduler.
     * @return the delivery time for this SMS
     */
    public DateTime getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * Checks whether this SMS has a specified delivery time. If specified, will schedule
     * the message for future delivery using the MOTECH scheduler. If not, the message
     * should be immediately delivered.
     * @return true if this SMS has a specific delivery time, false otherwise
     */
    public Boolean hasDeliveryTime() {
        return deliveryTime != null;
    }

    /**
     * Gets the delivery time for this SMS. If specified will schedule
     * the message for future delivery using the MOTECH scheduler.
     * @param deliveryTime  the delivery time for this SMS
    */
    public void setDeliveryTime(DateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    // TODO: for MOTECH-1466 - This does not belong in the API
    /**
     * Gets the failure counter, used by the SMS module to keep track of the failures
     * and execute retries.
     * @return the internal failure counter for this SMS
     */
    public Integer getFailureCount() {
        return failureCount;
    }

    // TODO: for MOTECH-1466 - This does not belong in the API
    /**
     * Sets the failure counter, used by the SMS module to keep track of the failures
     * and execute retries.
     * @param failureCount  the internal failure counter for this SMS
     */
    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }


    public Boolean hasMotechId() {
        return isNotBlank(motechId);
    }

    /**
     * Gets the MOTECH specific message GUID of this SMS.
     * @return the motech specific GUID for this SMS
     */
    public String getMotechId() {
        return motechId;
    }

    /**
     * Sets the MOTECH specific message GUID of this SMS.
     * @param motechId the motech specific GUID for this SMS
     */
    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    /**
     * @return the provider specific message id
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the provider specific message id
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    /**
     * @return map of custom parameters for the template
     */
    public Map<String, String> getCustomParams() {
        return customParams;
    }

    /**
     * @param customParams map of custom parameters for the template
     */
    public void setCustomParams(Map<String, String> customParams) {
        this.customParams = customParams;
    }

    @Override
    public int hashCode() {
        return Objects.hash(recipients, message, deliveryTime);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        final OutgoingSms other = (OutgoingSms) obj;

        return Objects.equals(this.config, other.config)
                && Objects.equals(this.recipients, other.recipients)
                && Objects.equals(this.message, other.message)
                && Objects.equals(this.deliveryTime, other.deliveryTime)
                && Objects.equals(this.failureCount, other.failureCount)
                && Objects.equals(this.motechId, other.motechId)
                && Objects.equals(this.providerId, other.providerId);
    }

    @Override
    public String toString() {
        return "OutgoingSms{" +
                "recipients=" + recipients +
                ", message='" + message + '\'' +
                ", config='" + config + '\'' +
                ", deliveryTime=" + deliveryTime +
                ", failureCount=" + failureCount +
                ", motechId='" + motechId + '\'' +
                ", providerId='" + providerId + '\'' +
                '}';
    }
}
