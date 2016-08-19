package org.motechproject.openmrs.resource;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.CohortQueryReport;

import java.util.Map;

public interface CohortResource {
    CohortQueryReport getCohortQueryReport(Config config, String cohortQueryUuid, Map<String, String> parameters);
}
