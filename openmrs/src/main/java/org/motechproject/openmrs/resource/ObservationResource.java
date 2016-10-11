package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.ObservationListResult;

/**
 * Interface for observations management.
 */
public interface ObservationResource {

    /**
     * Returns {@code ObservationListResult} of all observations for patient with given UUID. The given {@code config}
     * will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the patient
     * @return  the list of matching observations
     */
    ObservationListResult queryForObservationsByPatientId(Config config, String uuid);

    /**
     * Voids the observation with given UUID and sets given reason. The given {@code config} will be used while
     * performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param id  the UUID of the observation
     * @param reason  the reason of the observation being voided
     */
    void voidObservation(Config config, String id, String reason);

    /**
     * Gets the observation with the given UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the observation
     * @return  the observation with the given UUID
     */
    Observation getObservationById(Config config, String uuid);

    /**
     * Gets the latest observation with the given patientUUID and conceptUUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param patientUUID  the UUID of the patient
     * @param conceptUUID  the UUID of the concept
     * @return  the latest observation with the given patientUUID and conceptUUID
     */
    ObservationListResult getObservationByPatientUUIDAndConceptUUID(Config config, String patientUUID, String conceptUUID);

    /**
     * Creates the given observation on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @param observation  the observation to be created
     * @return  the saved observation
     */
    Observation createObservation(Config config, Observation observation);

    /**
     * Creates the given observation on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the configuration to be used while performing this action
     * @param observationJson  the observation json to be created
     * @return  the saved observation
     */
    Observation createObservationFromJson(Config config, String observationJson);

    /**
     * Deletes the observation with the given UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the configuration to be used while performing this action
     * @param uuid  the UUID of the observation
     */
    void deleteObservation(Config config, String uuid);
}
