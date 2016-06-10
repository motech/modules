package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.domain.EncounterType;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.EncounterListResult;
import org.motechproject.openmrs.config.Config;

/**
 * Interface for encounters management.
 */
public interface EncounterResource {

    /**
     * Creates given encounter on the OpenMRS server. The given {@code config} will be used while performing this action.
     *
     * @param config  the name of the configuration
     * @param encounter  the encounter to be created
     * @return the saved encounter
     */
    Encounter createEncounter(Config config, Encounter encounter);

    /**
     * Returns {@code EncounterListResult} of all encounters for the patient with given id. The given {@code config}
     * will be used while performing this action.
     *
     * @param config  the name of the configuration
     * @param id  the id of the patient
     * @return the {@code EncounterListResult} of all matching encounter
     */
    EncounterListResult queryForAllEncountersByPatientId(Config config, String id);

    /**
     * Gets the encounter by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the name of the configuration
     * @param uuid  the UUID of the encounter
     * @return the encounter with the given UUID
     */
    Encounter getEncounterById(Config config, String uuid);

    /**
     * Creates given encounter type on the OpenMRS server. The given {@code config} will be used while performing this
     * action.
     *
     * @param config  the name of the configuration
     * @param encounterType  the encounter type to be created
     * @return the saved encounter type
     */
    EncounterType createEncounterType(Config config, EncounterType encounterType);

    /**
     * Gets the encounter type by its UUID. The given {@code config} will be used while performing this action.
     *
     * @param config  the name of the configuration
     * @param uuid  the UUID of the encounter type
     * @return the encounter type with the given UUID
     */
    EncounterType getEncounterTypeByUuid(Config config, String uuid);

    /**
     * Deletes the encounter type with the given UUID from the OpenMRS server. The given {@code config} will be used
     * while performing this action.
     *
     * @param config  the name of the configuration
     * @param uuid  the UUID of the encounter type
     */
    void deleteEncounterType(Config config, String uuid);

    /**
     * Deletes the encounter with the given UUID from the OpenMRS server. The given {@code config} will be used while
     * performing this action.
     *
     * @param config  the name of the configuration
     * @param uuid  the UUID of the encounter
     */
    void deleteEncounter(Config config, String uuid);
}
