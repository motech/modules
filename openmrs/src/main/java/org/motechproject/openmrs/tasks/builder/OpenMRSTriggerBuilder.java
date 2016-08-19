package org.motechproject.openmrs.tasks.builder;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.tasks.constants.DisplayNames;
import org.motechproject.openmrs.tasks.constants.Keys;
import org.motechproject.openmrs.tasks.constants.EventSubjects;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * The <code>OpenMRSTriggerBuilder</code> class builds triggers for OpenMRS module.
 */
public class OpenMRSTriggerBuilder {
    private OpenMRSConfigService configService;

    public OpenMRSTriggerBuilder(OpenMRSConfigService configService) {
        this.configService = configService;
    }

    /**
     * Builds a list of triggers that can be used for creating channel for the Task module.
     *
     * @return the list of actions
     */
    public List<TriggerEventRequest> buildTriggers() {
        List<TriggerEventRequest> triggers = new ArrayList<>();
        for (Config config : configService.getConfigs().getConfigs()) {
            String configName = config.getName();
            triggers.addAll(buildCohortQueryTriggers(configName));
        }
        return triggers;
    }

    private List<TriggerEventRequest> buildCohortQueryTriggers(String configName) {
        List<TriggerEventRequest> triggers = new ArrayList<>();

        List<EventParameterRequest> parameterRequests = new ArrayList<>();
        parameterRequests.add(new EventParameterRequest(DisplayNames.PERSON_UUID, Keys.PATIENT_UUID));
        parameterRequests.add(new EventParameterRequest(DisplayNames.PATIENT_DISPLAY, Keys.PATIENT_DISPLAY));
        parameterRequests.add(new EventParameterRequest(DisplayNames.COHORT_QUERY_UUID, Keys.COHORT_QUERY_UUID));

        String displayName = DisplayNameHelper.buildDisplayName(DisplayNames.COHORT_QUERY_REPORTMEMBER, configName);

        triggers.add(new TriggerEventRequest(displayName, EventSubjects.GET_COHORT_QUERY_MEMBER_EVENT, null, parameterRequests));

        return triggers;
    }
}
