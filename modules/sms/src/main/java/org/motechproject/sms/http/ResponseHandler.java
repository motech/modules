package org.motechproject.sms.http;

import org.apache.commons.httpclient.Header;
import org.motechproject.event.MotechEvent;
import org.motechproject.sms.alert.MotechStatusMessage;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.sms.templates.Response;
import org.motechproject.sms.templates.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * figures out success or failure from an sms provider response
 */
public abstract class ResponseHandler {
    private Template template;
    private Config config;
    private Response templateOutgoingResponse;
    private List<MotechEvent> events = new ArrayList<MotechEvent>();
    private List<SmsRecord> auditRecords = new ArrayList<SmsRecord>();
    private Logger logger = LoggerFactory.getLogger(ResponseHandler.class);
    private MotechStatusMessage motechStatusMessage;

    ResponseHandler(Template template, Config config, MotechStatusMessage motechStatusMessage) {
        this.template = template;
        this.config = config;
        this.motechStatusMessage = motechStatusMessage;
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

    public MotechStatusMessage getMotechStatusMessage() {
        return motechStatusMessage;
    }
}
