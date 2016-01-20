package org.motechproject.odk.event;


import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormFailure;
import org.motechproject.odk.domain.FormInstance;
import org.motechproject.odk.domain.builder.FormInstanceBuilder;
import org.motechproject.odk.service.FormDefinitionService;
import org.motechproject.odk.service.FormFailureService;
import org.motechproject.odk.service.FormInstanceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * This handles any incoming {@link org.motechproject.event.MotechEvent} that contains event subjects
 * relevant to the module.
 */

@Component
public class EventHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventHandler.class);

    @Autowired
    private FormDefinitionService formDefinitionService;

    @Autowired
    private FormInstanceService formInstanceService;

    @Autowired
    private FormFailureService formFailureService;


    /**
     * Creates {@link FormInstance} from the event payload and saves it.
     *
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = EventSubjects.PERSIST_FORM_INSTANCE)
    public void handlePersistForm(MotechEvent event) {
        Map<String, Object> params = event.getParameters();
        String title = (String) params.get(EventParameters.FORM_TITLE);
        String configName = (String) params.get(EventParameters.CONFIGURATION_NAME);
        String instanceId = (String) params.get(EventParameters.INSTANCE_ID);

        if (title != null && configName != null && instanceId != null) {
            if (formInstanceService.getByInstanceId(instanceId) != null) {
                LOGGER.error("Form Instance with ID: " + instanceId + " already exists. Discarding form instance data");
            } else {
                LOGGER.debug("Saving form instance.Title: " + title + " ConfigName: " + configName + " Instance ID: " + instanceId);
                FormDefinition formDefinition = formDefinitionService.findByConfigurationNameAndTitle(configName, title);

                if (formDefinition != null) {
                    FormInstanceBuilder builder = new FormInstanceBuilder(formDefinition, params, instanceId);
                    FormInstance instance = builder.build();
                    formInstanceService.create(instance);
                }
            }

        }
    }

    /**
     * Creates a {@link FormFailure} from the event payload and saves it.
     *
     * @param event {@link MotechEvent}
     */
    @MotechListener(subjects = EventSubjects.FORM_FAIL)
    public void handlePersistFormFailure(MotechEvent event) {
        Map<String, Object> params = event.getParameters();
        String configName = (String) params.get(EventParameters.CONFIGURATION_NAME);
        String formTitle = (String) params.get(EventParameters.FORM_TITLE);
        String message = (String) params.get(EventParameters.MESSAGE);
        String exception = (String) params.get(EventParameters.EXCEPTION);
        String jsonContent = (String) params.get(EventParameters.JSON_CONTENT);

        FormFailure formFailure = new FormFailure(configName, formTitle, message, exception, jsonContent);
        LOGGER.debug("Saving Form Failure");
        formFailureService.create(formFailure);
    }


}
