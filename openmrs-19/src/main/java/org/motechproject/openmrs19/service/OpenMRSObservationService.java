package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.exception.ObservationNotFoundException;

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
     * Voids the given {@code observation} with the given {@code reason}. The default  configuration will be used
     * while performing this action.
     *
     * @param observation  the observation to be voided
     * @param reason  the reason for voiding the observation
     * @throws ObservationNotFoundException if the observation doesn't exist
     */
    void voidObservation(Observation observation, String reason) throws ObservationNotFoundException;

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
     * Returns the latest observation of the concept with the given {@code conceptName} for a patient with the given
     * {@code motechId}. The default  configuration will be used while performing this action.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param conceptName  the name of the observation concept
     * @return  the latest observation for the given concept name and patient MOTECH ID
     */
    Observation findObservation(String motechId, String conceptName);

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
     * Returns a list of observations of the concept with the given {@code conceptName} for a patient with the given
     * {@code motechId}. The default  configuration will be used while performing this action.
     *
     * @param patientMotechId  the MOTECH ID of the patient
     * @param conceptName  the name of the observation concept
     * @return the list of observations for the given concept name and patient MOTECH ID
     */
    List<Observation> findObservations(String patientMotechId, String conceptName);

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
     * Returns the observation with given {@code uuid}. The default  configuration will be used while performing
     * this action.
     *
     * @param uuid  the UUID of the observation
     * @return the observation with the given UUID, null if the observation doesn't exist
     */
    Observation getObservationByUuid(String uuid);

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
     * Creates the given {@code observation} on the OpenMRS server. The default  configuration will be used while
     * performing this action.
     *
     * @param observation  the observation to be created
     * @return  the created observation
     */
    Observation createObservation(Observation observation);

    /**
     * Deletes the observation with the given {@code uuid} from the OpenMRS server. If the observation with the given
     * {@code uuid} doesn't exist an error will be logged. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the observation
     */
    void deleteObservation(String configName, String uuid);

    /**
     * Deletes the observation with the given {@code uuid} from the OpenMRS server. If the observation with the given
     * {@code uuid} doesn't exist an error will be logged. The default  configuration will be used while
     * performing this action.
     *
     * @param uuid  the UUID of the observation
     */
    void deleteObservation(String uuid);
}
