package org.motechproject.sms.web;

import org.joda.time.DateTime;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.audit.SmsRecordsDataService;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.service.ConfigService;
import org.motechproject.sms.service.TemplateService;
import org.motechproject.sms.templates.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.sms.util.SmsEvents.inboundEvent;
import static org.motechproject.sms.audit.SmsDirection.INBOUND;

/**
 * Handles http requests to {motechserver}/motech-platform-server/module/sms/incoming{Config} sent by sms providers
 * when they receive an SMS
 */
@Controller
@RequestMapping(value = "/incoming")
public class IncomingController {

    private static final String SMS_MODULE = "motech-sms";
    private static final String RECEIVED = "RECEIVED";

    private static final Logger LOGGER = LoggerFactory.getLogger(IncomingController.class);
    private TemplateService templateService;
    private ConfigService configService;
    private EventRelay eventRelay;
    private SmsRecordsDataService smsRecordsDataService;
    private StatusMessageService statusMessageService;


    @Autowired
    public IncomingController(SmsRecordsDataService smsRecordsDataService,
                              @Qualifier("templateService") TemplateService templateService,
                              @Qualifier("configService") ConfigService configService,
                              StatusMessageService statusMessageService, EventRelay eventRelay) {
        this.smsRecordsDataService = smsRecordsDataService;
        this.templateService = templateService;
        this.configService = configService;
        this.statusMessageService = statusMessageService;
        this.eventRelay = eventRelay;
    }


    //todo: add provider-specific UI to explain how implementers must setup their providers' incoming callback

    /**
     * Handles an incoming SMS notification coming from the provider. A MOTECH Event notifying about this will also
     * get published. The request itself will get handled in the way that the configuration template specifies it.
     * @param configName the name of the configuration that should handle the SMS
     * @param params the request params coming from the provider
     */
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{configName}")
    public void handleIncoming(@PathVariable String configName, @RequestParam Map<String, String> params) {
        String sender = null;
        String recipient = null;
        String message = null;
        String providerMessageId = null;
        DateTime timestamp;

        LOGGER.info("Incoming SMS - configName = {}, params = {}", configName, params);

        Config config;
        if (configService.hasConfig(configName)) {
            config = configService.getConfig(configName);
        } else {
            String msg = String.format("Invalid config in incoming request: %s, params: %s", configName, params);
            LOGGER.error(msg);
            statusMessageService.warn(msg, SMS_MODULE);
            return;
        }
        Template template = templateService.getTemplate(config.getTemplateName());

        if (params.containsKey(template.getIncoming().getSenderKey())) {
            sender = params.get(template.getIncoming().getSenderKey());
            if (template.getIncoming().hasSenderRegex()) {
                sender = template.getIncoming().extractSender(sender);
            }
        }

        if (params.containsKey(template.getIncoming().getRecipientKey())) {
            recipient = params.get(template.getIncoming().getRecipientKey());
            if (template.getIncoming().hasRecipientRegex()) {
                recipient = template.getIncoming().extractRecipient(recipient);
            }
        }

        if (params.containsKey(template.getIncoming().getMessageKey())) {
            message = params.get(template.getIncoming().getMessageKey());
        }

        if (params.containsKey(template.getIncoming().getMsgIdKey())) {
            providerMessageId = params.get(template.getIncoming().getMsgIdKey());
        }

        if (params.containsKey(template.getIncoming().getTimestampKey())) {
            String dt = params.get(template.getIncoming().getTimestampKey());
            //todo: some providers may send timestamps in a different way, deal it it if/when we see that
            // replace "yyyy-mm-dd hh:mm:ss" with "yyyy-mm-ddThh:mm:ss" (note the T)
            if (dt.matches("(\\d\\d\\d\\d|\\d\\d)-\\d\\d?-\\d\\d? \\d\\d?:\\d\\d?:\\d\\d?")) {
                dt = dt.replace(" ", "T");
            }
            timestamp = DateTime.parse(dt);
        } else {
            timestamp = now();
        }

        eventRelay.sendEventMessage(inboundEvent(config.getName(), sender, recipient, message, providerMessageId,
                timestamp));
        smsRecordsDataService.create(new SmsRecord(config.getName(), INBOUND, sender, message, now(), RECEIVED,
                null, null, providerMessageId, null));
    }
}
