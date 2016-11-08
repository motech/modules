package org.motechproject.commcare.events.constants;

/**
 * Utility class for storing display names of the the task actions.
 */
public final class DisplayNames {

    public static final String QUERY_STOCK_LEDGER = "Query Stock Ledger";
    public static final String RETRIEVED_STOCK_TRANSACTION = "Retrieved Stock Transaction";

    public static final String CREATE_CASE = "Create Case";
    public static final String UPDATE_CASE = "Update Case";
    public static final String SUBMIT_FORM = "Submit Form";
    public static final String IMPORT_FORMS = "Import Forms";
    public static final String CREATE_REPORT = "Create Report";

    public static final String CONFIG_NAME = "commcare.configName";
    public static final String CASE_ID = "commcare.caseId";
    public static final String CASE_TYPE = "commcare.caseType";
    public static final String CASE_NAME = "commcare.caseName";
    public static final String CASE_PROPERTIES = "commcare.fieldValues";
    public static final String CLOSE_CASE = "commcare.closeCase";
    public static final String OWNER_ID = "commcare.ownerId";
    public static final String SECTION_ID = "commcare.sectionId";
    public static final String START_DATE = "commcare.startDate";
    public static final String END_DATE = "commcare.endDate";
    public static final String PRODUCT_ID = "commcare.productId";
    public static final String PRODUCT_NAME = "commcare.productName";
    public static final String QUANTITY = "commcare.quantity";
    public static final String STOCK_ON_HAND = "commcare.stockOnHand";
    public static final String TRANSACTION_DATE = "commcare.transactionDate";
    public static final String TYPE = "commcare.type";
    public static final String EXTRA_DATA = "commcare.extraData";

    /**
     * Utility class, should not be initiated.
     */
    private DisplayNames() {
    }
}
