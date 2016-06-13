package org.motechproject.openmrs.tasks.builder;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.tasks.constants.DisplayNames;
import org.motechproject.openmrs.tasks.constants.Keys;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;
import org.motechproject.tasks.domain.mds.ParameterType;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.motechproject.tasks.domain.mds.ParameterType.BOOLEAN;
import static org.motechproject.tasks.domain.mds.ParameterType.DATE;
import static org.motechproject.tasks.domain.mds.ParameterType.MAP;

/**
 * Responsible for building actions for the Tasks channel.
 */
public class ActionBuilder {
    private static final String CREATE_ENCOUNTER = "Create Encounter";
    private static final String CREATE_PATIENT = "Create Patient";
    private static final String UPDATE_PATIENT_IDENTIFIERS = "Update Patient Identifiers";
    private static final String UPDATE_PERSON = "Update Person";
    private static final String CREATE_PROGRAM_ENROLLMENT = "Create Program Enrollment";
    private static final String CHANGE_PROGRAM_ENROLLMENT_STATE = "Change Program Enrollment State";
    private static final String OPENMRS_ACTION_PROXY_SERVICE = "org.motechproject.openmrs.tasks.OpenMRSActionProxyService";
    private static final String OPENMRS_V1_12 = "1.10+";

    private OpenMRSConfigService configService;

    public ActionBuilder(OpenMRSConfigService configService) {
        this.configService = configService;
    }

    /**
     * Builds a list of actions that can be used for creating channel for the Task module.
     *
     * @return the list of actions
     */
    public List<ActionEventRequest> buildActions() {
        List<ActionEventRequest> actions = new ArrayList<>();
        for (Config config : configService.getConfigs().getConfigs()) {
            String configName = config.getName();
            actions.add(buildCreateEncounterAction(configName));
            actions.add(buildCreatePatientAction(configName));
            actions.add(buildUpdatePatientAction(configName));
            if(config.getOpenMrsVersion().equals(OPENMRS_V1_12)) {
                actions.add(buildCreateProgramEnrollmentAction(configName));
                actions.add(buildChangeStateOfProgramEnrollmentAction(configName));
            }
            actions.add(buildUpdatePatientIdentifiersAction(configName));
        }
        return actions;
    }

