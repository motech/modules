package org.motechproject.ivr.repository;

import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.service.MotechDataService;

import java.util.List;

import static org.motechproject.mds.util.Constants.Operators.MATCHES;
import static org.motechproject.mds.util.Constants.Operators.MATCHES_CASE_INSENSITIVE;

/**
 * MDS generated CallDetailRecord database queries
 */
public interface CallDetailRecordDataService extends MotechDataService<CallDetailRecord> {
    @Lookup
    List<CallDetailRecord> findByMotechCallId(@LookupField(name = "motechCallId", customOperator =
            MATCHES_CASE_INSENSITIVE) String motechCallId);

    @Lookup
    List<CallDetailRecord> findByProviderCallId(@LookupField(name = "providerCallId", customOperator =
            MATCHES_CASE_INSENSITIVE) String providerCallId);

    @Lookup
    List<CallDetailRecord> findByProviderCallIdAndCallStatus(
            @LookupField(name = "providerCallId", customOperator = MATCHES_CASE_INSENSITIVE) String providerCallId,
            @LookupField(name = "callStatus", customOperator = MATCHES_CASE_INSENSITIVE) String callStatus);

    @Lookup
    List<CallDetailRecord> findByExactProviderCallId(@LookupField(name = "providerCallId") String providerCallId);

    @Lookup
    List<CallDetailRecord> findByExactProviderCallId(@LookupField(name = "providerCallId") String providerCallId, QueryParams queryParams);

    @Lookup
    List<CallDetailRecord> findByExactCallStatus(@LookupField(name = "callStatus") String callStatus);

    @Lookup
    List<CallDetailRecord> findByCallStatus(@LookupField(name = "callStatus", customOperator = MATCHES_CASE_INSENSITIVE) String callStatus);

    @Lookup
    List<CallDetailRecord> findByMotechTimestamp(@LookupField(name = "motechTimestamp", customOperator = MATCHES) String motechTimestamp);

    @Lookup
    List<CallDetailRecord> findByMotechTimestampAndCallStatus(@LookupField(name = "motechTimestamp", customOperator = MATCHES) String motechTimestamp, @LookupField(name = "callStatus") String callStatus);


}
