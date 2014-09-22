package org.motechproject.commcare.web.domain;

import org.motechproject.commcare.domain.CaseInfo;
import java.util.ArrayList;
import java.util.List;

public class CasesRecords {
    private Integer page; // page number
    private Integer total; // number of total pages
    private Integer records; //total number of records
    private List<CaseInfo> rows; // data to display

    public CasesRecords(Integer page, Integer total, Integer rows, List<CaseInfo> allRecords) {
        this.page = page;
        this.total = total;
        this.records = rows;
        this.rows = new ArrayList<>(allRecords);
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

    public List<CaseInfo> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return String.format("CasesRecords{page=%d, total=%d, records=%d, rows=%s}", page, total, records, rows);
    }
}