    private ActionEventRequest buildCreateEncounterAction(String configName) {
        SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
        int order = 0;

        actionParameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, true, true,
                order++));
        actionParameters.add(prepareParameter(Keys.ENCOUNTER_DATE, DisplayNames.ENCOUNTER_DATE, DATE, true, order++));
        actionParameters.add(prepareParameter(Keys.ENCOUNTER_TYPE, DisplayNames.ENCOUNTER_TYPE, true, order++));
        actionParameters.add(prepareParameter(Keys.LOCATION_NAME, DisplayNames.LOCATION_NAME, false, order++));
        actionParameters.add(prepareParameter(Keys.PATIENT_UUID, DisplayNames.PATIENT_UUID, true, order++));
        actionParameters.add(prepareParameter(Keys.PROVIDER_UUID, DisplayNames.PROVIDER_UUID, true, order++));
        actionParameters.add(prepareParameter(Keys.OBSERVATION, DisplayNames.OBSERVATION, MAP, false, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_ENCOUNTER, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod("createEncounter")
                .setActionParameters(actionParameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildCreatePatientAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.addAll(prepareCommonParameters(order));
        order = parameters.size();
        parameters.add(prepareParameter(Keys.MOTECH_ID, DisplayNames.MOTECH_ID, true, order++));
        parameters.add(prepareParameter(Keys.LOCATION_FOR_MOTECH_ID, DisplayNames.LOCATION_FOR_MOTECH_ID, false,
                order++));
        parameters.add(prepareParameter(Keys.IDENTIFIERS, DisplayNames.IDENTIFIERS, MAP, false, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_PATIENT, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod("createPatient")
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildUpdatePatientAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.PERSON_UUID, DisplayNames.PERSON_UUID, true, order++));
        parameters.addAll(prepareCommonParameters(order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(UPDATE_PERSON, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod("updatePerson")
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildCreateProgramEnrollmentAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();

        int order = 0;

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.PATIENT_UUID, DisplayNames.PATIENT_UUID, true, order++));
        parameters.add(prepareParameter(Keys.PROGRAM_UUID, DisplayNames.PROGRAM_UUID, true, order++));
        parameters.add(prepareParameter(Keys.DATE_ENROLLED, DisplayNames.DATE_ENROLLED, DATE, true, order++));
        parameters.add(prepareParameter(Keys.DATE_COMPLETED, DisplayNames.DATE_COMPLETED, DATE, false, order++));
        parameters.add(prepareParameter(Keys.LOCATION_NAME, DisplayNames.LOCATION_NAME, false, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_PROGRAM_ENROLLMENT, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod("createProgramEnrollment")
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildChangeStateOfProgramEnrollmentAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();

        int order = 0;

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.PROGRAM_ENROLLMENT_UUID, DisplayNames.PROGRAM_ENROLLMENT_UUID, true, order++));
        parameters.add(prepareParameter(Keys.DATE_COMPLETED, DisplayNames.DATE_COMPLETED, DATE, false, order++));
        parameters.add(prepareParameter(Keys.STATE_UUID, DisplayNames.STATE_UUID, false, order++));
        parameters.add(prepareParameter(Keys.STATE_START_DATE, DisplayNames.STATE_START_DATE, DATE, false, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CHANGE_PROGRAM_ENROLLMENT_STATE, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod("changeStateOfProgramEnrollment")
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildUpdatePatientIdentifiersAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.PERSON_UUID, DisplayNames.PERSON_UUID, true, order++));
        parameters.add(prepareParameter(Keys.IDENTIFIERS, DisplayNames.IDENTIFIERS, MAP, true, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(UPDATE_PATIENT_IDENTIFIERS, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod("updatePatientIdentifiers")
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private SortedSet<ActionParameterRequest> prepareCommonParameters(int startOrder) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = startOrder;

        parameters.add(prepareParameter(Keys.GIVEN_NAME, DisplayNames.GIVEN_NAME, true, order++));
        parameters.add(prepareParameter(Keys.MIDDLE_NAME, DisplayNames.MIDDLE_NAME, false, order++));
        parameters.add(prepareParameter(Keys.FAMILY_NAME, DisplayNames.FAMILY_NAME, true, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_1, DisplayNames.ADDRESS_1, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_2, DisplayNames.ADDRESS_2, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_3, DisplayNames.ADDRESS_3, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_4, DisplayNames.ADDRESS_4, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_5, DisplayNames.ADDRESS_5, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_6, DisplayNames.ADDRESS_6, false, order++));
        parameters.add(prepareParameter(Keys.CITY_VILLAGE, DisplayNames.CITY_VILLAGE, false, order++));
        parameters.add(prepareParameter(Keys.STATE_PROVINCE, DisplayNames.STATE_PROVINCE, false, order++));
        parameters.add(prepareParameter(Keys.COUNTRY, DisplayNames.COUNTRY, false, order++));
        parameters.add(prepareParameter(Keys.POSTAL_CODE, DisplayNames.POSTAL_CODE, false, order++));
        parameters.add(prepareParameter(Keys.COUNTY_DISTRICT, DisplayNames.COUNTY_DISTRICT, false, order++));
        parameters.add(prepareParameter(Keys.LATITUDE, DisplayNames.LATITUDE, false, order++));
        parameters.add(prepareParameter(Keys.LONGITUDE, DisplayNames.LONGITUDE, false, order++));
        parameters.add(prepareParameter(Keys.START_DATE, DisplayNames.START_DATE, DATE, false, order++));
        parameters.add(prepareParameter(Keys.END_DATE, DisplayNames.END_DATE, DATE, false, order++));
        parameters.add(prepareParameter(Keys.BIRTH_DATE, DisplayNames.BIRTH_DATE, DATE, false, order++));
        parameters.add(prepareParameter(Keys.BIRTH_DATE_ESTIMATED, DisplayNames.BIRTH_DATE_ESTIMATED, BOOLEAN,
                false, order++));
        parameters.add(prepareParameter(Keys.GENDER, DisplayNames.GENDER, true, order++));
        parameters.add(prepareParameter(Keys.DEAD, DisplayNames.DEAD, BOOLEAN, false, order++));
        parameters.add(prepareParameter(Keys.CAUSE_OF_DEATH_UUID, DisplayNames.CAUSE_OF_DEATH_UUID, false,
                order));

        return parameters;
    }

    private ActionParameterRequest prepareParameter(String key, String displayName, boolean required,
                                                    int order) {
        return prepareParameterBuilder(key, displayName, required, order).createActionParameterRequest();
    }

    private ActionParameterRequest prepareParameter(String key, String displayName, ParameterType type,
                                                    boolean required, int order) {
        return prepareParameterBuilder(key, displayName, required, order)
                .setType(type.toString())
                .createActionParameterRequest();
    }

    private ActionParameterRequest prepareParameter(String key, String displayName, String value, boolean required,
                                                    boolean hidden, int order) {
        return prepareParameterBuilder(key, displayName, required, order)
                .setValue(value)
                .setHidden(hidden)
                .createActionParameterRequest();
    }

    private ActionParameterRequestBuilder prepareParameterBuilder(String key, String displayName, boolean required,
                                                                  int order) {
        return new ActionParameterRequestBuilder()
                .setKey(key)
                .setDisplayName(displayName)
                .setRequired(required)
                .setOrder(order);
    }

    private String getDisplayName(String actionName, String configName) {
        return String.format("%s [%s]", actionName, configName);
    }
}
