package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.CohortQueryReport;

import java.util.Map;

/**
 * Interface for cohorts management.
 */
public interface CohortResource {

    /**
     * Gets the Cohort Query report from the OpenMRS server with given {@code parameters}.
     * The Cohort Query is identified by UUID.
     * Configuration with the given {@code config} will be used while performing this action.
     *
     * @param config the configuration to be used while performing this action
     * @param cohortQueryUuid  the cohort query uuid
     * @param parameters  the additional parameters
     * @return the cohort query report
     */
    CohortQueryReport getCohortQueryReport(Config config, String cohortQueryUuid, Map<String, String> parameters);

}
