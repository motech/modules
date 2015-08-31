package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Representation of the CommCare metadata, present in various Data APIs exposed by CommCare.
 */
public class CommcareMetadataJson {

    @Expose
    @SerializedName("limit")
    private int limit;

    @Expose
    @SerializedName("next")
    private String nextPageQueryString;

    @Expose
    @SerializedName("offset")
    private int offset;

    @Expose
    @SerializedName("previous")
    private String previousPageQueryString;

    @Expose
    @SerializedName("total_count")
    private int totalCount;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getNextPageQueryString() {
        return nextPageQueryString;
    }

    public void setNextPageQueryString(String nextPageQueryString) {
        this.nextPageQueryString = nextPageQueryString;
    }

    public int getOffset() {
        return offset;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public String getPreviousPageQueryString() {
        return previousPageQueryString;
    }

    public void setPreviousPageQueryString(String previousPageQueryString) {
        this.previousPageQueryString = previousPageQueryString;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}
