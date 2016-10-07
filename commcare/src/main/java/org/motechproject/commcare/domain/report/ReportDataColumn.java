package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Represents a single column report data.
 */
public class ReportDataColumn {

    @SerializedName("header")
    private String header;

    @SerializedName("slug")
    private String slug;

    @SerializedName("expand_column_value")
    private String expandColumnValue;

    public ReportDataColumn() { }

    public ReportDataColumn(String header, String slug, String expandColumnValue) {
        this.header = header;
        this.slug = slug;
        this.expandColumnValue = expandColumnValue;
    }

    public String getHeader() {
        return header;
    }

    public String getSlug() {
        return slug;
    }

    public String getExpandColumnValue() {
        return expandColumnValue;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public void setExpandColumnValue(String expandColumnValue) {
        this.expandColumnValue = expandColumnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(header, slug, expandColumnValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ReportDataColumn)) {
            return false;
        }

        ReportDataColumn other = (ReportDataColumn) o;

        return Objects.equals(header, other.header) && Objects.equals(slug, other.slug)
                && Objects.equals(expandColumnValue, other.expandColumnValue);
    }
}
