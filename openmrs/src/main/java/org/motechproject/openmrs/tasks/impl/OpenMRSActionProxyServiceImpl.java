package org.motechproject.openmrs.tasks.impl;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.CohortQueryReport;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.ConceptName;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.EncounterType;
import org.motechproject.openmrs.domain.Identifier;
import org.motechproject.openmrs.domain.IdentifierType;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.domain.Program;
import org.motechproject.openmrs.domain.ProgramEnrollment;
import org.motechproject.openmrs.domain.Provider;
import org.motechproject.openmrs.domain.Visit;
import org.motechproject.openmrs.domain.VisitType;
import org.motechproject.openmrs.helper.EventHelper;
import org.motechproject.openmrs.service.OpenMRSCohortService;
import org.motechproject.openmrs.service.OpenMRSConceptService;
import org.motechproject.openmrs.service.OpenMRSEncounterService;
import org.motechproject.openmrs.service.OpenMRSLocationService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.motechproject.openmrs.service.OpenMRSPersonService;
import org.motechproject.openmrs.service.OpenMRSProgramEnrollmentService;
import org.motechproject.openmrs.service.OpenMRSProviderService;
import org.motechproject.openmrs.service.OpenMRSVisitService;
import org.motechproject.openmrs.tasks.OpenMRSActionProxyService;
import org.motechproject.openmrs.tasks.constants.EventSubjects;
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
 * Implementation of the {@link org.motechproject.openmrs.tasks.OpenMRSActionProxyService} interface.
 */
@Service("openMRSActionProxyService")
public class OpenMRSActionProxyServiceImpl implements OpenMRSActionProxyService {

    private static final Logger LOGGER = LoggerFactory.getLogger(OpenMRSActionProxyServiceImpl.class);

    private OpenMRSConceptService conceptService;
    private OpenMRSEncounterService encounterService;
    private OpenMRSLocationService locationService;
    private OpenMRSPatientService patientService;
    private OpenMRSVisitService visitService;
    private OpenMRSProviderService providerService;
    private OpenMRSProgramEnrollmentService programEnrollmentService;
    private OpenMRSPersonService personService;
    private OpenMRSCohortService cohortService;

    private EventRelay eventRelay;

    @Override
    public Encounter createEncounter(String configName, DateTime encounterDatetime, String encounterType,
                                String locationName, String patientUuid, String providerUuid, String visitUuid,
                                Map<String, String> observations) {
        Location location = getLocationByName(configName, locationName);
        Patient patient = patientService.getPatientByUuid(configName, patientUuid);
        Provider provider = providerService.getProviderByUuid(configName, providerUuid);
        Visit visit = null;
        if (StringUtils.isNotEmpty(visitUuid)) {
            visit = visitService.getVisitByUuid(configName, visitUuid);
        }

        //While creating observations, the encounterDateTime is used as a obsDateTime.
        List<Observation> observationList = MapUtils.isNotEmpty(observations) ? convertObservationMapToList(observations, encounterDatetime) : null;

        EncounterType type = new EncounterType(encounterType);

        Encounter encounter = new Encounter(location, type, encounterDatetime.toDate(), patient, visit, Collections.singletonList(provider.getPerson()), observationList);
        return encounterService.createEncounter(configName, encounter);
    }

