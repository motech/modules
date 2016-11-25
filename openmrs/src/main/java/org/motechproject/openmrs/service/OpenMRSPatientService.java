package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.exception.PatientNotFoundException;

import java.util.Date;
import java.util.List;

/**
 * Interface for handling patients on the OpenMRS server.
 */
public interface OpenMRSPatientService {

    /**
     * Creates the given {@code patient} on the OpenMRS server. Configuration with the given {@code configName} will be
     * used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param patient  the patient to be created
     * @return  the created patient
     */
    Patient createPatient(String configName, Patient patient);

    /**
     * Updates the patient with the  given {@code currentMotechId} with the information stored in the given
     * {@code patient} (including the new MOTECH ID passed in the given {@code patient}). Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param patient  the patient to be used as an update source
     * @param currentMotechId  the current MOTECH ID of the patient to update (used for searching)
     * @return the updated patient
     */
    Patient updatePatient(String configName, Patient patient, String currentMotechId);

    /**
     * Updates the patient with the information stored in the given {@code patient}. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param patient  the patient to be used as an update source
     * @return the updated patient
     */
    Patient updatePatient(String configName, Patient patient);

    /**
     * Updates the patient's identifier with the information stored in the given {@code patient}. Configuration with the given
     * {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param patient  the patient to be used as an update source
     * @return the updated patient
     */
    Patient updatePatientIdentifiers(String configName, Patient patient);

    /**
     * Returns the patient with the given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the patient
     * @return the patient with the given UUID, null if the patient doesn't exist
     */
    Patient getPatientByUuid(String configName, String uuid);

    /**
     * Returns the patient with the given {@code motechId}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param motechId  the MOTECH ID of the patient
     * @return the patient with the given MOTECH ID, null if the patient doesn't exist
     */
    Patient getPatientByMotechId(String configName, String motechId);

    /**
     * Returns the patient with the given {@code identifierName} and {@code identifierId}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param identifierId  the identifier id of the patient
     * @param identifierName  the identifier name of the patient
     * @return the patient with the given identifier, null if the patient doesn't exist
     */
    Patient getPatientByIdentifier(String configName, String identifierId, String identifierName);

    /**
     * If the {@code motechId} is null this method will return a list of patients with given {@code name}, else it will
     * return a list with a single patient that has the given {@code name} and {@code motechId}. If there are no
     * matching patients an empty list will be returned. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param name  the name of the patient to be searched for
     * @param motechId  the MOTECH ID of the patient to be searched for
     * @return list of matched patients
     */
    List<Patient> search(String configName, String name, String motechId);

    /**
     * Marks a patient with the given {@code motechId} as dead with the given {@code dateOfDeath}, {@code causeOfDeath}
     * and a {@code comment}. Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param motechId  the MOTECH ID of the patient
     * @param causeOfDeath  the cause of death
     * @param dateOfDeath  the date of death
     * @param comment  the additional information for the cause of death
     * @throws PatientNotFoundException if the patient with the given MOTECH ID doesn't exist
     */
    void deceasePatient(String configName, String motechId, Concept causeOfDeath, Date dateOfDeath, String comment) throws PatientNotFoundException;

    /**
     * Deletes the patient with the given {@code uuid}. Configuration with the given {@code configName} will be used
     * while performing this action.
     *
     * @param configName  the name of the configuration
     * @param uuid  the UUID of the patient
     * @throws PatientNotFoundException if the patient with the given UUID doesn't exist
     */
    void deletePatient(String configName, String uuid) throws PatientNotFoundException;
}
