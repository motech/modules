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

    List<ProgramEnrollment> getProgramEnrollmentByPatientUuid(String configName, String patientUuid);

    List<ProgramEnrollment> getProgramEnrollmentByPatientMotechId(String configName, String patientMotechId);
}
