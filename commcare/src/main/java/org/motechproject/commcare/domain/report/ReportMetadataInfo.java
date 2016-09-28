package org.motechproject.commcare.domain.report;

import java.util.List;
import java.util.Objects;

/**
 * Represents a single CommCareHQ report. It's a part of the MOTECH model.
 */
public class ReportMetadataInfo {

    private String id;
    private String title;
    private List<ReportMetadataColumn> columns;
    private List<ReportMetadataFilter> filters;

    public ReportMetadataInfo() {}

    public ReportMetadataInfo(String id, String title, List<ReportMetadataColumn> columns, List<ReportMetadataFilter> filters) {
        this.id = id;
        this.title = title;
        this.columns = columns;
        this.filters = filters;
    }


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

    @Override
    public int hashCode() {
        return Objects.hash(id, title, columns, filters);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ReportMetadataInfo)) {
            return false;
        }

        ReportMetadataInfo other = (ReportMetadataInfo) o;

        return Objects.equals(id, other.id) && Objects.equals(title, other.title)
                && Objects.equals(columns, other.columns) && Objects.equals(filters, other.filters);
    }
}