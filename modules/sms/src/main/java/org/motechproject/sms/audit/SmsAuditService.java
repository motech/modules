package org.motechproject.sms.audit;

import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.audit.SmsRecordSearchCriteria;
import org.motechproject.sms.audit.SmsRecords;

import java.util.List;

/**
 * Reading and writing to the SMS audit log
 */
public interface SmsAuditService {

    void log(SmsRecord smsRecord);

    List<SmsRecord> findAllSmsRecords();

    SmsRecords findAllSmsRecords(SmsRecordSearchCriteria criteria);

}
