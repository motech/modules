package org.motechproject.commcare.web.domain;

import org.motechproject.commcare.domain.CaseInfo;
import java.util.ArrayList;
import java.util.List;

/**
 * Wrapper class for a case list request. Includes page number, total number of pages, total number of records and the
 * data to display itself. It can be used by the UI for displaying cases.
 */
public class CasesRecords {

    private Integer page;
    private Integer total;
    private Integer records;
    private List<CaseInfo> rows;

    /**
     * Creates an instance of the {@link CasesRecords} class based on the given information.
     *
     * @param page  the number of the page
     * @param total  the total number of pages
     * @param rows  the total number of records
     * @param allRecords  the data to display
     */
    public CasesRecords(Integer page, Integer total, Integer rows, List<CaseInfo> allRecords) {
        this.page = page;
        this.total = total;
        this.records = rows;
        this.rows = new ArrayList<>(allRecords);
    }

    /**
     * Returns the number of the page.
     *
     * @return the number of the page
     */
    public Integer getPage() {
        return page;
    }

    /**
     * Returns the total number of pages.
     *
     * @return the total number of pages
     */
    public Integer getTotal() {
        return total;
    }

    /**
     * Returns the total number of records.
     *
     * @return the total number of records
     */
    public Integer getRecords() {
        return records;
    }

    /**
     * Returns a list of cases to display.
     *
     * @return the list of cases to display
     */
    public List<CaseInfo> getRows() {
        return rows;
    }

    @Override
    public String toString() {
        return String.format("CasesRecords{page=%d, total=%d, records=%d, rows=%s}", page, total, records, rows);
    }
}
