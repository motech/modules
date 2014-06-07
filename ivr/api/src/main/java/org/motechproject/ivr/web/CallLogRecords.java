package org.motechproject.ivr.web;

import org.motechproject.ivr.domain.CallDetailRecord;

import java.io.Serializable;
import java.util.List;

/**
 * Call log Records hold data about the call records and how to separate them
 * (total number of pages, current page, and total number of records), as well
 * as the records for the current page.
 */
public class CallLogRecords implements Serializable {


    private static final long serialVersionUID = -4969442305164225394L;
    private final Integer page; //current page
    private final Integer total; //total number of pages
    private final Integer records; //total number of records
    private final List<CallDetailRecord> rows; //rows for current page

    /**
     * Constructs a calllog record
     * @param page current page of the record to display
     * @param rows number of rows to display per page
     * @param callDetailRecords list of all records
     */
    public CallLogRecords(Integer page, Integer rows, List<CallDetailRecord> callDetailRecords) {
        this.page = page;
        records = callDetailRecords.size();
        int subtotal = 1;
        if (records > rows) {
            subtotal = records / rows;
            //if the number of records is not an exact multiple
            //of the number of rows, need to add 1 to number of rows
            if (records % rows != 0) {
                subtotal++;
            }
        }
        total = subtotal;

        //start is the first index of the list of records that will be displayed
        Integer start = rows * (page > total ? total : page) - rows;
        Integer count = start + rows;
        Integer end = count > records ? records : count;
        this.rows = callDetailRecords.subList(start, end);
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

    public List<CallDetailRecord> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return String.format("CallLogRecords{page=%d, total=%d, records=%d, rows=%s}", page, total, records, rows);
    }
}
