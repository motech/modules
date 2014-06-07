package org.motechproject.sms.http;

import org.apache.commons.httpclient.Header;
import org.motechproject.sms.SmsEventSubjects;
import org.motechproject.sms.alert.MotechStatusMessage;
import org.motechproject.sms.audit.DeliveryStatus;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.sms.templates.Template;

import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.sms.SmsEvents.outboundEvent;
import static org.motechproject.sms.audit.SmsDirection.OUTBOUND;

/**
 * Deals with providers who return multi-line responses, but return a different response when sending only one message,
 * like Clickatell does
 */
public class MultilineSingleResponseHandler extends ResponseHandler {

    MultilineSingleResponseHandler(Template template, Config config, MotechStatusMessage motechStatusMessage) {
        super(template, config, motechStatusMessage);
    }

    @Override
    public void handle(OutgoingSms sms, String response, Header[] headers) {

        String messageId = getTemplateOutgoingResponse().extractSingleSuccessMessageId(response);

        if (messageId == null) {
            Integer failureCount = sms.getFailureCount() + 1;

            String failureMessage = getTemplateOutgoingResponse().extractSingleFailureMessage(response);
            if (failureMessage == null) {
                failureMessage = response;
            }
            getEvents().add(outboundEvent(getConfig().retryOrAbortSubject(failureCount), getConfig().getName(),
                    sms.getRecipients(), sms.getMessage(), sms.getMotechId(), null, failureCount, null, null));
            getLogger().info("Failed to sent SMS: %s", failureMessage);
            getAuditRecords().add(new SmsRecord(getConfig().getName(), OUTBOUND, sms.getRecipients().get(0),
                    sms.getMessage(), now(), getConfig().retryOrAbortStatus(failureCount), null, sms.getMotechId(),
                    null, failureMessage));
        } else {
            //todo: HIPAA concerns?
            getLogger().info(String.format("Sent messageId %s '%s' to %s", messageId, messageForLog(sms),
                    sms.getRecipients().get(0)));
            getAuditRecords().add(new SmsRecord(getConfig().getName(), OUTBOUND, sms.getRecipients().get(0),
                    sms.getMessage(), now(), DeliveryStatus.DISPATCHED, null, sms.getMotechId(), messageId, null));
            getEvents().add(outboundEvent(SmsEventSubjects.DISPATCHED, getConfig().getName(), sms.getRecipients(),
                    sms.getMessage(), sms.getMotechId(), messageId, null, null, null));
        }
    }
}
