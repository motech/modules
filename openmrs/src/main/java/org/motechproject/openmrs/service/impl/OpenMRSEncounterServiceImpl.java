package org.motechproject.openmrs.service.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.EncounterType;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.exception.OpenMRSException;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.resource.EncounterResource;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.service.OpenMRSEncounterService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import java.util.ArrayList;
import java.util.List;

@Service("encounterService")
public class OpenMRSEncounterServiceImpl implements OpenMRSEncounterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSEncounterServiceImpl.class);

    private final OpenMRSPatientService patientService;

    private final OpenMRSConfigService configService;

    private final EncounterResource encounterResource;

    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSEncounterServiceImpl(EncounterResource encounterResource, OpenMRSPatientService patientAdapter,
                                       EventRelay eventRelay, OpenMRSConfigService configService) {
        this.encounterResource = encounterResource;
        this.patientService = patientAdapter;
        this.configService = configService;
        this.eventRelay = eventRelay;
    }

    @Override
    public Encounter createEncounter(String configName, Encounter encounter) {
        validateEncounter(encounter);

        Encounter createdEncounter;
        Config config = configService.getConfigByName(configName);

        try {
            createdEncounter = encounterResource.createEncounter(config, encounter);

            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT, EventHelper.encounterParameters(createdEncounter)));
        } catch (HttpClientErrorException e) {
            throw new OpenMRSException(String.format("Could not create encounter with patient uuid: %s. %s %s", encounter.getPatient().getUuid(), e.getMessage(), e.getResponseBodyAsString()), e);
        }

        return createdEncounter;
    }

    @Override
    public Encounter getLatestEncounterByPatientMotechId(String configName, String motechId, String encounterType) {
        Config config = configService.getConfigByName(configName);
        List<Encounter> encountersByEncounterType = getEncountersByEncounterType(config, motechId, encounterType);

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
            throw new OpenMRSException(String.format("Could not get encounter with uuid: %s. %s %s", uuid, e.getMessage(), e.getResponseBodyAsString()), e);
        }
    }

    @Override
    public List<Encounter> getEncountersByEncounterType(String configName, String motechId, String encounterType) {
        return getEncountersByEncounterType(configService.getConfigByName(configName), motechId, encounterType);
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

    private List<Encounter> getEncountersByEncounterType(Config config, String motechId, String encounterType) {
        Validate.notEmpty(motechId, "MOTECH Id cannot be empty");

        List<Encounter> encountersByEncounterType = new ArrayList<>();
        List<Encounter> encountersByPatientMotechId = getAllEncountersByPatientMotechId(config, motechId);

        for (Encounter encounter : encountersByPatientMotechId) {
            if (StringUtils.equals(encounter.getEncounterType().getUuid(), encounterType)) {
                encountersByEncounterType.add(encounter);
            }
        }

        return encountersByEncounterType;
    }

    private void validateEncounter(Encounter encounter) {
        Validate.notNull(encounter, "Encounter cannot be null");
        Validate.notNull(encounter.getPatient(), "Patient cannot be null");
        Validate.notEmpty(encounter.getPatient().getUuid(), "Patient must have an id");
        Validate.notNull(encounter.getEncounterProviders().get(0), "Provider cannot be null");
        Validate.notNull(encounter.getEncounterDatetime(), "Encounter Date cannot be null");
        Validate.notNull(encounter.getEncounterType(), "Encounter type cannot be null");
    }

    private List<Encounter> getAllEncountersByPatientMotechId(Config config, String motechId) {
        Validate.notEmpty(motechId, "MOTECH Id cannot be empty");

        List<Encounter> encounters = new ArrayList<>();
        Patient patient = patientService.getPatientByMotechId(config.getName(), motechId);

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
            throw new OpenMRSException(String.format("Could not get encounters for patient with uuid: %s. %s %s", patient.getUuid(), e.getMessage(), e.getResponseBodyAsString()), e);
        }

        return result;
    }
}
