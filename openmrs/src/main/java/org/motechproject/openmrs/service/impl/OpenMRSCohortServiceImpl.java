package org.motechproject.openmrs.service.impl;

import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.CohortQueryReport;
import org.motechproject.openmrs.domain.CohortQueryReport.CohortQueryReportMember;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.CohortResource;
import org.motechproject.openmrs.service.EventKeys;
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

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSCohortServiceImpl(CohortResource cohortResource, OpenMRSConfigService configService,
                                    EventRelay eventRelay) {
        this.cohortResource = cohortResource;
        this.configService = configService;
        this.eventRelay = eventRelay;
    }

    @Override
    public CohortQueryReport getCohortQueryReport(String configName, String cohortQueryUuid, Map<String, String> parameters) {
        try {
            Config config = configService.getConfigByName(configName);
            CohortQueryReport cohortQueryReport = cohortResource.getCohortQueryReport(config, cohortQueryUuid, parameters);

            for (CohortQueryReportMember member : cohortQueryReport.getMembers()) {
                eventRelay.sendEventMessage(new MotechEvent(EventKeys.GOT_COHORT_QUERY_MEMBER,
                        EventHelper.cohortMemberParameters(cohortQueryUuid, member)));
            }

            return cohortQueryReport;

        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Could not get cohort query report for uuid" + cohortQueryUuid, e);
        }
    }
}
