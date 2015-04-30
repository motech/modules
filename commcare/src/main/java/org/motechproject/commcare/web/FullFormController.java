package org.motechproject.commcare.web;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.exception.ConfigurationNotFoundException;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.exception.FullFormParserException;
import org.motechproject.commcare.parser.FullFormParser;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.impl.CommcareFormsEventParser;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.ATTRIBUTES;
import static org.motechproject.commcare.events.constants.EventDataKeys.CONFIG_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.ELEMENT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.FAILED_FORM_MESSAGE;
import static org.motechproject.commcare.events.constants.EventDataKeys.RECEIVED_ON;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;
import static org.motechproject.commcare.events.constants.EventSubjects.DEVICE_LOG_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_FAIL_EVENT;
import static org.motechproject.commcare.parser.FullFormParser.FORM;

/**
 * Controller that handles the incoming full form feed from CommCareHQ. The path to this endpoint
 * has to be configured on the CommCareHQ side.
 */
@Controller
@RequestMapping("/forms")
public class FullFormController {

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
        doReceiveForm(body, request, configService.getDefault());
    }

    @RequestMapping(value = "/{configName}")
    @ResponseStatus(HttpStatus.OK)
    public void receiveForm(@RequestBody String body, HttpServletRequest request) throws EndpointNotSupported {
        doReceiveForm(body, request, configService.getByName(getConfigName(request)));
    }

    @ExceptionHandler(ConfigurationNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleNotFound(Exception e) {
        LOGGER.error(e.getMessage());
        return e.getMessage();
    }

    @ExceptionHandler(EndpointNotSupported.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleBadRequest(Exception e) {
        LOGGER.error(e.getMessage());
        return e.getMessage();
    }

    private void publishMalformedFormMessage(String message) {
        Map<String, Object> params = new HashMap<>();
        params.put("message", "Received malformed form: " + message);
        params.put("level", "CRITICAL");
        params.put("moduleName", "commcare");

        eventRelay.sendEventMessage(new MotechEvent("org.motechproject.message", params));
    }

    private Multimap<String, Map<String, Object>> convertToMap(Multimap<String, FormValueElement> subElements) {
        Multimap<String, Map<String, Object>> elements = LinkedHashMultimap.create();

        for (Map.Entry<String, FormValueElement> entry : subElements.entries()) {
            Map<String, Object> elementAsMap = new HashMap<>(4); //NO CHECKSTYLE MagicNumber
            FormValueElement formValueElement = entry.getValue();

            elementAsMap.put(ELEMENT_NAME, formValueElement.getElementName());
            elementAsMap.put(SUB_ELEMENTS, convertToMap(formValueElement.getSubElements()));
            elementAsMap.put(ATTRIBUTES, formValueElement.getAttributes());
            elementAsMap.put(VALUE, formValueElement.getValue());

            elements.put(entry.getKey(), elementAsMap);
        }

        return elements;
    }

    private String getConfigName(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
    }

    private void doReceiveForm(String body, HttpServletRequest request, Config config) throws EndpointNotSupported {

        LOGGER.trace("Received request for mapping /forms: {}", body);

        if (!config.isForwardForms()) {
            throw new EndpointNotSupported(String.format("Configuration \"%s\" doesn't support endpoint for forms!", config.getName()));
        }

        FullFormParser parser = new FullFormParser(body);
        FormValueElement formValueElement;
        Map<String, Object> parameters = new HashMap<>();
        parameters.put(CONFIG_NAME, config.getName());
        parameters.put(TasksEventParser.CUSTOM_PARSER_EVENT_KEY, CommcareFormsEventParser.PARSER_NAME);

        try {
            formValueElement = parser.parse();

            parameters.put(RECEIVED_ON, request.getHeader("received-on"));
            parameters.put(VALUE, formValueElement.getValue());
            parameters.put(ELEMENT_NAME, formValueElement.getElementName());
            parameters.put(ATTRIBUTES, formValueElement.getAttributes());
            parameters.put(SUB_ELEMENTS, convertToMap(formValueElement.getSubElements()));

            if (FORM.equals(formValueElement.getElementName())) {
                eventRelay.sendEventMessage(new MotechEvent(FORMS_EVENT, parameters));
            } else {
                eventRelay.sendEventMessage(new MotechEvent(DEVICE_LOG_EVENT, parameters));
            }
        } catch (FullFormParserException e) {
            MotechEvent failedEvent = new MotechEvent(FORMS_FAIL_EVENT);
            failedEvent.getParameters().put(CONFIG_NAME, config.getName());
            failedEvent.getParameters().put(TasksEventParser.CUSTOM_PARSER_EVENT_KEY, CommcareFormsEventParser.PARSER_NAME);
            failedEvent.getParameters().put(FAILED_FORM_MESSAGE, e.getMessage());
            eventRelay.sendEventMessage(failedEvent);
            publishMalformedFormMessage(e.getMessage());
        }
    }
}
