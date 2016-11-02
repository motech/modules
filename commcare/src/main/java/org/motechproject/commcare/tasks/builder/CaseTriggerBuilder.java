package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;
import org.motechproject.tasks.domain.enums.ParameterType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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

    /**
     * Creates an instance of the {@link CaseTriggerBuilder} class. It will use the given {@code schemaService} and
     * {@code configService} for creating case triggers.
     *
     * @param schemaService the schema service
     * @param configService  the configuration service
     */
    public CaseTriggerBuilder(CommcareSchemaService schemaService, CommcareConfigService configService) {
        this.schemaService = schemaService;
        this.configService = configService;
    }

    @Override
    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggers = new ArrayList<>();

        Set<String> caseTypes = new HashSet<>();

        for (Config config : configService.getConfigs().getConfigs()) {
            for (CommcareApplicationJson application : schemaService.retrieveApplications(config.getName())) {
                for (CommcareModuleJson module : application.getModules()) {
                    if (!caseTypes.contains(module.getCaseType())) {
                        caseTypes.add(module.getCaseType());

                        String applicationName = application.getApplicationName();

                        List<EventParameterRequest> parameterRequests = new ArrayList<>();
                        parameterRequests.add(new EventParameterRequest("commcare.field.configName", CONFIG_NAME));

                        addCommonCaseFields(parameterRequests);

                        if (config.isEventStrategyPartial() || config.isEventStrategyFull()) {
                            addPartialCaseFields(parameterRequests);
                        }

                        if (config.isEventStrategyFull()) {
                            for (String caseProperty : module.getCaseProperties()) {
                                parameterRequests.add(new EventParameterRequest(caseProperty, caseProperty, ParameterType.DATE.getValue()));
                            }
                        }

                        String displayName = DisplayNameHelper.buildDisplayName(RECEIVED_CASE, module.getCaseType(), applicationName, config.getName());

                        triggers.add(new TriggerEventRequest(displayName, CASE_EVENT + "." + config.getName() + "." + module.getCaseType(),
                                null, parameterRequests, CASE_EVENT));
                    }
                }
            }
        }

        return triggers;
    }

    private void addCommonCaseFields(List<EventParameterRequest> parameters) {
        parameters.add(new EventParameterRequest("commcare.caseId", CASE_ID));
        parameters.add(new EventParameterRequest("commcare.caseType", CASE_TYPE));

    }

    private void addPartialCaseFields(List<EventParameterRequest> parameters) {
        parameters.add(new EventParameterRequest("commcare.userId", USER_ID));
        parameters.add(new EventParameterRequest("commcare.apiKey", API_KEY));
        parameters.add(new EventParameterRequest("commcare.caseAction", CASE_ACTION));
        parameters.add(new EventParameterRequest("commcare.dateModified", DATE_MODIFIED, ParameterType.DATE.getValue()));
        parameters.add(new EventParameterRequest("commcare.caseName", CASE_NAME));
        parameters.add(new EventParameterRequest("commcare.ownerId", OWNER_ID));
    }
}
