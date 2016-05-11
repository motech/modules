package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.ProgramEnrollment;

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
}
