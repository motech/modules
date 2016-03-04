package org.motechproject.sms.http;

import org.apache.commons.httpclient.Header;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.audit.constants.DeliveryStatuses;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.sms.templates.Template;
import org.motechproject.sms.util.SmsEventSubjects;

import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.sms.util.SmsEvents.outboundEvent;
import static org.motechproject.sms.audit.SmsDirection.OUTBOUND;

/**
 * Deals with providers who return a generic response in the body or header
 */
public class GenericResponseHandler extends ResponseHandler {

    /**
     * Constructs an instance using the provided template and configuration.
     * @param template the template to use
     * @param config the configuration to use
     */
    GenericResponseHandler(Template template, Config config) {
        super(template, config);
    }

    @Override
    public void handle(OutgoingSms sms, String response, Header[] headers) {

        if (!getTemplateOutgoingResponse().hasSuccessResponse() ||
                getTemplateOutgoingResponse().checkSuccessResponse(response)) {

            String providerMessageId = extractProviderMessageId(headers, response);

            //todo: HIPAA concerns?
            getLogger().info(String.format("Sent messageId %s '%s' to %s", providerMessageId, messageForLog(sms),
                    sms.getRecipients().toString()));
            for (String recipient : sms.getRecipients()) {
                getAuditRecords().add(new SmsRecord(getConfig().getName(), OUTBOUND, recipient, sms.getMessage(), now(),
                        DeliveryStatuses.DISPATCHED, null, sms.getMotechId(), providerMessageId, null));
            }
            getEvents().add(outboundEvent(SmsEventSubjects.DISPATCHED, getConfig().getName(), sms.getRecipients(),
                    sms.getMessage(), sms.getMotechId(), providerMessageId, null, null, null, sms.getCustomParams()));

        } else {
            Integer failureCount = sms.getFailureCount() + 1;

            String failureMessage = getTemplateOutgoingResponse().extractSingleFailureMessage(response);
            if (failureMessage == null) {
                failureMessage = response;
            }
            getEvents().add(outboundEvent(getConfig().retryOrAbortSubject(failureCount), getConfig().getName(),
                    sms.getRecipients(), sms.getMessage(), sms.getMotechId(), null, failureCount, null, null, sms.getCustomParams()));
            getLogger().debug("Failed to send SMS: {}", failureMessage);
            for (String recipient : sms.getRecipients()) {
                getAuditRecords().add(new SmsRecord(getConfig().getName(), OUTBOUND, recipient, sms.getMessage(), now(),
                        getConfig().retryOrAbortStatus(failureCount), null, sms.getMotechId(), null, failureMessage));
            }
        }
    }

    private String extractProviderMessageId(Header[] headers, String response) {
        String providerMessageId = null;

        if (getTemplateOutgoingResponse().hasHeaderMessageId()) {
            for (Header header : headers) {
                if (header.getName().equals(getTemplateOutgoingResponse().getHeaderMessageId())) {
                    providerMessageId = header.getValue();
                }
            }
            if (providerMessageId == null) {
                String message = String.format("Unable to find provider message id in '%s' header",
                        getTemplateOutgoingResponse().getHeaderMessageId());
                warn(message);
                getLogger().error(message);
            }
        } else if (getTemplateOutgoingResponse().hasSingleSuccessMessageId()) {
            providerMessageId = getTemplateOutgoingResponse().extractSingleSuccessMessageId(response);
        }

        return providerMessageId;
    }
}
