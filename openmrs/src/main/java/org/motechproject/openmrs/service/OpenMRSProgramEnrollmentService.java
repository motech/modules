package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.ProgramEnrollment;

import java.util.List;

/**
 * Interface for handling programs enrollment on the OpenMRS server.
 */
public interface OpenMRSProgramEnrollmentService {

    /**
     * Creates the given program enrollment on the OpenMRS server.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName the name of the configuration
     * @param programEnrollment the program enrollment to be created
     * @return the created program enrollment
     */
    ProgramEnrollment createProgramEnrollment(String configName, ProgramEnrollment programEnrollment);

    /**
     * Updates the given program enrollment on the OpenMRS server.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName the name of the configuration
     * @param programEnrollment the program enrollment to be updated
     * @return the updated program enrollment
     */
    ProgramEnrollment updateProgramEnrollment(String configName, ProgramEnrollment programEnrollment);

    /**
     * Returns Bahmni program enrollments associated with the patient having given UUID.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName the name of the configuration
     * @param patientUuid the UUID of the patient
     * @return list of matching Bahmni program enrollments
     */
    List<ProgramEnrollment> getBahmniProgramEnrollmentByPatientUuid(String configName, String patientUuid);

    /**
     * Returns Bahmni program enrollments associated with the patient having given MOTECH ID.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName the name of the configuration
     * @param patientMotechId the MOTECH ID of the patient
     * @return list of matching Bahmni program enrollments
     */
    List<ProgramEnrollment> getBahmniProgramEnrollmentByPatientMotechId(String configName, String patientMotechId);

    /**
     * Returns program enrollments associated with the patient having given UUID.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName the name of the configuration
     * @param patientUuid the UUID of the patient
     * @return list of matching program enrollments
     */
    List<ProgramEnrollment> getProgramEnrollmentByPatientUuid(String configName, String patientUuid);

    /**
     * Returns program enrollments associated with the patient having given MOTECH ID.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName the name of the configuration
     * @param patientMotechId the MOTECH ID of the patient
     * @return list of matching program enrollments
     */
    List<ProgramEnrollment> getProgramEnrollmentByPatientMotechId(String configName, String patientMotechId);

    /**
     * Deletes program enrollment with the given uuid.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName the name of the configuration
     * @param uuid the UUID of the program enrollment
     */
    void deleteProgramEnrollment(String configName, String uuid);
}
