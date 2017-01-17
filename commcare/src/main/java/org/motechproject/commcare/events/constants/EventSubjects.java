package org.motechproject.commcare.events.constants;

/**
 * Utility class for storing event subjects and providing simple subject parsing abilities.
 */
public final class EventSubjects {

    private static final String BASE_SUBJECT = "org.motechproject.commcare.api.";

    public static final String CASE_EVENT = BASE_SUBJECT + "case";
    public static final String REPORT_EVENT = BASE_SUBJECT + "report";
    public static final String MALFORMED_CASE_EXCEPTION = BASE_SUBJECT + "exception";
    public static final String FORM_STUB_EVENT = BASE_SUBJECT + "formstub";
    public static final String FORM_STUB_FAIL_EVENT = FORM_STUB_EVENT + ".failed";
    public static final String FORMS_EVENT = BASE_SUBJECT + "forms";
    public static final String FORMS_FAIL_EVENT = FORMS_EVENT + ".failed";
    public static final String CASES_FAIL_EVENT = CASE_EVENT + ".failed";
    public static final String RECEIVED_STOCK_TRANSACTION = BASE_SUBJECT + "receivedStockTransaction";
    public static final String QUERY_STOCK_LEDGER = BASE_SUBJECT + "queryStockLedger";
    public static final String IMPORT_FORMS = BASE_SUBJECT + "importForms";
    public static final String SCHEMA_CHANGE_EVENT = BASE_SUBJECT + "schemachange";
    public static final String DEVICE_LOG_EVENT = BASE_SUBJECT + "devicelog";
    public static final String CONFIG_CREATED = BASE_SUBJECT + "configcreated";
    public static final String CONFIG_DELETED = BASE_SUBJECT + "configdeleted";
    public static final String CONFIG_UPDATED = BASE_SUBJECT + "configupdated";
    public static final String CREATE_CASE = BASE_SUBJECT + "createCase";
    public static final String UPDATE_CASE = BASE_SUBJECT + "updateCase";
    public static final String SUBMIT_FORM = BASE_SUBJECT + "submitForm";
    public static final String RECEIVED_REPORT = BASE_SUBJECT + "receivedReport";

    public static String getConfigName(String subject) {
        return subject.substring(subject.lastIndexOf('.') + 1);
    }

    /**
     * Utility class, should not be initiated.
     */
    private EventSubjects() {
    }
}
