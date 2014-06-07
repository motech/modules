package org.motechproject.ivr.service;

import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.repository.AllCallDetailRecords;
import org.motechproject.ivr.service.contract.CallRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service used for persisting call detail records.
 */
@Service("callRecordsService")
public class CallRecordsServiceImpl implements CallRecordsService {

    private AllCallDetailRecords allCallDetailRecords;

    @Autowired
    public CallRecordsServiceImpl(AllCallDetailRecords allCallDetailRecords) {
        this.allCallDetailRecords = allCallDetailRecords;
    }

    public void add(CallDetailRecord callDetailRecord) {
        allCallDetailRecords.addOrUpdate(callDetailRecord);
    }

}
