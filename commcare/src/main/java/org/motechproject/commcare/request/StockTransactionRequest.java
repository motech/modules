package org.motechproject.commcare.request;

import org.motechproject.commcare.request.json.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores all possible query parameters that might be used while fetching stock transactions from the CommCareHQ server.
 */
public class StockTransactionRequest extends Request {

    private String sectionId;
    private String caseId;
    private String startDate;
    private String endDate;

    @Override
    public String toQueryString() {

        List<String> queryParams = new ArrayList<>();

        if (sectionId != null) {
            queryParams.add(concat("section_id", sectionId));
        }
        if (caseId != null) {
            queryParams.add(concat("case_id", caseId));
        }
        if (startDate != null) {
            queryParams.add(concat("start_date", startDate));
        }
        if (endDate != null) {
            queryParams.add(concat("end_date", endDate));
        }

        return toQueryString(queryParams);
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
     * Sets the ID of the section to {@code sectionId}.
     *
     * @param sectionId  the ID of the section
     */
    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    /**
     * Sets the beginning of the allowed period for transaction dates.
     *
     * @param startDate  the beginning of the allowed period for transaction dates
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * Sets the finish of the allowed period for transaction dates.
     *
     * @param endDate  the finish of the allowed period for transaction dates
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
