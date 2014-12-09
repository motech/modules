package org.motechproject.ivr.repository;

import org.motechproject.ivr.domain.CallStatus;
import org.motechproject.mds.annotations.Lookup;
import org.motechproject.mds.annotations.LookupField;
import org.motechproject.mds.service.MotechDataService;
import org.motechproject.ivr.domain.CallDetailRecord;

import java.util.List;

/**
 * MDS generated CallDetailRecord database queries
 */
public interface CallDetailRecordDataService extends MotechDataService<CallDetailRecord> {
    @Lookup
    List<CallDetailRecord> findByMotechCallId(@LookupField(name = "motechCallId") String motechCallId);

    @Lookup
    List<CallDetailRecord> findByProviderCallId(@LookupField(name = "providerCallId") String providerCallId);

    @Lookup
    List<CallDetailRecord> findByProviderCallIdAndCallStatus(@LookupField(name = "providerCallId") String providerCallId,
                                                             @LookupField(name = "callStatus") CallStatus callStatus);
}
