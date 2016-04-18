package org.motechproject.openmrs19.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.EncounterResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("encounterService")
public class OpenMRSEncounterServiceImpl implements OpenMRSEncounterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSEncounterServiceImpl.class);

    private final OpenMRSPatientService patientAdapter;
    private final OpenMRSConceptServiceImpl conceptAdapter;
    private final EncounterResource encounterResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSEncounterServiceImpl(EncounterResource encounterResource, OpenMRSPatientService patientAdapter,
            OpenMRSConceptServiceImpl conceptAdapter, EventRelay eventRelay) {
        this.encounterResource = encounterResource;
        this.patientAdapter = patientAdapter;
        this.conceptAdapter = conceptAdapter;
        this.eventRelay = eventRelay;
    }

    @Override
    public Encounter createEncounter(Encounter encounter) {
        validateEncounter(encounter);

        Encounter createdEncounter;

        // OpenMRS expects the observations to reference a concept uuid rather
        // than just a concept name. Attempt to map all concept names to concept
        // uuid's for each of the observations
        List<Observation> updatedObs = resolveConceptUuidForConceptNames(encounter.getObs());

        try {
            encounter.setObs(updatedObs);
            createdEncounter = encounterResource.createEncounter(encounter);

            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT, EventHelper.encounterParameters(createdEncounter)));
        } catch (HttpException e) {
            LOGGER.error("Could not create encounter: " + e.getMessage());
            return null;
        }

        return createdEncounter;
    }

    @Override
    public Encounter getLatestEncounterByPatientMotechId(String motechId, String encounterType) {
        List<Encounter> encountersByEncounterType = getEncountersByEncounterType(motechId, encounterType);

        Encounter latestEncounter = null;
        for (Encounter encounter : encountersByEncounterType) {
            if (latestEncounter == null) {
                latestEncounter = encounter;
            } else {
                latestEncounter = encounter.getEncounterDatetime().after(latestEncounter.getEncounterDatetime()) ? encounter : latestEncounter;
            }
        }

        return latestEncounter;
    }

    @Override
    public Encounter getEncounterByUuid(String uuid) {
        try {
            return encounterResource.getEncounterById(uuid);
        } catch (HttpException e) {
            return null;
        }
    }

    @Override
    public List<Encounter> getEncountersByEncounterType(String motechId, String encounterType) {
        Validate.notEmpty(motechId, "MOTECH Id cannot be empty");

        List<Encounter> encountersByEncounterType = new ArrayList<>();
        List<Encounter> encountersByPatientMotechId = getAllEncountersByPatientMotechId(motechId);

        for (Encounter encounter : encountersByPatientMotechId) {
            if (StringUtils.equals(encounter.getEncounterType().getName(), encounterType)) {
                encountersByEncounterType.add(encounter);
            }
        }

        return encountersByEncounterType;
    }

    @Override
    public void deleteEncounter(String uuid) {
        try {
            encounterResource.deleteEncounter(uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_ENCOUNTER_SUBJECT, EventHelper.encounterParameters(uuid)));
        } catch (HttpException e) {
            LOGGER.error("Error deleting encounter with UUID: " + uuid);
        }
    }

    @Override
    public EncounterType createEncounterType(EncounterType encounterType) {
        try {
            return encounterResource.createEncounterType(encounterType);
        } catch (HttpException e) {
            LOGGER.error("Error while creating encounter type with name: " + encounterType.getName());
            return null;
        }
    }

    @Override
    public EncounterType getEncounterTypeByUuid(String uuid) {
        try {
            return encounterResource.getEncounterTypeByUuid(uuid);
        } catch (HttpException e) {
            LOGGER.error("Error while fetching encounter type with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public void deleteEncounterType(String uuid) {
        try {
            encounterResource.deleteEncounterType(uuid);
        } catch (HttpException e) {
            LOGGER.error("Error deleting encounter type with UUID: " + uuid);
        }
    }

    private void validateEncounter(Encounter encounter) {
        Validate.notNull(encounter, "Encounter cannot be null");
        Validate.notNull(encounter.getPatient(), "Patient cannot be null");
        Validate.notEmpty(encounter.getPatient().getUuid(), "Patient must have an id");
        Validate.notNull(encounter.getProvider(), "Provider cannot be null");
        Validate.notNull(encounter.getEncounterDatetime(), "Encounter Date cannot be null");
        Validate.notNull(encounter.getEncounterType(), "Encounter type cannot be null");
    }

    private List<Observation> resolveConceptUuidForConceptNames(List<Observation> originalObservations) {
        List<Observation> updatedObs = new ArrayList<>();
        if (originalObservations != null) {
            for (Observation observation : originalObservations) {
                String conceptUuid = conceptAdapter.resolveConceptUuidFromConceptName(observation.getConcept().getName().getName());
                if (CollectionUtils.isNotEmpty(observation.getGroupsMembers())) {
                    resolveConceptUuidForConceptNames(observation.getGroupsMembers());
                }
                observation.getConcept().setUuid(conceptUuid);
                updatedObs.add(observation);
            }
        }

        return updatedObs;
    }

    private List<Encounter> getAllEncountersByPatientMotechId(String motechId) {
        Validate.notEmpty(motechId, "MOTECH Id cannot be empty");

        List<Encounter> encounters = new ArrayList<>();
        Patient patient = patientAdapter.getPatientByMotechId(motechId);

        if (patient != null) {
            encounters.addAll(getEncountersForPatient(patient));
        }

        return encounters;
    }

    private List<Encounter> getEncountersForPatient(Patient patient) {
        List<Encounter> result;
        try {
            result = encounterResource.queryForAllEncountersByPatientId(patient.getUuid()).getResults();
        } catch (HttpException e) {
            LOGGER.error("Error retrieving encounters for patient: " + patient.getUuid());
            return Collections.emptyList();
        }

        return result;
    }
}
