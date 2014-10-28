package org.motechproject.commcare.events.constants;

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

    public static final String META_INSTANCE_ID = "/data/meta/instanceid";
    public static final String META_USER_ID = "/data/meta/userid";
    public static final String META_DEVICE_ID = "/data/meta/deviceid";
    public static final String META_USERNAME = "/data/meta/username";
    public static final String META_APP_VERSION = "/data/meta/appversion";
    public static final String META_TIME_START = "/data/meta/timestart";
    public static final String META_TIME_END = "/data/meta/timeend";

    public static final String REGISTERED_CASE_TYPE = "/data/case/create/case_type";
    public static final String REGISTERED_CASE_NAME = "/data/case/create/case_name";


    //FullFormsExceptionEvent
    public static final String FAILED_FORM_MESSAGE = "failedMessage";

    private EventDataKeys() {
    }
}
