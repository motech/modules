package org.motechproject.openmrs19.service.impl;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.EventKeys;
import org.motechproject.openmrs19.domain.OpenMRSObservation;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.exception.ObservationNotFoundException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.service.OpenMRSObservationService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.rest.HttpException;
import org.motechproject.openmrs19.resource.ObservationResource;
import org.motechproject.openmrs19.resource.model.Observation;
import org.motechproject.openmrs19.resource.model.ObservationListResult;
import org.motechproject.openmrs19.util.ConverterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component("observationService")
public class OpenMRSObservationServiceImpl implements OpenMRSObservationService {
    private static final Logger LOGGER = Logger.getLogger(OpenMRSObservationServiceImpl.class);

    private final OpenMRSPatientService patientAdapter;
    private final ObservationResource obsResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSObservationServiceImpl(ObservationResource obsResource, OpenMRSPatientService patientAdapter, EventRelay eventRelay) {
        this.obsResource = obsResource;
        this.patientAdapter = patientAdapter;
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

        ObservationListResult result = null;
        try {
            result = obsResource.queryForObservationsByPatientId(patient.getPatientId());
        } catch (HttpException e) {
            LOGGER.error("Could not retrieve observations for patient with motech id: " + motechId);
            return Collections.emptyList();
        }

        for (Observation ob : result.getResults()) {
            if (ob.hasConceptByName(conceptName)) {
                obs.add(ConverterUtils.convertObservationToMrsObservation(ob));
            }
        }

        return obs;
    }

    @Override
    public void voidObservation(OpenMRSObservation mrsObservation, String reason, String mrsUserMotechId)
            throws ObservationNotFoundException {
        Validate.notNull(mrsObservation);
        Validate.notEmpty(mrsObservation.getObservationId());

        try {
            obsResource.deleteObservation(mrsObservation.getObservationId(), reason);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_OBSERVATION_SUBJECT, EventHelper.observationParameters(mrsObservation)));
        } catch (HttpException e) {
            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {
                LOGGER.warn("No Observation found with uuid: " + mrsObservation.getObservationId());
                throw new ObservationNotFoundException(mrsObservation.getObservationId(), e);
            }

            LOGGER.error("Could not void observation with uuid: " + mrsObservation.getObservationId());
        }
    }

    @Override
    public OpenMRSObservation findObservation(String patientMotechId, String conceptName) {
        Validate.notEmpty(patientMotechId, "MoTeCH Id cannot be empty");
        Validate.notEmpty(conceptName, "Concept name cannot be empty");

        List<OpenMRSObservation> observations = findObservations(patientMotechId, conceptName);
        if (observations.size() == 0) {
            return null;
        }

        return observations.get(0);
    }

    @Override
    public OpenMRSObservation getObservationById(String id) {
        try {
            Observation obs = obsResource.getObservationById(id);
            return ConverterUtils.convertObservationToMrsObservation(obs);
        } catch (HttpException e) {
            return null;
        }
    }
}
