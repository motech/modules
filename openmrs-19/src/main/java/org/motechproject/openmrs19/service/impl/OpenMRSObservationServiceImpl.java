package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.ObservationListResult;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.ObservationNotFoundException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.ConceptResource;
import org.motechproject.openmrs19.resource.ObservationResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSObservationService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("observationService")
public class OpenMRSObservationServiceImpl implements OpenMRSObservationService {
    private static final Logger LOGGER = Logger.getLogger(OpenMRSObservationServiceImpl.class);

    private final OpenMRSPatientService patientAdapter;
    private final ObservationResource obsResource;
    private final ConceptResource conceptResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSObservationServiceImpl(ObservationResource obsResource,
                                         OpenMRSPatientService patientAdapter,
                                         ConceptResource conceptResource,
                                         EventRelay eventRelay) {
        this.obsResource = obsResource;
        this.patientAdapter = patientAdapter;
        this.conceptResource = conceptResource;
        this.eventRelay = eventRelay;
    }

    @Override
    public List<Observation> findObservations(String motechId, String conceptName) {
        Validate.notEmpty(motechId, "Motech id cannot be empty");
        Validate.notEmpty(conceptName, "Concept name cannot be empty");

        List<Observation> obs = new ArrayList<>();
        Patient patient = patientAdapter.getPatientByMotechId(motechId);
        if (patient == null) {
            return obs;
        }

        ObservationListResult result;
        try {
            result = obsResource.queryForObservationsByPatientId(patient.getUuid());
        } catch (HttpException e) {
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

    @Override
    public void voidObservation(Observation observation, String reason) throws ObservationNotFoundException {
        Validate.notNull(observation);
        Validate.notEmpty(observation.getUuid());

        try {
            obsResource.voidObservation(observation.getUuid(), reason);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.VOIDED_OBSERVATION_SUBJECT, EventHelper.observationParameters(observation)));
        } catch (HttpException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                throw new ObservationNotFoundException("No Observation found with uuid: " + observation.getUuid(), e);
            }

            LOGGER.error("Could not void observation with uuid: " + observation.getUuid());
        }
    }

    @Override
    public Observation findObservation(String motechId, String conceptName) {
        Validate.notEmpty(motechId, "MOTECH Id cannot be empty");
        Validate.notEmpty(conceptName, "Concept name cannot be empty");

        List<Observation> observations = findObservations(motechId, conceptName);

        return observations.size() == 0 ? null : observations.get(0);
    }

    @Override
    public Observation getObservationByUuid(String uuid) {

        try {
            return obsResource.getObservationById(uuid);
        } catch (HttpException e) {
            LOGGER.error("Error while fetching obs with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public Observation createObservation(Observation observation) {
        Validate.notEmpty(observation.getPerson().getUuid(), "Patient uuid cannot be empty");
        Validate.notNull(observation.getConcept().getName(), "Concept name cannot be empty");
        Validate.notNull(observation.getObsDatetime());

        try {
            Concept concept = conceptResource.getConceptByName(observation.getConcept().getName().getName());
            Patient patient = patientAdapter.getPatientByUuid(observation.getPerson().getUuid());

            Validate.notNull(concept);
            Validate.notNull(patient);

            observation.setConcept(concept);
            observation.setPerson(patient.getPerson());

            Observation created = obsResource.createObservation(observation);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_OBSERVATION_SUBJECT, EventHelper.observationParameters(created)));
            return created;

        } catch (HttpException e) {
            LOGGER.error("Error while creating observation!");
            return null;
        }

    }

    @Override
    public void deleteObservation(String uuid) {
        try {
            obsResource.deleteObservation(uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_OBSERVATION_SUBJECT));
        } catch (HttpException e) {
            LOGGER.error("Error while deleting observation");
        }
    }
}
