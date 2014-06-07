package org.motechproject.commcare.web;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.exception.FullFormParserException;
import org.motechproject.commcare.parser.FullFormParser;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import static org.motechproject.commcare.events.constants.EventDataKeys.RECEIVED_ON;
import static org.motechproject.commcare.events.constants.EventDataKeys.ATTRIBUTES;
import static org.motechproject.commcare.events.constants.EventDataKeys.ELEMENT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;
import static org.motechproject.commcare.events.constants.EventSubjects.DEVICE_LOG_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_FAIL_EVENT;
import static org.motechproject.commcare.events.constants.EventDataKeys.FAILED_FORM_MESSAGE;
import static org.motechproject.commcare.parser.FullFormParser.FORM;

/**
 * Controller that handles the incoming full form feed from CommCareHQ.
 */
@Controller
public class FullFormController {
    private EventRelay eventRelay;

    @Autowired
    public FullFormController(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    @RequestMapping(value = "/forms")
    @ResponseStatus(HttpStatus.OK)
    public void receiveForm(@RequestBody String body, HttpServletRequest request) {
        FullFormParser parser = new FullFormParser(body);
        FormValueElement formValueElement = null;
        Map<String, Object> parameters = new HashMap<>();

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
            failedEvent.getParameters().put(FAILED_FORM_MESSAGE, e.getMessage());
            eventRelay.sendEventMessage(failedEvent);
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
}
