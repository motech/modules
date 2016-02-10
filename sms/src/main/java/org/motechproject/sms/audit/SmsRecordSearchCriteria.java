package org.motechproject.sms.audit;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.mds.query.QueryParams;

import java.util.HashSet;
import java.util.Set;

import static org.motechproject.commons.api.MotechEnumUtils.toStringSet;


/**
 * Helper used to generate a database lookup from log filter UI
 */
public class SmsRecordSearchCriteria {

    /**
     * The set of directions (inbound, outbound).
     */
    private Set<SmsDirection> smsDirections = new HashSet<>();

    /**
     * The name of the configuration associated with the SMS message.
     */
    private String config;

    /**
     * The number of the phone the message was received from or delivered to.
     */
    private String phoneNumber;

    /**
     * The contents of the SMS message.
     */
    private String messageContent;

    /**
     * The date-time range the timestamp of the SMS should fall into.
     */
    private Range<DateTime> timestampRange;

    private String providerStatus;

    /**
     * The set of delivery status for the messages.
     */
    private Set<String> deliveryStatuses = new HashSet<>();

    /**
     * The id by which MOTECH identifies the message.
     */
    private String motechId;

    /**
     * The provider generated ID for the SMS.
     */
    private String providerId;

    /**
     * The error message for the SMS.
     */
    private String errorMessage;

    /**
     * The query params controlling the ordering and size of the lookup that will be executed.
     */
    private QueryParams queryParams;

    /**
     * Sets the sms directions which should be included in the query.
     * @param smsDirections the set of directions (inbound, outbound)
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withSmsDirections(Set<SmsDirection> smsDirections) {
        this.smsDirections.addAll(smsDirections);
        return this;
    }

    /**
     * Sets the phone number part of the search.
     * @param phoneNumber the phone number which received or sent the SMS
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    /**
     * Sets the configuration name part of the search.
     * @param config the config name associated with the SMS
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withConfig(String config) {
        this.config = config;
        return this;
    }

    /**
     * Sets the MOTECH ID part of the search query.
     * @param motechId the ID used by MOTECH to identify this SMS message
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withMotechId(String motechId) {
        this.motechId = motechId;
        return this;
    }

    /**
     * Sets the provider ID part of the search query.
     * @param providerId the ID used by the provider to identify this SMS message
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withProviderId(String providerId) {
        this.providerId = providerId;
        return this;
    }

    /**
     * Sets the message content part of this search query.
     * @param messageContent the content of the SMS
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withMessageContent(String messageContent) {
        this.messageContent = messageContent;
        return this;
    }

    /**
     * Sets the error message part of this search query.
     * @param errorMessage the error message for the SMS
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
        return this;
    }

    /**
     * Sets the expected timestamp for the messages retrieved by this query.
     * @param timestamp the timestamp of the messages
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withTimestamp(DateTime timestamp) {
        this.timestampRange = new Range<>(timestamp, timestamp);
        return this;
    }

    /**
     * Sets the expected timestamp range into which the messages retrieved by this query must fall into.
     * @param timestampRange the timestamp range into which the messages must fall into
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withTimestampRange(Range<DateTime> timestampRange) {
        this.timestampRange = timestampRange;
        return this;
    }

    public SmsRecordSearchCriteria withProviderStatus(String providerStatus) {
        this.providerStatus = providerStatus;
        return this;
    }

    /**
     * Sets the delivery statuses the SMS messages retrieved by this search must match.
     * @param deliveryStatuses the set of delivery status that will be taken into consideration when executing the query
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withDeliverystatuses(Set<String> deliveryStatuses) {
        this.deliveryStatuses.addAll(deliveryStatuses);
        return this;
    }

    /**
     * Sets the query params that will be used when performing the lookup. This params will control the ordering
     * and size of the result set.
     * @param queryParam the params that will be passed to the lookup
     * @return this instance of the search criteria
     */
    public SmsRecordSearchCriteria withQueryParams(QueryParams queryParam) {
        this.queryParams = queryParam;
        return this;
    }

    // Getters

    /**
     * @return the set of expected SMS directions (inbound, outbound)
     */
    public Set<String> getSmsDirections() {
        return toStringSet(smsDirections);
    }

    /**
     * @return the configuration name which with the SMS should be associated with
     */
    public String getConfig() {
        return config;
    }

    /**
     * @return the phone number which received or sent the sms
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the content of the sms message
     */
    public String getMessageContent() {
        return messageContent;
    }

    /**
     * @return the timestamp range into which SNS messages should fall
     */
    public Range<DateTime> getTimestampRange() {
        return timestampRange;
    }

    /**
     * @return the set of expected delivery statuses for SMS messages
     */
    public Set<String> getDeliveryStatuses() {
        return deliveryStatuses;
    }

    /**
     * @return the ID by which MOTECH identifies the SMS
     */
    public String getMotechId() {
        return motechId;
    }

    /**
     * @return the ID by which the provider identifies the SMS
     */
    public String getProviderId() {
        return providerId;
    }

    public String getProviderStatus() {
        return providerStatus;
    }

    /**
     * @return the error message for the SMS
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @return the query params that will control the result set for the lookup
     */
    public QueryParams getQueryParams() {
        return queryParams;
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
                ", queryParams=" + queryParams +
                '}';
    }
}
