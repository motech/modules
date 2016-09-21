package org.motechproject.openmrs.helper;

import org.motechproject.openmrs.domain.CohortQueryReport.CohortQueryReportMember;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.domain.ProgramEnrollment;
import org.motechproject.openmrs.domain.Provider;
import org.motechproject.openmrs.domain.Visit;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.tasks.constants.Keys;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing various classes to a map of parameters that can be attached to a
 * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
 */
public final class EventHelper {

    /**
     * Utility class, should not be instantiated.
     */
    private EventHelper() {
    }

    /**
     * Parses the given {@link Patient} to a map of parameters, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param patient the patient to be parsed
     * @return the map of the parameters
     */
    public static Map<String, Object> patientParameters(Patient patient) {
        Map<String, Object> patientParameters = new HashMap<>();
        patientParameters.put(EventKeys.PATIENT_ID, patient.getUuid());
        if (patient.getLocationForMotechId() != null) {
            patientParameters.put(EventKeys.LOCATION_ID, patient.getLocationForMotechId().getUuid());
        } else {
            patientParameters.put(EventKeys.LOCATION_ID, null);
        }
        patientParameters.put(EventKeys.MOTECH_ID, patient.getMotechId());
        if (patient.getPerson() != null) {
            patientParameters.put(EventKeys.PERSON_ID, patient.getPerson().getUuid());
        } else {
            patientParameters.put(EventKeys.PERSON_ID, null);
        }
        return patientParameters;
    }

    /**
     * Parses the given {@link Person} to a map of parameters, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param person the person to be parsed
     * @return the map of the parameters
     */
    public static Map<String, Object> personParameters(Person person) {
        Map<String, Object> personParameters = new HashMap<>();
        personParameters.put(EventKeys.PERSON_ID, person.getUuid());

        if (person.getPreferredName() != null) {
            personParameters.put(EventKeys.PERSON_GIVEN_NAME, person.getPreferredName().getGivenName());
            personParameters.put(EventKeys.PERSON_MIDDLE_NAME, person.getPreferredName().getMiddleName());
            personParameters.put(EventKeys.PERSON_FAMILY_NAME, person.getPreferredName().getFamilyName());
        }
        if (person.getPreferredAddress() != null) {
            personParameters.put(EventKeys.PERSON_ADDRESS, person.getPreferredAddress().getFullAddressString());
        }

        personParameters.put(EventKeys.PERSON_DATE_OF_BIRTH, person.getBirthdate());
        personParameters.put(EventKeys.PERSON_BIRTH_DATE_ESTIMATED, person.getBirthdateEstimated());
        personParameters.put(EventKeys.PERSON_AGE, person.getAge());
        personParameters.put(EventKeys.PERSON_GENDER, person.getGender());
        personParameters.put(EventKeys.PERSON_DEAD, person.getDead());
        personParameters.put(EventKeys.PERSON_DEATH_DATE, person.getDeathDate());
        return personParameters;
    }

    /**
     * Parses the given {@link Encounter} to a map of parameters, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param encounter the encounter to be parsed
     * @return the map of the parameters
     */
    public static Map<String, Object> encounterParameters(Encounter encounter) {
        Map<String, Object> encounterParameters = new HashMap<>();
        encounterParameters.put(EventKeys.ENCOUNTER_ID, encounter.getUuid());
        if (encounter.getEncounterProviders().get(0) != null) {
            encounterParameters.put(EventKeys.PROVIDER_ID, encounter.getEncounterProviders().get(0).getUuid());
        } else {
            encounterParameters.put(EventKeys.PROVIDER_ID, null);
        }
        if (encounter.getLocation() != null) {
            encounterParameters.put(EventKeys.LOCATION_ID, encounter.getLocation().getUuid());
        } else {
            encounterParameters.put(EventKeys.LOCATION_ID, null);
        }
        encounterParameters.put(EventKeys.ENCOUNTER_DATE, encounter.getEncounterDatetime());
        encounterParameters.put(EventKeys.ENCOUNTER_TYPE, encounter.getEncounterType().getUuid());
        encounterParameters.put(EventKeys.VISIT_ID, encounter.getVisit().getUuid());
        return encounterParameters;
    }

    /**
     * Parses the given ID of the encounter to a map with a single parameter, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param uuid the encounter ID to be parsed
     * @return the map with single ID parameter
     */
    public static Map<String, Object> encounterParameters(String uuid) {
        Map<String, Object> encounterParameters = new HashMap<>();
        encounterParameters.put(EventKeys.ENCOUNTER_ID, uuid);
        return encounterParameters;
    }

