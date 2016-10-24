package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import org.motechproject.commcare.domain.report.constants.FilterDataType;
import org.motechproject.commcare.domain.report.constants.FilterType;

import java.io.Serializable;
import java.util.Objects;

/**
 * Represents a single CommCareHQ {@link ReportMetadataInfo} filter.
 */
public class ReportMetadataFilter implements Serializable {

    private static final long serialVersionUID = 8994777814607875504L;

    @Expose
    @SerializedName("datatype")
    private FilterDataType datatype;

    @Expose
    @SerializedName("slug")
    private String slug;

    @Expose
    @SerializedName("type")
    private FilterType type;

    public ReportMetadataFilter() { }

    public ReportMetadataFilter(FilterDataType datatype, String slug, FilterType type) {
        this.datatype = datatype;
        this.slug = slug;
        this.type = type;
    }

    public FilterDataType getDatatype() {
        return datatype;
    }

    public void setDatatype(FilterDataType datatype) {
        this.datatype = datatype;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public FilterType getType() {
        return type;
    }

    public void setType(FilterType type) {
        this.type = type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(datatype, slug, type);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ReportMetadataFilter)) {
            return false;
        }

        ReportMetadataFilter other = (ReportMetadataFilter) o;

        return Objects.equals(datatype, other.datatype) && Objects.equals(slug, other.slug) && Objects.equals(type, other.type);
    }

}
