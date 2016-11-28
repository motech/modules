package org.motechproject.commcare.events.constants;

/**
 * Utility class for storing data keys for events.
 */
public final class EventDataKeys {

    //CaseEvent
    public static final String SERVER_MODIFIED_ON = "serverModifiedOn";
    public static final String CASE_ID = "caseId";
    public static final String USER_ID = "userId";
    public static final String API_KEY = "apiKey";
    public static final String DATE_MODIFIED = "dateModified";
    public static final String CASE_ACTION = "caseAction";
    public static final String FIELD_VALUES = "fieldValues";
    public static final String CASE_TYPE = "caseType";
    public static final String CASE_NAME = "caseName";
    public static final String OWNER_ID = "ownerId";
    public static final String CASE_DATA_XMLNS = "caseDataXmlns";
    public static final String CLOSE_CASE = "closeCase";

    //Malformed case xml exception event
    public static final String MESSAGE = "message";

    //FormStubEvent and FullFormsEvent
    public static final String RECEIVED_ON = "receivedOn";

    //FormStubEvent
    public static final String FORM_ID = "formId";
    public static final String CASE_IDS = "caseIds";

    //FullFormsEvent
    public static final String ELEMENT_NAME = "elementName";
    public static final String SUB_ELEMENTS = "subElements";
    public static final String ATTRIBUTES = "attributes";
    public static final String VALUE = "value";
    public static final String META_BASE_URL = "configBaseUrl";
    public static final String META_DOMAIN = "configDomain";

    public static final String META_INSTANCE_ID = "/data/meta/instanceID";
    public static final String META_USER_ID = "/data/meta/userID";
    public static final String META_DEVICE_ID = "/data/meta/deviceID";
    public static final String META_USERNAME = "/data/meta/username";
    public static final String META_APP_VERSION = "/data/meta/appVersion";
    public static final String META_TIME_START = "/data/meta/timeStart";
    public static final String META_TIME_END = "/data/meta/timeEnd";

    public static final String REGISTERED_CASE_TYPE = "/data/case/create/case_type";
    public static final String REGISTERED_CASE_NAME = "/data/case/create/case_name";

    //ConfigurationEvent
    public static final String CONFIG_NAME = "configName";
    public static final String CONFIG_VERIFIED = "configVerified";
    public static final String CONFIG_DOMAIN = "configDomain";
    public static final String CONFIG_BASE_URL = "configBaseUrl";

    //QueryStockLedgerEvent
    public static final String SECTION_ID = "section_id";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String EXTRA_DATA = "extra_data";

    //ReceivedStockTransactionEvent
    public static final String PRODUCT_ID = "product_id";
    public static final String PRODUCT_NAME = "product_name";
    public static final String QUANTITY = "quantity";
    public static final String STOCK_ON_HAND = "stock_on_hand";
    public static final String TRANSACTION_DATE = "transaction_date";
    public static final String TYPE = "type";

    //FullFormsExceptionEvent
    public static final String FAILED_FORM_MESSAGE = "failedMessage";
    public static final String FAILED_CASE_MESSAGE = "failed case import message";

    //ReportEvent
    public static final String REPORT_ID = "report_id";
    public static final String REPORT_NAME = "report_name";

    /**
     * Utility class, should not be initiated.
     */
    private EventDataKeys() {
    }
}
