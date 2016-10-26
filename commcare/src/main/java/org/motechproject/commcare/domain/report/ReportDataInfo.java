package org.motechproject.commcare.domain.report;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Represents a single CommCareHQ report data. It's a part of the MOTECH model.
 */
public class ReportDataInfo {

    private List<ReportDataColumn> columns;
    private List<Map<String, String>> data;
    private String nextPage;
    private Integer totalRecords;

    public ReportDataInfo() { }

    public ReportDataInfo(List<ReportDataColumn> columns, List<Map<String, String>> data, String nextPage, Integer totalRecords) {
        this.columns = columns;
        this.data = data;
        this.nextPage = nextPage;
        this.totalRecords = totalRecords;
    }

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

    @Override
    public int hashCode() {
        return Objects.hash(columns, data, nextPage, totalRecords);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

            if (!(o instanceof ReportDataInfo)) {
            return false;
        }

        ReportDataInfo other = (ReportDataInfo) o;

        return Objects.equals(columns, other.columns) && Objects.equals(data, other.data)
                && Objects.equals(nextPage, other.nextPage) && Objects.equals(totalRecords, other.totalRecords);
    }

}
