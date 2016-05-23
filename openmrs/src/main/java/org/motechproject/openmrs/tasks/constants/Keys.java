package org.motechproject.openmrs.tasks.constants;

/**
 * Utility class for storing keys related to the Tasks channel.
 */
public final class Keys {

    public static final String CONFIG_NAME = "configName";

    //Encounter action
    public static final String ENCOUNTER_DATE = "encounterDatetime";
    public static final String ENCOUNTER_TYPE = "encounterType";
    public static final String LOCATION_NAME = "locationName";
    public static final String PATIENT_UUID = "patientUuid";
    public static final String PROVIDER_UUID = "providerUuid";
    public static final String OBSERVATION = "observation";

    //Patient action
    public static final String PERSON_UUID = "personUuid";
    public static final String GIVEN_NAME = "givenName";
    public static final String MIDDLE_NAME = "middleName";
    public static final String FAMILY_NAME = "familyName";
    public static final String ADDRESS_1 = "address1";
    public static final String ADDRESS_2 = "address2";
    public static final String ADDRESS_3 = "address3";
    public static final String ADDRESS_4 = "address4";
    public static final String ADDRESS_5 = "address5";
    public static final String ADDRESS_6 = "address6";
    public static final String CITY_VILLAGE = "cityVillage";
    public static final String STATE_PROVINCE = "stateProvince";
    public static final String COUNTRY = "country";
    public static final String POSTAL_CODE = "postalCode";
    public static final String COUNTY_DISTRICT = "countyDistrict";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String START_DATE = "startDate";
    public static final String END_DATE = "endDate";
    public static final String BIRTH_DATE = "birthDate";
    public static final String BIRTH_DATE_ESTIMATED = "birthDateEstimated";
    public static final String GENDER = "gender";
    public static final String DEAD = "dead";
    public static final String CAUSE_OF_DEATH_UUID = "causeOfDeathUUID";
    public static final String MOTECH_ID = "motechId";
    public static final String LOCATION_FOR_MOTECH_ID = "locationForMotechId";
    public static final String IDENTIFIERS = "identifiers";

    //Program Enrollment action
    public static final String PROGRAM_UUID = "programUuid";
    public static final String PROGRAM_ENROLLMENT_UUID = "programEnrollmentUuid";
    public static final String DATE_ENROLLED = "dateEnrolled";
    public static final String DATE_COMPLETED = "dateCompleted";
    public static final String STATE_UUID = "stateUuid";
    public static final String STATE_START_DATE = "startDate";

    /**
     * Utility class, should not be initiated.
     */
    private Keys() {
    }
}
