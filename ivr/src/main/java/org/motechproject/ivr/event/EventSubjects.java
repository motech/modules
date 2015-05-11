package org.motechproject.ivr.event;

/**
 * Event subjects
 */
public final class EventSubjects {

    /**
     * Signals a status update from the provider.
     */
    public static final String CALL_STATUS = "ivr_call_status";

    /**
     * Signals a request for an IVR template.
     */
    public static final String TEMPLATE_REQUEST = "ivr_template_request";

    /**
     * Signals that a call was initiated by MOTECH.
     */
    public static final String CALL_INITIATED = "ivr_call_initiated";

    /**
     * The event subject used for initiating a call. The IVR module listens to this subject.
     */
    public static final String INITIATE_CALL = "ivr_initiate_call";

    private EventSubjects() { }
}
