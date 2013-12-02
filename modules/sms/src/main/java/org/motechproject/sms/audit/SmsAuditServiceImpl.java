package org.motechproject.sms.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Reading and writing to the SMS audit log
 */
@Service
public class SmsAuditServiceImpl implements SmsAuditService {

    private AllSmsRecords allSmsRecords;
    private Logger logger = LoggerFactory.getLogger(SmsAuditServiceImpl.class);

    @Autowired
    public SmsAuditServiceImpl(AllSmsRecords allSmsRecords) {
        this.allSmsRecords = allSmsRecords;
    }

    @Override
    public void log(SmsRecord smsRecord) {
        logger.info(smsRecord.toString());
        allSmsRecords.add(smsRecord);
    }

    public List<SmsRecord> findAllSmsRecords() {
        return allSmsRecords.getAll();
    }

    @Override
    public SmsRecords findAllSmsRecords(SmsRecordSearchCriteria criteria) {
        return allSmsRecords.findAllBy(criteria);
    }
}
