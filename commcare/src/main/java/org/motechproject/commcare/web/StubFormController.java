package org.motechproject.commcare.web;

import com.google.gson.JsonParseException;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.FormStubJson;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.impl.CommcareStubFormsEventParser;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.commons.api.json.MotechJsonReader;
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
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * Controller that handles the incoming stub form feed from CommCareHQ. Maps to /commcare/stubforms. It is capable of
 * handling multiple configurations by parameterizing the endpoint URL.
 */
@Controller
@RequestMapping("/stub")
public class StubFormController extends CommcareController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StubFormController.class);

    private EventRelay eventRelay;

    private MotechJsonReader jsonReader;

    private CommcareConfigService configService;

    @Autowired
    public StubFormController(EventRelay eventRelay, CommcareConfigService configService) {
        this.eventRelay = eventRelay;
        this.configService = configService;
        jsonReader = new MotechJsonReader();
    }

    @RequestMapping
    @ResponseStatus(HttpStatus.OK)
    public ModelAndView receiveFormEventForDefault(HttpServletRequest request, @RequestBody String body) throws EndpointNotSupported {
        return doReceiveFormEvent(body, configService.getDefault());
    }

    @RequestMapping({ "/{configName}" })
    public ModelAndView receiveFormEvent(HttpServletRequest request, @RequestBody String body) throws EndpointNotSupported {
        return doReceiveFormEvent(body, configService.getByName(getConfigName(request)));
    }

    private String getConfigName(HttpServletRequest request) {
        String pathInfo = request.getPathInfo();
        return pathInfo.substring(pathInfo.lastIndexOf('/') + 1);
    }

    private ModelAndView doReceiveFormEvent(String body, Config config) throws EndpointNotSupported {

        if (!config.isForwardStubs()) {
            throw new EndpointNotSupported(String.format("Configuration \"%s\" doesn't support endpoint for stubs!", config.getName()));
        }

        FormStubJson formStub;

        try {
            formStub = (FormStubJson) jsonReader.readFromString(body, FormStubJson.class);
        } catch (JsonParseException e) {
            LOGGER.warn("Unable to parse Json: " + e.getMessage());
            MotechEvent formFailEvent = new MotechEvent(EventSubjects.FORM_STUB_FAIL_EVENT);
            eventRelay.sendEventMessage(formFailEvent);
            return null;
        }

        if (formStub != null) {
            MotechEvent formEvent = new MotechEvent(EventSubjects.FORM_STUB_EVENT);

            formEvent.getParameters().put(TasksEventParser.CUSTOM_PARSER_EVENT_KEY, CommcareStubFormsEventParser.PARSER_NAME);
            formEvent.getParameters().put(EventDataKeys.RECEIVED_ON, formStub.getReceivedOn());
            formEvent.getParameters().put(EventDataKeys.FORM_ID, formStub.getFormId());
            formEvent.getParameters().put(EventDataKeys.CASE_IDS, formStub.getCaseIds());
            formEvent.getParameters().put(EventDataKeys.CONFIG_NAME, config.getName());

            eventRelay.sendEventMessage(formEvent);
        } else {
            LOGGER.error("Unable to parse form stub: " + body);
        }

        return null;
    }
}
