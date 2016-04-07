package org.motechproject.odk.event.builder;

import org.motechproject.event.MotechEvent;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.odk.domain.Configuration;
import org.motechproject.odk.domain.FormDefinition;
import org.motechproject.odk.domain.FormElement;
import org.motechproject.odk.exception.EventBuilderException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parent class for all {@link org.motechproject.odk.event.builder.EventBuilder} implementations.
 */
public abstract class AbstractEventBuilder implements EventBuilder {

    /**
     * Builds a list of {@link MotechEvent} from the JSON payload. It will create
     * one persist form instance event and one event for each repeat group, including nested repeat
     * groups.
     * @param json JSON representation of the form instance data
     * @param formDefinition The internal representation of the XML form.
     * @param configuration {@link Configuration}
     * @return A List of {@link MotechEvent}
     * @throws EventBuilderException If an error is encountered while building the event list.
     */
    @Override
    public List<MotechEvent> createEvents(String json, FormDefinition formDefinition, Configuration configuration) throws EventBuilderException {
        Map<String, Object> data = getData(json);
        List<MotechEvent> events = createRepeatGroupEvents(data, formDefinition, configuration);
        events.add(createFormEvent(data, formDefinition, configuration));
        return events;
    }

    private MotechEvent createFormEvent(Map<String, Object> data, FormDefinition formDefinition, Configuration configuration) throws EventBuilderException {
        Map<String, Object> params = new HashMap<>();
        for (FormElement formElement : formDefinition.getFormElements()) {
            Object value = data.get(formElement.getName());
            Object formattedValue = formatValue(formElement.getType(), value);
            if (formattedValue != null) {
                params.put(formElement.getName(), formattedValue);
            }
        }

        params.put(EventParameters.FORM_TITLE, formDefinition.getTitle());
        params.put(EventParameters.CONFIGURATION_NAME, configuration.getName());
        String subject = EventSubjects.RECEIVED_FORM + "." + configuration.getName() + "." + formDefinition.getTitle();
        return new MotechEvent(subject, params);
    }


    private List<MotechEvent> createRepeatGroupEvents(Map<String, Object> data, FormDefinition formDefinition, Configuration configuration) throws EventBuilderException {
        List<MotechEvent> events = new ArrayList<>();
        Map<String, Object> rootScopeValues = getRootScope(data, formDefinition);
        List<FormElement> repeatGroups = getNonNestedRepeatGroups(formDefinition);

        for (FormElement repeatGroup : repeatGroups) {
            List<Map<String, Object>> repeatGroupInstances = (List<Map<String, Object>>) data.get(repeatGroup.getName());
            for (Map<String, Object> repeatGroupInstance : repeatGroupInstances) {
                createRepeatGroupEvent(repeatGroup, events, formDefinition, repeatGroupInstance, rootScopeValues, configuration.getName());
            }
        }
        return events;
    }

    private Map<String, Object> getRootScope(Map<String, Object> data, FormDefinition formDefinition) throws EventBuilderException {
        Map<String, Object> values = new HashMap<>();

        for (FormElement formElement : formDefinition.getFormElements()) {
            if (!formElement.isRepeatGroup() && !formElement.isPartOfRepeatGroup()) {

                Object value = data.get(formElement.getName());
                if (value != null) {
                    values.put(formElement.getName(), formatValue(formElement.getType(), value));
                } else {
                    values.put(formElement.getName(), null);
                }
            }
        }
        return values;
    }

    private List<FormElement> getNonNestedRepeatGroups(FormDefinition formDefinition) {
        List<FormElement> formElements = new ArrayList<>();

        for (FormElement formElement : formDefinition.getFormElements()) {
            if (!formElement.isPartOfRepeatGroup() && formElement.isRepeatGroup()) {
                formElements.add(formElement);
            }
        }
        return formElements;
    }

    private void createRepeatGroupEvent(FormElement repeatGroup, List<MotechEvent> events, FormDefinition formDefinition, Map<String, Object> data,
                                        Map<String, Object> scope, String configName) throws EventBuilderException {
        List<FormElement> childRepeatGroups = new ArrayList<>();
        Map<String, Object> localScope = new HashMap<>();
        localScope.putAll(scope);

        for (FormElement child : repeatGroup.getChildren()) {
            if (child.isRepeatGroup()) {
                childRepeatGroups.add(child);
            } else {
                Object value = data.get(child.getName());
                localScope.put(child.getName(), formatValue(child.getType(), value));
            }
        }

        String subject = EventSubjects.REPEAT_GROUP + "." + configName + "." + formDefinition.getTitle() + "." + repeatGroup.getName();
        events.add(new MotechEvent(subject, localScope));

        for (FormElement childRepeatGroup : childRepeatGroups) {
            List<Map<String, Object>> childRepeatGroupInstances = (List<Map<String, Object>>) data.get(childRepeatGroup.getName());

            for (Map<String, Object> childInstance : childRepeatGroupInstances) {
                createRepeatGroupEvent(childRepeatGroup, events, formDefinition, childInstance, localScope, configName);
            }
        }
    }

    protected abstract Object formatValue(String type, Object value) throws EventBuilderException;

    protected abstract Map<String, Object> getData(String json) throws EventBuilderException;


}
