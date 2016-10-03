package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Map;

/**
 * Represents a single CommCareHQ report data. It's a part of the CommCareHQ model.
 */
public class ReportDataResponseJson {

    @SerializedName("columns")
    private List<ReportDataColumn> columns;

    @SerializedName("data")
    private List<Map<String, String>> data;

    @SerializedName("next_page")
    private String nextPage;

    @SerializedName("total_records")
    private Integer totalRecords;

    public List<ReportDataColumn> getColumns() {
        return columns;
    }

    public List<Map<String, String>> getData() {
        return data;
    }

    public String getNextPage() {
        return nextPage;
    }

    public Integer getTotalRecords() {
        return totalRecords;
    }

    public void setColumns(List<ReportDataColumn> reportDataColumns) {
        this.columns = reportDataColumns;
    }

    public void setData(List<Map<String, String>> reportDataList) {
        this.data = reportDataList;
    }

    public void setNextPage(String nextPage) {
        this.nextPage = nextPage;
    }

    public void setTotalRecords(Integer totalRecords) {
        this.totalRecords = totalRecords;
    }

}
