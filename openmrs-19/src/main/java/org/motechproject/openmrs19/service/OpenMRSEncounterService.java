package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSEncounter;
import org.motechproject.openmrs19.domain.OpenMRSEncounterType;

import java.util.List;

/**
 * Interface for handling encounters on the OpenMRS server.
 */
public interface OpenMRSEncounterService {

    /**
     * Creates the given {@code encounter} on the OpenMRS server.
     *
     * @param encounter  the encounter to be created
     * @return the created encounter
     */
    OpenMRSEncounter createEncounter(OpenMRSEncounter encounter);

    /**
     * Returns the latest encounter of type {@code encounterType} for a patient with the given {@code motechId}.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param encounterType  the type of the encounter
     * @return the latest encounter of the given type for the patient with the given MOTECH ID
     */
    OpenMRSEncounter getLatestEncounterByPatientMotechId(String motechId, String encounterType);

    /**
     * Returns the encounter with the given {@code uuid}.
     *
     * @param uuid  the UUID of the encounter
     * @return the encounter with the given UUID
     */
    OpenMRSEncounter getEncounterByUuid(String uuid);

    /**
     * Returns a list of encounters of the given {@code encounterType} for a patient with the given {@code motechId}.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param encounterType  the type of the encounter
     * @return the list of encounters with the given encounter type for a patient with the given MOTECH ID
     */
    List<OpenMRSEncounter> getEncountersByEncounterType(String motechId, String encounterType);

    /**
     * Deletes the encounter with the given {@code uuid} from the OpenMRS server.
     *
     * @param uuid  the UUID of the encounter
     */
    void deleteEncounter(String uuid);

    /**
     * Creates the given {@code encounterType} on the OpenMRS server.
     *
     * @param encounterType  the encounter type to be created
     * @return  the created encounter type
     */
    OpenMRSEncounterType createEncounterType(OpenMRSEncounterType encounterType);

    /**
     * Returns the encounter type with the given {@code uuid}.
     *
     * @param uuid  the UUID of the encounter type
     * @return  the encounter type with given UUID
     */
    OpenMRSEncounterType getEncounterTypeByUuid(String uuid);

    /**
     * Deletes the encounter type with the given {@code uuid}.
     *
     * @param uuid  the UUID of the encounter type
     */
    void deleteEncounterType(String uuid);
}
