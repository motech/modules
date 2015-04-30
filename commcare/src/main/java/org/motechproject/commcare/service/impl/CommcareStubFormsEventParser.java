package org.motechproject.commcare.service.impl;

import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commons.api.TasksEventParser;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * The <code>CommcareFormsEventParser</code> class is an implementation of
 * {@link org.motechproject.commons.api.TasksEventParser}, that lets Tasks module
 * expose actual Commcare fields, instead of an abstract representation present
 * in the event parameters. Additionally, the event subject is adjusted that way,
 * so the same event subject can match more than one trigger on the Tasks side.
 */
@Service
public class CommcareStubFormsEventParser implements TasksEventParser {

    public static final String PARSER_NAME = "CommcareStubForms";

    @Override
    public Map<String, Object> parseEventParameters(String eventSubject, Map<String, Object> eventParameters) {
        return eventParameters;
    }

    @Override
    public String parseEventSubject(String eventSubject, Map<String, Object> eventParameters) {
        return eventSubject.concat(".").concat((String) eventParameters.get(EventDataKeys.CONFIG_NAME));
    }

    @Override
    public String getName() {
        return PARSER_NAME;
    }
}
