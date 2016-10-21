package org.motechproject.openmrs.tasks.constants;

/**
 * Utility class for storing display names related with the Tasks actions.
 */
public final class DisplayNames {

    public static final String CONFIG_NAME = "openMRS.configuration.name";

    //Encounter action
    public static final String ENCOUNTER_DATE = "openMRS.encounter.date";
    public static final String ENCOUNTER_TYPE = "openMRS.encounter.type";
    public static final String LOCATION_NAME = "openMRS.location.name";
    public static final String PATIENT_UUID = "openMRS.patient.uuid";
    public static final String PROVIDER_UUID = "openMRS.provider.uuid";
    public static final String OBSERVATION = "openMRS.encounter.observation";
    public static final String ENCOUNTER_UUID = "openMRS.encounter.uuid";
    public static final String FORM = "openMRS.encounter.form";

    //Patient action
    public static final String PERSON_UUID = "openMRS.person.uuid";
    public static final String GIVEN_NAME = "openMRS.person.givenName";
    public static final String MIDDLE_NAME = "openMRS.person.middleName";
    public static final String FAMILY_NAME = "openMRS.person.familyName";
    public static final String ADDRESS_1 = "openMRS.address.address1";
    public static final String ADDRESS_2 = "openMRS.address.address2";
    public static final String ADDRESS_3 = "openMRS.address.address3";
    public static final String ADDRESS_4 = "openMRS.address.address4";
    public static final String ADDRESS_5 = "openMRS.address.address5";
    public static final String ADDRESS_6 = "openMRS.address.address6";
    public static final String CITY_VILLAGE = "openMRS.address.cityVillage";
    public static final String STATE_PROVINCE = "openMRS.address.stateProvince";
    public static final String COUNTRY = "openMRS.address.country";
    public static final String POSTAL_CODE = "openMRS.address.postalCode";
    public static final String COUNTY_DISTRICT = "openMRS.address.countyDistrict";
    public static final String LATITUDE = "openMRS.address.latitude";
    public static final String LONGITUDE = "openMRS.address.longitude";
    public static final String START_DATE = "openMRS.address.startDate";
    public static final String END_DATE = "openMRS.address.endDate";
    public static final String BIRTH_DATE = "openMRS.person.birthDate";
    public static final String BIRTH_DATE_ESTIMATED = "openMRS.person.birthDateEstimated";
    public static final String GENDER = "openMRS.person.gender";
    public static final String DEAD = "openMRS.person.dead";
    public static final String CAUSE_OF_DEATH_UUID = "openMRS.person.causeOfDeath.uuid";
    public static final String MOTECH_ID = "openMRS.motechId";
    public static final String LOCATION_FOR_MOTECH_ID = "openMRS.location.name";
    public static final String IDENTIFIERS = "openMRS.patient.identifiers";
    public static final String PERSON_ATTRIBUTES = "openMRS.patient.personAttributes";

    //Observation json
    public static final String OBSERVATION_JSON = "openMRS.observationJSON";
    public static final String CONCEPT_UUID = "openMRS.concept.uuid";
    public static final String OBSERVATION_DATETIME = "openMRS.obsDatetime";
    public static final String COMMENT = "openMRS.comment";

    //Visit action
    public static final String VISIT_UUID = "openMRS.visit.uuid";
    public static final String VISIT_START_DATETIME = "openMRS.visit.startDatetime";
    public static final String VISIT_STOP_DATETIME = "openMRS.visit.stopDatetime";
    public static final String VISIT_TYPE_UUID = "openMRS.visit.type.uuid";
    //Program Enrollment action
    public static final String PROGRAM_UUID = "openMRS.program.uuid";
    public static final String PROGRAM_ENROLLMENT_UUID = "openMRS.programEnrollment.uuid";
    public static final String DATE_ENROLLED = "openMRS.programEnrollment.dateEnrolled";

    public static final String DATE_COMPLETED = "openMRS.programEnrollment.dateCompleted";
    public static final String STATE_UUID = "openMRS.program.state.uuid";
    public static final String STATE_START_DATE = "openMRS.program.state.startDate";
    public static final String PROGRAM_ENROLLMENT_ATTRIBUTES = "openMRS.programEnrollment.attributes";
    //CohortQuery Report action
    public static final String COHORT_QUERY_UUID = "openMRS.cohortQuery.uuid";
    public static final String COHORT_QUERY_PARAMETERS = "openMRS.cohortQuery.parameters";
    public static final String PATIENT_DISPLAY = "openMRS.patient.display";
    public static final String COHORT_QUERY_REPORTMEMBER = "openMRS.cohortQuery.reportMember";
    //Order Action
    public static final String ORDER_TYPE = "openMRS.orderType";
    public static final String ORDER_UUID = "openMRS.order.uuid";
    public static final String ORDER_ORDERER_UUID = "openMRS.order.orderer.uuid";
    public static final String ORDER_CARE_SETTING = "openMRS.order.careSetting";

    /**
     * Utility class, should not be initiated.
     */
    private DisplayNames() {
    }
}
