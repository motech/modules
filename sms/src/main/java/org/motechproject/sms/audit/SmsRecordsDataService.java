package org.motechproject.sms.audit;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;
import java.util.Set;

import static org.motechproject.mds.util.Constants.Operators.MATCHES;

/**
 * Used to query and save audit records in the database.
 * This is a service interface for which MDS will generate the implementation at runtime.
 */
public interface SmsRecordsDataService extends MotechDataService<SmsRecord> {

    /**
     * Retrieves all sms records matching the given criteria. All string fields in this lookup are matched
     * using the matches() operator, meaning they will be matched using the same rules that {@link String#matches(String)}
     * uses.
     * @param config the name of the configuration associated with the SMS message
     * @param directions the set of directions (inbound, outbound)
     * @param phoneNumber the number of the phone the message was received from or delivered to
     * @param messageContent the contents of the SMS message
     * @param timestamp the date-time range the timestamp of the SMS should fall into
     * @param deliveryStatuses the set of delivery status for the messages
     * @param providerStatus
     * @param motechId the id by which MOTECH identifies the message
     * @param providerId the provider generated ID for the SMS
     * @param errorMessage the error message for the SMS
     * @param queryParams the query params controlling the ordering and size of the lookup
     * @return the matching records
     */
    @Lookup
    List<SmsRecord> findByCriteria(@LookupField(name = "config", customOperator = MATCHES) String config, //NO CHECKSTYLE ParameterNumber
                                   @LookupField(name = "smsDirection") Set<SmsDirection> directions,
                                   @LookupField(name = "phoneNumber", customOperator = MATCHES) String phoneNumber,
                                   @LookupField(name = "messageContent", customOperator = MATCHES) String messageContent,
                                   @LookupField(name = "timestamp") Range<DateTime> timestamp,
                                   @LookupField(name = "deliveryStatus") Set<DeliveryStatus> deliveryStatuses,
                                   @LookupField(name = "providerStatus", customOperator = MATCHES) String providerStatus,
                                   @LookupField(name = "motechId", customOperator = MATCHES) String motechId,
                                   @LookupField(name = "providerId", customOperator = MATCHES) String providerId,
                                   @LookupField(name = "errorMessage", customOperator = MATCHES) String errorMessage,
                                   QueryParams queryParams);

    /**
     * Retrieves the total count of SMS messages matching the given criteria. All string fields in this lookup are matched
     * using the matches() operator, meaning they will be matched using the same rules that {@link String#matches(String)}
     * uses.
     * @param config the name of the configuration associated with the SMS message
     * @param directions the set of directions (inbound, outbound)
     * @param phoneNumber the number of the phone the message was received from or delivered to
     * @param messageContent the contents of the SMS message
     * @param timestamp the date-time range the timestamp of the SMS should fall into
     * @param deliveryStatuses the set of delivery status for the messages
     * @param providerStatus
     * @param motechId the id by which MOTECH identifies the message
     * @param providerId the provider generated ID for the SMS
     * @param errorMessage the error message for the SMS
     * @return the matching records
     */
    long countFindByCriteria(@LookupField(name = "config", customOperator = MATCHES) String config, //NO CHECKSTYLE ParameterNumber
                             @LookupField(name = "smsDirection") Set<SmsDirection> directions,
                             @LookupField(name = "phoneNumber", customOperator = MATCHES) String phoneNumber,
                             @LookupField(name = "messageContent", customOperator = MATCHES) String messageContent,
                             @LookupField(name = "timestamp") Range<DateTime> timestamp,
                             @LookupField(name = "deliveryStatus") Set<DeliveryStatus> deliveryStatuses,
                             @LookupField(name = "providerStatus", customOperator = "matches()") String providerStatus,
                             @LookupField(name = "motechId", customOperator = MATCHES) String motechId,
                             @LookupField(name = "providerId", customOperator = MATCHES) String providerId,
                             @LookupField(name = "errorMessage", customOperator = MATCHES) String errorMessage);

}
