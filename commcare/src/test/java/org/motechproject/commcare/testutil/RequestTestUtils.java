package org.motechproject.commcare.testutil;

import org.motechproject.commcare.request.StockTransactionRequest;

/**
 * Utility class for preparing sample stock transactions request.
 */
public class RequestTestUtils {

    public static final String CASE_ID = "caseId";
    public static final String SECTION_ID = "s1";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";

    /**
     * Prepares a sample stock transaction request.
     *
     * @return the sample request
     */
    public static StockTransactionRequest prepareRequest() {

        StockTransactionRequest request = new StockTransactionRequest();

        request.setCaseId(CASE_ID);
        request.setSectionId(SECTION_ID);
        request.setStartDate(START_DATE);
        request.setEndDate(END_DATE);

        return request;
    }
}
