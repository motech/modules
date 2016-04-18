package org.motechproject.openmrs19.tasks;

import org.joda.time.DateTime;

import java.util.Map;

/**
 * Proxy registered with the task channel, exposing the OpenMRS service as task actions.
 */
public interface OpenMRSActionProxyService {

    /**
     * The default location name. This name is used when a location name is not provided by user.
     */
    String DEFAULT_LOCATION_NAME = "Unknown Location";

    /**
     * Creates an encounter with the given {@code encounterDate}, {@code encounterType}, {@code locationName},
     * {@code patientUuid} and {@code providerUuid}. The locationName is the only not required field.
     *
     * @param encounterDatetime the date of encounter
     * @param encounterType the type of encounter
     * @param locationName the name of location
     * @param patientUuid the patient uuid
     * @param providerUuid the provider uuid
     */
    void createEncounter(DateTime encounterDatetime, String encounterType, String locationName, String patientUuid, String providerUuid);

    /**
     * Creates a patient with the given params. The required fields are : {@code givenName}, {@code familyName},
     * {@code gender}, {@code motechId}. If the locationName is not provided, the default location will be used.
     *
     * @param givenName the given name of person
     * @param middleName the middle name of person
     * @param familyName the family name of person
     * @param address the address of person
     * @param birthdate the person date of birth
     * @param birthdateEstimated the person birthDateEstimated flag
     * @param gender the person gender
     * @param dead the person dead flag
     * @param causeOfDeathUUID the cause of the death
     * @param motechId the patient motechId
     * @param locationForMotechId the location name for identifiers
     * @param identifiers the additional identifiers to be stored to patient
     */
    void createPatient(String givenName, String middleName, String familyName, String address, DateTime birthdate,
                       Boolean birthdateEstimated, String gender, Boolean dead, String causeOfDeathUUID, String motechId,
                       String locationForMotechId, Map<String, String> identifiers);

    /**
     * Updates a person with the given {@personUuid}
     *
     * @param personUuid the person uuid
     * @param givenName the given name of person
     * @param middleName the middle name of person
     * @param familyName the family name of person
     * @param address the address of person
     * @param gender the person gender
     */
    void updatePerson(String personUuid, String givenName, String middleName, String familyName, String gender, String address);

}
