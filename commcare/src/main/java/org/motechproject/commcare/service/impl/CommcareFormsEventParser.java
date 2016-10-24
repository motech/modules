package org.motechproject.commcare.service.impl;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import org.apache.commons.lang.StringUtils;
import org.motechproject.commons.api.TasksEventParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final String ID = "id";
    private static final Logger LOGGER = LoggerFactory.getLogger(CommcareFormsEventParser.class);

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
            String appId = (String) ((Map) eventParameters.get(ATTRIBUTES)).get("app_id");

            if (appId == null) {
                appId = "";
                LOGGER.warn("AppId for form is null. XMLNS: {}", xmlns);
            }
            return eventSubject.concat(".").concat(configName).concat(".").concat(xmlns).concat(appId);
        }

        return eventSubject.concat(".").concat(configName);
    }

    @Override
    public String getName() {
        return PARSER_NAME;
    }

    private void addParameters(Map<String, Object> parameters, Map<String, Object> parsedParameters, String paramPrefix) {
        Multimap<String, Map> nodes = (Multimap<String, Map>) parameters.get(SUB_ELEMENTS);

        addAttributes(parameters, parsedParameters, paramPrefix);

        for (Map.Entry<String, Map> entry : nodes.entries()) {
            String tagName = entry.getKey();
            // Create ID postfix, in case we handle repeat data
            String idPostfix = createIdPostfix(tagName, (Map<String, String>) entry.getValue().get(ATTRIBUTES), nodes);

            // If there's a non-null value, add it to the parameters
            String value = (String) entry.getValue().get(VALUE);

            if (value != null) {
                parsedParameters.put(paramPrefix + "/" + tagName + idPostfix, value);
            }

            // Call our method recursively for subelements
            addParameters(entry.getValue(), parsedParameters, paramPrefix + "/" + tagName + idPostfix);
        }
    }

    private void addAttributes(Map<String, Object> parameters, Map<String, Object> parsedParameters, String paramPrefix) {
        Map<String, String> nodes = (Map<String, String>) parameters.get(ATTRIBUTES);

        for(Map.Entry<String, String> entry: nodes.entrySet()) {
            parsedParameters.put(paramPrefix + "/@" + entry.getKey(), entry.getValue());
        }
    }

    private String createIdPostfix(String key, Map<String, String> attributes, Multimap<String, Map> nodeSubelements) {
        // We classify node as repeat data if the "id" attribute is present and there is at least one other node of the same name
        if (attributes.containsKey(ID) && nodeSubelements.get(key).size() > 1) {
            return "_" + attributes.get(ID);
        } else {
            return StringUtils.EMPTY;
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
