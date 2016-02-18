package org.motechproject.commcare.service.impl;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.motechproject.commons.api.TasksEventParser;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.motechproject.commcare.events.constants.EventDataKeys.ATTRIBUTES;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;
import static org.motechproject.commcare.events.constants.EventSubjects.DEVICE_LOG_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_EVENT;


/**
 * The <code>CommcareFormsEventParser</code> class is an implementation of
 * {@link org.motechproject.commons.api.TasksEventParser}, that lets Tasks module
 * expose actual Commcare fields, instead of an abstract representation present
 * in the event parameters. Additionally, the event subject is adjusted that way,
 * so the same event subject can match more than one trigger on the Tasks side.
 */
@Service
public class CommcareFormsEventParser implements TasksEventParser {

    private static final String INITIAL_PARAM_PREFIX = "/data";
    public static final String PARSER_NAME = "CommcareForms";

    @Override
    public Map<String, Object> parseEventParameters(String subject, Map<String, Object> entryParameters) {

        if (subject.equals(FORMS_EVENT) || subject.equals(DEVICE_LOG_EVENT)) {
            Map<String, Object> parsedParameters = new HashMap<>();
            parsedParameters.put("configName", entryParameters.get("configName"));
            addParameters(entryParameters, parsedParameters, INITIAL_PARAM_PREFIX);
            addCaseIdIfPresent(entryParameters, parsedParameters);
            return parsedParameters;
        } else {
            return entryParameters;
        }
    }

    @Override
    public String parseEventSubject(String eventSubject, Map<String, Object> eventParameters) {

        String configName = (String) eventParameters.get("configName");

        if (eventSubject.equals(FORMS_EVENT)) {
            String xmlns = (String) ((Map) eventParameters.get(ATTRIBUTES)).get("xmlns");
            return eventSubject.concat(".").concat(configName).concat(".").concat(xmlns);
        }

        return eventSubject.concat(".").concat(configName);
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

    private void addCaseIdIfPresent(Map<String, Object> entryParameters, Map<String, Object> parsedParameters) {
        if (((LinkedHashMultimap) entryParameters.get(SUB_ELEMENTS)).asMap().containsKey("case")) {
            Map caseMap = (Map) ((Set) ((LinkedHashMultimap) entryParameters.get(SUB_ELEMENTS)).asMap().get("case")).toArray()[0];
            Map<String, Object> caseAttrs = (Map<String, Object>) caseMap.get(ATTRIBUTES);
            parsedParameters.put(CASE_ID, caseAttrs.get("case_id"));
        }
    }
}
