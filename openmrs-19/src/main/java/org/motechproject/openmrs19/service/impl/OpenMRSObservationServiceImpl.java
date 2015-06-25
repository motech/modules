package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.OpenMRSObservation;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.ObservationNotFoundException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.ConceptResource;
import org.motechproject.openmrs19.resource.ObservationResource;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.Observation;
import org.motechproject.openmrs19.resource.model.ObservationListResult;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSObservationService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.util.ConverterUtils;
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
    public List<OpenMRSObservation> findObservations(String motechId, String conceptName) {

        Validate.notEmpty(motechId, "Motech id cannot be empty");
        Validate.notEmpty(conceptName, "Concept name cannot be empty");

        List<OpenMRSObservation> obs = new ArrayList<>();
        OpenMRSPatient patient = patientAdapter.getPatientByMotechId(motechId);
        if (patient == null) {
            return obs;
        }

        ObservationListResult result;
        try {
            result = obsResource.queryForObservationsByPatientId(patient.getPatientId());
        } catch (HttpException e) {
            LOGGER.error("Could not retrieve observations for patient with motech id: " + motechId);
            return Collections.emptyList();
        }

        for (Observation ob : result.getResults()) {
            if (ob.hasConceptByName(conceptName)) {
                obs.add(ConverterUtils.toOpenMRSObservation(ob));
            }
        }

        return obs;
    }

    @Override
    public void voidObservation(OpenMRSObservation observation, String reason) throws ObservationNotFoundException {

        Validate.notNull(observation);
        Validate.notEmpty(observation.getObservationId());

        try {
            obsResource.voidObservation(observation.getObservationId(), reason);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.VOIDED_OBSERVATION_SUBJECT, EventHelper.observationParameters(observation)));
        } catch (HttpException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                throw new ObservationNotFoundException("No Observation found with uuid: " + observation.getObservationId(), e);
            }

            LOGGER.error("Could not void observation with uuid: " + observation.getObservationId());
        }
    }

    @Override
    public OpenMRSObservation findObservation(String motechId, String conceptName) {

        Validate.notEmpty(motechId, "MoTeCH Id cannot be empty");
        Validate.notEmpty(conceptName, "Concept name cannot be empty");

        List<OpenMRSObservation> observations = findObservations(motechId, conceptName);

        return observations.size() == 0 ? null : observations.get(0);
    }

    @Override
    public OpenMRSObservation getObservationByUuid(String uuid) {

        try {
            return ConverterUtils.toOpenMRSObservation(obsResource.getObservationById(uuid));
        } catch (HttpException e) {
            LOGGER.error("Error while fetching obs with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public OpenMRSObservation createObservation(OpenMRSObservation openMRSObservation) {

        Validate.notEmpty(openMRSObservation.getPatientId(), "MoTeCH Id cannot be empty");
        Validate.notEmpty(openMRSObservation.getConceptName(), "Concept name cannot be empty");
        Validate.notNull(openMRSObservation.getDate());

        try {
            Concept concept = conceptResource.getConceptByName(openMRSObservation.getConceptName());
            OpenMRSPatient patient = patientAdapter.getPatientByMotechId(openMRSObservation.getPatientId());

            Validate.notNull(concept);
            Validate.notNull(patient);

            Observation observation = ConverterUtils.toObservation(openMRSObservation);
            observation.setConcept(concept);
            observation.setPerson(ConverterUtils.toPerson(patient.getPerson(), true));

            OpenMRSObservation created = ConverterUtils.toOpenMRSObservation(obsResource.createObservation(observation));
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
