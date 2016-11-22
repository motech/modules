package org.motechproject.commcare.request.json;

import org.apache.http.client.utils.URIBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all possible query parameters that might be used while fetching cases from the CommCareHQ server.
 */
public class CaseRequest extends Request {

    private String userId;
    private String ownerId;
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
        if (ownerId != null) {
            queryParams.add(concat("owner_id", ownerId));
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

    public void addQueryParams(URIBuilder uriBuilder) {
        if (userId != null) {
            uriBuilder.addParameter("user_id", userId);
        }
        if (ownerId != null) {
            uriBuilder.addParameter("owner_id", ownerId);
        }
        if (caseId != null) {
            uriBuilder.addParameter("case_id", caseId);
        }
        if (type != null) {
            uriBuilder.addParameter("type", type);
        }
        if (caseName != null) {
            uriBuilder.addParameter("case_name", caseName);
        }
        if (dateModifiedStart != null) {
            uriBuilder.addParameter("date_modified_start", dateModifiedStart);
        }
        if (dateModifiedEnd != null) {
            uriBuilder.addParameter("date_modified_end", dateModifiedEnd);
        }

        addOtherQueryParams(uriBuilder);
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
     * Sets the ID of the owner to {@code ownerId}.
     *
     * @param ownerId  the ID of the owner
     */
    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
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
