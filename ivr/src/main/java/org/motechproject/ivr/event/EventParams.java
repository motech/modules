package org.motechproject.ivr.event;

import org.motechproject.ivr.domain.CallDetailRecord;

import java.util.HashMap;
import java.util.Map;

/**
 * Possible Event payloads (ie: params)
 */
public final class EventParams {

    private EventParams() { }

    /**
     * date & time when this event happened on the Motech side
     */
    public static final String MOTECH_TIMESTAMP = "motech_timestamp";
    /**
     * date & time when this event happened at the provider's end
     */
    public static final String PROVIDER_TIMESTAMP = "provider_timestamp";
    /**
     * Config that was used for this message
     */
    public static final String CONFIG = "config";
    /**
     * Phone number that the call was issued from
     */
    public static final String FROM = "from";
    /**
     * Call recipient (phone number)
     */
    public static final String TO = "to";
    /**
     * Call direction  -  INBOUND (MO) or OUTBOUND (MT) or UNKNOWN
     */
    public static final String CALL_DIRECTION = "call_direction";
    /**
     * Call status
     */
    public static final String CALL_STATUS = "call_status";
    /**
     * MOTECH call id - a MOTECH-generated GUID
     */
    public static final String MOTECH_CALL_ID = "motech_call_id";
    /**
     * Provider call id - provider-generated id
     */
    public static final String PROVIDER_CALL_ID = "provider_call_id";
    /**
     * Provider extra data
     */
    public static final String PROVIDER_EXTRA_DATA = "provider_extra_data";

    /**
     * Provider JSON response
     */
    public static final String PROVIDER_JSON_RESPONSE = "provider_json_response";

    /**
     * Create a MOTECH event parameters map payload from a given {@link org.motechproject.ivr.domain.CallDetailRecord}.
     *
     * @param callDetailRecord the call detail record used for constructing the event payload
     * @return the payload corresponding to the provided call detail record
     */
    public static Map<String, Object> eventParamsFromCallDetailRecord(CallDetailRecord callDetailRecord) {
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventParams.MOTECH_TIMESTAMP, callDetailRecord.getMotechTimestamp());
        eventParams.put(EventParams.PROVIDER_TIMESTAMP, callDetailRecord.getProviderTimestamp());
        eventParams.put(EventParams.CONFIG, callDetailRecord.getConfigName());
        eventParams.put(EventParams.FROM, callDetailRecord.getFrom());
        eventParams.put(EventParams.TO, callDetailRecord.getTo());
        eventParams.put(EventParams.CALL_DIRECTION, callDetailRecord.getCallDirection());
        eventParams.put(EventParams.CALL_STATUS, callDetailRecord.getCallStatus());
        eventParams.put(EventParams.MOTECH_CALL_ID, callDetailRecord.getMotechCallId());
        eventParams.put(EventParams.PROVIDER_CALL_ID, callDetailRecord.getProviderCallId());
        eventParams.put(EventParams.PROVIDER_EXTRA_DATA, callDetailRecord.getProviderExtraData());
        return eventParams;
    }
}
