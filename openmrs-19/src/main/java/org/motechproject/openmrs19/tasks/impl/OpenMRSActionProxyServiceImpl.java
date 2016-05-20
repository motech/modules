package org.motechproject.openmrs19.tasks.impl;

import com.google.common.base.Strings;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.ConceptName;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Identifier;
import org.motechproject.openmrs19.domain.IdentifierType;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.domain.Program;
import org.motechproject.openmrs19.domain.ProgramEnrollment;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSLocationService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSProgramEnrollmentService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.openmrs19.tasks.OpenMRSActionProxyService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Implementation of the {@link org.motechproject.openmrs19.tasks.OpenMRSActionProxyService} interface.
 */
@Service("openMRSActionProxyService")
public class OpenMRSActionProxyServiceImpl implements OpenMRSActionProxyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSActionProxyServiceImpl.class);

    private OpenMRSConceptService conceptService;
    private OpenMRSEncounterService encounterService;
    private OpenMRSLocationService locationService;
    private OpenMRSPatientService patientService;
    private OpenMRSProviderService providerService;
    private OpenMRSProgramEnrollmentService programEnrollmentService;
    private OpenMRSPersonService personService;

    @Override
    public void createEncounter(String configName, DateTime encounterDatetime, String encounterType,
                                String locationName, String patientUuid, String providerUuid,
                                Map<String, String> observations) {
        Location location = getLocationByName(configName, locationName);
        Patient patient = patientService.getPatientByUuid(configName, patientUuid);
        Provider provider = providerService.getProviderByUuid(configName, providerUuid);

        //While creating observations, the encounterDateTime is used as a obsDateTime.
        List<Observation> observationList = MapUtils.isNotEmpty(observations) ? convertObservationMapToList(observations, encounterDatetime) : null;

        EncounterType type = new EncounterType(encounterType);

        Encounter encounter = new Encounter(location, type, encounterDatetime.toDate(), patient, provider.getPerson(), observationList);
        encounterService.createEncounter(configName, encounter);
    }

    @Override
    public void createPatient(String configName, String givenName, String middleName, String familyName,
                              String address1, String address2, String address3, String address4, String address5,
                              String address6, String cityVillage, String stateProvince, String country,
                              String postalCode, String countyDistrict, String latitude, String longitude,
                              DateTime startDate, DateTime endDate, DateTime birthDate, Boolean birthDateEstimated,
                              String gender, Boolean dead, String causeOfDeathUUID, String motechId,
                              String locationForMotechId, Map<String, String> identifiers) {
        Concept causeOfDeath = StringUtils.isNotEmpty(causeOfDeathUUID) ? conceptService.getConceptByUuid(configName, causeOfDeathUUID) : null;

        Person person = preparePerson(givenName, middleName, familyName, address1, address2,
                address3, address4, address5, address6, cityVillage, stateProvince,
                country, postalCode, countyDistrict, latitude, longitude,
                startDate, endDate, birthDate, birthDateEstimated,
                gender, dead, causeOfDeath);

        Location location = StringUtils.isNotEmpty(locationForMotechId) ? getLocationByName(configName, locationForMotechId) : getDefaultLocation(configName);

        List<Identifier> identifierList = convertIdentifierMapToList(identifiers);

        Patient patient = new Patient(identifierList, person, motechId, location);
        patientService.createPatient(configName, patient);
    }

    @Override
    public void updatePatientIdentifiers(String configName, String patientUuid, Map<String, String> identifiers) {
        Patient patient = new Patient();

        List<Identifier> identifierList = convertIdentifierMapToList(identifiers);
        patient.setIdentifiers(identifierList);
        patient.setUuid(patientUuid);

        patientService.updatePatientIdentifiers(configName, patient);
    }

    @Override
    public void updatePerson(String configName, String personUuid, String givenName, String middleName,
                             String familyName, String address1, String address2, String address3, String address4,
                             String address5, String address6, String cityVillage, String stateProvince, String country,
                             String postalCode, String countyDistrict, String latitude, String longitude,
                             DateTime startDate, DateTime endDate, DateTime birthDate, Boolean birthDateEstimated,
                             String gender, Boolean dead, String causeOfDeathUUID) {
        Concept causeOfDeath = StringUtils.isNotEmpty(causeOfDeathUUID) ? conceptService.getConceptByUuid(configName, causeOfDeathUUID) : null;

        Person person = preparePerson(givenName, middleName, familyName, address1, address2,
                address3, address4, address5, address6, cityVillage, stateProvince,
                country, postalCode, countyDistrict, latitude, longitude,
                startDate, endDate, birthDate, birthDateEstimated,
                gender, dead, causeOfDeath);
        person.setUuid(personUuid);

        personService.updatePerson(configName, person);
    }

    @Override
    public void createProgramEnrollment(String configName, String patientUuid, String programUuid,
                                        DateTime dateEnrolled, DateTime dateCompleted, String locationName) {
        Patient patient = new Patient();
        patient.setUuid(patientUuid);

        Program program = new Program();
        program.setUuid(programUuid);

        Location location = null;
        if (StringUtils.isNotBlank(locationName)) {
            location = getLocationByName(configName, locationName);
        }

        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setPatient(patient);
        programEnrollment.setProgram(program);
        programEnrollment.setDateEnrolled(dateEnrolled.toDate());
        programEnrollment.setDateCompleted(Objects.nonNull(dateCompleted) ? dateCompleted.toDate() : null);
        programEnrollment.setLocation(location);

        programEnrollmentService.createProgramEnrollment(configName, programEnrollment);
    }

    @Override
    public void changeStateOfProgramEnrollment(String configName, String programEnrollmentUuid, DateTime programCompletedDate,
                                               String stateUuid, DateTime startDate) {
        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setUuid(programEnrollmentUuid);
        programEnrollment.setDateCompleted(Objects.nonNull(programCompletedDate) ? programCompletedDate.toDate() : null);

        if (!Strings.isNullOrEmpty(stateUuid)) {
            Program.State state = new Program.State();
            state.setUuid(stateUuid);

            ProgramEnrollment.StateStatus stateStatus = new ProgramEnrollment.StateStatus();
            stateStatus.setState(state);
            stateStatus.setStartDate(startDate.toDate());

            programEnrollment.setStates(Collections.singletonList(stateStatus));
        }

        programEnrollmentService.updateProgramEnrollment(configName, programEnrollment);
    }

    private Location getDefaultLocation(String configName) {
        return getLocationByName(configName, DEFAULT_LOCATION_NAME);
    }

    private Location getLocationByName(String configName, String locationName) {
        Location location = null;

        if (StringUtils.isNotEmpty(locationName)) {
            List<Location> locations = locationService.getLocations(configName, locationName);
            if (locations.isEmpty()) {
                LOGGER.warn("There is no location with name {}", locationName);
            } else {
                if (locations.size() > 1) {
                    LOGGER.warn("There is more than one location with name {}.", locationName);
                }
                location = locations.get(0);
            }
        }

        return location;
    }

    private List<Identifier> convertIdentifierMapToList(Map<String, String> identifiers) {
        List<Identifier> identifierList = new ArrayList<>();

        for (String identifierTypeName : identifiers.keySet()) {
            IdentifierType identifierType = new IdentifierType();
            identifierType.setName(identifierTypeName);

            Identifier identifier = new Identifier(identifiers.get(identifierTypeName), identifierType);

            identifierList.add(identifier);
        }

        return identifierList;
    }

    private List<Observation> convertObservationMapToList(Map<String, String> observations, DateTime obsDatetime) {
        List<Observation> observationList = new ArrayList<>();

        for (String observationConceptName : observations.keySet()) {
            Observation observation = new Observation();

            ConceptName conceptName = new ConceptName(observationConceptName);
            Concept concept = new Concept(conceptName);
            observation.setConcept(concept);

            String observationMapValue = observations.get(observationConceptName);
            Observation.ObservationValue observationValue = new Observation.ObservationValue(observationMapValue);
            observation.setValue(observationValue);

            observation.setObsDatetime(obsDatetime.toDate());

            observationList.add(observation);
        }
        return observationList;
    }

    private Person preparePerson(String givenName, String middleName, String familyName, String address1, String address2,
                                 String address3, String address4, String address5, String address6, String cityVillage, String stateProvince,
                                 String country, String postalCode, String countyDistrict, String latitude, String longitude,
                                 DateTime startDate, DateTime endDate, DateTime birthDate, Boolean birthDateEstimated,
                                 String gender, Boolean dead, Concept causeOfDeath)  {
        Person person = new Person();

        Person.Name personName = new Person.Name();
        personName.setGivenName(givenName);
        personName.setMiddleName(middleName);
        personName.setFamilyName(familyName);
        person.setPreferredName(personName);
        person.setNames(Collections.singletonList(personName));

        Person.Address personAddress = new Person.Address(address1, address2, address3, address4, address5, address6,
                cityVillage, stateProvince, country, postalCode, countyDistrict, latitude, longitude,
                Objects.nonNull(startDate) ? startDate.toDate() : null, Objects.nonNull(endDate) ? endDate.toDate() : null);
        person.setPreferredAddress(personAddress);
        person.setAddresses(Collections.singletonList(personAddress));

        person.setBirthdate(Objects.nonNull(birthDate) ? birthDate.toDate() : null);
        person.setBirthdateEstimated(birthDateEstimated);
        person.setDead(dead);
        person.setCauseOfDeath(causeOfDeath);
        person.setGender(gender);

        return person;
    }

    @Autowired
    public void setConceptService(OpenMRSConceptService conceptService) {
        this.conceptService = conceptService;
    }

    @Autowired
    public void setEncounterService(OpenMRSEncounterService encounterService) {
        this.encounterService = encounterService;
    }

    @Autowired
    public void setLocationService(OpenMRSLocationService locationService) {
        this.locationService = locationService;
    }

    @Autowired
    public void setPatientService(OpenMRSPatientService patientService) {
        this.patientService = patientService;
    }

    @Autowired
    public void setPersonService(OpenMRSPersonService personService) { this.personService = personService; }

    @Autowired
    public void setProviderService(OpenMRSProviderService providerService) {
        this.providerService = providerService;
    }

    @Autowired
    public void setProgramEnrollmentService(OpenMRSProgramEnrollmentService programEnrollmentService) {
        this.programEnrollmentService = programEnrollmentService;
    }
}
