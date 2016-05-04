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
     * @param observations the map of observations where concept name is a key and value from the observation is a value
     */
    void createEncounter(DateTime encounterDatetime, String encounterType, String locationName, String patientUuid, String providerUuid, Map<String, String> observations);

    /**
     * Creates a patient with the given params. The required fields are : {@code givenName}, {@code familyName},
     * {@code gender}, {@code motechId}. If the locationName is not provided, the default location will be used.
     *
     * @param givenName the given name of person
     * @param middleName the middle name of person
     * @param familyName the family name of person
     * @param address1 the first line of person address
     * @param address2 teh second line of person address
     * @param address3 the third line of person address
     * @param address4 the fourth line of person address
     * @param address5 the fifth line of person address
     * @param address6 the sixth line of person address
     * @param cityVillage the city or village of person address
     * @param stateProvince the state or province of person address
     * @param country the country of person address
     * @param postalCode the postal code of person address
     * @param countyDistrict the county district of person address
     * @param latitude the latitude of person address
     * @param longitude the longitude of person address
     * @param startDate the start date of person address
     * @param endDate the end date of person address
     * @param birthdate the person date of birth
     * @param birthdateEstimated the person birthDateEstimated flag
     * @param gender the person gender
     * @param dead the person dead flag
     * @param causeOfDeathUUID the cause of the death
     * @param motechId the patient motechId
     * @param locationForMotechId the location name for identifiers
     * @param identifiers the additional identifiers to be stored to patient
     */
    void createPatient(String givenName, String middleName, String familyName, String address1, String address2,
                       String address3, String address4, String address5, String address6, String cityVillage, String stateProvince,
                       String country, String postalCode, String countyDistrict, String latitude, String longitude,
                       DateTime startDate, DateTime endDate, DateTime birthdate, Boolean birthdateEstimated,
                       String gender, Boolean dead, String causeOfDeathUUID, String motechId,
                       String locationForMotechId, Map<String, String> identifiers);
    /**
      * Updates a patient with the given {@patientUuid}
      *
      * @param patientUuid the patient uuid
      * @param identifiers the identifiers to be stored to patient
    */
    void updatePatientIdentifiers(String patientUuid, Map<String, String> identifiers);
}
