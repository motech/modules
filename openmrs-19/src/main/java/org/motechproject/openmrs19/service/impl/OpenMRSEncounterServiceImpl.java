package org.motechproject.openmrs19.service.impl;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs19.domain.OpenMRSEncounterType;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.domain.OpenMRSEncounter;
import org.motechproject.openmrs19.domain.OpenMRSObservation;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.helper.EventHelper;
import org.motechproject.openmrs19.resource.EncounterResource;
import org.motechproject.openmrs19.resource.model.Concept;
import org.motechproject.openmrs19.resource.model.Encounter;
import org.motechproject.openmrs19.resource.model.Encounter.EncounterType;
import org.motechproject.openmrs19.resource.model.EncounterListResult;
import org.motechproject.openmrs19.resource.model.Location;
import org.motechproject.openmrs19.resource.model.Observation;
import org.motechproject.openmrs19.resource.model.Observation.ObservationValue;
import org.motechproject.openmrs19.resource.model.Patient;
import org.motechproject.openmrs19.resource.model.Person;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.util.ConverterUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service("encounterService")
public class OpenMRSEncounterServiceImpl implements OpenMRSEncounterService {
    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSEncounterServiceImpl.class);

    private final OpenMRSPatientService patientAdapter;
    private final OpenMRSPersonServiceImpl personAdapter;
    private final OpenMRSConceptServiceImpl conceptAdapter;
    private final EncounterResource encounterResource;
    private final EventRelay eventRelay;

    @Autowired
    public OpenMRSEncounterServiceImpl(EncounterResource encounterResource, OpenMRSPatientService patientAdapter,
            OpenMRSPersonServiceImpl personAdapter, OpenMRSConceptServiceImpl conceptAdapter, EventRelay eventRelay) {
        this.encounterResource = encounterResource;
        this.patientAdapter = patientAdapter;
        this.personAdapter = personAdapter;
        this.conceptAdapter = conceptAdapter;
        this.eventRelay = eventRelay;
    }

    @Override
    public OpenMRSEncounter createEncounter(OpenMRSEncounter encounter) {
        validateEncounter(encounter);

        // OpenMRS expects the observations to reference a concept uuid rather
        // than just a concept name. Attempt to map all concept names to concept
        // uuid's for each of the observations
        Set<? extends OpenMRSObservation> updatedObs = resolveConceptUuidForConceptNames(encounter.getObservations());
        OpenMRSEncounter encounterCopy = new OpenMRSEncounter.OpenMRSEncounterBuilder().withId(encounter.getEncounterId())
                .withProvider(encounter.getProvider()).withCreator(encounter.getCreator())
                .withFacility(encounter.getFacility()).withDate(encounter.getDate().toDate())
                .withPatient(encounter.getPatient()).withObservations(updatedObs)
                .withEncounterType(encounter.getEncounterType()).build();

        Encounter converted = toEncounter(encounterCopy);
        Encounter saved;
        OpenMRSEncounter returnedEncounter;
        try {
            saved = encounterResource.createEncounter(converted);
            returnedEncounter = new OpenMRSEncounter.OpenMRSEncounterBuilder().withId(saved.getUuid()).withProvider(encounter.getProvider())
                    .withCreator(encounter.getCreator()).withFacility(encounter.getFacility())
                    .withDate(encounter.getDate().toDate()).withPatient(encounter.getPatient())
                    .withObservations(encounter.getObservations()).withEncounterType(encounter.getEncounterType()).build();
            eventRelay.sendEventMessage(new MotechEvent(EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT, EventHelper.encounterParameters(returnedEncounter)));
        } catch (HttpException e) {
            LOGGER.error("Could not create encounter: " + e.getMessage());
            return null;
        }

        return returnedEncounter;
    }

    private void validateEncounter(OpenMRSEncounter encounter) {
        Validate.notNull(encounter, "Encounter cannot be null");
        Validate.notNull(encounter.getPatient(), "Patient cannot be null");
        Validate.notEmpty(encounter.getPatient().getPatientId(), "Patient must have an id");
        Validate.notNull(encounter.getDate(), "Encounter Date cannot be null");
        Validate.notEmpty(encounter.getEncounterType(), "Encounter type cannot be empty");
    }

    private Encounter toEncounter(OpenMRSEncounter encounter) {
        Encounter converted = new Encounter();
        converted.setEncounterDatetime(encounter.getDate().toDate());

        EncounterType encounterType = new EncounterType();
        encounterType.setName(encounter.getEncounterType());
        converted.setEncounterType(encounterType);

        Location location = new Location();
        location.setUuid(encounter.getFacility().getFacilityId());
        converted.setLocation(location);

        Patient patient = new Patient();
        patient.setUuid(encounter.getPatient().getPatientId());
        converted.setPatient(patient);

        Person person = new Person();
        person.setUuid(encounter.getProvider().getPerson().getId());
        converted.setProvider(person);

        converted.setObs(convertToObservations(encounter.getObservations()));

        return converted;
    }

    private List<Observation> convertToObservations(Set<? extends OpenMRSObservation> observations) {
        List<Observation> obs = new ArrayList<>();

        for (OpenMRSObservation observation : observations) {
            Observation ob = new Observation();
            ob.setObsDatetime(observation.getDate().toDate());

            Concept concept = new Concept();
            concept.setDisplay(observation.getConceptName());
            ob.setConcept(concept);

            ObservationValue value = new ObservationValue();
            value.setDisplay(observation.getValue().toString());
            ob.setValue(value);

            if (CollectionUtils.isNotEmpty(observation.getDependentObservations())) {
                ob.setGroupsMembers(convertToObservations(observation.getDependentObservations()));
            }

            obs.add(ob);
        }

        return obs;
    }

    private Set<? extends OpenMRSObservation> resolveConceptUuidForConceptNames(Set<? extends OpenMRSObservation> originalObservations) {
        Set<OpenMRSObservation> updatedObs = new HashSet<>();
        for (OpenMRSObservation observation : originalObservations) {
            String conceptUuid = conceptAdapter.resolveConceptUuidFromConceptName(observation.getConceptName());
            if (CollectionUtils.isNotEmpty(observation.getDependentObservations())) {
                resolveConceptUuidForConceptNames(observation.getDependentObservations());
            }
            updatedObs.add(new OpenMRSObservation(observation.getObservationId(), observation.getDate().toDate(), conceptUuid, observation
                    .getValue()));
        }

        return updatedObs;
    }

    @Override
    public OpenMRSEncounter getLatestEncounterByPatientMotechId(String motechId, String encounterType) {
        Validate.notEmpty(motechId, "MoTeCH Id cannot be empty");

        List<OpenMRSEncounter> previousEncounters = getAllEncountersByPatientMotechId(motechId);

        removeEncounters(previousEncounters, encounterType);

        OpenMRSEncounter latestEncounter = null;
        for (OpenMRSEncounter enc : previousEncounters) {
            if (latestEncounter == null) {
                latestEncounter = enc;
            } else {
                latestEncounter = enc.getDate().isAfter(latestEncounter.getDate()) ? enc : latestEncounter;
            }
        }

        return latestEncounter;
    }

    public List<OpenMRSEncounter> getAllEncountersByPatientMotechId(String motechId) {
        Validate.notEmpty(motechId, "MoTeCH Id cannot be empty");

        List<OpenMRSEncounter> encounters = new ArrayList<>();
        OpenMRSPatient patient = patientAdapter.getPatientByMotechId(motechId);

        if (patient != null) {
            encounters.addAll(getEncountersForPatient(patient));
        }

        return encounters;
    }

    private List<OpenMRSEncounter> getEncountersForPatient(OpenMRSPatient patient) {
        EncounterListResult result;
        try {
            result = encounterResource.queryForAllEncountersByPatientId(patient.getPatientId());
        } catch (HttpException e) {
            LOGGER.error("Error retrieving encounters for patient: " + patient.getPatientId());
            return Collections.emptyList();
        }

        if (result.getResults().size() == 0) {
            return Collections.emptyList();
        }

        // the response JSON from the OpenMRS does not contain full information
        // for
        // the provider. therefore, separate request(s) must be made to obtain
        // full provider
        // information. As an optimization, only make 1 request per unique
        // provider
        Map<String, OpenMRSPerson> providers = new HashMap<>();
        for (Encounter encounter : result.getResults()) {
            String providerUuid = encounter.getProvider().getUuid();
            OpenMRSPerson provider = personAdapter.getByUuid(providerUuid);
            providers.put(providerUuid, provider);
        }

        List<OpenMRSEncounter> updatedEncounters = new ArrayList<>();
        for (Encounter encounter : result.getResults()) {
            OpenMRSPerson person = providers.get(encounter.getProvider().getUuid());
            OpenMRSProvider provider = new OpenMRSProvider(person);
            provider.setProviderId(person.getPersonId());
            OpenMRSEncounter mrsEncounter = convertToMrsEncounter(encounter,
                    provider, patient);
            updatedEncounters.add(mrsEncounter);
        }

        return updatedEncounters;
    }

    private OpenMRSEncounter convertToMrsEncounter(Encounter encounter, OpenMRSProvider mrsPerson, OpenMRSPatient patient) {

        return new OpenMRSEncounter.OpenMRSEncounterBuilder().withId(encounter.getUuid()).withProvider(mrsPerson)
                .withFacility(ConverterUtils.toOpenMRSFacility(encounter.getLocation()))
                .withDate(encounter.getEncounterDatetime()).withPatient(patient)
                .withObservations(convertToMrsObservation(encounter.getObs()))
                .withEncounterType(encounter.getEncounterType().getName()).build();
    }

    private Set<? extends OpenMRSObservation> convertToMrsObservation(List<Observation> obs) {
        Set<OpenMRSObservation> mrsObs = new HashSet<>();

        for (Observation ob : obs) {
            mrsObs.add(ConverterUtils.toOpenMRSObservation(ob));
        }

        return mrsObs;
    }

    @Override
    public OpenMRSEncounter getEncounterById(String id) {
        try {
            Encounter encounter = encounterResource.getEncounterById(id);
            OpenMRSPatient patient = patientAdapter.getPatient(encounter.getPatient().getUuid());
            OpenMRSPerson person = personAdapter.getByUuid(encounter.getProvider().getUuid());
            OpenMRSProvider provider = new OpenMRSProvider(person);
            provider.setProviderId(person.getPersonId());
            return convertToMrsEncounter(encounter, provider, patient);
        } catch (HttpException e) {
            return null;
        }
    }

    @Override
    public List<OpenMRSEncounter> getEncountersByEncounterType(String motechId, String encounterType) {
        Validate.notEmpty(motechId, "MoTeCH Id cannot be empty");

        List<OpenMRSEncounter> previousEncounters = getAllEncountersByPatientMotechId(motechId);

        removeEncounters(previousEncounters, encounterType);

        return previousEncounters;
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
    public OpenMRSEncounterType createEncounterType(OpenMRSEncounterType encounterType) {
        try {
            EncounterType converted = ConverterUtils.toEncounterType(encounterType);
            return ConverterUtils.toOpenMRSEncounterType(encounterResource.createEncounterType(converted));
        } catch (HttpException e) {
            LOGGER.error("Error while creating encounter type with name: " + encounterType.getName());
            return null;
        }
    }

    @Override
    public OpenMRSEncounterType getEncounterTypeByUuid(String uuid) {
        try {
            return ConverterUtils.toOpenMRSEncounterType(encounterResource.getEncounterTypeByUuid(uuid));
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

    private void removeEncounters(List<OpenMRSEncounter> previousEncounters, String encounterType) {

        Iterator<OpenMRSEncounter> encounterItr = previousEncounters.iterator();

        // filter out encounters with non matching encounterType
        while (StringUtils.isNotBlank(encounterType) && encounterItr.hasNext()) {
            OpenMRSEncounter enc = encounterItr.next();
            if (!encounterType.equals(enc.getEncounterType())) {
                encounterItr.remove();
            }
        }
    }


}
