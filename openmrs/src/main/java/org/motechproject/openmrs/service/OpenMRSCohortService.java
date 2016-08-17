package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.CohortQueryReport;

import java.util.Map;

/**
 * Service responsible for managing cohorts on the OpenMRS servers.
 */
public interface OpenMRSCohortService {

    /**
     * Gets the Cohort Query report from the OpenMRS server with given {@code parameters}.
     * The Cohort Query is identified by UUID.
     * Configuration with the given {@code configName} will be used while performing this action.
     *
     * @param configName  the name of the configuration
     * @param cohortQueryUuid  the cohort query uuid
     * @param parameters  the additional parameters
     * @return the cohort query report
     */
    CohortQueryReport getCohortQueryReport(String configName, String cohortQueryUuid, Map<String, String> parameters);

}
