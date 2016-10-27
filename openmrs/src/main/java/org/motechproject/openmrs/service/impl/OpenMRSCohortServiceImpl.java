package org.motechproject.openmrs.service.impl;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.CohortQueryReport;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.resource.CohortResource;
import org.motechproject.openmrs.service.OpenMRSCohortService;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Map;

@Service("cohortService")
public class OpenMRSCohortServiceImpl implements OpenMRSCohortService {

    private final CohortResource cohortResource;

    private final OpenMRSConfigService configService;

    @Autowired
    public OpenMRSCohortServiceImpl(CohortResource cohortResource, OpenMRSConfigService configService) {
        this.cohortResource = cohortResource;
        this.configService = configService;
    }

    @Override
    public CohortQueryReport getCohortQueryReport(String configName, String cohortQueryUuid, Map<String, String> parameters) {
        try {
            Config config = configService.getConfigByName(configName);
            return cohortResource.getCohortQueryReport(config, cohortQueryUuid, parameters);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not get cohort query report for uuid: %s. %s %s" + cohortQueryUuid, e.getMessage(), e.getResponseBodyAsString()), e);
        }
    }
}
