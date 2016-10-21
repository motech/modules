package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.exception.ObservationNotFoundException;

import java.util.List;

/**
 * Interface for handling observations on the OpenMRS server.
 */
public interface OpenMRSObservationService {

    /**
     * Voids the given {@code observation} with the given {@code reason}. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param observation  the observation to be voided
     * @param reason  the reason for voiding the observation
     * @throws ObservationNotFoundException if the observation doesn't exist
     */
    void voidObservation(String configName, Observation observation, String reason) throws ObservationNotFoundException;

    /**
     * Returns the latest observation of the concept with the given {@code conceptName} for a patient with the given
     * {@code motechId}. Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param motechId  the MOTECH ID of the patient
     * @param conceptName  the name of the observation concept
     * @return  the latest observation for the given concept name and patient MOTECH ID
     */
    Observation findObservation(String configName, String motechId, String conceptName);

    /**
     * Returns a list of observations of the concept with the given {@code conceptName} for a patient with the given
     * {@code motechId}. Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param patientMotechId  the MOTECH ID of the patient
     * @param conceptName  the name of the observation concept
     * @return the list of observations for the given concept name and patient MOTECH ID
     */
    List<Observation> findObservations(String configName, String patientMotechId, String conceptName);

    /**
     * Returns the observation with given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the observation
     * @return the observation with the given UUID, null if the observation doesn't exist
     */
    Observation getObservationByUuid(String configName, String uuid);

    /**
     * Returns the observation with given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param patientUUID the UUID of the patient
     * @param conceptUUID the UUID of the concept
     * @return the latest observation with the given patientUUID and conceptUUID, null if the observation doesn't exist
     */
    Observation getLatestObservationByPatientUUIDAndConceptUUID(String configName, String patientUUID, String conceptUUID);

    /**
     * Returns the latest observation with given {@code patientUuid} and {@code value}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param patientUuid the UUID of the patient
     * @param value       the observation value
     * @return the latest observation with the given patientUuid and value, null if the observation doesn't exist
     */
    Observation getLatestObservationByValueAndPatientUuid(String configName, String patientUuid, String value);

    /**
     * Creates the given {@code observation} on the OpenMRS server. Configuration with the given {@code configName} will
     * be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param observation  the observation to be created
     * @return  the created observation
     */
    Observation createObservation(String configName, Observation observation);

    /**
     * Creates the given {@code observation} on the OpenMRS server. Configuration with the given {@code configName} will
     * be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param observationJson  the observation json to be created
     * @return  the created observation
     */
    Observation createObservationFromJson(String configName, String observationJson);

    /**
     * Deletes the observation with the given {@code uuid} from the OpenMRS server. If the observation with the given
     * {@code uuid} doesn't exist an error will be logged. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the observation
     */
    void deleteObservation(String configName, String uuid);
}
