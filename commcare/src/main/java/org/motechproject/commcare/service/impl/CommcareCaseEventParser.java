package org.motechproject.commcare.service.impl;

import org.motechproject.commons.api.TasksEventParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_TYPE;
import static org.motechproject.commcare.events.constants.EventDataKeys.FIELD_VALUES;

/**
 * The <code>CommcareCaseEventParser</code> class is an implementation of
 * {@link org.motechproject.commons.api.TasksEventParser}, that lets Tasks module
 * expose actual Commcare fields, instead of the single map of fields present
 * in the event parameters. Additionaly, the event subject is adjusted that way,
 * so the same event subject can match more than one trigger on the Tasks side.
 */
@Service
public class CommcareCaseEventParser implements TasksEventParser {

    public static final String PARSER_NAME = "CommcareCases";

    @Override
    public Map<String, Object> parseEventParameters(String subject, Map<String, Object> eventParameters) {
        Map<String, Object> modifiedParameters = new HashMap<>();

        modifiedParameters.putAll(eventParameters);
        Map fieldValues = (Map) modifiedParameters.remove(FIELD_VALUES);

        if (fieldValues != null) {
            modifiedParameters.putAll(fieldValues);
        }

        return modifiedParameters;
    }

    @Override
    public String parseEventSubject(String eventSubject, Map<String, Object> eventParameters) {
        String subject = eventSubject;
        String casePostfix = (String) eventParameters.get(CASE_TYPE);

        if (casePostfix != null) {
            subject = subject.concat(".").concat(casePostfix);
        }

        return subject;
    }

    @Override
    public String getName() {
        return PARSER_NAME;
    }
}
