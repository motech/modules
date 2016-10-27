package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a single CommCareHQ report. It's a part of the MOTECH model.
 */
public class ReportMetadataInfo implements Serializable {

    private static final long serialVersionUID = 1616570156650333507L;

    @Expose
    @SerializedName("id")
    private String id;

    @Expose
    @SerializedName("title")
    private String title;

    @Expose
    @SerializedName("resource")
    private String resource;

    @Expose
    @SerializedName("columns")
    private List<ReportMetadataColumn> columns;

    @Expose
    @SerializedName("filters")
    private List<ReportMetadataFilter> filters;

    public ReportMetadataInfo() { }

    public ReportMetadataInfo(String id, String title, List<ReportMetadataColumn> columns, List<ReportMetadataFilter> filters) {
        this.id = id;
        this.title = title;
        this.columns = columns;
        this.filters = filters;
    }

    public ReportMetadataInfo (String id, String title, String resource, List<ReportMetadataColumn> columns, List<ReportMetadataFilter> filters) {
        this(id, title, columns, filters);
        this.resource = resource;
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

    public String getResource () {
        return resource;
    }

    public void setResource (String resource) {
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
