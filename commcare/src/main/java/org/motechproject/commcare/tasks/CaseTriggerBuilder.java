package org.motechproject.commcare.tasks;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.motechproject.commcare.events.constants.EventDataKeys.API_KEY;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_ACTION;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_TYPE;
import static org.motechproject.commcare.events.constants.EventDataKeys.DATE_MODIFIED;
import static org.motechproject.commcare.events.constants.EventDataKeys.OWNER_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.USER_ID;
import static org.motechproject.commcare.events.constants.EventSubjects.CASE_EVENT;

/**
 * The <code>CaseTriggerBuilder</code> class builds case triggers for each case type
 * present in MOTECH database. Each trigger has got its own attributes, depending on
 * the case properties of each type. Some fields are common for all cases.
 */
public class CaseTriggerBuilder implements TriggerBuilder {

    private CommcareApplicationDataService applicationDataService;

    private static final String RECEIVED_CASE_PREFIX = "Received Case: ";

    public CaseTriggerBuilder(CommcareApplicationDataService applicationDataService) {
        this.applicationDataService = applicationDataService;
    }

    @Override
    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Map.Entry<String, Set<String>> entry : getAllCaseTypes().entrySet()) {
            List<EventParameterRequest> parameterRequests = new ArrayList<>();
            addCommonCaseFields(parameterRequests);

            for (String caseProperty : entry.getValue()) {
                parameterRequests.add(new EventParameterRequest(caseProperty, caseProperty));
            }

            triggers.add(new TriggerEventRequest(RECEIVED_CASE_PREFIX + entry.getKey(), CASE_EVENT + "." + entry.getKey(), null, parameterRequests, CASE_EVENT));
        }

        addTriggerForMinimalStrategy(triggers);

        return triggers;
    }

    private void addTriggerForMinimalStrategy(List<TriggerEventRequest> triggers) {
        List<EventParameterRequest> parameterRequests = new ArrayList<>();
        parameterRequests.add(new EventParameterRequest("commcare.caseId", CASE_ID));

        triggers.add(new TriggerEventRequest("Received Case ID", CASE_EVENT, null, parameterRequests, CASE_EVENT));
    }

    private void addCommonCaseFields(List<EventParameterRequest> parameters) {
        parameters.add(new EventParameterRequest("commcare.caseId", CASE_ID));
        parameters.add(new EventParameterRequest("commcare.userId", USER_ID));
        parameters.add(new EventParameterRequest("commcare.apiKey", API_KEY));
        parameters.add(new EventParameterRequest("commcare.dateModified", DATE_MODIFIED));
        parameters.add(new EventParameterRequest("commcare.caseAction", CASE_ACTION));
        parameters.add(new EventParameterRequest("commcare.caseType", CASE_TYPE));
        parameters.add(new EventParameterRequest("commcare.caseName", CASE_NAME));
        parameters.add(new EventParameterRequest("commcare.ownerId", OWNER_ID));
    }

    private Map<String, Set<String>> getAllCaseTypes() {
        Map<String, Set<String>> allCaseTypes = new HashMap<>();

        for (CommcareApplicationJson app : applicationDataService.retrieveAll()) {
            for (CommcareModuleJson module : app.getModules()) {
                String caseType = module.getCaseType();
                if (!allCaseTypes.containsKey(caseType)) {
                    allCaseTypes.put(caseType, new HashSet<>(module.getCaseProperties()));
                }
            }
        }

        return allCaseTypes;
    }
}
