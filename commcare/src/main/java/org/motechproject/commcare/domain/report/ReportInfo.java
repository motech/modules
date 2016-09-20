package org.motechproject.commcare.domain.report;

import java.util.List;

/**
 * Represents a single CommCareHQ report. It's a part of the MOTECH model.
 */
public class ReportInfo {

    private String id;
    private String title;
    private List<Column> columns;
    private List<Filter> filters;

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

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    public List<Filter> getFilters() {
        return filters;
    }

    public void setFilters(List<Filter> filters) {
        this.filters = filters;
    }
}