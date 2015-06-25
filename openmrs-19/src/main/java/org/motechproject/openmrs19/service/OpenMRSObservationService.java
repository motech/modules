package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSObservation;
import org.motechproject.openmrs19.exception.ObservationNotFoundException;

import java.util.List;

/**
 * Interface for handling observations on the OpenMRS server.
 */
public interface OpenMRSObservationService {

    /**
     * Voids the given {@code observation} with the given {@code reason}.
     *
     * @param observation  the observation to be voided
     * @param reason  the reason for voiding the observation
     * @throws ObservationNotFoundException if the observation doesn't exist
     */
    void voidObservation(OpenMRSObservation observation, String reason) throws ObservationNotFoundException;

    /**
     * Returns the latest observation of the concept with the given {@code conceptName} for a patient with the given
     * {@code motechId}.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param conceptName  the name of the observation concept
     * @return  the latest observation for the given concept name and patient MOTECH ID
     */
    OpenMRSObservation findObservation(String motechId, String conceptName);

    /**
     * Returns a list of observations of the concept with the given {@code conceptName} for a patient with the given
     * {@code motechId}.
     *
     * @param patientMotechId  the MOTECH ID of the patient
     * @param conceptName  the name of the observation concept
     * @return the list of observations for the given concept name and patient MOTECH ID
     */
    List<OpenMRSObservation> findObservations(String patientMotechId, String conceptName);

    /**
     * Returns the observation with given {@code uuid}.
     *
     * @param uuid  the UUID of the observation
     * @return the observation with the given UUID, null if the observation doesn't exist
     */
    OpenMRSObservation getObservationByUuid(String uuid);

    /**
     * Creates the given {@code observation} on the OpenMRS server.
     *
     * @param observation  the observation to be created
     * @return  the created observation
     */
    OpenMRSObservation createObservation(OpenMRSObservation observation);

    /**
     * Deletes the observation with the given {@code uuid} from the OpenMRS server. If the observation with the given
     * {@code uuid} doesn't exist an error will be logged.
     *
     * @param uuid  the UUID of the observation
     */
    void deleteObservation(String uuid);
}
