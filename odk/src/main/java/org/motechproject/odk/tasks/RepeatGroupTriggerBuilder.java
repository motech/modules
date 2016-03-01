package org.motechproject.odk.tasks;

import org.motechproject.odk.constant.DisplayNames;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;



/**
 * Builds A list of {@link TriggerEventRequest } for repeat groups.
 */
public class RepeatGroupTriggerBuilder {

    private List<FormDefinition> formDefinitions;
    private List<TriggerEventRequest> triggerEventRequests;


    public RepeatGroupTriggerBuilder(List<FormDefinition> formDefinitions) {
        this.formDefinitions = formDefinitions;
        this.triggerEventRequests = new ArrayList<>();

    }

    public List<TriggerEventRequest> buildTriggers() {

        for (FormDefinition formDefinition : formDefinitions) {
            buildTriggersForFormDef(formDefinition);
        }
        return triggerEventRequests;
    }

    private void buildTriggersForFormDef(FormDefinition formDefinition) {
        List<FormElement> rootScope = getRootScope(formDefinition);

        for (FormElement formElement : formDefinition.getFormElements()) {
            if (formElement.isRepeatGroup() && !formElement.isPartOfRepeatGroup()) {
                buildTriggerForRepeatGroup(formElement, rootScope, formDefinition.getTitle(), formDefinition.getConfigurationName());
            }
        }
    }

    private List<FormElement> getRootScope(FormDefinition formDefinition) {
        List<FormElement> rootScope = new ArrayList<>();

        for (FormElement formElement : formDefinition.getFormElements()) {
            if (!formElement.isRepeatGroup() && !formElement.isPartOfRepeatGroup()) {
                rootScope.add(formElement);
            }
        }
        return rootScope;
    }


    private void buildTriggerForRepeatGroup(FormElement repeatGroup, List<FormElement> scope, String title, String configName) {
        List<FormElement> localScope = addFieldsToScope(repeatGroup, scope);
        List<EventParameterRequest> eventParameterRequests = new ArrayList<>();

        for (FormElement field : localScope) {
            eventParameterRequests.add(new EventParameterRequest(field.getLabel(), field.getName(), TypeMapper.getType(field.getType())));
        }

        triggerEventRequests.add(new TriggerEventRequest(formatDisplayName(repeatGroup, title, configName), formatEventSubject(repeatGroup, title, configName), null, eventParameterRequests));

        for (FormElement child : repeatGroup.getChildren()) {
            if (child.isRepeatGroup()) {
                buildTriggerForRepeatGroup(child, localScope, title, configName);
            }
        }
    }

    private List<FormElement> addFieldsToScope(FormElement formElement, List<FormElement> scope) {
        List<FormElement> localScope = new ArrayList<>();
        localScope.addAll(scope);

        for (FormElement child : formElement.getChildren()) {
            if (!child.isRepeatGroup()) {
                localScope.add(child);
            }
        }
        return localScope;
    }

    private String formatDisplayName(FormElement repeatGroup, String title, String configName) {
        return DisplayNames.REPEAT_GROUP + "[Configuration: " + configName + "][Title: " + title + "][Repeat Group: " + repeatGroup.getLabel() + "]";
    }

    private String formatEventSubject(FormElement repeatGroup, String title, String configName) {
        return EventSubjects.REPEAT_GROUP + "." + configName + "." + title + "." + repeatGroup.getName();
    }
}
