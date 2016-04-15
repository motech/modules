package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;

import java.util.List;

/**
 * Interface for handling encounters on the OpenMRS server.
 */
public interface OpenMRSEncounterService {

    /**
     * Creates the given {@code encounter} on the OpenMRS server. Configuration with the given {@code configName} will
     * be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param encounter  the encounter to be created
     * @return the created encounter
     */
    Encounter createEncounter(String configName, Encounter encounter);

    /**
     * Creates the given {@code encounter} on the OpenMRS server. The default  configuration will be used while
     * performing this action.
     *
     * @param encounter  the encounter to be created
     * @return the created encounter
     */
    Encounter createEncounter(Encounter encounter);

    /**
     * Returns the latest encounter of type {@code encounterType} for a patient with the given {@code motechId}.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param motechId  the MOTECH ID of the patient
     * @param encounterType  the type of the encounter
     * @return the latest encounter of the given type for the patient with the given MOTECH ID
     */
    Encounter getLatestEncounterByPatientMotechId(String configName, String motechId, String encounterType);

    /**
     * Returns the latest encounter of type {@code encounterType} for a patient with the given {@code motechId}. The
     * default  configuration will be used while performing this action.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param encounterType  the type of the encounter
     * @return the latest encounter of the given type for the patient with the given MOTECH ID
     */
    Encounter getLatestEncounterByPatientMotechId(String motechId, String encounterType);

    /**
     * Returns the encounter with the given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the encounter
     * @return the encounter with the given UUID
     */
    Encounter getEncounterByUuid(String configName, String uuid);

    /**
     * Returns the encounter with the given {@code uuid}. The default  configuration will be used while
     * performing this action.
     *
     * @param uuid  the UUID of the encounter
     * @return the encounter with the given UUID
     */
    Encounter getEncounterByUuid(String uuid);

    /**
     * Returns a list of encounters of the given {@code encounterType} for a patient with the given {@code motechId}.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param motechId  the MOTECH ID of the patient
     * @param encounterType  the type of the encounter
     * @return the list of encounters with the given encounter type for a patient with the given MOTECH ID
     */
    List<Encounter> getEncountersByEncounterType(String configName, String motechId, String encounterType);

    /**
     * Returns a list of encounters of the given {@code encounterType} for a patient with the given {@code motechId}.
     * The default  configuration will be used while performing this action.
     *
     * @param motechId  the MOTECH ID of the patient
     * @param encounterType  the type of the encounter
     * @return the list of encounters with the given encounter type for a patient with the given MOTECH ID
     */
    List<Encounter> getEncountersByEncounterType(String motechId, String encounterType);

    /**
     * Deletes the encounter with the given {@code uuid} from the OpenMRS server. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the encounter
     */
    void deleteEncounter(String configName, String uuid);

    /**
     * Deletes the encounter with the given {@code uuid} from the OpenMRS server. The default  configuration will
     * be used while performing this action.
     *
     * @param uuid  the UUID of the encounter
     */
    void deleteEncounter(String uuid);

    /**
     * Creates the given {@code encounterType} on the OpenMRS server. Configuration with the given {@code configName}
     * will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param encounterType  the encounter type to be created
     * @return  the created encounter type
     */
    EncounterType createEncounterType(String configName, EncounterType encounterType);

    /**
     * Creates the given {@code encounterType} on the OpenMRS server. The default  configuration will be used
     * while performing this action.
     *
     * @param encounterType  the encounter type to be created
     * @return  the created encounter type
     */
    EncounterType createEncounterType(EncounterType encounterType);

    /**
     * Returns the encounter type with the given {@code uuid}. Configuration with the given {@code configName} will be
     * used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the encounter type
     * @return  the encounter type with given UUID
     */
    EncounterType getEncounterTypeByUuid(String configName, String uuid);

    /**
     * Returns the encounter type with the given {@code uuid}. The default  configuration will be used while
     * performing this action.
     *
     * @param uuid  the UUID of the encounter type
     * @return  the encounter type with given UUID
     */
    EncounterType getEncounterTypeByUuid(String uuid);

    /**
     * Deletes the encounter type with the given {@code uuid}. Configuration with the given {@code configName} will be
     * used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the encounter type
     */
    void deleteEncounterType(String configName, String uuid);

    /**
     * Deletes the encounter type with the given {@code uuid}. The default  configuration will be used while
     * performing this action.
     *
     * @param uuid  the UUID of the encounter type
     */
    void deleteEncounterType(String uuid);
}
