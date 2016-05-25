package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.ProgramEnrollment;

import java.util.List;

/**
 * Interface for programs enrollment management.
 */
public interface ProgramEnrollmentResource {

    /**
     * Creates the given program enrollment on the OpenMRS server. The given {@code config} will be used
     * while performing this action.
     *
     * @param config the configuration to be used while performing this action
     * @param programEnrollment the program enrollment to be created
     * @return the saved program enrollment
     */
    ProgramEnrollment createProgramEnrollment(Config config, ProgramEnrollment programEnrollment);

    /**
     * Updates the given program enrollment on the OpenMRS server. The given {@code config} will be used
     * while performing this action.
     *
     * @param config the configuration to be used while performing this action
     * @param programEnrollment the program enrollment to be updated
     * @return the updated program enrollment
     */
    ProgramEnrollment updateProgramEnrollment(Config config, ProgramEnrollment programEnrollment);

    /**
     * Gets program enrollments with given patient UUID. The given {@code config} will be used
     * while performing this action.
     *
     * @param config the configuration to be used while performing this action
     * @param patientUuid the UUID of the patient
     * @return the list of matching program enrollments
     */
    List<ProgramEnrollment> getProgramEnrollmentByPatientUuid(Config config, String patientUuid);
}
