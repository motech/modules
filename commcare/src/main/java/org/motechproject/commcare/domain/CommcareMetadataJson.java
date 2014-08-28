package org.motechproject.commcare.domain;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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

    public String getNextPageQueryString() {
        return nextPageQueryString;
    }

    public int getOffset() {
        return offset;
    }

    public String getPreviousPageQueryString() {
        return previousPageQueryString;
    }

    public int getTotalCount() {
        return totalCount;
    }
}
