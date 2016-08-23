package org.motechproject.commcare.testutil;

import org.joda.time.DateTime;
import org.motechproject.commcare.request.StockTransactionRequest;
import org.motechproject.commcare.util.CommcareParamHelper;

/**
 * Utility class for preparing sample stock transactions request.
 */
public class RequestTestUtils {

    public static final String CASE_ID = "caseId";
    public static final String SECTION_ID = "s1";
    public static final DateTime START_DATE = new DateTime(2012, 11, 1, 10, 20, 33);
    public static final DateTime END_DATE = new DateTime(2012, 12, 24, 1, 1, 59);

    /**
     * Prepares a sample stock transaction request.
     *
     * @return the sample request
     */
    public static StockTransactionRequest prepareRequest() {

        StockTransactionRequest request = new StockTransactionRequest();

        request.setCaseId(CASE_ID);
        request.setSectionId(SECTION_ID);
        request.setStartDate(CommcareParamHelper.printObjectAsDateTime(START_DATE));
        request.setEndDate(CommcareParamHelper.printObjectAsDateTime(END_DATE));

        return request;
    }
}
