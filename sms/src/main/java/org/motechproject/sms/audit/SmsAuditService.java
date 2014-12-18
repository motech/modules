package org.motechproject.sms.audit;

import java.util.List;

/**
 * Reading and writing to the SMS audit log
 */
public interface SmsAuditService {

    /**
     * Finds and returns all <code>SmsRecord</code> entries in the sms log.
     *
     * @return all sms records in the sms log
     */
    List<SmsRecord> findAllSmsRecords();

    /**
     * Finds and returns all <code>SmsRecords</code> entries matching the specified
     * search criteria.
     *
     * @return all sms records matching the provided criteria
     */
    SmsRecords findAllSmsRecords(SmsRecordSearchCriteria criteria);

    /**
     * Returns the count of <code>SmsRecords</code> entries matching the specified
     * search criteria.
     *
     * @return the count of sms records matching the provided criteria
     */
    long countAllSmsRecords(SmsRecordSearchCriteria criteria);
}