    /**
     * Parses the given {@link Visit} to a map of parameters, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param visit the visit to be parsed
     * @return the map of the parameters
     */
    public static Map<String, Object> visitParameters(Visit visit) {
        Map<String, Object> visitParameters = new HashMap<>();
        visitParameters.put(EventKeys.VISIT_ID, visit.getUuid());
        visitParameters.put(EventKeys.VISIT_START_DATETIME, visit.getStartDatetime());
        visitParameters.put(EventKeys.VISIT_STOP_DATETIME, visit.getStopDatetime());
        visitParameters.put(EventKeys.VISIT_TYPE, visit.getVisitType().getUuid());
        if (visit.getLocation() != null) {
            visitParameters.put(EventKeys.LOCATION_ID, visit.getLocation().getUuid());
        }
        return visitParameters;
    }

    /**
     * Parses the given ID of the visit to a map with a single parameter, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param uuid the visit ID to be parsed
     * @return the map with single ID parameter
     */
    public static Map<String, Object> visitParameters(String uuid) {
        Map<String, Object> visitParameters = new HashMap<>();
        visitParameters.put(EventKeys.VISIT_ID, uuid);
        return visitParameters;
    }

    /**
     * Parses the given {@link Observation} to a map of parameters, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param obs the observation to be parsed
     * @return the map of the parameters
     */
    public static Map<String, Object> observationParameters(Observation obs) {
        Map<String, Object> observationParameters = new HashMap<>();
        observationParameters.put(EventKeys.OBSERVATION_DATE, obs.getObsDatetime());
        if (obs.getConcept().getName() != null) {
            observationParameters.put(EventKeys.OBSERVATION_CONCEPT_NAME, obs.getConcept().getName().getName());
        }
        observationParameters.put(EventKeys.PATIENT_ID, obs.getPerson().getUuid());
        observationParameters.put(EventKeys.OBSERVATION_VALUE, obs.getValue().getDisplay());
        return observationParameters;
    }

    /**
     * Parses the given {@link Location} to a map of parameters, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param location the location to be parsed
     * @return the map of the parameters
     */
    public static Map<String, Object> locationParameters(Location location) {
        Map<String, Object> locationParameters = new HashMap<>();
        locationParameters.put(EventKeys.LOCATION_ID, location.getUuid());
        locationParameters.put(EventKeys.LOCATION_NAME, location.getName());
        locationParameters.put(EventKeys.LOCATION_COUNTRY, location.getCountry());
        locationParameters.put(EventKeys.LOCATION_REGION, location.getAddress6());
        locationParameters.put(EventKeys.LOCATION_COUNTY_DISTRICT, location.getCountyDistrict());
        locationParameters.put(EventKeys.LOCATION_STATE_PROVINCE, location.getStateProvince());
        return locationParameters;
    }

    /**
     * Parses the given {@link Provider} to a map of parameters, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param provider the provider to be parsed
     * @return the map of the parameters
     */
    public static Map<String, Object> providerParameters(Provider provider) {
        Map<String, Object> providerParameters = new HashMap<>();
        providerParameters.put(EventKeys.PROVIDER_ID, provider.getUuid());
        if (provider.getPerson() != null) {
            providerParameters.put(EventKeys.PERSON_ID, provider.getPerson().getUuid());
        } else {
            providerParameters.put(EventKeys.PERSON_ID, null);
        }
        return providerParameters;
    }

    /**
     * Parses the given {@link Concept} to a map of parameters, which can then be attached to a
     * {@link org.motechproject.event.MotechEvent} and sent via the {@link org.motechproject.event.listener.EventRelay}.
     *
     * @param concept the concept to be parsed
     * @return the map of the parameters
     */
    public static Map<String, Object> conceptParameters(Concept concept) {
        Map<String, Object> conceptParameters = new HashMap<>();
        conceptParameters.put(EventKeys.CONCEPT_NAME, concept.getDisplay());
        conceptParameters.put(EventKeys.CONCEPT_UUID, concept.getUuid());
        conceptParameters.put(EventKeys.CONCEPT_DATA_TYPE, concept.getDatatype().getDisplay());
        conceptParameters.put(EventKeys.CONCEPT_CONCEPT_CLASS, concept.getConceptClass().getDisplay());
        return conceptParameters;
    }

    public static Map<String, Object> programEnrollmentParameters(ProgramEnrollment programEnrollment) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(EventKeys.PROGRAM_ENROLLMENT_ID, programEnrollment.getUuid());
        parameters.put(EventKeys.PROGRAM_ID, programEnrollment.getProgram().getUuid());
        parameters.put(EventKeys.PATIENT_ID, programEnrollment.getPatient().getUuid());

        return parameters;
    }

    public static Map<String, Object> cohortMemberParameters(String cohortQueryUuid, CohortQueryReportMember member) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(Keys.COHORT_QUERY_UUID, cohortQueryUuid);
        parameters.put(Keys.PATIENT_UUID, member.getUuid());
        parameters.put(Keys.PATIENT_DISPLAY, member.getDisplay());

        return parameters;
    }
}
