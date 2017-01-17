package org.motechproject.openmrs.tasks.builder;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Order;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.tasks.constants.DisplayNames;
import org.motechproject.openmrs.tasks.constants.Keys;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;
import org.motechproject.tasks.domain.enums.ParameterType;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.motechproject.tasks.domain.enums.ParameterType.BOOLEAN;
import static org.motechproject.tasks.domain.enums.ParameterType.DATE;
import static org.motechproject.tasks.domain.enums.ParameterType.MAP;
import static org.motechproject.tasks.domain.enums.ParameterType.TEXTAREA;

/**
 * Responsible for building actions for the Tasks channel.
 */
public class ActionBuilder {
    private static final String CREATE_ENCOUNTER = "Create Encounter";
    private static final String CREATE_PATIENT = "Create Patient";
    private static final String UPDATE_PATIENT_IDENTIFIERS = "Update Patient Identifiers";
    private static final String UPDATE_PERSON = "Update Person";
    private static final String CREATE_OBSERVATION_JSON = "Create Observation JSON";
    private static final String CREATE_VISIT = "Create Visit";
    private static final String CREATE_PROGRAM_ENROLLMENT = "Create Program Enrollment";
    private static final String UPDATE_PROGRAM_ENROLLMENT = "Update Program Enrollment";
    private static final String CHANGE_PROGRAM_ENROLLMENT_STATE = "Change Program Enrollment State";
    private static final String GET_COHORT_QUERY_REPORT = "Get CohortQuery Report";
    private static final String CREATE_ORDER = "Create Order";
    private static final String OPENMRS_ACTION_PROXY_SERVICE = "org.motechproject.openmrs.tasks.OpenMRSActionProxyService";
    private static final String OPENMRS_V1_9 = "1.9";

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
            actions.add(buildCreateObservationJSON(configName));
            actions.add(buildCreateVisitAction(configName));
            actions.add(buildUpdatePatientIdentifiersAction(configName));
            actions.add(buildGetCohortQueryReport(configName));

