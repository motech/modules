package org.motechproject.openmrs19.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.EncounterResource;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service("encounterService")
public class OpenMRSEncounterServiceImpl implements OpenMRSEncounterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSEncounterServiceImpl.class);

    private final OpenMRSConceptServiceImpl conceptAdapter;
    private final OpenMRSPatientServiceImpl patientAdapter;

    private final OpenMRSConfigService configService;

    private final EncounterResource encounterResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSEncounterServiceImpl(EncounterResource encounterResource, OpenMRSPatientServiceImpl patientAdapter,
                                       OpenMRSConceptServiceImpl conceptAdapter, EventRelay eventRelay,
                                       OpenMRSConfigService configService) {
        this.encounterResource = encounterResource;
        this.patientAdapter = patientAdapter;
        this.conceptAdapter = conceptAdapter;
        this.configService = configService;
        this.eventRelay = eventRelay;
    }

    @Override
    public Encounter createEncounter(String configName, Encounter encounter) {
        validateEncounter(encounter);

        Encounter createdEncounter;
        Config config = configService.getConfigByName(configName);

        // OpenMRS expects the observations to reference a concept uuid rather
        // than just a concept name. Attempt to map all concept names to concept
        // uuid's for each of the observations
        List<Observation> updatedObs = resolveConceptUuidForConceptNames(config, encounter.getObs());

        try {
            encounter.setObs(updatedObs);
            createdEncounter = encounterResource.createEncounter(config, encounter);

            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT, EventHelper.encounterParameters(createdEncounter)));
        } catch (HttpClientErrorException e) {
            LOGGER.error("Could not create encounter: " + e.getMessage());
            return null;
        }

        return createdEncounter;
    }

    @Override
    public Encounter getLatestEncounterByPatientMotechId(String configName, String motechId, String encounterType) {
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
    public Encounter getEncounterByUuid(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            return encounterResource.getEncounterById(config, uuid);
        } catch (HttpClientErrorException e) {
            return null;
        }
    }

    @Override
    public List<Encounter> getEncountersByEncounterType(String configName, String motechId, String encounterType) {
        Validate.notEmpty(motechId, "MOTECH Id cannot be empty");

        Config config = configService.getConfigByName(configName);
        List<Encounter> encountersByEncounterType = new ArrayList<>();
        List<Encounter> encountersByPatientMotechId = getAllEncountersByPatientMotechId(config, motechId);

        for (Encounter encounter : encountersByPatientMotechId) {
            if (StringUtils.equals(encounter.getEncounterType().getName(), encounterType)) {
                encountersByEncounterType.add(encounter);
            }
        }

        return encountersByEncounterType;
    }

    @Override
    public void deleteEncounter(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            encounterResource.deleteEncounter(config, uuid);
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.DELETED_ENCOUNTER_SUBJECT, EventHelper.encounterParameters(uuid)));
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error deleting encounter with UUID: " + uuid);
        }
    }

    @Override
    public EncounterType createEncounterType(String configName, EncounterType encounterType) {
        try {
            Config config = configService.getConfigByName(configName);
            return encounterResource.createEncounterType(config, encounterType);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while creating encounter type with name: " + encounterType.getName());
            return null;
        }
    }

    @Override
    public EncounterType getEncounterTypeByUuid(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            return encounterResource.getEncounterTypeByUuid(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error while fetching encounter type with UUID: " + uuid);
            return null;
        }
    }

    @Override
    public void deleteEncounterType(String configName, String uuid) {
        try {
            Config config = configService.getConfigByName(configName);
            encounterResource.deleteEncounterType(config, uuid);
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error deleting encounter type with UUID: " + uuid);
        }
    }

    @Override
    public Encounter createEncounter(Encounter encounter) {
        return createEncounter(null, encounter);
    }

    @Override
    public Encounter getLatestEncounterByPatientMotechId(String motechId, String encounterType) {
        return getLatestEncounterByPatientMotechId(null, motechId, encounterType);
    }

    @Override
    public Encounter getEncounterByUuid(String uuid) {
        return getEncounterByUuid(null, uuid);
    }

    @Override
    public List<Encounter> getEncountersByEncounterType(String motechId, String encounterType) {
        return getEncountersByEncounterType(null, motechId, encounterType);
    }

    @Override
    public void deleteEncounter(String uuid) {
        deleteEncounter(null, uuid);
    }

    @Override
    public EncounterType createEncounterType(EncounterType encounterType) {
        return createEncounterType(null, encounterType);
    }

    @Override
    public EncounterType getEncounterTypeByUuid(String uuid) {
        return getEncounterTypeByUuid(null, uuid);
    }

    @Override
    public void deleteEncounterType(String uuid) {
        deleteEncounterType(null, uuid);
    }

    private void validateEncounter(Encounter encounter) {
        Validate.notNull(encounter, "Encounter cannot be null");
        Validate.notNull(encounter.getPatient(), "Patient cannot be null");
        Validate.notEmpty(encounter.getPatient().getUuid(), "Patient must have an id");
        Validate.notNull(encounter.getProvider(), "Provider cannot be null");
        Validate.notNull(encounter.getEncounterDatetime(), "Encounter Date cannot be null");
        Validate.notNull(encounter.getEncounterType(), "Encounter type cannot be null");
    }

    private List<Observation> resolveConceptUuidForConceptNames(Config config, List<Observation> originalObservations) {
        List<Observation> updatedObs = new ArrayList<>();
        if (originalObservations != null) {
            for (Observation observation : originalObservations) {
                String conceptUuid = conceptAdapter.resolveConceptUuidFromConceptName(config, observation.getConcept().getName().getName());
                if (CollectionUtils.isNotEmpty(observation.getGroupsMembers())) {
                    resolveConceptUuidForConceptNames(config, observation.getGroupsMembers());
                }
                observation.getConcept().setUuid(conceptUuid);
                updatedObs.add(observation);
            }
        }

        return updatedObs;
    }

    private List<Encounter> getAllEncountersByPatientMotechId(Config config, String motechId) {
        Validate.notEmpty(motechId, "MOTECH Id cannot be empty");

        List<Encounter> encounters = new ArrayList<>();
        Patient patient = patientAdapter.getPatientByMotechId(config, motechId);

        if (patient != null) {
            encounters.addAll(getEncountersForPatient(config, patient));
        }

        return encounters;
    }

    private List<Encounter> getEncountersForPatient(Config config, Patient patient) {
        List<Encounter> result;
        try {
            result = encounterResource.queryForAllEncountersByPatientId(config, patient.getUuid()).getResults();
        } catch (HttpClientErrorException e) {
            LOGGER.error("Error retrieving encounters for patient: " + patient.getUuid());
            return Collections.emptyList();
        }

        return result;
    }
}
