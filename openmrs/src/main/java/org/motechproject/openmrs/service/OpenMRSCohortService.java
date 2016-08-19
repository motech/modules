package org.motechproject.openmrs.service;

import org.motechproject.openmrs.domain.CohortQueryReport;

import java.util.Map;

public interface OpenMRSCohortService {
    // MOTECH-2801: method should return some kind of collection (containing cohort members)
    CohortQueryReport getCohortQueryReport(String configName, String cohortQueryUuid, Map<String, String> parameters);
}
