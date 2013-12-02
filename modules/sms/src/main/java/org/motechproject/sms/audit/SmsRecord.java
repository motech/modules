package org.motechproject.sms.audit;

//todo: motechTimestanp & providerTimestamp instead of just timestamp?
//todo: 'senderNumber' & 'recipientNumber' instead of 'phoneNumber'?

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;
import org.motechproject.commons.date.util.DateUtil;

/**
 * SMS audit record for the database
 */
@TypeDiscriminator("doc.type === 'SmsRecord'")
public class SmsRecord extends MotechBaseDataObject {

    @JsonProperty
    private String config;
    @JsonProperty
    private SmsDirection smsDirection;
    @JsonProperty
    private String phoneNumber;
    @JsonProperty
    private String messageContent;
    @JsonProperty
    private DateTime timestamp;
    @JsonProperty
    private DeliveryStatus deliveryStatus;
    @JsonProperty
    private String providerStatus;
    @JsonProperty
    private String motechId;
    @JsonProperty
    private String providerId;
    @JsonProperty
    private String errorMessage;

    public SmsRecord() {
    }

    public SmsRecord(String config, SmsDirection smsDirection, String number, String message, DateTime timestamp,
                     DeliveryStatus deliveryStatus, String providerStatus, String motechId, String providerId,
                     String errorMessage) {
        super("SmsRecord");
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

    public SmsDirection getSmsDirection() {
        return smsDirection;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public DateTime getTimestamp() {
        return DateUtil.setTimeZoneUTC(timestamp);
    }

    public DeliveryStatus getDeliveryStatus() {
        return deliveryStatus;
    }

    public String getMotechId() {
        return motechId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setTimestamp(DateTime timestamp) {
        this.timestamp = timestamp;
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

    @Override
    public String toString() {
        return "SmsRecord{" +
                "config='" + config + '\'' +
                ", smsDirection=" + smsDirection +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", timestamp=" + timestamp +
                ", deliveryStatus=" + deliveryStatus +
                ", providerStatus='" + providerStatus + '\'' +
                ", motechId='" + motechId + '\'' +
                ", providerId='" + providerId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                '}';
    }
}
