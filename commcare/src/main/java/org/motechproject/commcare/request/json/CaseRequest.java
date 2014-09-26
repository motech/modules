package org.motechproject.commcare.request.json;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    public void setDateModifiedStart(String dateModifiedStart) {
        this.dateModifiedStart = dateModifiedStart;
    }

    public void setDateModifiedEnd(String dateModifiedEnd) {
        this.dateModifiedEnd = dateModifiedEnd;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public int getLimit() {
        return limit;
    }

    public void setOffset(int offset) {
        this.offset = offset;
    }

    public int getOffset() {
        return offset;
    }

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

    private String concat(String key, Object value) {
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

        CaseRequest that = (CaseRequest) o;

        return toQueryString().equals(that.toQueryString());
    }

    @Override
    public int hashCode() {
        return toQueryString().hashCode();
    }
}
