package org.motechproject.sms.web;

import org.joda.time.DateTime;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.server.config.SettingsFacade;
import org.motechproject.sms.alert.MotechStatusMessage;
import org.motechproject.sms.audit.DeliveryStatus;
import org.motechproject.sms.audit.SmsAuditService;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.configs.ConfigReader;
import org.motechproject.sms.configs.Configs;
import org.motechproject.sms.templates.Template;
import org.motechproject.sms.templates.TemplateReader;
import org.motechproject.sms.templates.Templates;
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
import static org.motechproject.sms.SmsEvents.inboundEvent;
import static org.motechproject.sms.audit.SmsDirection.INBOUND;

/**
 * Handles http requests to {motechserver}/motech-platform-server/module/sms/incoming{Config} sent by sms providers
 * when they receive an SMS
 */
@Controller
@RequestMapping(value = "/incoming")
public class IncomingController {

    @Autowired
    private MotechStatusMessage motechStatusMessage;
    private Logger logger = LoggerFactory.getLogger(IncomingController.class);
    private ConfigReader configReader;
    private Configs configs;
    private Templates templates;
    private EventRelay eventRelay;
    private SmsAuditService smsAuditService;

    @Autowired
    public IncomingController(@Qualifier("smsSettings") SettingsFacade settingsFacade, EventRelay eventRelay,
                              TemplateReader templateReader, SmsAuditService smsAuditService) {
        this.eventRelay = eventRelay;
        configReader = new ConfigReader(settingsFacade);
        //todo: this means we'll crash/error out when a new config is created and we get an incoming call before
        //todo: restarting the module but going to the new config system (with change notification) will fix that
        configs = configReader.getConfigs();
        templates = templateReader.getTemplates();
        this.smsAuditService = smsAuditService;
    }


    //todo: add provider-specific UI to explain how implementers must setup their providers' incoming callback

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/{configName}")
    public void handleIncoming(@PathVariable String configName, @RequestParam Map<String, String> params) {
        String sender = null;
        String recipient = null;
        String message = null;
        String providerMessageId = null;
        DateTime timestamp = null;

        logger.info("Incoming SMS - configName = {}, params = {}", configName, params);

        Config config;
        if (configs.hasConfig(configName)) {
            config = configs.getConfig(configName);
        } else {
            String msg = String.format("Invalid config in incoming request: %s, params: %s", configName, params);
            logger.error(msg);
            motechStatusMessage.alert(msg);
            return;
        }
        Template template = templates.getTemplate(config.getTemplateName());

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
        smsAuditService.log(new SmsRecord(config.getName(), INBOUND, sender, message, now(), DeliveryStatus.RECEIVED,
                null, null, providerMessageId, null));
    }
}
