package org.motechproject.rapidpro.util;


import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.event.MotechEvent;
import org.motechproject.rapidpro.constant.EventParameters;
import org.motechproject.rapidpro.constant.EventSubjects;
import org.motechproject.rapidpro.constant.WebHookTypes;
import org.motechproject.rapidpro.exception.WebHookParserException;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public final class WebHookParser {

    private static final String ENCODING = "UTF-8";
    private static final String RP_EVENT = "event";
    private static final String RP_RELAYER = "relayer";
    private static final String RP_RELAYER_PHONE = "relayer_phone";
    private static final String RP_SMS = "sms";
    private static final String RP_PHONE = "phone";
    private static final String RP_TEXT = "text";
    private static final String RP_STATUS = "status";
    private static final String RP_DIRECTION = "direction";
    private static final String RP_TIME = "time";
    private static final String RP_CALL = "call";
    private static final String RP_DURATION = "duration";
    private static final String RP_POWER_LEVEL = "power_level";
    private static final String RP_POWER_STATUS = "power_status";
    private static final String RP_POWER_SOURCE = "power_source";
    private static final String RP_NETWORK_TYPE = "network_type";
    private static final String RP_PENDING_MESSAGE_COUNT = "pending_message_count";
    private static final String RP_RETRY_MESSAGE_COUNT = "retry_message_count";
    private static final String RP_LAST_SEEN = "last_seen";
    private static final String RP_FLOW = "flow";
    private static final String RP_FLOW_NAME = "flow-name";
    private static final String RP_STEP = "step";
    private static final String RP_VALUES = "values";

    private static final String NOT_INTEGER = "Value cannot be converted to an integer: ";
    private static final String NOT_DATE = "Improperly formatted date time value: ";
    private static final String ERROR_DECODE = "Error decoding web hook request: ";

    private static final DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");

    public static MotechEvent parse(Map<String, String[]> requestParams) {
        try {
            Map<String, String> decoded = decodeParams(requestParams);

            switch (decoded.get(RP_EVENT)) {

                case WebHookTypes.WEB_HOOK_MO_SMS:
                    return receivedSMS(decoded);

                case WebHookTypes.WEB_HOOK_MT_SENT:
                    return sentSMS(decoded);

                case WebHookTypes.WEB_HOOK_MT_DLVD:
                    return deliverySMS(decoded);

                case WebHookTypes.WEB_HOOK_MO_CALL:
                    return incomingCall(decoded);

                case WebHookTypes.WEB_HOOK_MO_MISS:
                    return missedCall(decoded);

                case WebHookTypes.WEB_HOOK_MT_CALL:
                    return callConnected(decoded);

                case WebHookTypes.WEB_HOOK_MT_MISS:
                    return callNotConnected(decoded);

                case WebHookTypes.WEB_HOOK_ALARM:
                    return alarm(decoded);

                case WebHookTypes.WEB_HOOK_FLOW:
                    return flow(decoded);

                default:
                    return null;
            }
        } catch (WebHookParserException e) {
            return webHookFail(e, requestParams);
        }
    }

    private static Map<String, String> decodeParams(Map<String, String[]> requestParams) throws WebHookParserException {
        Map<String, String> decoded = new HashMap<>();
        try {
            for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
                decoded.put(entry.getKey(), URLDecoder.decode(entry.getValue()[0], ENCODING));
            }
            return decoded;

        } catch (UnsupportedEncodingException e) {
            throw new WebHookParserException(ERROR_DECODE + e.getMessage(), e);
        }
    }

    private static MotechEvent receivedSMS (Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = buildSMSParameters(requestParams);
        return new MotechEvent(EventSubjects.WEB_HOOK_RECEIVED_SMS, eventParams);
    }

    private static MotechEvent sentSMS (Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = buildSMSParameters(requestParams);
        return new MotechEvent(EventSubjects.WEB_HOOK_SENT_SMS, eventParams);
    }

    private static MotechEvent deliverySMS (Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = buildSMSParameters(requestParams);
        return new MotechEvent(EventSubjects.WEB_HOOK_SMS_DELIVERED, eventParams);
    }

    private static MotechEvent incomingCall (Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = buildCallParameters(requestParams);
        return new MotechEvent(EventSubjects.WEB_HOOK_INCOMING_CALL, eventParams);
    }

    private static MotechEvent missedCall (Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = buildCallParameters(requestParams);
        return new MotechEvent(EventSubjects.WEB_HOOK_MISSED_CALL, eventParams);
    }

    private static MotechEvent callConnected (Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = buildCallParameters(requestParams);
        return new MotechEvent(EventSubjects.WEB_HOOK_CALL_CONNECTED, eventParams);
    }

    private static MotechEvent callNotConnected (Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = buildCallParameters(requestParams);
        return new MotechEvent(EventSubjects.WEB_HOOK_CALL_NOT_CONNECTED, eventParams);
    }

    private static MotechEvent flow(Map<String, String> requestParams) {
        Map<String, Object> eventParams = new HashMap<> ();
        eventParams.put(EventParameters.RELAYER, requestParams.get(RP_RELAYER));
        eventParams.put(EventParameters.RELAYER_PHONE, requestParams.get(RP_RELAYER_PHONE));
        eventParams.put(EventParameters.PHONE, requestParams.get(RP_PHONE));
        eventParams.put(EventParameters.FLOW, requestParams.get(RP_FLOW));
        eventParams.put(EventParameters.FLOW_NAME, requestParams.get(RP_FLOW_NAME));
        eventParams.put(EventParameters.STEP, requestParams.get(RP_STEP));
        eventParams.put(EventParameters.VALUES, requestParams.get(RP_VALUES));
        return new MotechEvent(EventSubjects.WEB_HOOK_FLOW, eventParams);
    }

    private static MotechEvent alarm(Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventParameters.RELAYER, requestParams.get(RP_RELAYER));
        eventParams.put(EventParameters.RELAYER_PHONE, requestParams.get(RP_RELAYER_PHONE));
        eventParams.put(EventParameters.POWER_LEVEL, requestParams.get(RP_POWER_LEVEL));
        eventParams.put(EventParameters.POWER_STATUS, requestParams.get(RP_POWER_STATUS));
        eventParams.put(EventParameters.POWER_SOURCE, requestParams.get(RP_POWER_SOURCE));
        eventParams.put(EventParameters.NETWORK_TYPE, requestParams.get(RP_NETWORK_TYPE));
        int pendingMessages = toInt(requestParams.get(RP_PENDING_MESSAGE_COUNT));
        int retryMessages = toInt(requestParams.get(RP_RETRY_MESSAGE_COUNT));
        DateTime lastSeen = toDateTime(requestParams.get(RP_LAST_SEEN));
        eventParams.put(EventParameters.PENDING_MESSAGE_COUNT, pendingMessages);
        eventParams.put(EventParameters.RETRY_MESSAGE_COUNT, retryMessages);
        eventParams.put(EventParameters.LAST_SEEN, lastSeen);
        return new MotechEvent(EventSubjects.WEB_HOOK_ALARM, eventParams);
    }

    private static Map<String, Object> buildSMSParameters(Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventParameters.RELAYER, requestParams.get(RP_RELAYER));
        eventParams.put(EventParameters.RELAYER_PHONE, requestParams.get(RP_RELAYER_PHONE));
        eventParams.put(EventParameters.SMS, requestParams.get(RP_SMS));
        eventParams.put(EventParameters.PHONE, requestParams.get(RP_PHONE));
        eventParams.put(EventParameters.TEXT, requestParams.get(RP_TEXT));
        eventParams.put(EventParameters.STATUS, requestParams.get(RP_STATUS));
        eventParams.put(EventParameters.DIRECTION, requestParams.get(RP_DIRECTION));

        DateTime dateTime = toDateTime(requestParams.get(RP_TIME));
        eventParams.put(EventParameters.TIME, dateTime);
        return eventParams;
    }

    private static Map<String, Object> buildCallParameters(Map<String, String> requestParams) throws WebHookParserException {
        Map<String, Object> eventParams = new HashMap<>();
        eventParams.put(EventParameters.RELAYER, requestParams.get(RP_RELAYER));
        eventParams.put(EventParameters.RELAYER_PHONE, requestParams.get(RP_RELAYER_PHONE));
        eventParams.put(EventParameters.CALL, requestParams.get(RP_CALL));
        eventParams.put(EventParameters.PHONE, requestParams.get(RP_PHONE));

        DateTime time = toDateTime(requestParams.get(RP_TIME));
        int duration = toInt(requestParams.get(RP_DURATION));
        eventParams.put(EventParameters.TIME, time);
        eventParams.put(EventParameters.DURATION, duration);
        return eventParams;
    }

    private static MotechEvent webHookFail(WebHookParserException e, Map<String, String[]> requestParams) {
        Map<String, Object> eventParams = new HashMap<>();
        Map<String, String> stringValues = new HashMap<>();
        for (Map.Entry<String, String[]> entry : requestParams.entrySet()) {
            stringValues.put(entry.getKey(), entry.getValue()[0]);
        }
        eventParams.put(EventParameters.ERROR_MESSAGE, e.getMessage());
        eventParams.put(EventParameters.EVENT, stringValues.get(RP_EVENT));
        eventParams.put(EventParameters.REQUEST_VALUES, stringValues);
        return new MotechEvent(EventSubjects.WEB_HOOK_FAIL, eventParams);
    }

    private static DateTime toDateTime(String s) throws WebHookParserException {
        try {
            DateTime dateTime = null;
            if (s != null) {
                dateTime = FORMATTER.parseDateTime(s);
            }
            return dateTime;

        } catch (UnsupportedOperationException | IllegalArgumentException e) {
            throw new WebHookParserException(NOT_DATE + s, e);
        }
    }

    private static int toInt (String s) throws WebHookParserException {
        try {
            int i = 0;
            if (s != null) {
                i = Integer.parseInt(s);
            }
            return i;
        } catch (NumberFormatException e) {
            throw new WebHookParserException(NOT_INTEGER + s, e);
        }
    }
}