    @Override
    public Patient createPatient(String configName, String givenName, String middleName, String familyName,
                                 String address1, String address2, String address3, String address4, String address5,
                                 String address6, String cityVillage, String stateProvince, String country,
                                 String postalCode, String countyDistrict, String latitude, String longitude,
                                 DateTime startDate, DateTime endDate, DateTime birthDate, Boolean birthDateEstimated,
                                 String gender, Boolean dead, String causeOfDeathUUID, String motechId,
                                 String locationForMotechId, Map<String, String> identifiers, Map<String, String> personAttributes) {
        Concept causeOfDeath = StringUtils.isNotEmpty(causeOfDeathUUID) ? conceptService.getConceptByUuid(configName, causeOfDeathUUID) : null;

        Person person = preparePerson(givenName, middleName, familyName, address1, address2,
                address3, address4, address5, address6, cityVillage, stateProvince,
                country, postalCode, countyDistrict, latitude, longitude,
                startDate, endDate, birthDate, birthDateEstimated,
                gender, dead, causeOfDeath, personAttributes);

        Location location = StringUtils.isNotEmpty(locationForMotechId) ? getLocationByName(configName, locationForMotechId) : getDefaultLocation(configName);

        List<Identifier> identifierList = convertIdentifierMapToList(identifiers);

        Patient patient = new Patient(identifierList, person, motechId, location);
        return patientService.createPatient(configName, patient);
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
                             String gender, Boolean dead, String causeOfDeathUUID, Map<String, String> personAttributes) {
        Concept causeOfDeath = StringUtils.isNotEmpty(causeOfDeathUUID) ? conceptService.getConceptByUuid(configName, causeOfDeathUUID) : null;

        Person person = preparePerson(givenName, middleName, familyName, address1, address2,
                address3, address4, address5, address6, cityVillage, stateProvince,
                country, postalCode, countyDistrict, latitude, longitude,
                startDate, endDate, birthDate, birthDateEstimated,
                gender, dead, causeOfDeath, personAttributes);
        person.setUuid(personUuid);

        personService.updatePerson(configName, person);
    }

    @Override
    public Visit createVisit(String configName, String patientUuid, DateTime startDatetime,
                             DateTime stopDatetime, String visitTypeUuid, String locationName) {
        Location location = getLocationByName(configName, locationName);
        Patient patient = patientService.getPatientByUuid(configName, patientUuid);
        VisitType type = new VisitType(visitTypeUuid);
        Visit visit = new Visit(startDatetime.toDate(), stopDatetime.toDate(), patient, type, location);

        return visitService.createVisit(configName, visit);
    }

    @Override
    public void createProgramEnrollment(String configName, String patientUuid, String programUuid,
                                        DateTime dateEnrolled, DateTime dateCompleted, String locationName,
                                        Map<String, String> programEnrollmentAttributes) {
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
        programEnrollment.setAttributes(MapUtils.isEmpty(programEnrollmentAttributes) ? null :
                convertAttributeMapToList(programEnrollmentAttributes, false));

        programEnrollmentService.createProgramEnrollment(configName, programEnrollment);
    }

    @Override
    public void updateProgramEnrollment(String configName, String programEnrollmentUuid, DateTime programCompletedDate,
                                        String stateUuid, DateTime startDate, Map<String, String> attributes) {

        ProgramEnrollment updatedProgram = new ProgramEnrollment();
        List<Attribute> attributesList;

        updatedProgram.setUuid(programEnrollmentUuid);

        if (programCompletedDate != null) {
            updatedProgram.setDateCompleted(programCompletedDate.toDate());
        }

        if (stateUuid != null) {
            List<ProgramEnrollment.StateStatus> statusList = new ArrayList<>();
            ProgramEnrollment.StateStatus status = new ProgramEnrollment.StateStatus();

            status.setUuid(stateUuid);
            statusList.add(status);
            updatedProgram.setStates(statusList);
        }

        if (startDate != null) {
            updatedProgram.setDateEnrolled(startDate.toDate());
        }

        attributesList = convertAttributeMapToList(attributes, true);
        updatedProgram.setAttributes(!attributesList.isEmpty() ? attributesList : null);

        programEnrollmentService.updateProgramEnrollment(configName, updatedProgram);
    }

    @Override
    public void changeStateOfProgramEnrollment(String configName, String programEnrollmentUuid, DateTime programCompletedDate,
                                               String stateUuid, DateTime startDate) {
        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setUuid(programEnrollmentUuid);
        programEnrollment.setDateCompleted(Objects.nonNull(programCompletedDate) ? programCompletedDate.toDate() : null);

        if (StringUtils.isNotBlank(stateUuid)) {
            Program.State state = new Program.State();
            state.setUuid(stateUuid);

            ProgramEnrollment.StateStatus stateStatus = new ProgramEnrollment.StateStatus();
            stateStatus.setState(state);
            stateStatus.setStartDate(startDate.toDate());

            programEnrollment.setStates(Collections.singletonList(stateStatus));
        }

        programEnrollmentService.updateProgramEnrollment(configName, programEnrollment);
    }

