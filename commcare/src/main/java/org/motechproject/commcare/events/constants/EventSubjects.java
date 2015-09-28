package org.motechproject.commcare.events.constants;

/**
 * Utility class for storing event subjects.
 */
public final class EventSubjects {

    private static final String BASE_SUBJECT = "org.motechproject.commcare.api.";

    public static final String CASE_EVENT = BASE_SUBJECT + "case";

    public static final String MALFORMED_CASE_EXCEPTION = BASE_SUBJECT + "exception";

    public static final String FORM_STUB_EVENT = BASE_SUBJECT + "formstub";

    public static final String FORM_STUB_FAIL_EVENT = FORM_STUB_EVENT + ".failed";

    public static final String FORMS_EVENT = BASE_SUBJECT + "forms";

    public static final String FORMS_FAIL_EVENT = FORMS_EVENT + ".failed";

    public static final String RECEIVED_STOCK_TRANSACTION = BASE_SUBJECT + "receivedStockTransaction";

    public static final String QUERY_STOCK_LEDGER = BASE_SUBJECT + "queryStockLedger";

    public static final String SCHEMA_CHANGE_EVENT = BASE_SUBJECT + "schemachange";

    public static final String DEVICE_LOG_EVENT = BASE_SUBJECT + "devicelog";

    public static final String CONFIG_CREATED = BASE_SUBJECT + "configcreated";

    public static final String CONFIG_DELETED = BASE_SUBJECT + "configdeleted";

    public static final String CONFIG_UPDATED = BASE_SUBJECT + "configupdated";

    /**
     * Utility class, should not be initiated.
     */
    private EventSubjects() {
    }
}
