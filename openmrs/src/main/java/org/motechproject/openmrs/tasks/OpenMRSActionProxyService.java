package org.motechproject.openmrs.tasks;

import org.joda.time.DateTime;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.Order;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Visit;

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
     * {@code patientUuid},{@code providerUuid} and {@code formId}. The locationName and formId are the only not required fields.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName        the name of the configuration
     * @param encounterDatetime the date of encounter
     * @param encounterType     the type of encounter
     * @param locationName      the name of location
     * @param patientUuid       the patient uuid
     * @param providerUuid      the provider uuid
     * @param visitUuid         the visit uuid
     * @param formUuid          the form uuid
     * @param observations      the map of observations where concept Uuid is a key and value from the observation is a value
     * @return the created encounter
     */
    Encounter createEncounter(String configName, DateTime encounterDatetime, String encounterType,
                         String locationName, String patientUuid, String providerUuid, String visitUuid,
                         String formUuid, Map<String, String> observations);

    /**
     * Creates a patient with the given params. The required fields are : {@code givenName}, {@code familyName},
     * {@code gender}, {@code motechId}. If the locationName is not provided, the default location will be used.
     * Configuration with the given {@code configName} will be used while performing this action. Returns created patient.
     *
     * @param configName          the name of the configuration
     * @param givenName           the given name of person
     * @param middleName          the middle name of person
     * @param familyName          the family name of person
     * @param address1            the first line of person address
     * @param address2            teh second line of person address
     * @param address3            the third line of person address
     * @param address4            the fourth line of person address
     * @param address5            the fifth line of person address
     * @param address6            the sixth line of person address
     * @param cityVillage         the city or village of person address
     * @param stateProvince       the state or province of person address
     * @param country             the country of person address
     * @param postalCode          the postal code of person address
     * @param countyDistrict      the county district of person address
     * @param latitude            the latitude of person address
     * @param longitude           the longitude of person address
     * @param startDate           the start date of person address
     * @param endDate             the end date of person address
     * @param birthDate           the person date of birth
     * @param birthDateEstimated  the person birthDateEstimated flag
     * @param gender              the person gender
     * @param dead                the person dead flag
     * @param causeOfDeathUUID    the cause of the death
     * @param motechId            the patient motechId
     * @param locationForMotechId the location name for identifiers
     * @param identifiers         the additional identifiers to be stored to patient
     * @param personAttributes    the additional attributes of person
     * @return created Patient
     */
    Patient createPatient(String configName, String givenName, String middleName, String familyName, String address1,
                          String address2, String address3, String address4, String address5, String address6,
                          String cityVillage, String stateProvince, String country, String postalCode,
                          String countyDistrict, String latitude, String longitude, DateTime startDate, DateTime endDate,
                          DateTime birthDate, Boolean birthDateEstimated, String gender, Boolean dead,
                          String causeOfDeathUUID, String motechId, String locationForMotechId,
                          Map<String, String> identifiers, Map<String, String> personAttributes);

    /**
     * Updates a patient with the given {@patientUuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param patientUuid the patient uuid
     * @param identifiers the identifiers to be stored to patient
     */
    void updatePatientIdentifiers(String configName, String patientUuid, Map<String, String> identifiers);

    /**
     * Creates an observation from given {@code observationJSON}. The observationJSON is the only required field. Other
     * params are optional and will overwrite any matching key of {@code observationJSON}. Configuration with
     * the given {@code configName} will be used while performing this action.
     *  @param configName      the name of the configuration
     * @param observationJSON the observation in JSON
     * @param encounterUuid   the encounter uuid
     * @param conceptUuid     the concept uuid
     * @param obsDatetime     the observation datetime
     * @param orderUuid       the order uuid
     * @param comment         the comment
     * @return the created observation
     */
    Observation createObservationJSON(String configName, String observationJSON, String encounterUuid, String conceptUuid,
                                      DateTime obsDatetime, String orderUuid, String comment);

    /**
     * Creates a visit with the given {@code patientUuid}, {@code visitStartDatetime},
     * {@code visitEndDatetime}, {@code visitType} and {@code locationName}. The locationName and visitType are the only not required field. Configuration with
     * the given {@code configName} will be used while performing this action.
     *
     * @param configName    the name of the configuration
     * @param patientUuid   the patient uuid
     * @param startDatetime the start datetime of visit
     * @param endDatetime   the end datetime of visit
     * @param visitType     the type of visit
     * @param locationName  the name of location
     */
    Visit createVisit(String configName, String patientUuid, DateTime startDatetime, DateTime endDatetime,
                      String visitType, String locationName);

    /**
     * Updates a person with the given {@code personUuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName         the name of the configuration
     * @param personUuid         the person uuid
     * @param givenName          the given name of person
     * @param middleName         the middle name of person
     * @param familyName         the family name of person
     * @param address1           the first line of person address
     * @param address2           teh second line of person address
     * @param address3           the third line of person address
     * @param address4           the fourth line of person address
     * @param address5           the fifth line of person address
     * @param address6           the sixth line of person address
     * @param cityVillage        the city or village of person address
     * @param stateProvince      the state or province of person address
     * @param country            the country of person address
     * @param postalCode         the postal code of person address
     * @param countyDistrict     the county district of person address
     * @param latitude           the latitude of person address
     * @param longitude          the longitude of person address
     * @param startDate          the start date of person address
     * @param endDate            the end date of person address
     * @param birthDate          the person date of birth
     * @param birthDateEstimated the person birthDateEstimated flag
     * @param gender             the person gender
     * @param dead               the person dead flag
     * @param causeOfDeathUUID   the cause of the death
     * @param personAttributes   the additional attributes of person
     */
    void updatePerson(String configName, String personUuid, String givenName, String middleName, String familyName,
                      String address1, String address2, String address3, String address4, String address5,
                      String address6, String cityVillage, String stateProvince, String country, String postalCode,
                      String countyDistrict, String latitude, String longitude, DateTime startDate, DateTime endDate,
                      DateTime birthDate, Boolean birthDateEstimated, String gender, Boolean dead,
                      String causeOfDeathUUID, Map<String, String> personAttributes);

    /**
     * Creates a program enrollment with the given params.
     * The required fields are: {@code patientUuid}, {@code programUuid}, {@code dateEnrolled}.
     *
     * @param configName    the name of the configuration
     * @param patientUuid   the patient uuid
     * @param programUuid   the program uuid
     * @param dateEnrolled  the program enrollment date
     * @param dateCompleted the program completed date
     * @param locationName  the name of location
     */
    void createProgramEnrollment(String configName, String patientUuid, String programUuid,
                                 DateTime dateEnrolled, DateTime dateCompleted,
                                 String locationName, Map<String, String> programEnrollmentAttributes);

    /**
     * Updates a Bahmni program enrollment with the given params.
     * The required fields are: {@code programEnrollmentUuid}.
     *
     * @param configName            the name of the configuration
     * @param programEnrollmentUuid the program enrollment uuid
     * @param programCompletedDate  the program enrollment completed date
     * @param stateUuid             the state uuid
     * @param startDate             the state start date
     * @param attributes            the additional attributes
     */
    void updateProgramEnrollment(String configName, String programEnrollmentUuid, DateTime programCompletedDate,
                                 String stateUuid, DateTime startDate, Map<String, String> attributes);

    /**
     * Changes state of program enrollment. Program enrollment is identified by UUID.
     *
     * @param configName            the name of the configuration
     * @param programEnrollmentUuid the program enrollment uuid
     * @param programCompletedDate  the program enrollment completed date
     * @param stateUuid             the state uuid
     * @param startDate             the state start date
     */
    void changeStateOfProgramEnrollment(String configName, String programEnrollmentUuid, DateTime programCompletedDate,
                                        String stateUuid, DateTime startDate);

    /**
     * Gets the Cohort Query report. The Cohort Query is identified by UUID.
     * The required fields are: {@code cohortQueryUuid}.
     *
     * @param configName      the name of the configuration
     * @param cohortQueryUuid the cohort query uuid
     * @param parameters      the additional parameters
     */
    void getCohortQueryReport(String configName, String cohortQueryUuid, Map<String, String> parameters);

    /**
     * Creates an order with the given params.
     *
     * @param configName    the name of the configuration
     * @param type the order type
     * @param encounterUuid the encounter uuid
     * @param patientUuid the patient uuid
     * @param conceptUuid the concept uuid
     * @param ordererUuid the orderer uuid
     * @param careSetting the care setting value
     * @return the created Order
     */
    Order createOrder(String configName, String type, String encounterUuid, String patientUuid, String conceptUuid, String ordererUuid, String careSetting);
}
