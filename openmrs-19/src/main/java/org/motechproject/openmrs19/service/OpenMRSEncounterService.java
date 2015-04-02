package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSEncounter;
import org.motechproject.openmrs19.domain.OpenMRSEncounterType;

import java.util.List;

/**
 * Interface for fetching and storing encounter details.
 */
public interface OpenMRSEncounterService {
    /**
     * Stores a given OpenMRSEncounter in the MRS system.
     *
     * @param  mrsEncounter Object to be saved.
     * @return saved MRS Encounter object
     */
    OpenMRSEncounter createEncounter(OpenMRSEncounter mrsEncounter);

    /**
     * Fetches the latest encounter of a patient identified by MOTECH ID and the encounter type.
     *
     * @param motechId Identifier of the patient
     * @param encounterType Type of the encounter. (e.g. ANCVISIT)
     * @return The latest MRSEncounter if found.
     */
    OpenMRSEncounter getLatestEncounterByPatientMotechId(String motechId, String encounterType);

    /**
     * Fetches an encounter by its UUID.
     *
     * @param id The UUID in OpenMRS of the encounter to retrieve
     * @return The MRSEncounter with the specified id
     */
    OpenMRSEncounter getEncounterById(String id);

    /**
     * Fetches a list of encounters of a patient identified by MOTECH ID and the encounter type.
     *
     * @param motechId  identifier of the patient
     * @param encounterType  type of the encounter. (e.g. ANCVISIT)
     * @return a list of all MRSEncounters of the corresponding encounter type for the patient
     * identified by MOTECH ID
     */
    List<OpenMRSEncounter> getEncountersByEncounterType(String motechId, String encounterType);

    /**
     * Deletes encounter with the given UUID.
     *
     * @param uuid  the UUID of the encounter
     */
    void deleteEncounter(String uuid);

    /**
     * Creates given encounter type to the OpenMRS server.
     *
     * @param encounterType  the encounter type to be created
     * @return  the created encounter type
     */
    OpenMRSEncounterType createEncounterType(OpenMRSEncounterType encounterType);

    /**
     * Fetches encounter type with the given UUID.
     *
     * @param uuid  the UUID of the encounter type
     * @return  the encounter type with given UUID
     */
    OpenMRSEncounterType getEncounterTypeByUuid(String uuid);

    /**
     * Deletes encounter type with given uuid.
     *
     * @param uuid  the UUID of the encounter type to be deleted
     */
    void deleteEncounterType(String uuid);
}
