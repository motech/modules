package org.motechproject.sms.audit;

import java.util.List;

/**
 * Reading and writing to the SMS audit log
 */
public interface SmsAuditService {

    void log(SmsRecord smsRecord);

    List<SmsRecord> findAllSmsRecords();

    SmsRecords findAllSmsRecords(SmsRecordSearchCriteria criteria);

}
