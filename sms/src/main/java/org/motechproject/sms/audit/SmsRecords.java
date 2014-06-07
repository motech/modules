package org.motechproject.sms.audit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SMS audit records from the database
 */
public class SmsRecords implements Serializable {

    private static final long serialVersionUID = -1584588569625856505L;
    private int count;
    private List<SmsRecord> records;

    public SmsRecords() {
        this.count = 0;
        this.records = new ArrayList<>();
    }

    public SmsRecords(int count, List<SmsRecord> records) {
        this.count = count;
        this.records = records;
    }

    public int getCount() {
        return count;
    }

    public List<SmsRecord> getRecords() {
        return records;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setRecords(List<SmsRecord> records) {
        this.records = records;
    }
}
