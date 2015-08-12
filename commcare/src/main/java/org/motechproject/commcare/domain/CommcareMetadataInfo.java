package org.motechproject.commcare.domain;

/**
 * Domain class representing case metadata, retrieved from CommCareHQ server.
 */
public class CommcareMetadataInfo {

    private int limit;
    private String nextPageQueryString;
    private int offset;
    private String previousPageQueryString;
    private int totalCount;

    public String getPreviousPageQueryString() {
        return previousPageQueryString;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public int getOffset() {
        return offset;
    }

    public String getNextPageQueryString() {
        return nextPageQueryString;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public void setNextPageQueryString(String nextPageQueryString) {
        this.nextPageQueryString = nextPageQueryString;
    }

    public void setPreviousPageQueryString(String previousPageQueryString) {
        this.previousPageQueryString = previousPageQueryString;
    }
}
