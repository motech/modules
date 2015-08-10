package org.motechproject.commcare.web.domain;

/**
 * Settings used by jqGrid to properly show cases list.
 */
public class GridSettings {

    private Integer rows;
    private Integer page;
    private String sortColumn;
    private String sortDirection;
    private String filter;
    private String caseName;
    private String dateModifiedStart;
    private String dateModifiedEnd;

    /**
     * Creates an instance of the {@link GridSettings} class.
     */
    public GridSettings() {
    }

    /**
     * Determines which filter should be set based on the information stored in this object.
     */
    public void determineFilter() {
        if (!getCaseName().isEmpty()) {
            setFilter("filterByAll");
        } else if (!getDateModifiedStart().isEmpty() || !getDateModifiedEnd().isEmpty()) {
            setFilter("filterByDateModified");
        } else if (!getCaseName().isEmpty() && (!getDateModifiedStart().isEmpty() || !getDateModifiedEnd().isEmpty())) {
            setFilter("filerByName");
        } else {
            setFilter("");
        }
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public String getSortColumn() {
        return sortColumn;
    }

    public void setSortColumn(String sortColumn) {
        this.sortColumn = sortColumn;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }

    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public String getDateModifiedStart() {
        return dateModifiedStart;
    }

    public void setDateModifiedStart(String dateModifiedStart) {
        this.dateModifiedStart = dateModifiedStart;
    }

    public String getDateModifiedEnd() {
        return dateModifiedEnd;
    }

    public void setDateModifiedEnd(String dateModifiedEnd) {
        this.dateModifiedEnd = dateModifiedEnd;
    }
}
