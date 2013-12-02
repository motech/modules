package org.motechproject.sms.http;

import org.apache.commons.httpclient.Header;
import org.motechproject.sms.SmsEventSubjects;
import org.motechproject.sms.alert.MotechStatusMessage;
import org.motechproject.sms.audit.DeliveryStatus;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.sms.templates.Template;

import java.util.Arrays;
import java.util.List;

import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.sms.SmsEvents.outboundEvent;
import static org.motechproject.sms.audit.SmsDirection.OUTBOUND;

/**
 * Deals with multi-line responses, like the ones sent by Clickatell
 */
public class MultilineResponseHandler extends ResponseHandler {

    MultilineResponseHandler(Template template, Config config, MotechStatusMessage motechStatusMessage) {
        super(template, config, motechStatusMessage);
    }

    @Override
    public void handle(OutgoingSms sms, String response, Header[] headers) {

        //
        // as the class name suggest we're dealing with a provider which returns a status code for each individual
        // recipient phone number in the original request on a separate line, ie: if we send an SMS to 4 recipients
        // then we should receive four lines of provider_message_id & status information
        //
        for (String responseLine : response.split("\\r?\\n")) {

            String[] messageIdAndRecipient = getTemplateOutgoingResponse().extractSuccessMessageIdAndRecipient(
                    responseLine);

            if (messageIdAndRecipient == null) {
                Integer failureCount = sms.getFailureCount() + 1;
                String[] messageAndRecipient;

                messageAndRecipient = getTemplateOutgoingResponse().extractFailureMessageAndRecipient(responseLine);
                if (messageAndRecipient == null) {
                    getEvents().add(outboundEvent(getConfig().retryOrAbortSubject(failureCount), getConfig().getName(),
                            sms.getRecipients(), sms.getMessage(), sms.getMotechId(), null, failureCount, null, null));

                    String errorMessage = String.format(
                            "Failed to sent SMS. Template error. Can't parse response: %s", responseLine);
                    getLogger().error(errorMessage);
                    getMotechStatusMessage().alert(errorMessage);

                    getAuditRecords().add(new SmsRecord(getConfig().getName(), OUTBOUND, sms.getRecipients().toString(),
                            sms.getMessage(), now(), getConfig().retryOrAbortStatus(failureCount), null,
                            sms.getMotechId(), null, null));
                } else {
                    String failureMessage = messageAndRecipient[0];
                    String recipient = messageAndRecipient[1];
                    List<String> recipients = Arrays.asList(new String[]{recipient});
                    getEvents().add(outboundEvent(getConfig().retryOrAbortSubject(failureCount), getConfig().getName(),
                            recipients, sms.getMessage(), sms.getMotechId(), null, failureCount, null, null));
                    getLogger().info("Failed to sent SMS: {}", failureMessage);
                    getAuditRecords().add(new SmsRecord(getConfig().getName(), OUTBOUND, recipient, sms.getMessage(),
                            now(), getConfig().retryOrAbortStatus(failureCount), null, sms.getMotechId(), null,
                            failureMessage));
                }
            } else {
                String messageId = messageIdAndRecipient[0];
                String recipient = messageIdAndRecipient[1];
                List<String> recipients = Arrays.asList(new String[]{recipient});
                //todo: HIPAA concerns?
                getLogger().info(String.format("Sent messageId %s '%s' to %s", messageId, messageForLog(sms),
                        recipient));
                getAuditRecords().add(new SmsRecord(getConfig().getName(), OUTBOUND, recipient, sms.getMessage(), now(),
                        DeliveryStatus.DISPATCHED, null, sms.getMotechId(), messageId, null));
                getEvents().add(outboundEvent(SmsEventSubjects.DISPATCHED, getConfig().getName(), recipients,
                        sms.getMessage(), sms.getMotechId(), messageId, null, null, null));
            }
        }
    }
}
