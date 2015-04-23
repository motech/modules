package org.motechproject.csd.constants;

public final class CSDEventKeys {

    public static final String XML_URL = "xml_url";

    public static final String EVENT_PARAMETERS = "event_parameters";
    public static final String START_DATE = "start_date";
    public static final String END_DATE = "end_date";
    public static final String PERIOD = "period";

    public static final String CONSUME_XML_EVENT_BASE = "consume_xml.";
    public static final String CONSUME_XML_EVENT_WILDCARD = "consume_xml.*";

    public static final String CSD_UPDATE_BASE = "csd_update.";
    public static final String CSD_UPDATE_CUSTOM = CSD_UPDATE_BASE + "custom";
    public static final String CSD_UPDATE_SCHEDULED = CSD_UPDATE_BASE + "scheduled";
    public static final String CSD_UPDATE_TASK = CSD_UPDATE_BASE + "task";

    public static final String UPDATE_DATE = "update_date";

    public static final String CSD_TASK_REST_UPDATE = "csd_task_rest_update";
    public static final String CSD_TASK_SOAP_UPDATE = "csd_task_soap_update";

    public static final String LAST_MODIFIED = "last_modified";

    private CSDEventKeys() {
    }
}
