package org.motechproject.commcare.domain.report;

import com.google.gson.annotations.SerializedName;

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

    public String getHeader(){
        return header;
    }

    public String getSlug(){
        return slug;
    }

    public String getExpandColumnValue(){
        return expandColumnValue;
    }

    public void setHeader(String header){
        this.header = header;
    }

    public void setSlug(String slug){
        this.slug = slug;
    }

    public void setExpandColumnValue(String expandColumnValue){
        this.expandColumnValue = expandColumnValue;
    }
}