    @Override
    public void getCohortQueryReport(String configName, String cohortQueryUuid, Map<String, String> parameters) {
        CohortQueryReport cohortQueryReport = cohortService.getCohortQueryReport(configName, cohortQueryUuid, parameters);

        for (CohortQueryReport.CohortQueryReportMember member : cohortQueryReport.getMembers()) {
            eventRelay.sendEventMessage(new MotechEvent(EventSubjects.GET_COHORT_QUERY_MEMBER_EVENT.concat(configName),
                    EventHelper.cohortMemberParameters(cohortQueryUuid, member)));
        }
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
            if (valueIsNotEmpty(observations, observationConceptName)) {
                String[] observationValues = observations.get(observationConceptName).replace(", ", ",").split(",");
                for (String value : observationValues) {
                    Observation observation = new Observation();

                    ConceptName conceptName = new ConceptName(observationConceptName);
                    Concept concept = new Concept(conceptName);
                    observation.setConcept(concept);

                    Observation.ObservationValue observationValue = new Observation.ObservationValue(value);
                    observation.setValue(observationValue);

                    observation.setObsDatetime(obsDatetime.toDate());

                    observationList.add(observation);
                }
            } else {
                LOGGER.warn("Observation value is null or empty for concept: " + observationConceptName
                        + " and will not be created");
            }
        }
        return observationList;
    }

    private boolean valueIsNotEmpty(Map<String, String> map, String key) {
        return StringUtils.isNotEmpty(map.get(key));
    }

    private List<Attribute> convertAttributeMapToList(Map<String, String> attributes, boolean isProgramEnrollmentUpdate) {
        List<Attribute> attributesList = new ArrayList<>();

        for (String attributeName : attributes.keySet()) {
            Attribute attribute = new Attribute();
            attribute.setValue(attributes.get(attributeName));

            if (isProgramEnrollmentUpdate) {
                attribute.setUuid(attributeName);
            } else {
                Attribute.AttributeType attributeType = new Attribute.AttributeType();
                attributeType.setUuid(attributeName);

                attribute.setAttributeType(attributeType);
            }

            attributesList.add(attribute);
        }
        return attributesList;
    }

    private Person preparePerson(String givenName, String middleName, String familyName, String address1, String address2,
                                 String address3, String address4, String address5, String address6, String cityVillage, String stateProvince,
                                 String country, String postalCode, String countyDistrict, String latitude, String longitude,
                                 DateTime startDate, DateTime endDate, DateTime birthDate, Boolean birthDateEstimated,
                                 String gender, Boolean dead, Concept causeOfDeath, Map<String, String> attributes) {
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

        List<Attribute> attributesList = convertAttributeMapToList(attributes, false);

        person.setBirthdate(Objects.nonNull(birthDate) ? birthDate.toDate() : null);
        person.setBirthdateEstimated(birthDateEstimated);
        person.setDead(dead);
        person.setCauseOfDeath(causeOfDeath);
        person.setGender(gender);
        person.setAttributes(attributesList);

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
    public void setPersonService(OpenMRSPersonService personService) {
        this.personService = personService;
    }

    @Autowired
    public void setProviderService(OpenMRSProviderService providerService) {
        this.providerService = providerService;
    }

    @Autowired
    public void setProgramEnrollmentService(OpenMRSProgramEnrollmentService programEnrollmentService) {
        this.programEnrollmentService = programEnrollmentService;
    }

    @Autowired
    public void setCohortService(OpenMRSCohortService cohortService) {
        this.cohortService = cohortService;
    }

    @Autowired
    public void setEventRelay(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    @Autowired
    public void setVisitService(OpenMRSVisitService visitService) {
        this.visitService = visitService;
    }
}
