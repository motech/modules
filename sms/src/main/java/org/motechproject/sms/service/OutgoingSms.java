package org.motechproject.sms.service;

import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.sms.SmsEventParams;

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

    public OutgoingSms() {
    }

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
    }

    public OutgoingSms(String config, List<String> recipients, String message, DateTime deliveryTime) {
        this.recipients = recipients;
        this.message = message;
        this.config = config;
        this.deliveryTime = deliveryTime;
    }

    public OutgoingSms(String config, List<String> recipients, String message) {
        this.recipients = recipients;
        this.message = message;
        this.config = config;
    }

    public OutgoingSms(List<String> recipients, String message, DateTime deliveryTime) {
        this.recipients = recipients;
        this.message = message;
        this.deliveryTime = deliveryTime;
    }

    public OutgoingSms(List<String> recipients, String message) {

        this.recipients = recipients;
        this.message = message;
    }

    public String getConfig() {
        return config;
    }

    public Boolean hasConfig() {
        return isNotBlank(config);
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public List<String> getRecipients() {
        return recipients;
    }

    public void setRecipients(List<String> recipients) {
        this.recipients = recipients;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public DateTime getDeliveryTime() {
        return deliveryTime;
    }

    public Boolean hasDeliveryTime() {
        return deliveryTime != null;
    }


    public void setDeliveryTime(DateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public Integer getFailureCount() {
        return failureCount;
    }

    public void setFailureCount(Integer failureCount) {
        this.failureCount = failureCount;
    }

    public Boolean hasMotechId() {
        return isNotBlank(motechId);
    }

    public String getMotechId() {
        return motechId;
    }

    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
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
