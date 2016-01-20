package org.motechproject.odk.web;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.event.builder.EventBuilder;
import org.motechproject.odk.event.factory.FormEventBuilderFactory;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.ConfigurationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller that maps to /forms. Receives forms from external applications and publishes the appropriate events
 * upon successful reciept and processing of the form.
 */
@Controller
@RequestMapping(value = "/forms")
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
public class FormController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormController.class);

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private FormDefinitionService formDefinitionService;

    @Autowired
    private EventRelay eventRelay;


    /**
     * Recieves the form data from the external application and publishes the appropriate events.
     * @param config The name of the {@link Configuration}
     * @param form The title of the form.
     * @param body The JSON representation of the form instance data.
     */
    @RequestMapping(value = "/{config}/{form}", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void receiveForm(@PathVariable("config") String config, @PathVariable("form") String form, @RequestBody String body) {
        LOGGER.debug("Received form: " + form + " Configuration: " + config);

        Configuration configuration = configurationService.getConfigByName(config);
        FormDefinition formDefinition = formDefinitionService.findByConfigurationNameAndTitle(config, form);

        if (configuration == null) {
            LOGGER.error("Configuration " + config + " does not exist");
            publishFailureEvent("Configuration " + config + " does not exist", null, config, form, body);

        } else if (formDefinition == null) {
            LOGGER.error("Form " + form + " does not exist");
            publishFailureEvent("Form " + form + " does not exist", null, config, form, body);

        } else {
            publishEvents(body, configuration, formDefinition);
        }
    }

    private void publishEvents(String body, Configuration configuration, FormDefinition formDefinition) {
        EventBuilder builder = new FormEventBuilderFactory().getBuilder(configuration.getType());

        try {
            List<MotechEvent> events = builder.createEvents(body, formDefinition, configuration);

            for (MotechEvent event : events) {
                LOGGER.debug("Publishing event with subject : " + event.getSubject());
                eventRelay.sendEventMessage(event);
            }

        } catch (Exception e) {
            LOGGER.error("Publishing form receipt failure event:\n" + e.toString());
            publishFailureEvent("Error parsing JSON form data", e.toString(), configuration.getName(), formDefinition.getTitle(), body);
        }
    }

    private void publishFailureEvent(String message, String error, String configName, String formTitle, String body) {
        Map<String, Object> params = new HashMap<>();
        params.put(EventParameters.CONFIGURATION_NAME, configName);
        params.put(EventParameters.FORM_TITLE, formTitle);
        params.put(EventParameters.MESSAGE, message);
        params.put(EventParameters.EXCEPTION, error);
        params.put(EventParameters.JSON_CONTENT, body);
        eventRelay.sendEventMessage(new MotechEvent(EventSubjects.FORM_FAIL, params));
    }

}
