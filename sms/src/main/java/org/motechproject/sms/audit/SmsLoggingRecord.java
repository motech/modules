package org.motechproject.sms.audit;

import org.joda.time.format.DateTimeFormat;
import org.motechproject.commons.date.util.DateUtil;

/**
 * Represents one record in the logging UI
 */
public class SmsLoggingRecord {

    private String config;
    private String phoneNumber;
    private String smsDirection;
    private String timestamp;
    private String deliveryStatus;
    private String providerStatus;
    private String messageContent;
    private String motechId;
    private String providerId;
    private String errorMessage;

    public SmsLoggingRecord(SmsRecord record) {
        this.config = record.getConfig();
        this.phoneNumber = record.getPhoneNumber();
        this.smsDirection = record.getSmsDirection().toString();
        // DateUtil.setTimeZone converts the message time from UTC to local time for display
        this.timestamp = DateTimeFormat.forPattern("Y-MM-dd hh:mm:ss").print(DateUtil.setTimeZone(
                record.getTimestamp()));
        this.deliveryStatus = record.getDeliveryStatus().toString();
        this.providerStatus = record.getProviderStatus();
        this.messageContent = record.getMessageContent();
        this.motechId = record.getMotechId();
        this.providerId = record.getProviderId();
        this.errorMessage = record.getErrorMessage();
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getSmsDirection() {
        return smsDirection;
    }

    public void setSmsDirection(String smsDirection) {
        this.smsDirection = smsDirection;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
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

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
    public String toString() {
        return "SmsLoggingRecord{" +
                "config='" + config + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", smsDirection='" + smsDirection + '\'' +
                ", timestamp='" + timestamp + '\'' +
                ", deliveryStatus='" + deliveryStatus + '\'' +
                ", providerStatus='" + providerStatus + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", motechId='" + motechId + '\'' +
                ", providerId='" + providerId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
