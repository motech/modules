package org.motechproject.commcare.request.json;

import org.apache.commons.lang.StringUtils;
import org.apache.http.client.utils.URIBuilder;

import java.util.List;

/**
 * Base abstract class for storing common query parameters that might be used for fetching data from the CommCareHQ
 * server.
 */
public abstract class Request {

    private static final int DEFAULT_LIMIT = 20;

    private int limit;
    private int offset;

    /**
     * Builds a query parameters string based on the information stored in the instance of the class. The string can
     * then be attached to the request URL in order to retrieve filtered data from the CommCareHQ server.
     *
     * @return the string representation of the stored parameters
     */
    public abstract String toQueryString();

    /**
     * Sets the maximum number of records to return. Default value is 20, maximum is 100.
     *
     * @param limit  the maximum number of records to returns
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }

    /**
     * Returns the maximum number of records to return.
     *
     * @return the maximum number of records to return
     */
    public int getLimit() {
        return limit;
    }

    /**
     * Sets the number of records to offset in the results. Default value is 0.
     *
     * @param offset  the number of records to offset in the results
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Returns the number of records to offset in the results.
     *
     * @return the number of records to offset in the results
     */
    public int getOffset() {
        return offset;
    }

    protected String toQueryString(List<String> queryParams) {
        queryParams.add(concat("limit", limit < 1 ? DEFAULT_LIMIT : limit));
        queryParams.add(concat("offset", offset < 0 ? 0 : offset));

        return StringUtils.join(queryParams, "&");
    }

    protected String concat(String key, Object value) {
        return String.format("%s=%s", key, value.toString());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Request that = (Request) o;

        return toQueryString().equals(that.toQueryString());
    }

    @Override
    public int hashCode() {
        return toQueryString().hashCode();
    }

    public void addOtherQueryParams(URIBuilder uriBuilder) {
        uriBuilder.addParameter("limit", String.valueOf(limit < 1 ? DEFAULT_LIMIT : limit));
        uriBuilder.addParameter("offset", String.valueOf(offset < 0 ? 0 : offset));
    }
}
