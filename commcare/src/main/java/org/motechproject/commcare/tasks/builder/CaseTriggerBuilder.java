package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.motechproject.commcare.events.constants.EventDataKeys.API_KEY;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_ACTION;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_ID;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.CASE_TYPE;
import static org.motechproject.commcare.events.constants.EventDataKeys.CONFIG_NAME;
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

    private CommcareSchemaService schemaService;
    private CommcareConfigService configService;

    private static final String RECEIVED_CASE = "Received Case";
    private static final String RECEIVED_CASE_ID = "Received Case ID";


    public CaseTriggerBuilder(CommcareSchemaService schemaService, CommcareConfigService configService) {
        this.schemaService = schemaService;
        this.configService = configService;
    }

    @Override
    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {
            for (Map.Entry<String, Set<String>> entry : schemaService.getAllCaseTypes(config.getName()).entrySet()) {
                List<EventParameterRequest> parameterRequests = new ArrayList<>();
                parameterRequests.add(new EventParameterRequest("commcare.field.configName", CONFIG_NAME));
                addCommonCaseFields(parameterRequests);

                for (String caseProperty : entry.getValue()) {
                    parameterRequests.add(new EventParameterRequest(caseProperty, caseProperty));
                }

                String displayName = DisplayNameHelper.buildDisplayName(RECEIVED_CASE, entry.getKey(), config.getName());

                triggers.add(new TriggerEventRequest(displayName, CASE_EVENT + "." + config.getName() + "." + entry.getKey(),
                        null, parameterRequests, CASE_EVENT));
            }
        }

        triggers.addAll(buildTriggersForMinimalStrategy());

        return triggers;
    }

    private List<TriggerEventRequest> buildTriggersForMinimalStrategy() {

        List<TriggerEventRequest> triggers = new ArrayList<>();

        for (Config config : configService.getConfigs().getConfigs()) {
            List<EventParameterRequest> parameterRequests = new ArrayList<>();
            parameterRequests.add(new EventParameterRequest("commcare.caseId", CASE_ID));
            parameterRequests.add(new EventParameterRequest("commcare.field.configName", CONFIG_NAME));

            String displayName = DisplayNameHelper.buildDisplayName(RECEIVED_CASE_ID, config.getName());

            triggers.add(new TriggerEventRequest(displayName, CASE_EVENT + "." + config.getName(), null,
                    parameterRequests, CASE_EVENT));
        }

        return triggers;
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
}
