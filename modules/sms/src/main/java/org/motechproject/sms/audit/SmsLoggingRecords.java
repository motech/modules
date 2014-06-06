package org.motechproject.sms.audit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SMS record collection for the logging UI
 */
public class SmsLoggingRecords implements Serializable {

    private static final long serialVersionUID = -6205245415683301270L;

    private final Integer page;
    private final Integer total;
    private final Integer records;
    private final List<SmsLoggingRecord> rows;

    public SmsLoggingRecords(Integer page, Integer rows, SmsRecords smsRecords) {
        this.page = page;
        records = smsRecords.getCount();
        total = records <= rows ? 1 : (records / rows) + 1;

        List<SmsLoggingRecord> smsLoggingRecords = new ArrayList<>();
        for (SmsRecord smsRecord : smsRecords.getRecords()) {
            smsLoggingRecords.add(new SmsLoggingRecord(smsRecord));
        }

        this.rows = smsLoggingRecords;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getRecords() {
        return records;
    }

    public List<SmsLoggingRecord> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return String.format("SmsLoggingRecords{page=%d, total=%d, records=%d, rows=%s}", page, total, records, rows);
    }
}
