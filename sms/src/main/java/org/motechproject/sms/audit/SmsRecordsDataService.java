package org.motechproject.sms.audit;

import org.joda.time.DateTime;
import org.motechproject.commons.api.Range;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;
import java.util.Set;

/**
 * Used to query the audit records in the database
 */
public interface SmsRecordsDataService extends MotechDataService<SmsRecord> {

    @Lookup
    List<SmsRecord> findByCriteria(@LookupField(name = "config") String config, //NO CHECKSTYLE ParameterNumber
                                   @LookupField(name = "smsDirection") Set<SmsDirection> directions,
                                   @LookupField(name = "phoneNumber") String phoneNumber,
                                   @LookupField(name = "messageContent") String messageContent,
                                   @LookupField(name = "timestamp") Range<DateTime> timestamp,
                                   @LookupField(name = "deliveryStatus") Set<DeliveryStatus> deliveryStatuses,
                                   @LookupField(name = "providerStatus") String providerStatus,
                                   @LookupField(name = "motechId") String motechId,
                                   @LookupField(name = "providerId") String providerId,
                                   @LookupField(name = "errorMessage") String errorMessage);

}
