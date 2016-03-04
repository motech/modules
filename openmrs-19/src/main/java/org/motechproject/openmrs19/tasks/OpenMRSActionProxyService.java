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
     * @param encounterDate the date of encounter
     * @param encounterType the type of encounter
     * @param locationName the name of location
     * @param patientUuid the patient uuid
     * @param providerUuid the provider uuid
     */
    void createEncounter(DateTime encounterDate, String encounterType, String locationName, String patientUuid, String providerUuid);

    /**
     * Creates a patient with the given params. The required fields are : {@code firstName}, {@code lastName},
     * {@code gender}, {@code motechId}. If the locationName is not provided, the default location will be used.
     *
     * @param firstName the given name of person
     * @param middleName the middle name of person
     * @param lastName the family name of person
     * @param address the address of person
     * @param dateOfBirth the person date of birth
     * @param birthDateEstimated the person birthDateEstimated flag
     * @param gender the person gender, M - Male, F - Female
     * @param dead the person dead flag
     * @param causeOfDeathUUID the cause of the death
     * @param motechId the patient motechId
     * @param locationName the location name for identifiers
     * @param identifiers the additional identifiers to be stored to patient
     */
    void createPatient(String firstName, String middleName, String lastName, String address, DateTime dateOfBirth,
                       Boolean birthDateEstimated, String gender, Boolean dead, String causeOfDeathUUID, String motechId, String locationName,
                       Map<String, String> identifiers);
}
