package org.motechproject.openmrs19.resource;

import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.ProgramEnrollment;

/**
 * Initerface for programs enrollment management.
 */
public interface ProgramEnrollmentResource {

    ProgramEnrollment createProgramEnrollment(Config config, ProgramEnrollment programEnrollment);
}
