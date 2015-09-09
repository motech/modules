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
import static org.motechproject.mds.util.Constants.Operators.MATCHES_CASE_INSENSITIVE;

/**
 * Used to query the audit records in the database
 */
public interface SmsRecordsDataService extends MotechDataService<SmsRecord> {

    String PROVIDER_ID = "providerId";
    String MOTECH_ID = "motechId";

    @Lookup
    List<SmsRecord> findByCriteria(@LookupField(name = "config", customOperator = MATCHES) String config, //NO CHECKSTYLE ParameterNumber
                                   @LookupField(name = "smsDirection") Set<SmsDirection> directions,
                                   @LookupField(name = "phoneNumber", customOperator = MATCHES) String phoneNumber,
                                   @LookupField(name = "messageContent", customOperator = MATCHES) String messageContent,
                                   @LookupField(name = "timestamp") Range<DateTime> timestamp,
                                   @LookupField(name = "deliveryStatus") Set<DeliveryStatus> deliveryStatuses,
                                   @LookupField(name = "providerStatus", customOperator = MATCHES) String providerStatus,
                                   @LookupField(name = MOTECH_ID, customOperator = MATCHES) String motechId,
                                   @LookupField(name = PROVIDER_ID, customOperator = MATCHES) String providerId,
                                   @LookupField(name = "errorMessage", customOperator = MATCHES) String errorMessage,
                                   QueryParams queryParams);

    long countFindByCriteria(@LookupField(name = "config", customOperator = MATCHES) String config, //NO CHECKSTYLE ParameterNumber
                             @LookupField(name = "smsDirection") Set<SmsDirection> directions,
                             @LookupField(name = "phoneNumber", customOperator = MATCHES) String phoneNumber,
                             @LookupField(name = "messageContent", customOperator = MATCHES) String messageContent,
                             @LookupField(name = "timestamp") Range<DateTime> timestamp,
                             @LookupField(name = "deliveryStatus") Set<DeliveryStatus> deliveryStatuses,
                             @LookupField(name = "providerStatus", customOperator = "matches()") String providerStatus,
                             @LookupField(name = MOTECH_ID, customOperator = MATCHES) String motechId,
                             @LookupField(name = PROVIDER_ID, customOperator = MATCHES) String providerId,
                             @LookupField(name = "errorMessage", customOperator = MATCHES) String errorMessage);

    /**
     * Retrieves records by the provider ID.
     * @param providerId the provider ID
     * @return the list of matching records
     */
    @Lookup
    List<SmsRecord> findByProviderId(@LookupField(name = PROVIDER_ID, customOperator = MATCHES_CASE_INSENSITIVE)
                                     String providerId);
    /**
     * Retrieves records by the MOTECH ID.
     * @param motechId the MOTECH ID
     * @return the list of matching records
     */
    @Lookup
    List<SmsRecord> findByMotechId(@LookupField(name = MOTECH_ID, customOperator = MATCHES_CASE_INSENSITIVE)
                                     String motechId);
    /**
     * Retrieves records by both provider and MOTECH IDs.
     * @param providerId the provider ID
     * @param motechId the MOTECH ID
     * @return the list of matching records
     */
    @Lookup
    List<SmsRecord> findByProviderAndMotechId(
            @LookupField(name = PROVIDER_ID, customOperator = MATCHES_CASE_INSENSITIVE) String providerId,
            @LookupField(name = MOTECH_ID, customOperator = MATCHES_CASE_INSENSITIVE) String motechId);
}
