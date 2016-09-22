package org.motechproject.commcare.domain.report;

import java.util.List;

/**
 * Represents a single CommCareHQ report. It's a part of the MOTECH model.
 */
public class ReportMetadataInfo {

    private String id;
    private String title;
    private List<ReportMetadataColumn> columns;
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