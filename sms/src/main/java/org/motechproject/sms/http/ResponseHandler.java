package org.motechproject.sms.http;

import org.apache.commons.httpclient.Header;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.MotechEvent;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.sms.templates.Response;
import org.motechproject.sms.templates.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * figures out success or failure from an sms provider response
 */
public abstract class ResponseHandler {
    private static final String SMS_MODULE = "motech-sms";
    private Template template;
    private Config config;
    private Response templateOutgoingResponse;
    private List<MotechEvent> events = new ArrayList<>();
    private List<SmsRecord> auditRecords = new ArrayList<>();
    private Logger logger = LoggerFactory.getLogger(ResponseHandler.class);

    @Autowired
    private StatusMessageService statusMessageService;

    ResponseHandler(Template template, Config config) {
        this.template = template;
        this.config = config;
        templateOutgoingResponse = template.getOutgoing().getResponse();
    }

    public abstract void handle(OutgoingSms sms, String response, Header[] headers);

    public String messageForLog(OutgoingSms sms) {
        return sms.getMessage().replace("\n", "\\n");
    }

    public List<MotechEvent> getEvents() {
        return events;
    }

    public List<SmsRecord> getAuditRecords() {
        return auditRecords;
    }

    public Template getTemplate() {
        return template;
    }

    protected void setTemplate(Template template) {
        this.template = template;
    }

    protected Config getConfig() {
        return config;
    }

    protected void setConfig(Config config) {
        this.config = config;
    }

    protected Response getTemplateOutgoingResponse() {
        return templateOutgoingResponse;
    }

    protected Logger getLogger() {
        return logger;
    }

    public void warn(String message) {
        statusMessageService.warn(message, SMS_MODULE);
    }
}
