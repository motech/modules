package org.motechproject.openmrs.service;

/**
 * Utility class for storing event keys.
 */
public final class EventKeys {

    public static final String MOTECH_ID = "MotechId";
    public static final String CONCEPT_NAME = "ConceptName";
    public static final String CONCEPT_UUID = "ConceptUuid";
    public static final String CONCEPT_DATA_TYPE = "ConceptDataType";
    public static final String CONCEPT_CONCEPT_CLASS = "ConceptConceptClass";
    public static final String DATE_OF_DEATH = "DateOfDeath";
    public static final String COMMENT = "Comment";
    public static final String PATIENT_ID = "PatientId";
    public static final String PERSON_ID = "PersonId";
    public static final String PERSON_GIVEN_NAME = "PersonFirstName";
    public static final String PERSON_MIDDLE_NAME = "PersonMiddleName";
    public static final String PERSON_FAMILY_NAME = "PersonLastName";
    public static final String PERSON_PREFERRED_NAME = "PersonPreferredName";
    public static final String PERSON_ADDRESS = "PersonAddress";
    public static final String PERSON_DATE_OF_BIRTH = "PersonDateOfBirth";
    public static final String PERSON_BIRTH_DATE_ESTIMATED = "PersonBirthDateEstimated";
    public static final String PERSON_AGE = "PersonAge";
    public static final String PERSON_GENDER = "PersonGender";
    public static final String PERSON_DEAD = "PersonDead";
    public static final String PERSON_DEATH_DATE = "PersonDeathDate";
    public static final String LOCATION_ID = "LocationId";
    public static final String LOCATION_NAME = "LocationName";
    public static final String LOCATION_COUNTRY = "LocationCountry";
    public static final String LOCATION_REGION = "LocationRegion";
    public static final String LOCATION_COUNTY_DISTRICT = "LocationCountryDistrict";
    public static final String LOCATION_STATE_PROVINCE = "LocationStateProvince";
    public static final String OBSERVATION_VALUE = "ObservationValue";
    public static final String OBSERVATION_CONCEPT_NAME = "ObservationConceptName";
    public static final String OBSERVATION_DATE = "ObservationDate";
    public static final String ENCOUNTER_ID = "EncounterID";
    public static final String ENCOUNTER_TYPE = "EncounterType";
    public static final String ENCOUNTER_DATE = "EncounterDate";
    public static final String VISIT_ID = "VisitID";
    public static final String VISIT_TYPE = "VisitType";
    public static final String VISIT_START_DATETIME = "VisitStartDateTime";
    public static final String VISIT_STOP_DATETIME = "VisitStopDateTime";
    public static final String PROVIDER_ID = "ProviderId";
    public static final String USER_ID = "UserId";
    public static final String PROGRAM_ID = "ProgramId";
    public static final String PROGRAM_ENROLLMENT_ID = "ProgramEnrollmentId";
    public static final String FORM_ID = "FormId";

    public static final String BASE_SUBJECT = "org.motechproject.mrs.api.";

    public static final String CREATED_NEW_PATIENT_SUBJECT = BASE_SUBJECT + "Patient.Created";
    public static final String UPDATED_PATIENT_SUBJECT = BASE_SUBJECT + "Patient.Updated";
    public static final String UPDATED_PATIENT_IDENTIFIERS_SUBJECT = BASE_SUBJECT + "PatientIdentifiers.Updated";
    public static final String PATIENT_DECEASED_SUBJECT = BASE_SUBJECT + "Patient.Deceased";
    public static final String DELETED_PATIENT_SUBJECT = BASE_SUBJECT + "Patient.Deleted";
    public static final String CREATED_NEW_OBSERVATION_SUBJECT = BASE_SUBJECT + "Observation.Created";
    public static final String CREATED_NEW_ENCOUNTER_SUBJECT = BASE_SUBJECT + "Encounter.Created";
    public static final String UPDATED_ENCOUNTER_SUBJECT = BASE_SUBJECT + "Encounter.Updated";
    public static final String DELETED_ENCOUNTER_SUBJECT = BASE_SUBJECT + "Encounter.Deleted";
    public static final String CREATED_NEW_VISIT_SUBJECT = BASE_SUBJECT + "Visit.Created";
    public static final String DELETED_VISIT_SUBJECT = BASE_SUBJECT + "Visit.Deleted";
    public static final String CREATED_NEW_LOCATION_SUBJECT = BASE_SUBJECT + "Location.Create";
    public static final String UPDATED_LOCATION_SUBJECT = BASE_SUBJECT + "Location.Updated";
    public static final String DELETED_LOCATION_SUBJECT = BASE_SUBJECT + "Location.Deleted";
    public static final String CREATED_NEW_PERSON_SUBJECT = BASE_SUBJECT + "Person.Created";
    public static final String UPDATED_PERSON_SUBJECT = BASE_SUBJECT + "Person.Updated";
    public static final String DELETED_PERSON_SUBJECT = BASE_SUBJECT + "Person.Deleted";
    public static final String CREATED_NEW_PROVIDER_SUBJECT = BASE_SUBJECT + "Provider.Created";
    public static final String UPDATED_PROVIDER_SUBJECT = BASE_SUBJECT + "Provider.Updated";
    public static final String DELETED_PROVIDER_SUBJECT = BASE_SUBJECT + "Provider.Deleted";
    public static final String DELETED_OBSERVATION_SUBJECT = BASE_SUBJECT + "Observation.Deleted";
    public static final String CREATED_UPDATED_OBSERVATION_SUBJECT = "Observation.Updated";
    public static final String CREATED_NEW_CONCEPT_SUBJECT = BASE_SUBJECT + "Concept.Created";
    public static final String UPDATED_CONCEPT_SUBJECT = BASE_SUBJECT + "Concept.Updated";
    public static final String DELETED_CONCEPT_SUBJECT = BASE_SUBJECT + "Concept.Deleted";
    public static final String VOIDED_OBSERVATION_SUBJECT = "Observation.Voided";
    public static final String CREATED_PROGRAM_ENROLLMENT = BASE_SUBJECT + "ProgramEnrollment.Created";

    /**
     * Utility class, should not be instantiated.
     */
    private EventKeys() { }
}
