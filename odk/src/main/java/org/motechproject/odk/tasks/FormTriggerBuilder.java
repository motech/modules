package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.constant.TasksDataTypes;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds A list of {@link TriggerEventRequest } for full forms.
 */
public class FormTriggerBuilder {

    private List<FormDefinition> formDefinitions;


    public FormTriggerBuilder(List<FormDefinition> formDefinitions) {
        this.formDefinitions = formDefinitions;
    }

    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggerEventRequests = new ArrayList<>();

        for (FormDefinition formDefinition : formDefinitions) {
            List<EventParameterRequest> eventParameterRequests = buildEventParameterRequests(formDefinition);
            TriggerEventRequest eventRequest = new TriggerEventRequest(DisplayNames.FORM_TRIGGER_DISPLAY_NAME + " [Configuration: " +
                    formDefinition.getConfigurationName() + "] " + "[Title: " + formDefinition.getTitle() + "]",
                    EventSubjects.RECEIVED_FORM + "." + formDefinition.getConfigurationName() + "." + formDefinition.getTitle(), null, eventParameterRequests);
            triggerEventRequests.add(eventRequest);
        }
        triggerEventRequests.add(buildFailureEventTrigger());
        return triggerEventRequests;
    }


    private List<EventParameterRequest> buildEventParameterRequests(FormDefinition formDefinition) {
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();

        for (FormElement formElement : formDefinition.getFormElements()) {
            if (!formElement.isPartOfRepeatGroup()) {
                String type = TypeMapper.getType(formElement.getType());
                EventParameterRequest request = new EventParameterRequest(formElement.getLabel(), formElement.getName(), type);
                eventParameterRequests.add(request);
            }
        }
        eventParameterRequests.addAll(addTitleAndConfigFields());
        return eventParameterRequests;
    }

    private List<EventParameterRequest> addTitleAndConfigFields() {
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.FORM_TITLE, EventParameters.FORM_TITLE, TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.CONFIGURATION_NAME, EventParameters.CONFIGURATION_NAME, TasksDataTypes.UNICODE));
        return eventParameterRequests;
    }


    private TriggerEventRequest buildFailureEventTrigger() {
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.CONFIGURATION_NAME, EventParameters.CONFIGURATION_NAME, TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.EXCEPTION, EventParameters.EXCEPTION, TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.FORM_TITLE, EventParameters.FORM_TITLE, TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.MESSAGE, EventParameters.MESSAGE, TasksDataTypes.UNICODE));
        eventParameterRequests.add(new EventParameterRequest(DisplayNames.JSON_CONTENT, EventParameters.JSON_CONTENT, TasksDataTypes.UNICODE));
        return new TriggerEventRequest(DisplayNames.FORM_FAIL, EventSubjects.FORM_FAIL, null, eventParameterRequests);
    }

}
