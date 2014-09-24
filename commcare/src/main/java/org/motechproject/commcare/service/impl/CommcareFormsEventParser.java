package org.motechproject.commcare.service.impl;

import com.google.common.collect.Multimap;
import org.motechproject.commons.api.TasksEventParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

import static org.motechproject.commcare.events.constants.EventDataKeys.ATTRIBUTES;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;

/**
 * The <code>CommcareFormsEventParser</code> class is an implementation of
 * {@link org.motechproject.commons.api.TasksEventParser}, that lets Tasks module
 * expose actual Commcare fields, instead of an abstract representation present
 * in the event parameters. Additionaly, the event subject is adjusted that way,
 * so the same event subject can match more than one trigger on the Tasks side.
 */
@Service
public class CommcareFormsEventParser implements TasksEventParser {

    private static final String INITIAL_PARAM_PREFIX = "/data";
    public static final String PARSER_NAME = "CommcareForms";

    @Override
    public Map<String, Object> parseEventParameters(String subject, Map<String, Object> entryParameters) {
        Map<String, Object> parsedParameters = new HashMap<>();
        addParameters(entryParameters, parsedParameters, INITIAL_PARAM_PREFIX);

        return parsedParameters;
    }

    @Override
    public String parseEventSubject(String eventSubject, Map<String, Object> eventParameters) {
        String formName = (String) ((Map) eventParameters.get(ATTRIBUTES)).get("name");
        return eventSubject.concat(".").concat(formName);
    }

    @Override
    public String getName() {
        return PARSER_NAME;
    }

    private void addParameters(Map<String, Object> parameters, Map<String, Object> parsedParameters, String paramPrefix) {
        for (Map.Entry<String, Map> entry : ((Multimap<String, Map>) parameters.get(SUB_ELEMENTS)).entries()) {
            // If there's a non-null value, add it to the parameters
            String value = (String) entry.getValue().get(VALUE);
            if (value != null) {
                parsedParameters.put(paramPrefix + "/" + entry.getKey(), value);
            }

            // Call our method recursively for subelements
            addParameters(entry.getValue(), parsedParameters, paramPrefix + "/" + entry.getKey());
        }
    }
}
