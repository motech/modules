package org.motechproject.odk.web;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.event.builder.EventBuilder;
import org.motechproject.odk.event.builder.impl.FailureEventBuilder;
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

import java.util.List;

/**
 * Controller that maps to /forms. Receives forms from external applications and publishes the appropriate events
 * upon successful reciept and processing of the form.
 */
@Controller
@RequestMapping(value = "/forms")
public class FormController {

    private static final Logger LOGGER = LoggerFactory.getLogger(FormController.class);
    private static final String RECEIVED_FORM = "Received form: ";
    private static final String CONFIGURATION = "Configuration: ";
    private static final String DOES_NOT_EXIST = " does not exist";
    private static final String FORM = "Form: ";
    private static final String PUBLISHING_EVENT = "Publishing event with subject: ";
    private static final String PUBLISHING_FAILURE = "Publishing form receipt failure event:\n";
    private static final String ERROR_JSON = "Error parsing JSON form data";

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
        LOGGER.debug(RECEIVED_FORM + form + " " + CONFIGURATION + config);

        Configuration configuration = configurationService.getConfigByName(config);
        FormDefinition formDefinition = formDefinitionService.findByConfigurationNameAndTitle(config, form);

        if (configuration == null) {
            LOGGER.error(CONFIGURATION + config + DOES_NOT_EXIST);
            publishFailureEvent(CONFIGURATION + " " + config + DOES_NOT_EXIST, null, config, form, body);

        } else if (formDefinition == null) {
            LOGGER.error(FORM + form + FORM);
            publishFailureEvent(FORM + form + FORM, null, config, form, body);

        } else {
            publishEvents(body, configuration, formDefinition);
        }
    }

    private void publishEvents(String body, Configuration configuration, FormDefinition formDefinition) {
        try {
            EventBuilder builder = new FormEventBuilderFactory().getBuilder(configuration.getType());
            List<MotechEvent> events = builder.createEvents(body, formDefinition, configuration);

            for (MotechEvent event : events) {
                LOGGER.debug(PUBLISHING_EVENT + event.getSubject());
                eventRelay.sendEventMessage(event);
            }

        } catch (Exception e) {
            LOGGER.error(PUBLISHING_FAILURE + e.toString());
            publishFailureEvent(ERROR_JSON, e.toString(), configuration.getName(), formDefinition.getTitle(), body);
        }
    }

    private void publishFailureEvent(String message, String error, String configName, String formTitle, String body) {
        FailureEventBuilder builder = new FailureEventBuilder();
        builder.setMessage(message)
                .setError(error)
                .setConfigName(configName)
                .setFormTitle(formTitle)
                .setBody(body);
        MotechEvent event = builder.createFailureEvent();
        eventRelay.sendEventMessage(event);
    }
}
