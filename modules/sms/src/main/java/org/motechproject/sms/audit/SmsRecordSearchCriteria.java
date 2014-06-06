package org.motechproject.sms.audit;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.commons.couchdb.query.QueryParam;

import java.util.HashSet;
import java.util.Set;

import static org.motechproject.commons.api.MotechEnumUtils.toStringSet;


/**
 * Helper used to generate lucene query from log filter UI
 */
public class SmsRecordSearchCriteria {

    private Set<SmsDirection> smsDirections = new HashSet<>();
    private String config;
    private String phoneNumber;
    private String messageContent;
    private Range<DateTime> timestampRange;
    private String providerStatus;
    private Set<DeliveryStatus> deliveryStatuses = new HashSet<>();
    private String motechId;
    private String providerId;
    private String errorMessage;
    private QueryParam queryParam = new QueryParam();

    public SmsRecordSearchCriteria withSmsDirections(Set<SmsDirection> smsDirections) {
        this.smsDirections.addAll(smsDirections);
        return this;
    }

    public SmsRecordSearchCriteria withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public SmsRecordSearchCriteria withConfig(String config) {
        this.config = config;
        return this;
    }

    public SmsRecordSearchCriteria withMotechId(String motechId) {
        this.motechId = motechId;
        return this;
    }

    public SmsRecordSearchCriteria withProviderId(String providerId) {
        //todo: temporary couchdb-lucene kludge : lucene interprets - as an OR, so enclose in quotes
        this.providerId = "\"" + providerId + "\"";
        return this;
    }

    public SmsRecordSearchCriteria withMessageContent(String messageContent) {
        this.messageContent = messageContent;
        return this;
    }

    public SmsRecordSearchCriteria withErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    public SmsRecordSearchCriteria withTimestamp(DateTime timestamp) {
        this.timestampRange = new Range<>(timestamp, timestamp);
        return this;
    }

    public SmsRecordSearchCriteria withTimestampRange(Range<DateTime> timestampRange) {
        this.timestampRange = timestampRange;
        return this;
    }

    public SmsRecordSearchCriteria withProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
        return this;
    }

    public SmsRecordSearchCriteria withDeliverystatuses(Set<DeliveryStatus> deliveryStatuses) {
        this.deliveryStatuses.addAll(deliveryStatuses);
        return this;
    }

    public SmsRecordSearchCriteria withQueryParam(QueryParam queryParam) {
        this.queryParam = queryParam;
        return this;
    }

    // Getters

    public Set<String> getSmsDirections() {
        return toStringSet(smsDirections);
    }

    public String getConfig() {
        return config;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public Range<DateTime> getTimestampRange() {
        return timestampRange;
    }

    public Set<String> getDeliveryStatuses() {
        return toStringSet(deliveryStatuses);
    }

    public String getMotechId() {
        return motechId;
    }

    public String getProviderId() {
        return providerId;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public QueryParam getQueryParam() {
        return queryParam;
    }

    @Override
    public String toString() {
        return "SmsRecordSearchCriteria{" +
                "smsDirections=" + smsDirections +
                ", config='" + config + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", messageContent='" + messageContent + '\'' +
                ", timestampRange=" + timestampRange +
                ", providerStatus='" + providerStatus + '\'' +
                ", deliveryStatuses=" + deliveryStatuses +
                ", motechId='" + motechId + '\'' +
                ", providerId='" + providerId + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", queryParam=" + queryParam +
                '}';
    }
}
