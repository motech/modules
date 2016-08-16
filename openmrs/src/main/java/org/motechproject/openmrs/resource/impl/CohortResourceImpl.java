package org.motechproject.openmrs.resource.impl;


import org.apache.commons.httpclient.HttpClient;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.CohortQueryReport;
import org.motechproject.openmrs.resource.CohortResource;
import org.motechproject.openmrs.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.Map;

@Component
public class CohortResourceImpl extends BaseResource implements CohortResource {

    private static final Logger LOGGER = LoggerFactory.getLogger(CohortResourceImpl.class);

    @Autowired
    protected CohortResourceImpl(RestOperations restOperations, HttpClient httpClient) {
        super(restOperations, httpClient);
    }

    @Override
    public CohortQueryReport getCohortQueryReport(Config config, String cohortQueryUuid, Map<String, String> parameters) {
        String path = prepareCohortQueryPath(cohortQueryUuid, parameters);
        String responseJson = getJson(config, path);
        CohortQueryReport cohortQueryReport = (CohortQueryReport) JsonUtils.readJson(responseJson, CohortQueryReport.class);

        LOGGER.info("Querying Cohort Query endpoint returned report with {} members", cohortQueryReport.getMembers().size());

        return cohortQueryReport;
    }

    private String prepareCohortQueryPath(String path, Map<String, String> parameters) {
        StringBuilder pathBuilder = new StringBuilder("/reportingrest/cohort/" + path);

        if (parameters != null && parameters.size() != 0) {
            pathBuilder.append("?");

            for (Map.Entry<String, String> parameter : parameters.entrySet()) {
                pathBuilder.append(String.format("%s=%s&", parameter.getKey(), parameter.getValue()));
            }

            // Remove last ampersand
            pathBuilder.setLength(pathBuilder.length() - 1);
        }

        return pathBuilder.toString();
    }
}
