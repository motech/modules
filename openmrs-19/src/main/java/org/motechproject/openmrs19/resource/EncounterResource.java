package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterListResult;

/**
 * Interface for encounters management.
 */
public interface EncounterResource {

    /**
     * Creates given encounter on the OpenMRS server.
     *
     * @param encounter  the encounter to be created
     * @return  the saved encounter
     * @throws HttpException  when there were problems while creating encounter
     */
    Encounter createEncounter(Encounter encounter) throws HttpException;

    /**
     * Returns {@code EncounterListResult} of all encounters for the patient with given id.
     *
     * @param id  the id of the patient
     * @return  the {@code EncounterListResult} of all matching encounter
     * @throws HttpException  when there were problems while fetching encounters
     */
    EncounterListResult queryForAllEncountersByPatientId(String id) throws HttpException;

    /**
     * Gets the encounter by its UUID.
     *
     * @param uuid  the UUID of the encounter
     * @return  the encounter with the given UUID
     * @throws HttpException  when there were problems while fetching encounter
     */
    Encounter getEncounterById(String uuid) throws HttpException;

    /**
     * Creates given encounter type on the OpenMRS server.
     *
     * @param encounterType  the encounter type to be created
     * @return  the saved encounter type
     * @throws HttpException  when there were problems while creating encounter type
     */
    EncounterType createEncounterType(EncounterType encounterType) throws HttpException;

    /**
     * Gets the encounter type by its UUID.
     *
     * @param uuid  the UUID of the encounter type
     * @return  the encounter type with the given UUID
     * @throws HttpException  when there were problems while fetching encounter type
     */
    EncounterType getEncounterTypeByUuid(String uuid) throws HttpException;

    /**
     * Deletes the encounter type with the given UUID from the OpenMRS server.
     *
     * @param uuid  the UUID of the encounter type
     * @throws HttpException  when there were problems while deleting encounter type
     */
    void deleteEncounterType(String uuid) throws HttpException;

    /**
     * Deletes the encounter with the given UUID from the OpenMRS server.
     *
     * @param uuid  the UUID of the encounter
     * @throws HttpException  when there were problems while deleting encounter
     */
    void deleteEncounter(String uuid) throws HttpException;

}
