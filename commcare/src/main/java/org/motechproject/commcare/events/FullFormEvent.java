package org.motechproject.commcare.events;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.service.impl.CommcareFormsEventParser;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.event.MotechEvent;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.ATTRIBUTES;
import static org.motechproject.commcare.events.constants.EventDataKeys.CONFIG_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.ELEMENT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.RECEIVED_ON;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;
import static org.motechproject.commcare.events.constants.EventSubjects.DEVICE_LOG_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_EVENT;
import static org.motechproject.commcare.parser.FullFormParser.FORM;

/**
 * A utility class representing a full form event. This event signals a form received a form or a device
 * log received in Motech, coming from Commcare.
 */
public class FullFormEvent {

    /**
     * The name of the configuration.
     */
    private final String configName;

    /**
     * The date on which the form was received.
     */
    private final String receivedOn;

    /**
     * The form value.
     */
    private final String value;

    /**
     * The element name, can signify a device log.
     */
    private final String elementName;

    /**
     * The attributes of the form.
     */
    private final Map<String, String> attributes;

    /**
     * Form sub elements.
     */
    private final Multimap<String, Map<String, Object>> subElements;

    /**
     * Constructs this event.
     * @param formValueElement the full form received
     * @param receivedOn the date the form was received - as the string from Commcare
     * @param configName the name of the Motech Commcare configuration for this form
     */
    public FullFormEvent(FormValueElement formValueElement, String receivedOn, String configName) {
        this.configName = configName;
        this.receivedOn = receivedOn;
        this.value = formValueElement.getValue();
        this.elementName = formValueElement.getElementName();
        this.attributes = formValueElement.getAttributes();
        this.subElements = convertToMap(formValueElement.getSubElements());
    }

    /**
     * Builds a {@link MotechEvent} for this form event.
     * @return the Motech event to send
     */
    public MotechEvent toMotechEvent() {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(TasksEventParser.CUSTOM_PARSER_EVENT_KEY, CommcareFormsEventParser.PARSER_NAME);

        parameters.put(CONFIG_NAME, configName);

        parameters.put(RECEIVED_ON, receivedOn);
        parameters.put(VALUE, value);
        parameters.put(ELEMENT_NAME, elementName);
        parameters.put(ATTRIBUTES, attributes);
        parameters.put(SUB_ELEMENTS, subElements);

        if (FORM.equals(elementName)) {
            return new MotechEvent(FORMS_EVENT, parameters);
        } else {
            return new MotechEvent(DEVICE_LOG_EVENT, parameters);
        }
    }

    private static Multimap<String, Map<String, Object>> convertToMap(Multimap<String, FormValueElement> subElements) {
        Multimap<String, Map<String, Object>> elements = LinkedHashMultimap.create();

        for (Map.Entry<String, FormValueElement> entry : subElements.entries()) {
            Map<String, Object> elementAsMap = new HashMap<>(4); // NO CHECKSTYLE MagicNumber
            FormValueElement formValueElement = entry.getValue();

            elementAsMap.put(ELEMENT_NAME, formValueElement.getElementName());
            elementAsMap.put(SUB_ELEMENTS, convertToMap(formValueElement.getSubElements()));
            elementAsMap.put(ATTRIBUTES, formValueElement.getAttributes());
            elementAsMap.put(VALUE, formValueElement.getValue());

            elements.put(entry.getKey(), elementAsMap);
        }

        return elements;
    }
}
