package org.motechproject.commcare.web;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.events.FullFormEvent;
import org.motechproject.commcare.events.FullFormFailureEvent;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.exception.FullFormParserException;
import org.motechproject.commcare.parser.FullFormParser;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller that handles the incoming full form feed from CommCareHQ. The path to this endpoint has to be configured
 * on the CommCareHQ side. It is capable of handling multiple configurations by parameterizing the endpoint URL.
 */
@Controller
@RequestMapping("/forms")
public class FullFormController extends CommcareController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FullFormController.class);

    private CommcareConfigService configService;
    private EventRelay eventRelay;

    @Autowired
    public FullFormController(EventRelay eventRelay, CommcareConfigService configService) {
        this.eventRelay = eventRelay;
        this.configService = configService;
    }

    @RequestMapping
    @ResponseStatus(HttpStatus.OK)
    public void receiveFormForDefaultConfig(@RequestBody String body, HttpServletRequest request) throws EndpointNotSupported {
        doReceiveFullForm(body, request, configService.getDefault());
    }

    @RequestMapping(value = "/{configName}")
    @ResponseStatus(HttpStatus.OK)
    public void receiveForm(@RequestBody String body, HttpServletRequest request) throws EndpointNotSupported {
        doReceiveFullForm(body, request, configService.getByName(getConfigName(request)));
    }

    private void doReceiveFullForm(String body, HttpServletRequest request, Config config) throws EndpointNotSupported {

        LOGGER.trace("Received request for mapping /forms: {}", body);

        if (!config.isForwardForms()) {
            throw new EndpointNotSupported(String.format("Configuration \"%s\" doesn't support endpoint for forms!", config.getName()));
        }

        FullFormParser parser = new FullFormParser(body);

        try {
            FormValueElement formValueElement = parser.parse();

            FullFormEvent fullFormEvent = new FullFormEvent(formValueElement, request.getHeader("received-on"),
                    config.getName());

            eventRelay.sendEventMessage(fullFormEvent.toMotechEvent());
        } catch (FullFormParserException e) {
            FullFormFailureEvent failureEvent = new FullFormFailureEvent(config.getName(), e.getMessage());
            eventRelay.sendEventMessage(failureEvent.toMotechEvent());
            publishMalformedFormMessage(e.getMessage());
        }
    }

    private void publishMalformedFormMessage(String message) {
        Map<String, Object> params = new HashMap<>();
        params.put("message", "Received malformed form: " + message);
        params.put("level", "CRITICAL");
        params.put("moduleName", "commcare");

        eventRelay.sendEventMessage(new MotechEvent("org.motechproject.message", params));
    }

    private String getConfigName(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
    }
}
