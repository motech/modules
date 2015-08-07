package org.motechproject.commcare.request.json;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all possible query parameters that might be used while fetching cases from the CommCareHQ server.
 */
public class CaseRequest {

    private static final int DEFAULT_LIMIT = 20;

    private String userId;
    private String caseId;
    private String type;
    private String caseName;
    private String dateModifiedStart;
    private String dateModifiedEnd;
    private int limit;
    private int offset;

    /**
     * Builds a query parameters string based on the information stored in this instance of the class. The string can
     * then be attached to the request URL in order to retrieve filtered data from the CommCareHQ server.
     *
     * @return the string representation of the stored parameters
     */
    public String toQueryString() {
        List<String> queryParams = new ArrayList<>();
        if (userId != null) {
            queryParams.add(concat("user_id", userId));
        }
        if (caseId != null) {
            queryParams.add(concat("case_id", caseId));
        }
        if (type != null) {
            queryParams.add(concat("type", type));
        }
        if (caseName != null) {
            queryParams.add(concat("case_name", caseName));
        }
        if (dateModifiedStart != null) {
            queryParams.add(concat("date_modified_start", dateModifiedStart));
        }
        if (dateModifiedEnd != null) {
            queryParams.add(concat("date_modified_end", dateModifiedEnd));
        }
        queryParams.add(concat("limit", limit < 1 ? DEFAULT_LIMIT : limit));
        queryParams.add(concat("offset", offset < 0 ? 0 : offset));

        return StringUtils.join(queryParams, "&");
    }

    /**
     * Sets the ID of the user to {@code userId}.
     *
     * @param userId  the ID of the user
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Sets the ID of the case to {@code caseId}.
     *
     * @param caseId  the ID of the case
     */
    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    /**
     * Sets the type of the case to {@code type}.
     *
     * @param type  the type of the case
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Sets the name of the case to {@code caseName}.
     *
     * @param caseName  the name of the case
     */
    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    /**
     * Sets the beginning of the allowed modification period.
     *
     * @param dateModifiedStart  the start date of the allowed modification period
     */
    public void setDateModifiedStart(String dateModifiedStart) {
        this.dateModifiedStart = dateModifiedStart;
    }

    /**
     * Sets the finish of the allowed modification period.
     *
     * @param dateModifiedEnd  the end date of the allowed modification period
     */
    public void setDateModifiedEnd(String dateModifiedEnd) {
        this.dateModifiedEnd = dateModifiedEnd;
    }

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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CaseRequest that = (CaseRequest) o;

        return toQueryString().equals(that.toQueryString());
    }

    @Override
    public int hashCode() {
        return toQueryString().hashCode();
    }

    private String concat(String key, Object value) {
        return String.format("%s=%s", key, value.toString());
    }
}
