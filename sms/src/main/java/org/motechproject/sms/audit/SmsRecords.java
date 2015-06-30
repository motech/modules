package org.motechproject.sms.audit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SMS audit records from the database. Represents a subset of all records.
 */
public class SmsRecords implements Serializable {

    private static final long serialVersionUID = -1584588569625856505L;

    /**
     * The total number of SMS records in the current context.
     */
    private int count;

    /**
     * The records to display. The number of records should be less than or equal to the total record count.
     */
    private List<SmsRecord> records;

    /**
     * Constructs an instance with no records and a total count of zero.
     */
    public SmsRecords() {
        this.count = 0;
        this.records = new ArrayList<>();
    }

    /**
     * Constructs an instance from the given records and count.
     * @param count the total count of records in this context
     * @param records the subset of records
     */
    public SmsRecords(int count, List<SmsRecord> records) {
        this.count = count;
        this.records = records;
    }

    /**
     * @return the total number of SMS records in the current context
     */
    public int getCount() {
        return count;
    }

    /**
     * @param count the total number of SMS records in the current context
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * @return the actual subset of records
     */
    public List<SmsRecord> getRecords() {
        return records;
    }

    /**
     * @param records the actual subset of records
     */
    public void setRecords(List<SmsRecord> records) {
        this.records = records;
    }
}
