package org.motechproject.sms.http;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.audit.SmsRecordsDataService;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.configs.ConfigProp;
import org.motechproject.sms.service.ConfigService;
import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.sms.service.TemplateService;
import org.motechproject.sms.templates.Response;
import org.motechproject.sms.templates.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.motechproject.commons.date.util.DateUtil.now;
import static org.motechproject.sms.audit.SmsDirection.OUTBOUND;
import static org.motechproject.sms.util.SmsEvents.outboundEvent;

/**
 * This is the main meat - here we talk to the providers using HTTP.
 */
@Service
public class SmsHttpService {

    private static final String SMS_MODULE = "motech-sms";
    private static final Logger LOGGER = LoggerFactory.getLogger(SmsHttpService.class);

    private TemplateService templateService;
    private ConfigService configService;
    private EventRelay eventRelay;
    private HttpClient commonsHttpClient;
    private StatusMessageService statusMessageService;
    private SmsRecordsDataService smsRecordsDataService;

    /**
     * This method allows sending outgoing sms messages through HTTP. The configuration specified in the {@link OutgoingSms}
     * object will be used for dealing with the provider.
     * @param sms the representation of the sms to send
     */
    @Transactional
    public synchronized void send(OutgoingSms sms) {

        Config config = configService.getConfigOrDefault(sms.getConfig());
        Template template = templateService.getTemplate(config.getTemplateName());
        HttpMethod httpMethod = null;
        Integer failureCount = sms.getFailureCount();
        Integer httpStatus = null;
        String httpResponse = null;
        String errorMessage = null;
        Map<String, String> props = generateProps(sms, template, config);
        List<MotechEvent> events = new ArrayList<>();
        List<SmsRecord> auditRecords = new ArrayList<>();

        //
        // Generate the HTTP request
        //
        try {
            httpMethod = prepHttpMethod(template, props, config);
            httpStatus = commonsHttpClient.executeMethod(httpMethod);
            httpResponse = httpMethod.getResponseBodyAsString();
        } catch (UnknownHostException e) {
            errorMessage = String.format("Network connectivity issues or problem with '%s' template? %s",
                    template.getName(), e.toString());
        } catch (IllegalArgumentException | IOException | IllegalStateException e) {
            errorMessage = String.format("Problem with '%s' template? %s", template.getName(), e.toString());
        } finally {
            if (httpMethod != null) {
                httpMethod.releaseConnection();
            }
        }

        //
        // make sure we don't talk to the SMS provider too fast (some only allow a max of n per minute calls)
        //
        delayProviderAccess(template);

        //
        // Analyze provider's response
        //
        Response templateResponse = template.getOutgoing().getResponse();
        if (httpStatus == null || !templateResponse.isSuccessStatus(httpStatus) || httpMethod == null) {
            //
            // HTTP Request Failure
            //
            failureCount++;
            handleFailure(httpStatus, errorMessage, failureCount, templateResponse, httpResponse, config, sms,
                    auditRecords, events);
        } else {
            //
            // HTTP Request Success, now look more closely at what the provider is telling us
            //
            ResponseHandler handler = createResponseHandler(template, templateResponse, config, sms);

            try {
                handler.handle(sms, httpResponse, httpMethod.getResponseHeaders());
            } catch (IllegalStateException | IllegalArgumentException e) {
                // exceptions generated above should only come from config/template issues, try to display something
                // useful in the motech messages and tomcat log
                statusMessageService.warn(e.getMessage(), SMS_MODULE);
                throw e;
            }
            events = handler.getEvents();
            auditRecords = handler.getAuditRecords();
        }

        //
        // Finally send all the events that need sending...
        //
        for (MotechEvent event : events) {
            eventRelay.sendEventMessage(event);
        }

        //
        // ...and audit all the records that need auditing
        //
        for (SmsRecord smsRecord : auditRecords) {
            smsRecordsDataService.create(smsRecord);
        }
    }

    private static String printableMethodParams(HttpMethod method) {
        if (method.getClass().equals(PostMethod.class)) {
            PostMethod postMethod = (PostMethod) method;
            RequestEntity requestEntity = postMethod.getRequestEntity();
            if (MediaType.APPLICATION_FORM_URLENCODED_VALUE.equals(requestEntity.getContentType())) {
                StringBuilder sb = new StringBuilder();
                NameValuePair[] params = postMethod.getParameters();
                for (NameValuePair param : params) {
                    if (sb.length() > 0) {
                        sb.append(", ");
                    }
                    sb.append(String.format("%s: %s", param.getName(), param.getValue()));
                }
                return "POST Parameters: " + sb.toString();
            } else if (requestEntity.getClass() == StringRequestEntity.class) {
                // Assume MediaType.APPLICATION_JSON_VALUE
                return "POST JSON: " + ((StringRequestEntity) requestEntity).getContent();
            }
        } else if (method.getClass().equals(GetMethod.class)) {
            GetMethod g = (GetMethod) method;
            return String.format("GET QueryString: %s", g.getQueryString());
        }

        throw new IllegalStateException(String.format("Unexpected HTTP method: %s", method.getClass()));
    }

