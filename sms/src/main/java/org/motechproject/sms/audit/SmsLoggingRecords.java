package org.motechproject.sms.audit;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * SMS record collection for the logging UI
 */
public class SmsLoggingRecords implements Serializable {

    private static final long serialVersionUID = -6205245415683301270L;

    private final Integer page; // page number
    private final Integer total; // number of rows per page
    private final Long records; // total number of records
    private final List<SmsLoggingRecord> rows; // data to display

    public SmsLoggingRecords(Integer page, Integer rows, Long totalRecords, SmsRecords smsRecords) {
        this.page = page;
        records = totalRecords;
        total = rows;

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

    public Long getRecords() {
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
