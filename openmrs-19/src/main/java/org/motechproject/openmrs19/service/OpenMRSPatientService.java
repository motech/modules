package org.motechproject.openmrs19.service;

import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.exception.PatientNotFoundException;

import java.util.Date;
import java.util.List;

/**
 * Interface for handling patients.
 */
public interface OpenMRSPatientService {
    /**
     * Creates the given patient on the OpenMRS server.
     *
     * @param patient  the patient to be created
     * @return  the created patient
     */
    OpenMRSPatient createPatient(OpenMRSPatient patient);

    /**
     * Finds a patient by current Motech ID, and updates the patients details (including new
     * Motech ID) in the MRS system.
     *
     * @param patient  patient instance with updated values (Motech IDentifier cannot be changed here)
     * @param currentMotechId  current Motech ID of this patient (used for searching)
     * @return Updated instance of the patient
     */
    OpenMRSPatient updatePatient(OpenMRSPatient patient, String currentMotechId);

    /**
     * Finds a patient by Motech ID and updates the patients details in the MRS system.
     *
     * @param patient Patient instance with updated values (Motech IDentifier cannot be changed)
     * @return The Motech IDentifier of the updated patient if successfully updated
     */
    OpenMRSPatient updatePatient(OpenMRSPatient patient);

    /**
     * Fetches a patient by the given patient ID.
     *
     * @param patientId  value to be used to find a patient
     * @return patient with the given patient ID if one exists
     */
    OpenMRSPatient getPatient(String patientId);

    /**
     * Fetches a patient by Motech ID.
     *
     * @param motechId Value to be used to find a patient
     * @return Patient with the given Motech ID if exists
     */
    OpenMRSPatient getPatientByMotechId(String motechId);

    /**
     * Searches for patients in the MRS system by patient's name and Motech ID
     *
     * @param name  name of the patient to be searched for
     * @param motechId  Motech ID of the patient to be searched for
     * @return list of matched patients
     */
    List<OpenMRSPatient> search(String name, String motechId);

    /**
     * Marks a patient as dead with the given date of death and comment.
     *
     * @param motechId  deceased patient's Motech ID
     * @param causeOfDeath  the cause of death
     * @param dateOfDeath  patient's date of death
     * @param comment  additional information for the cause of death
     * @throws PatientNotFoundException when the expected Patient does not exist
     */
    void deceasePatient(String motechId, OpenMRSConcept causeOfDeath, Date dateOfDeath, String comment) throws PatientNotFoundException;

    /**
     * Deletes patient with the given UUID.
     *
     * @param uuid  the UUID of the patient
     * @throws PatientNotFoundException when the expected Patient does not exist
     */
    void deletePatient(String uuid) throws PatientNotFoundException;
}
