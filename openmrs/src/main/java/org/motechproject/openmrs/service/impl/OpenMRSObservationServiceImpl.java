package org.motechproject.openmrs.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.ObservationListResult;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.exception.ObservationNotFoundException;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.ObservationResource;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSObservationService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("observationService")
public class OpenMRSObservationServiceImpl implements OpenMRSObservationService {
    private static final Logger LOGGER = Logger.getLogger(OpenMRSObservationServiceImpl.class);

    private final OpenMRSPatientService patientService;

    private final OpenMRSConfigService configService;

    private final ObservationResource obsResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSObservationServiceImpl(ObservationResource obsResource, OpenMRSPatientService patientAdapter,
                                         EventRelay eventRelay, OpenMRSConfigService configService) {
        this.obsResource = obsResource;
        this.patientService = patientAdapter;
        this.eventRelay = eventRelay;
        this.configService = configService;
    }

    @Override
    public List<Observation> findObservations(String configName, String motechId, String conceptName) {
        return findObservations(configService.getConfigByName(configName), motechId, conceptName);
    }

    @Override
    public void voidObservation(String configName, Observation observation, String reason) throws ObservationNotFoundException {
        Validate.notNull(observation);
        Validate.notEmpty(observation.getUuid());

        try {
            Config config = configService.getConfigByName(configName);
            obsResource.voidObservation(config, observation.getUuid(), reason);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.VOIDED_OBSERVATION_SUBJECT, EventHelper.observationParameters(observation)));
        } catch (HttpClientErrorException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                throw new ObservationNotFoundException("No Observation found with uuid: " + observation.getUuid(), e);
            }

            LOGGER.error("Could not void observation with uuid: " + observation.getUuid());
        }
    }

    @Override
    public Observation findObservation(String configName, String motechId, String conceptName) {
        Validate.notEmpty(motechId, "MOTECH Id cannot be empty");
        Validate.notEmpty(conceptName, "Concept name cannot be empty");

        Config config = configService.getConfigByName(configName);
        List<Observation> observations = findObservations(config, motechId, conceptName);

        return observations.size() == 0 ? null : observations.get(0);
    }

    @Override
    public Observation getObservationByUuid(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            return obsResource.getObservationById(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching obs with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public Observation getLatestObservationByPatientUUIDAndConceptUUID(String configName, String patientUUID, String conceptUUID) {
        Validate.notEmpty(patientUUID, "Patient uuid cannot be empty");
        Validate.notEmpty(conceptUUID, "Concept uuid cannot be empty");

        try {
            Config config = configService.getConfigByName(configName);
            ObservationListResult obs = obsResource.getObservationByPatientUUIDAndConceptUUID(config, patientUUID, conceptUUID);
            return CollectionUtils.isEmpty(obs.getResults()) ? null : obs.getResults().get(0);
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not get Observation for Patient uuid: %s and Concept: %s. %s %s",
                    patientUUID, conceptUUID, e.getMessage(), e.getResponseBodyAsString()), e);
        }
    }

    @Override
    public Observation getLatestObservationByValueAndPatientUuid(String configName, String patientUuid, String value) {
        try {
            Config config = configService.getConfigByName(configName);
            ObservationListResult obs = obsResource.queryForObservationsByPatientId(config, patientUuid);
            return getLatestObservationByValue(obs.getResults(), new Observation.ObservationValue(value));
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not get Observation for Patient uuid: %s. %s %s",
                    patientUuid, e.getMessage(), e.getResponseBodyAsString()), e);
        }
    }

    @Override
    public Observation createObservation(String configName, Observation observation) {
        Validate.notEmpty(observation.getPerson().getUuid(), "Patient uuid cannot be empty");
        Validate.notEmpty(observation.getConcept().getUuid(), "Concept uuid cannot be empty");
        Validate.notNull(observation.getObsDatetime());

        try {
            Config config = configService.getConfigByName(configName);

            Observation created = obsResource.createObservation(config, observation);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_OBSERVATION_SUBJECT, EventHelper.observationParameters(created)));
            return created;
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Error while creating observation. Response body: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public Observation createObservationFromJson(String configName, String observationJson) {
        try {
            Config config = configService.getConfigByName(configName);

            Observation created = obsResource.createObservationFromJson(config, observationJson);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_OBSERVATION_SUBJECT, EventHelper.observationParameters(created)));
            return created;
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException("Error while creating observation. Response body: " + e.getResponseBodyAsString(), e);
        }
    }

    @Override
    public void deleteObservation(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            obsResource.deleteObservation(config, uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_OBSERVATION_SUBJECT));
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while deleting observation");
        }
    }

    private List<Observation> findObservations(Config config, String motechId, String conceptName) {
        Validate.notEmpty(motechId, "Motech id cannot be empty");
        Validate.notEmpty(conceptName, "Concept name cannot be empty");

        List<Observation> obs = new ArrayList<>();
        Patient patient = patientService.getPatientByMotechId(config.getName(), motechId);
        if (patient == null) {
            return obs;
        }

        ObservationListResult result;
        try {
            result = obsResource.queryForObservationsByPatientId(config, patient.getUuid());
        } catch (HttpClientErrorException e) {
            LOGGER.error("Could not retrieve observations for patient with motech id: " + motechId);
            return Collections.emptyList();
        }

        for (Observation ob : result.getResults()) {
            if (ob.hasConceptByName(conceptName)) {
                obs.add(ob);
            }
        }

        return obs;
    }

    private Observation getLatestObservationByValue(List<Observation> obs, Observation.ObservationValue value) {
        Observation result = null;

        for (Observation observation : obs) {
            if (value.equals(observation.getValue())) {
                result = observation;
                break;
            }
        }

        return result;
    }
}
