package org.motechproject.sms.audit;

//todo: motechTimestanp & providerTimestamp instead of just timestamp?
//todo: 'senderNumber' & 'recipientNumber' instead of 'phoneNumber'?

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.joda.time.DateTime;
import org.motechproject.commons.date.util.DateUtil;
import org.motechproject.mds.annotations.Entity;

/**
 * SMS audit record for the database
 */
@Entity
public class SmsRecord {
    private String config;
    private SmsDirection smsDirection;
    private String phoneNumber;
    private String messageContent;
    private DateTime timestamp;
    private DeliveryStatus deliveryStatus;
    private String providerStatus;
    private String motechId;
    private String providerId;
    private String errorMessage;

    public SmsRecord() {
        this(null, null, null, null, null, null, null, null, null, null);
    }

    public SmsRecord(String config, SmsDirection smsDirection, String number,  //NO CHECKSTYLE ParameterNumber
                     String message, DateTime timestamp, DeliveryStatus deliveryStatus, String providerStatus,
                     String motechId, String providerId, String errorMessage) {
        this.config = config;
        this.smsDirection = smsDirection;
        this.phoneNumber = number;
        this.messageContent = message;
        this.timestamp = timestamp;
        this.deliveryStatus = deliveryStatus;
        this.providerStatus = providerStatus;
        this.motechId = motechId;
        this.providerId = providerId;
        this.errorMessage = errorMessage;
    }

    public String getConfig() {
        return config;
    }

    public void setConfig(String config) {
        this.config = config;
    }

    public SmsDirection getSmsDirection() {
        return smsDirection;
    }

    public void setSmsDirection(SmsDirection smsDirection) {
        this.smsDirection = smsDirection;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public DateTime getTimestamp() {
        return DateUtil.setTimeZoneUTC(timestamp);
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public void setDeliveryStatus(DeliveryStatus deliveryStatus) {
        this.deliveryStatus = deliveryStatus;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public void setProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
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
    public boolean equals(Object obj) {
        return EqualsBuilder.reflectionEquals(this, obj);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
