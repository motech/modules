package org.motechproject.commcare.domain.report;


import java.util.List;
import java.util.Map;

/**
 * Represents a single CommCareHQ report data. It's a part of the MOTECH model.
 */
public class ReportDataInfo {

    private List<ReportDataColumn> columns;
    private List<Map<String, String>> data;
    private String nextPage;
    private Integer totalRecords;

    public List<ReportDataColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ReportDataColumn> columns) {
        this.columns = columns;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public void setData(List<Map<String, String>> data) {
        this.data = data;
    }

    public String getNextPage() {
        return nextPage;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }
}
