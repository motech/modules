package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Represents a single CommCareHQ report. It's a part of the CommCareHQ model.
 */
public class ReportMetadataJson {

    @SerializedName("title")
    private String title;

    @SerializedName("id")
    private String id;

    @SerializedName("resource")
    private String resource;

    @SerializedName("columns")
    private List<ReportMetadataColumn> columns;

    @SerializedName("filters")
    private List<ReportMetadataFilter> filters;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getResource() {
        return resource;
    }

    public void setResource() {
        this.resource = resource;
    }

    public List<ReportMetadataColumn> getColumns() {
        return columns;
    }

    public void setColumns(List<ReportMetadataColumn> columns) {
        this.columns = columns;
    }

    public List<ReportMetadataFilter> getFilters() {
        return filters;
    }

    public void setFilters(List<ReportMetadataFilter> filters) {
        this.filters = filters;
    }
}
