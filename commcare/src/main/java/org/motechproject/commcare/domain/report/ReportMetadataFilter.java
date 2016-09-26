package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;
import org.motechproject.commcare.domain.report.constants.FilterDataType;
import org.motechproject.commcare.domain.report.constants.FilterType;

/**
 * Represents a single CommCareHQ {@link ReportMetadataInfo} filter.
 */
public class ReportMetadataFilter {

    @SerializedName("datatype")
    private FilterDataType datatype;

    @SerializedName("slug")
    private String slug;

    @SerializedName("type")
    private FilterType type;

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

}