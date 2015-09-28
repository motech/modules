package org.motechproject.commcare.request.json;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all possible query parameters that might be used while fetching cases from the CommCareHQ server.
 */
public class CaseRequest extends Request {

    private String userId;
    private String caseId;
    private String type;
    private String caseName;
    private String dateModifiedStart;
    private String dateModifiedEnd;

    @Override
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

        return toQueryString(queryParams);
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
}
