package org.motechproject.openmrs.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Visit;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.VisitResource;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSVisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("visitService")
public class OpenMRSVisitServiceImpl implements OpenMRSVisitService {

    private final OpenMRSConfigService configService;
    private final VisitResource visitResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSVisitServiceImpl(OpenMRSConfigService configService, VisitResource visitResource, EventRelay eventRelay) {
        this.configService = configService;
        this.visitResource = visitResource;
        this.eventRelay = eventRelay;
    }

    @Override
    public Visit createVisit(String configName, Visit visit) {
        validateVisit(visit);

        Config config = configService.getConfigByName(configName);
        Visit createdVisit = visitResource.createVisit(config, visit);

        eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_VISIT_SUBJECT, EventHelper.visitParameters(createdVisit)));
        return createdVisit;
    }

    private void validateVisit(Visit visit) {
        Validate.notNull(visit, "Visit cannot be null");
        Validate.notNull(visit.getPatient(), "Patient cannot be null");
        Validate.notEmpty(visit.getPatient().getUuid(), "Patient must have an id");
        Validate.notNull(visit.getStartDatetime(), "Visit start datetime cannot be null");
        Validate.notNull(visit.getStopDatetime(), "Visit end datetime cannot be null");
    }
}
