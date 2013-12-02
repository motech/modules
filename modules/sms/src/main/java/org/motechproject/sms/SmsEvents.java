package org.motechproject.sms;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import static org.motechproject.commons.date.util.DateUtil.now;


/**
 * MotechEvent Helper class
 */
public final class SmsEvents {

    private SmsEvents() { }

    public static MotechEvent inboundEvent(String config, String sender, String recipient, String message,
                                           String providerMessageId, DateTime timestamp) {
        Map<String, Object> params = new HashMap<>();
        params.put(SmsEventParams.CONFIG, config);
        params.put(SmsEventParams.SENDER, sender);
        params.put(SmsEventParams.RECIPIENT, recipient);
        params.put(SmsEventParams.MESSAGE, message);
        params.put(SmsEventParams.PROVIDER_MESSAGE_ID, providerMessageId);
        params.put(SmsEventParams.TIMESTAMP_DATETIME, timestamp);

        DateTime dtUTC = new DateTime(timestamp, DateTimeZone.forTimeZone(TimeZone.getTimeZone("UTC")));
        String time = String.format("%02d:%02d Z", dtUTC.getHourOfDay(), dtUTC.getMinuteOfHour());
        params.put(SmsEventParams.TIMESTAMP_TIME, time);

        return new MotechEvent(SmsEventSubjects.INBOUND_SMS, params);
    }

    public static MotechEvent outboundEvent(String subject, String config, List<String> recipients, String message,
                                            String motechId, String providerMessageId, Integer failureCount,
                                            String providerStatus, DateTime timestamp) {
        Map<String, Object> params = new HashMap<>();
        params.put(SmsEventParams.CONFIG, config);
        params.put(SmsEventParams.RECIPIENTS, recipients);
        params.put(SmsEventParams.MESSAGE, message);
        params.put(SmsEventParams.MOTECH_ID, motechId);
        if (providerMessageId != null) {
            params.put(SmsEventParams.PROVIDER_MESSAGE_ID, providerMessageId);
        }
        if (failureCount != null) {
            params.put(SmsEventParams.FAILURE_COUNT, failureCount);
        } else {
            params.put(SmsEventParams.FAILURE_COUNT, 0);
        }
        if (providerStatus != null) {
            params.put(SmsEventParams.PROVIDER_STATUS, providerStatus);
        }
        if (timestamp == null) {
            params.put(SmsEventParams.TIMESTAMP, now());
        } else {
            params.put(SmsEventParams.TIMESTAMP, timestamp);
        }
        return new MotechEvent(subject, params);
    }
}