            buildActionsForVersionGreaterThan19(config, configName, actions);

        }
        return actions;
    }

    private void buildActionsForVersionGreaterThan19(Config config, String configName, List<ActionEventRequest> actions) {

        if (!OPENMRS_V1_9.equals(config.getOpenMrsVersion())) {
            actions.add(buildCreateProgramEnrollmentAction(configName));
            actions.add(buildUpdateProgramEnrollmentAction(configName));
            actions.add(buildChangeStateOfProgramEnrollmentAction(configName));
            actions.add(buildCreateOrderAction(configName));
        }
    }

    private ActionEventRequest buildCreateEncounterAction(String configName) {
        SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
        SortedSet<ActionParameterRequest> postActionParameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "createEncounter";

        actionParameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, true, true,
                order++));
        actionParameters.add(prepareParameter(Keys.ENCOUNTER_DATE, DisplayNames.ENCOUNTER_DATE, DATE, true, order++));
        actionParameters.add(prepareParameter(Keys.ENCOUNTER_TYPE, DisplayNames.ENCOUNTER_TYPE, true, order++));
        actionParameters.add(prepareParameter(Keys.LOCATION_NAME, DisplayNames.LOCATION_NAME, TEXTAREA, false, order++));
        actionParameters.add(prepareParameter(Keys.PATIENT_UUID, DisplayNames.PATIENT_UUID, true, order++));
        actionParameters.add(prepareParameter(Keys.PROVIDER_UUID, DisplayNames.PROVIDER_UUID, true, order++));
        actionParameters.add(prepareParameter(Keys.VISIT_UUID, DisplayNames.VISIT_UUID, false, order++));
        actionParameters.add(prepareParameter(Keys.FORM, DisplayNames.FORM, false, order++));
        actionParameters.add(prepareParameter(Keys.OBSERVATION, DisplayNames.OBSERVATION, MAP, false, order));

        postActionParameters.add(prepareParameter(Keys.UUID, DisplayNames.ENCOUNTER_UUID, false, 0));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_ENCOUNTER, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(actionParameters)
                .setPostActionParameters(postActionParameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildCreatePatientAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        SortedSet<ActionParameterRequest> postActionParameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "createPatient";

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.addAll(prepareCommonParameters(order));
        order = parameters.size();
        parameters.add(prepareParameter(Keys.MOTECH_ID, DisplayNames.MOTECH_ID, true, order++));
        parameters.add(prepareParameter(Keys.LOCATION_FOR_MOTECH_ID, DisplayNames.LOCATION_FOR_MOTECH_ID, false,
                order++));
        parameters.add(prepareParameter(Keys.IDENTIFIERS, DisplayNames.IDENTIFIERS, MAP, false, order++));
        parameters.add(prepareParameter(Keys.PERSON_ATTRIBUTES, DisplayNames.PERSON_ATTRIBUTES, MAP, false, order));

        postActionParameters.add(prepareParameter(Keys.UUID, DisplayNames.PATIENT_UUID, false, 0));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_PATIENT, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(parameters)
                .setPostActionParameters(postActionParameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildUpdatePatientAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "updatePerson";

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.PERSON_UUID, DisplayNames.PERSON_UUID, true, order++));
        parameters.addAll(prepareCommonParameters(order));
        order = parameters.size();
        parameters.add(prepareParameter(Keys.PERSON_ATTRIBUTES, DisplayNames.PERSON_ATTRIBUTES, MAP, false, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(UPDATE_PERSON, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildCreateObservationJSON(String configName) {
        SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "createObservationJSON";
        String defaultValueForJsonField = "{}";

        actionParameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, true, true, order++));
        actionParameters.add(prepareParameter(Keys.OBSERVATION_JSON, DisplayNames.OBSERVATION_JSON, TEXTAREA,
                defaultValueForJsonField, true, order++));
        actionParameters.add(prepareParameter(Keys.ENCOUNTER_UUID, DisplayNames.ENCOUNTER_UUID, false, order++));
        actionParameters.add(prepareParameter(Keys.CONCEPT_UUID, DisplayNames.CONCEPT_UUID, false, order++));
        actionParameters.add(prepareParameter(Keys.OBSERVATION_DATETIME, DisplayNames.OBSERVATION_DATETIME, DATE, false, order++));
        actionParameters.add(prepareParameter(Keys.ORDER_UUID, DisplayNames.ORDER_UUID, false, order++));
        actionParameters.add(prepareParameter(Keys.COMMENT, DisplayNames.COMMENT, TEXTAREA, false, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_OBSERVATION_JSON, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(actionParameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildCreateVisitAction(String configName) {
        SortedSet<ActionParameterRequest> actionParameters = new TreeSet<>();
        SortedSet<ActionParameterRequest> postActionParameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "createVisit";

        actionParameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, true, true,
                order++));
        actionParameters.add(prepareParameter(Keys.PATIENT_UUID, DisplayNames.PATIENT_UUID, true, order++));
        actionParameters.add(prepareParameter(Keys.VISIT_START_DATETIME, DisplayNames.VISIT_START_DATETIME, DATE, true, order++));
        actionParameters.add(prepareParameter(Keys.VISIT_STOP_DATETIME, DisplayNames.VISIT_STOP_DATETIME, DATE, true, order++));
        actionParameters.add(prepareParameter(Keys.VISIT_TYPE_UUID, DisplayNames.VISIT_TYPE_UUID, true, order++));
        actionParameters.add(prepareParameter(Keys.LOCATION_NAME, DisplayNames.LOCATION_NAME, TEXTAREA, false, order));

        postActionParameters.add(prepareParameter(Keys.UUID, DisplayNames.VISIT_UUID, false, 0));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_VISIT, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(actionParameters)
                .setPostActionParameters(postActionParameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildCreateProgramEnrollmentAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "createProgramEnrollment";

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.PATIENT_UUID, DisplayNames.PATIENT_UUID, true, order++));
        parameters.add(prepareParameter(Keys.PROGRAM_UUID, DisplayNames.PROGRAM_UUID, true, order++));
        parameters.add(prepareParameter(Keys.DATE_ENROLLED, DisplayNames.DATE_ENROLLED, DATE, true, order++));
        parameters.add(prepareParameter(Keys.DATE_COMPLETED, DisplayNames.DATE_COMPLETED, DATE, false, order++));
        parameters.add(prepareParameter(Keys.LOCATION_NAME, DisplayNames.LOCATION_NAME, false, order++));
        parameters.add(prepareParameter(Keys.PROGRAM_ENROLLMENT_ATTRIBUTES, DisplayNames.PROGRAM_ENROLLMENT_ATTRIBUTES,
                MAP, false, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_PROGRAM_ENROLLMENT, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildUpdateProgramEnrollmentAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "updateProgramEnrollment";

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.addAll(prepareProgramEnrollmentParameters(order, true));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(UPDATE_PROGRAM_ENROLLMENT, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildChangeStateOfProgramEnrollmentAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "changeStateOfProgramEnrollment";

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.addAll(prepareProgramEnrollmentParameters(order, false));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CHANGE_PROGRAM_ENROLLMENT_STATE, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildUpdatePatientIdentifiersAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "updatePatientIdentifiers";

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.PERSON_UUID, DisplayNames.PERSON_UUID, true, order++));
        parameters.add(prepareParameter(Keys.IDENTIFIERS, DisplayNames.IDENTIFIERS, MAP, true, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(UPDATE_PATIENT_IDENTIFIERS, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildGetCohortQueryReport(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = 0;
        String serviceMethod = "getCohortQueryReport";

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.COHORT_QUERY_UUID, DisplayNames.COHORT_QUERY_UUID, true, order++));
        parameters.add(prepareParameter(Keys.COHORT_QUERY_PARAMETERS, DisplayNames.COHORT_QUERY_PARAMETERS, MAP, false, order));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(GET_COHORT_QUERY_REPORT, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(parameters)
                .createActionEventRequest();
    }

    private ActionEventRequest buildCreateOrderAction(String configName) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        SortedSet<ActionParameterRequest> postActionParameters = new TreeSet<>();

        int order = 0;
        String serviceMethod = "createOrder";

        parameters.add(prepareParameter(Keys.CONFIG_NAME, DisplayNames.CONFIG_NAME, configName, false, true, order++));
        parameters.add(prepareParameter(Keys.ORDER_TYPE, DisplayNames.ORDER_TYPE, ParameterType.UNICODE, Order.DEFAULT_TYPE, true, order++));
        parameters.add(prepareParameter(Keys.ENCOUNTER_UUID, DisplayNames.ENCOUNTER_UUID, true, order++));
        parameters.add(prepareParameter(Keys.PATIENT_UUID, DisplayNames.PATIENT_UUID, true, order++));
        parameters.add(prepareParameter(Keys.CONCEPT_UUID, DisplayNames.CONCEPT_UUID, true, order++));
        parameters.add(prepareParameter(Keys.ORDERER_UUID, DisplayNames.ORDER_ORDERER_UUID, true, order++));
        parameters.add(prepareParameter(Keys.CARE_SETTING, DisplayNames.ORDER_CARE_SETTING, ParameterType.SELECT,
                Order.CareSetting.getValuesAsStringSet(), true, order));

        postActionParameters.add(prepareParameter(Keys.UUID, DisplayNames.ORDER_UUID, false, 0));

        return new ActionEventRequestBuilder()
                .setDisplayName(getDisplayName(CREATE_ORDER, configName))
                .setServiceInterface(OPENMRS_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(getSubject(serviceMethod, configName))
                .setActionParameters(parameters)
                .setPostActionParameters(postActionParameters)
                .createActionEventRequest();
    }

    private SortedSet<ActionParameterRequest> prepareProgramEnrollmentParameters(int startOrder, boolean addAttributeMap) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = startOrder;

        parameters.add(prepareParameter(Keys.PROGRAM_ENROLLMENT_UUID, DisplayNames.PROGRAM_ENROLLMENT_UUID, true, order++));
        parameters.add(prepareParameter(Keys.DATE_COMPLETED, DisplayNames.DATE_COMPLETED, DATE, false, order++));
        parameters.add(prepareParameter(Keys.STATE_UUID, DisplayNames.STATE_UUID, false, order++));
        parameters.add(prepareParameter(Keys.STATE_START_DATE, DisplayNames.STATE_START_DATE, DATE, false, order++));

        if (addAttributeMap) {
            parameters.add(prepareParameter(Keys.PROGRAM_ENROLLMENT_ATTRIBUTES, DisplayNames.PROGRAM_ENROLLMENT_ATTRIBUTES, MAP, false, order));
        }

        return parameters;
    }

    private SortedSet<ActionParameterRequest> prepareCommonParameters(int startOrder) {
        SortedSet<ActionParameterRequest> parameters = new TreeSet<>();
        int order = startOrder;

        parameters.add(prepareParameter(Keys.GIVEN_NAME, DisplayNames.GIVEN_NAME, TEXTAREA, true, order++));
        parameters.add(prepareParameter(Keys.MIDDLE_NAME, DisplayNames.MIDDLE_NAME, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.FAMILY_NAME, DisplayNames.FAMILY_NAME, TEXTAREA, true, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_1, DisplayNames.ADDRESS_1, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_2, DisplayNames.ADDRESS_2, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_3, DisplayNames.ADDRESS_3, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_4, DisplayNames.ADDRESS_4, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_5, DisplayNames.ADDRESS_5, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.ADDRESS_6, DisplayNames.ADDRESS_6, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.CITY_VILLAGE, DisplayNames.CITY_VILLAGE, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.STATE_PROVINCE, DisplayNames.STATE_PROVINCE, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.COUNTRY, DisplayNames.COUNTRY, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.POSTAL_CODE, DisplayNames.POSTAL_CODE, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.COUNTY_DISTRICT, DisplayNames.COUNTY_DISTRICT, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.LATITUDE, DisplayNames.LATITUDE, TEXTAREA, false, order++));
        parameters.add(prepareParameter(Keys.LONGITUDE, DisplayNames.LONGITUDE, TEXTAREA, false, order++));
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

    private ActionParameterRequest prepareParameter(String key, String displayName, ParameterType type,
                                                    SortedSet<String> options, boolean required, int order) {
        return prepareParameterBuilder(key, displayName, required, order)
                .setType(type.toString())
                .setOptions(options)
                .createActionParameterRequest();
    }

    private ActionParameterRequest prepareParameter(String key, String displayName, ParameterType type, String value,
                                                    boolean required, int order) {
        return prepareParameterBuilder(key, displayName, required, order)
                .setValue(value)
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

    private String getSubject(String serviceMethod, String configName) {
        return String.format("%s%s%s", serviceMethod, ".", configName);
    }
}
