package org.motechproject.openmrs.tasks.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.CohortQueryReport;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.EncounterType;
import org.motechproject.openmrs.domain.Form;
import org.motechproject.openmrs.domain.Identifier;
import org.motechproject.openmrs.domain.IdentifierType;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Observation;
import org.motechproject.openmrs.domain.Order;
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
import org.motechproject.openmrs.service.OpenMRSFormService;
import org.motechproject.openmrs.service.OpenMRSLocationService;
import org.motechproject.openmrs.service.OpenMRSObservationService;
import org.motechproject.openmrs.service.OpenMRSOrderService;
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
    private OpenMRSObservationService observationService;
    private OpenMRSVisitService visitService;
    private OpenMRSProviderService providerService;
    private OpenMRSProgramEnrollmentService programEnrollmentService;
    private OpenMRSPersonService personService;
    private OpenMRSCohortService cohortService;
    private OpenMRSFormService formService;
    private OpenMRSOrderService orderService;

    private EventRelay eventRelay;

    @Override
    public Encounter createEncounter(String configName, DateTime encounterDatetime, String encounterType,
                                String locationName, String patientUuid, String providerUuid, String visitUuid,
                                String formUuid, Map<String, String> observations) {
        Location location = getLocationByName(configName, locationName);
        Patient patient = patientService.getPatientByUuid(configName, patientUuid);
        Provider provider = providerService.getProviderByUuid(configName, providerUuid);
        Visit visit = null;
        Form form = null;
        if (StringUtils.isNotEmpty(visitUuid)) {
            visit = visitService.getVisitByUuid(configName, visitUuid);
        }
        if (StringUtils.isNotEmpty(formUuid)) {
            form = formService.getFormByUuid(configName, formUuid);
        }

        EncounterType type = new EncounterType(null, encounterType);

        //While creating observations, the encounterDateTime is used as a obsDateTime.
        List<Observation> observationList = MapUtils.isNotEmpty(observations) ? convertObservationMapToList(observations, encounterDatetime) : null;

        Encounter encounter = new Encounter(location, type, encounterDatetime.toDate(), patient, visit, Collections.singletonList(provider.getPerson()), observationList, form);
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

        String parsedMiddleName;
        if ("\"\"".equals(middleName)) {
            parsedMiddleName = "";
        } else {
            parsedMiddleName = middleName;
        }

        Person person = preparePerson(givenName, parsedMiddleName, familyName, address1, address2,
                address3, address4, address5, address6, cityVillage, stateProvince,
                country, postalCode, countyDistrict, latitude, longitude,
                startDate, endDate, birthDate, birthDateEstimated,
                gender, dead, causeOfDeath, personAttributes);
        person.setUuid(personUuid);

        personService.updatePerson(configName, person);
    }

    @Override
    public Observation createObservationJSON(String configName, String observationJSON, String encounterUuid, String conceptUuid,
                                             DateTime obsDatetime, String orderUuid, String comment) {
        JsonParser parser = new JsonParser();
        JsonObject obj = parser.parse(observationJSON).getAsJsonObject();

        if (StringUtils.isNotEmpty(encounterUuid)) {
            obj.addProperty("encounter", encounterUuid);
        }
        if (StringUtils.isNotEmpty(conceptUuid)) {
            obj.addProperty("concept", conceptUuid);
        }
        if (obsDatetime != null) {
            DateTimeFormatter fullDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
            obj.addProperty("obsDatetime", obsDatetime.toString(fullDateTimeFormatter));
        }
        if (StringUtils.isNotEmpty(orderUuid)) {
            obj.addProperty("order", orderUuid);
        }
        if (StringUtils.isNotEmpty(comment)) {
            obj.addProperty("comment", comment);
        }

        return observationService.createObservationFromJson(configName, obj.toString());
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

    @Override
    public Order createOrder(String configName, String type, String encounterUuid, String patientUuid, String conceptUuid, String ordererUuid, String careSetting) {
        Encounter encounter = new Encounter();
        encounter.setUuid(encounterUuid);

        Patient patient = new Patient();
        patient.setUuid(patientUuid);

        Concept concept = new Concept();
        concept.setUuid(conceptUuid);

        Provider orderer = new Provider();
        orderer.setUuid(ordererUuid);

        Order order = new Order(type, encounter, orderer, patient, concept, Order.CareSetting.valueOf(careSetting));

        return orderService.createOrder(configName, order);
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
        List<Observation> observationChildren = new ArrayList<>();
        List<Observation> observationParents = new ArrayList<>();

        for (String observationConceptPath : observations.keySet()) {
            if (valueIsNotEmpty(observations, observationConceptPath)) {
                if (isObservationGroups(observationConceptPath)) {
                    String[] concepts = observationConceptPath.split("/");

                    Observation childrenObservation = null;
                    for (int i = concepts.length - 1; i >= 0; i--) {
                        String concept = concepts[i];

                        if (i == 0) {
                            createOrUpdateObservation(observationParents, concept, obsDatetime, childrenObservation);
                        } else if (i == concepts.length - 1) {
                            String value = observations.get(observationConceptPath);
                            childrenObservation = createObservation(concept, obsDatetime, value, null);
                        } else {
                            childrenObservation = createOrUpdateObservation(observationChildren, concept, obsDatetime, childrenObservation);
                        }
                    }
                } else {
                    String[] observationValues = observations.get(observationConceptPath).replace(", ", ",").split(",");
                    for (String value : observationValues) {
                        if (StringUtils.isNotEmpty(value)) {
                            observationList.add(createObservation(observationConceptPath, obsDatetime, value, null));
                        }
                    }
                }
            } else {
                LOGGER.warn("Observation value is null or empty for concept path: " + observationConceptPath
                        + " and will not be created");
            }
        }

        observationList.addAll(observationParents);
        return observationList;
    }

    private boolean valueIsNotEmpty(Map<String, String> map, String key) {
        return StringUtils.isNotEmpty(map.get(key));
    }

    private Observation createOrUpdateObservation(List<Observation> observationList, String concept, DateTime obsDatetime, Observation childrenObservation) {
        Observation observation = getObservation(observationList, concept, null);
        if (observation == null) {
            observation = createObservation(concept, obsDatetime, null, childrenObservation);
            observationList.add(observation);
        } else if (getObservation(observation.getGroupMembers(), childrenObservation) == null){
            observation.getGroupMembers().add(childrenObservation);
        }

        return observation;
    }

    private Observation createObservation(String observationConceptUuid, DateTime obsDatetime, String value, Observation childrenObs) {
        Observation observation = new Observation();

        Concept concept = new Concept();
        concept.setUuid(observationConceptUuid);
        observation.setConcept(concept);

        observation.setObsDatetime(obsDatetime.toDate());

        if (StringUtils.isNotEmpty(value)) {
            Observation.ObservationValue observationValue = new Observation.ObservationValue(value);
            observation.setValue(observationValue);
        }

        if (childrenObs != null) {
            List<Observation> groupsMembers = new ArrayList<>();
            groupsMembers.add(childrenObs);
            observation.setGroupMembers(groupsMembers);
        }

        return observation;
    }

    private boolean isObservationGroups(String observationConceptPath) {
        return observationConceptPath.contains("/");
    }

    private Observation getObservation(List<Observation> observationList, Observation obsToFind) {
        String value = obsToFind.getValue() != null ? obsToFind.getValue().getDisplay() : null;
        return getObservation(observationList, obsToFind.getConcept().getUuid(), value);
    }

    private Observation getObservation(List<Observation> observationList, String conceptUuid, String value) {
        Observation observation = null;

        for (Observation obs : observationList) {
            if (StringUtils.equals(conceptUuid, obs.getConcept().getUuid())) {
                if (obs.getValue() == null && value == null) {
                    observation = obs;
                    break;
                } else if (obs.getValue() != null && StringUtils.equals(obs.getValue().getDisplay(), value)) {
                    observation = obs;
                    break;
                }
            }
        }

        return observation;
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

    @Autowired
    public void setObservationService(OpenMRSObservationService observationService) {
        this.observationService = observationService;
    }

    @Autowired
    public void setFormService(OpenMRSFormService formService) {
        this.formService = formService;
    }

    @Autowired
    public void setOrderService(OpenMRSOrderService orderService) {
        this.orderService = orderService;
    }
}
