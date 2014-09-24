package org.motechproject.ivr.web;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.motechproject.admin.service.StatusMessageService;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.ivr.domain.Template;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.repository.TemplateDataService;
import org.motechproject.ivr.service.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;

import static org.motechproject.ivr.web.LogAndEventHelper.sendAndLogEvent;

/**
 * Responds to HTTP queries to {motech-server}/module/ivr/template/{configName}/{templateName} by creating a
 * CallDetailRecord entry in the database, posting a corresponding Motech event on the queue and returning the text
 * corresponding to the given template name and where all variable are replaced by the values passed as query parameters
 * See https://velocity.apache.org/ for the template language rules.
 */
@Controller
@RequestMapping(value = "/template")
public class TemplateController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);
    private CallDetailRecordDataService callDetailRecordDataService;
    private TemplateDataService templateDataService;
    private ConfigService configService;
    private StatusMessageService statusMessageService;
    private EventRelay eventRelay;

    @Autowired
    public TemplateController(CallDetailRecordDataService callDetailRecordDataService,
                              TemplateDataService templateDataService, EventRelay eventRelay,
                              @Qualifier("configService") ConfigService configService,
                              StatusMessageService statusMessageService) {
        this.callDetailRecordDataService = callDetailRecordDataService;
        this.templateDataService = templateDataService;
        this.eventRelay = eventRelay;
        this.configService = configService;
        this.statusMessageService = statusMessageService;
        try {
            Velocity.init();
        } catch (Exception e) {
            throw new IllegalStateException(String.format("Error initializing template engine: %s", e.toString()), e);
        }
    }

    /**
     * Listens to HTTP calls to http://{server}:{port}/module/ivr/template/{config}/{id}?key1=val1&key2=val2&... from
     * IVR providers. Creates a corresponding CDR entity in the database. Sends a MOTECH message with the CDR data in
     * the payload and the call status as the subject. Returns the template corresponding to the given id.
     *
     * @param configName
     * @param params
     * @return static XML content with an OK response element.
     */
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @RequestMapping(value = "/{configName}/{templateName}", produces = "text/xml")
    public String handle(@PathVariable String configName, @PathVariable String templateName,
                         @RequestParam Map<String, String> params, @RequestHeader Map<String, String> headers) {
        LOGGER.debug(String.format("handle(configName = %s, templateName = %s, parameters = %s, headers = %s)",
                configName, templateName, params, headers));

        sendAndLogEvent(configService, callDetailRecordDataService, statusMessageService, eventRelay, configName,
                templateName, params);

        // Render the template
        VelocityContext context = new VelocityContext();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            context.put(entry.getKey(), entry.getValue());
        }
        StringWriter writer = new StringWriter();
        Template template = templateDataService.findByName(templateName);
        try {
            Velocity.evaluate(context, writer, String.format("%s-%s", configName, templateName), template.getValue());
        } catch (IOException e) {
            throw new IllegalStateException(String.format("Error evaluating template: %s", e.toString()), e);
        }

        return writer.toString();
    }
}
