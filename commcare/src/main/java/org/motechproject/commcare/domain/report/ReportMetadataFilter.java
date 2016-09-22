package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;

/**
 * Represents a single CommCareHQ {@link ReportMetadataInfo} filter.
 */
public class ReportMetadataFilter {

    @SerializedName("datatype")
    private String datatype;

    @SerializedName("slug")
    private String slug;

    @SerializedName("type")
    private String type;

    public String getDatatype() {
        return datatype;
    }

    public void setDatatype(String datatype) {
        this.datatype = datatype;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

}