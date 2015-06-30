package org.motechproject.sms.audit;

import org.joda.time.format.DateTimeFormat;
import org.motechproject.commons.date.util.DateUtil;

/**
 * Represents one record in the logging UI
 */
public class SmsLoggingRecord {

    /**
     * The name of the configuration this SMS should be associated with.
     */
    private String config;

    /**
     * The phone number to which the SMS was sent, or from which it was received.
     */
    private String phoneNumber;

    /**
     * Whether this is an inbound or an outbound SMS.
     */
    private String smsDirection;

    /**
     * The timestamp for the SMS.
     */
    private String timestamp;

    /**
     * The delivery status for this SMS.
     */
    private String deliveryStatus;


    private String providerStatus;

    /**
     * The content of the SMS message.
     */
    private String messageContent;

    /**
     * The ID used to identify the SMS in Motech.
     */
    private String motechId;

    /**
     * The provider generated ID for the SMS.
     */
    private String providerId;

    /**
     * The error message from the provider, if applicable.
     */
    private String errorMessage;

    /**
     * Constructs an instance of SmsLoggingRecord from an {@link SmsRecord}. In other words
     * it converts the domain object to the view object.
     * @param record the domain representation of the record
     */
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

    /**
     * @return the name of the configuration this SMS should be associated with
     */
    public String getConfig() {
        return config;
    }

    /**
     * @param config the name of the configuration this SMS should be associated with
     */
    public void setConfig(String config) {
        this.config = config;
    }

    /**
     * @return the phone number to which the SMS was sent, or from which it was received
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber the phone number to which the SMS was sent, or from which it was received
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @return the direction of the SMS, inbound or outbound
     */
    public String getSmsDirection() {
        return smsDirection;
    }

    /**
     * @param smsDirection the direction of the SMS, inbound or outbound
     */
    public void setSmsDirection(String smsDirection) {
        this.smsDirection = smsDirection;
    }

    /**
     * @return the timestamp for the SMS
     */
    public String getTimestamp() {
        return timestamp;
    }

    /**
     * @param timestamp the timestamp for the SMS
     */
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * @return the delivery status for this SMS
     */
    public String getDeliveryStatus() {
        return deliveryStatus;
    }

    /**
     * @param deliveryStatus the delivery status for this SMS
     */
    public void setDeliveryStatus(String deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
    }

    /**
     * @return the content of the SMS message
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @param messageContent the content of the SMS message
     */
    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    /**
     * @return the ID used to identify the SMS in Motech
     */
    public String getMotechId() {
        return motechId;
    }

    /**
     * @param motechId the ID used to identify the SMS in Motech
     */
    public void setMotechId(String motechId) {
        this.motechId = motechId;
    }

    /**
     * @return the provider generated ID for the SMS
     */
    public String getProviderId() {
        return providerId;
    }

    /**
     * @param providerId the provider generated ID for the SMS
     */
    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    /**
     * @return the error message from the provider
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the error message from the provider
     */
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