    private void authenticate(Map<String, String> props, Config config) {
        if (props.containsKey("username") && props.containsKey("password")) {
            String u = props.get("username");
            String p = props.get("password");
            commonsHttpClient.getParams().setAuthenticationPreemptive(true);
            commonsHttpClient.getState().setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(u, p));
        } else {
            String message;
            if (props.containsKey("username")) {
                message = String.format("Config %s: missing password", config.getName());
            } else if (props.containsKey("password")) {
                message = String.format("Config %s: missing username", config.getName());
            } else {
                message = String.format("Config %s: missing username and password", config.getName());
            }
            statusMessageService.warn(message, SMS_MODULE);
            throw new IllegalStateException(message);
        }
    }

    private void delayProviderAccess(Template template) {
        //todo: serialize access to configs, ie: one provider may allow 100 sms/min and another may allow 10...
        //This prevents us from sending more messages per second than the provider allows
        Integer milliseconds = template.getOutgoing().getMillisecondsBetweenMessages();
        LOGGER.debug("Sleeping thread id {} for {}ms", Thread.currentThread().getId(), milliseconds);
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    private Map<String, String> generateProps(OutgoingSms sms, Template template, Config config) {
        Map<String, String> props = new HashMap<>();
        props.put("recipients", template.recipientsAsString(sms.getRecipients()));
        props.put("message", sms.getMessage());
        props.put("motechId", sms.getMotechId());
        props.put("callback", configService.getServerUrl() + "/module/sms/status/" + config.getName());
        if (sms.getCustomParams() != null) {
            props.putAll(sms.getCustomParams());
        }

        for (ConfigProp configProp : config.getProps()) {
            props.put(configProp.getName(), configProp.getValue());
        }

        // ***** WARNING *****
        // This displays usernames & passwords in the server log! But then again, so does the settings UI...
        // ***** WARNING *****
        if (LOGGER.isDebugEnabled()) {
            for (Map.Entry<String, String> entry : props.entrySet()) {
                LOGGER.debug("PROP {}: {}", entry.getKey(), entry.getValue());
            }
        }

        return props;
    }

    private void handleFailure(Integer httpStatus, String priorErrorMessage, //NO CHECKSTYLE ParameterNumber
                               Integer failureCount, Response templateResponse, String httpResponse, Config config,
                               OutgoingSms sms, List<SmsRecord> auditRecords, List<MotechEvent> events) {
        String errorMessage = priorErrorMessage;

        if (httpStatus == null) {
            String msg = String.format("Delivery to SMS provider failed: %s", errorMessage);
            LOGGER.error(msg);
            statusMessageService.warn(msg, SMS_MODULE);
        } else {
            errorMessage = templateResponse.extractGeneralFailureMessage(httpResponse);
            if (errorMessage == null) {
                statusMessageService.warn(String.format("Unable to extract failure message for '%s' config: %s",
                        config.getName(), httpResponse), SMS_MODULE);
                errorMessage = httpResponse;
            }
            LOGGER.error("Delivery to SMS provider failed with HTTP {}: {}", httpStatus, errorMessage);
        }

        for (String recipient : sms.getRecipients()) {
            auditRecords.add(new SmsRecord(config.getName(), OUTBOUND, recipient, sms.getMessage(), now(),
                    config.retryOrAbortStatus(failureCount), null, sms.getMotechId(), null, errorMessage));
        }
        events.add(outboundEvent(config.retryOrAbortSubject(failureCount), config.getName(), sms.getRecipients(),
                sms.getMessage(), sms.getMotechId(), null, sms.getFailureCount() + 1, null, null, sms.getCustomParams()));

    }

    private ResponseHandler createResponseHandler(Template template, Response templateResponse, Config config,
                                                  OutgoingSms sms) {
        ResponseHandler handler;
        if (templateResponse.supportsSingleRecipientResponse()) {
            if (sms.getRecipients().size() == 1 && templateResponse.supportsSingleRecipientResponse()) {
                handler = new MultilineSingleResponseHandler(template, config);
            } else {
                handler = new MultilineResponseHandler(template, config);
            }
        } else {
            handler = new GenericResponseHandler(template, config);
        }

        return handler;
    }

    private HttpMethod prepHttpMethod(Template template, Map<String, String> props, Config config) {
        HttpMethod method = template.generateRequestFor(props);
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug(printableMethodParams(method));
        }

        if (template.getOutgoing().hasAuthentication()) {
            authenticate(props, config);
        }
        return method;
    }

    @Autowired
    @Qualifier("templateService")
    public void setTemplateService(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Autowired
    public void setEventRelay(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    @Autowired
    @Qualifier("configService")
    public void setConfigService(ConfigService configService) {
        this.configService = configService;
    }

    @Autowired
    public void setCommonsHttpClient(HttpClient commonsHttpClient) {
        this.commonsHttpClient = commonsHttpClient;
    }

    @Autowired
    public void setStatusMessageService(StatusMessageService statusMessageService) {
        this.statusMessageService = statusMessageService;
    }

    @Autowired
    public void setSmsRecordsDataService(SmsRecordsDataService smsRecordsDataService) {
        this.smsRecordsDataService = smsRecordsDataService;
    }
}
