package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.model.Observation;
import org.motechproject.openmrs19.resource.model.ObservationListResult;

/**
 * Interface for observations management.
 */
public interface ObservationResource {

    /**
     * Returns {@code ObservationListResult} of all observations for patient with given UUID.
     *
     * @param uuid  the UUID of the patient
     * @return  the list of matching observations
     * @throws HttpException  when there were problems while fetching observations
     */
    ObservationListResult queryForObservationsByPatientId(String uuid) throws HttpException;

    /**
     * Voids the observation with given UUID and sets given reason.
     *
     * @param id  the UUID of the observation
     * @param reason  the reason of the observation being voided
     * @throws HttpException  when there were problems while voiding observations
     */
    void voidObservation(String id, String reason) throws HttpException;

    /**
     * Gets the observation with the given UUID.
     *
     * @param uuid  the UUID of the observation
     * @return  the observation with the given UUID
     * @throws HttpException  when there were problems while fetching observations
     */
    Observation getObservationById(String uuid) throws HttpException;

    /**
     * Creates the given observation on the OpenMRS server.
     *
     * @param observation  the observation to be created
     * @return  the saved observation
     * @throws HttpException  when there were problems while creating observations
     */
    Observation createObservation(Observation observation) throws HttpException;

    /**
     * Deletes the observation with the given UUID.
     *
     * @param uuid  the UUID of the observation
     * @throws HttpException  when there were problems while deleting observation
     */
    void deleteObservation(String uuid) throws HttpException;

}
