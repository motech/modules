package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.resource.model.Patient;
import org.motechproject.openmrs19.resource.model.PatientListResult;

/**
 * Interface for patients management.
 */
public interface PatientResource {

    /**
     * Creates the given patient on the OpenMRS server.
     *
     * @param patient  the patient to be created
     * @return  the saved patient
     * @throws HttpException  when there were problems while creating patient
     */
    Patient createPatient(Patient patient) throws HttpException;

    /**
     * Returns {@code PatientListResult} of all patients matching given term.
     *
     * @param term  the term to be matched
     * @return  the list of matching patients
     * @throws HttpException  when there were problems while fetching patients
     */
    PatientListResult queryForPatient(String term) throws HttpException;

    /**
     * Gets patient by its UUID.
     *
     * @param patientId  the UUID of the patient
     * @return  the patient with the given UUID.
     * @throws HttpException  when there were problems while fetching patient
     */
    Patient getPatientById(String patientId) throws HttpException;

    /**
     * Returns the UUID of the MOTECH patient identifier.
     *
     * @return  the UUD of the MOTECH patient identifier
     * @throws HttpException  when there were problems while fetching MOTECH patient identifier
     */
    String getMotechPatientIdentifierUuid() throws HttpException;

    /**
     * Deletes the patient with the given UUID.
     * @param uuid  the UUID of the patient
     * @throws HttpException  when there were problems while deleting patient
     */
    void deletePatient(String uuid) throws HttpException;

    /**
     * Updates the MOTECH Id for the patient with given UUID.
     *
     * @param patientUuid  the UUID of the patient
     * @param newMotechId  the new MOTECH Id
     * @throws HttpException  when there were problems while updating MOTECH Id
     */
    void updatePatientMotechId(String patientUuid, String newMotechId) throws HttpException;

}
