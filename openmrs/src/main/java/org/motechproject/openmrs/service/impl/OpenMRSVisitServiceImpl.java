package org.motechproject.openmrs.service.impl;

import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Visit;
import org.motechproject.openmrs.domain.VisitType;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.VisitResource;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSVisitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

@Service("visitService")
public class OpenMRSVisitServiceImpl implements OpenMRSVisitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSVisitServiceImpl.class);

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

    @Override
    public Visit getVisitByUuid(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            return visitResource.getVisitById(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching visit with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public void deleteVisit(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            visitResource.deleteVisit(config, uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_VISIT_SUBJECT, EventHelper.visitParameters(uuid)));
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error deleting visit with UUID: " + uuid);
        }
    }

    @Override
    public VisitType createVisitType(String configName, VisitType visitType) {
        try {
            Config config = configService.getConfigByName(configName);
            return visitResource.createVisitType(config, visitType);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while creating visit type with name: " + visitType.getName());
            return null;
        }
    }

    @Override
    public VisitType getVisitTypeByUuid(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            return visitResource.getVisitTypeById(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching visit type with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public void deleteVisitType(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            visitResource.deleteVisitType(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error deleting visit type with UUID: " + uuid);
        }

    }

    private void validateVisit(Visit visit) {
        Validate.notNull(visit, "Visit cannot be null");
        Validate.notNull(visit.getPatient(), "Patient cannot be null");
        Validate.notEmpty(visit.getPatient().getUuid(), "Patient must have an id");
        Validate.notNull(visit.getStartDatetime(), "Visit start datetime cannot be null");
        Validate.notNull(visit.getStopDatetime(), "Visit end datetime cannot be null");
    }
}
