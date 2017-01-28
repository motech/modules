package org.motechproject.openmrs.tasks.impl;

import com.google.gson.JsonObject;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.CohortQueryReport;
import org.motechproject.openmrs.domain.CohortQueryReport.CohortQueryReportMember;
import org.motechproject.openmrs.domain.Concept;
import org.motechproject.openmrs.domain.ConceptName;
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
import org.motechproject.openmrs.tasks.constants.Keys;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSActionProxyServiceTest {

    private static final String CONFIG_NAME = "Configuration name";
    private static final DateTimeFormatter fullDateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
    private static final Date DATE = new DateTime("2000-08-16T07:22:05Z").toDate();

    @Mock
    private OpenMRSConceptService conceptService;

    @Mock
    private OpenMRSEncounterService encounterService;

    @Mock
    private OpenMRSLocationService locationService;

    @Mock
    private OpenMRSPatientService patientService;

    @Mock
    private OpenMRSPersonService personService;

    @Mock
    private OpenMRSObservationService observationService;

    @Mock
    private OpenMRSOrderService orderService;

    @Mock
    private OpenMRSVisitService visitService;

    @Mock
    private OpenMRSProviderService providerService;

    @Mock
    private OpenMRSProgramEnrollmentService programEnrollmentService;

    @Mock
    private OpenMRSCohortService cohortService;

    @Mock
    private EventRelay eventRelay;

    @Mock
    private OpenMRSFormService formService;

    @Captor
    private ArgumentCaptor<Patient> patientCaptor;

    @Captor
    private ArgumentCaptor<Encounter> encounterCaptor;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

    @Captor
    private ArgumentCaptor<String> observationCaptor;

    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Captor
    private ArgumentCaptor<Visit> visitCaptor;

    @Captor
    private ArgumentCaptor<ProgramEnrollment> programEnrollmentCaptor;

    @InjectMocks
    private OpenMRSActionProxyService openMRSActionProxyService = new OpenMRSActionProxyServiceImpl();

    @Test
    public void shouldCreateEncounterWithGivenParameters() {
        Location location = new Location();
        location.setName("testLocation");

        Patient patient = new Patient();
        patient.setUuid("10");

        Provider provider = new Provider();
        provider.setUuid("20");

        Person person = new Person();
        person.setUuid("30");
        provider.setPerson(person);

        Visit visit = new Visit();
        visit.setUuid("40");

        Form form = new Form();
        form.setUuid("50");

        Map<String, String> observations = new HashMap<>();
        observations.put("18ff53df-744a-4a3f-8f25-dac6de5b7131", "testObservationValueName0");

        List<Observation> obsList = createObservationList(1);

        Encounter encounter = new Encounter(location, new EncounterType(null, "testId"), DATE, patient, visit, Collections.singletonList(provider.getPerson()), obsList);

        doReturn(patient).when(patientService).getPatientByUuid(eq(CONFIG_NAME), eq(patient.getUuid()));
        doReturn(provider).when(providerService).getProviderByUuid(eq(CONFIG_NAME), eq(provider.getUuid()));
        doReturn(visit).when(visitService).getVisitByUuid(eq(CONFIG_NAME), eq(visit.getUuid()));
        doReturn(encounter).when(encounterService).createEncounter(eq(CONFIG_NAME), eq(encounter));

        doReturn(Collections.singletonList(location))
                .when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        Encounter encounterCreated = openMRSActionProxyService.createEncounter(CONFIG_NAME, new DateTime(encounter.getEncounterDatetime()),
                encounter.getEncounterType().getUuid(), location.getName(), patient.getUuid(), provider.getUuid(),
                visit.getUuid(), form.getUuid(),  observations);

        assertEquals(encounter, encounterCreated);
    }

    @Test
    public void shouldCreateEncounterWithGivenParametersWithoutObsWithEmptyValue() {
        Location location = new Location();
        location.setName("testLocation");

        Patient patient = new Patient();
        patient.setUuid("10");

        Provider provider = new Provider();
        provider.setUuid("20");

        Person person = new Person();
        person.setUuid("30");
        provider.setPerson(person);

        Visit visit = new Visit();
        visit.setUuid("40");

        Form form = new Form();
        form.setUuid("50");

        Map<String, String> observations = new HashMap<>();
        observations.put("18ff53df-744a-4a3f-8f25-dac6de5b7131", "");

        List<Observation> obsList = new ArrayList<>();

        Encounter encounter = new Encounter(location, new EncounterType(null, "testId"), DATE, patient, visit, Collections.singletonList(provider.getPerson()), obsList);

        doReturn(patient).when(patientService).getPatientByUuid(eq(CONFIG_NAME), eq(patient.getUuid()));
        doReturn(provider).when(providerService).getProviderByUuid(eq(CONFIG_NAME), eq(provider.getUuid()));
        doReturn(visit).when(visitService).getVisitByUuid(eq(CONFIG_NAME), eq(visit.getUuid()));
        doReturn(encounter).when(encounterService).createEncounter(eq(CONFIG_NAME), eq(encounter));

        doReturn(Collections.singletonList(location))
                .when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        Encounter encounterCreated = openMRSActionProxyService.createEncounter(CONFIG_NAME, new DateTime(encounter.getEncounterDatetime()),
                encounter.getEncounterType().getUuid(), location.getName(), patient.getUuid(), provider.getUuid(),
                visit.getUuid(), form.getUuid(), observations);

        assertEquals(encounter, encounterCreated);
    }

    @Test
    public void shouldCreateEncounterWithGivenParametersWithObsWithManyValues() {
        Location location = new Location();
        location.setName("testLocation");

        Patient patient = new Patient();
        patient.setUuid("10");

        Provider provider = new Provider();
        provider.setUuid("20");

        Person person = new Person();
        person.setUuid("30");
        provider.setPerson(person);

        Visit visit = new Visit();
        visit.setUuid("40");

        Form form = new Form();
        form.setUuid("50");

        Map<String, String> observations = new HashMap<>();

        /* Empty value in observations should not be included */
        observations.put("18ff53df-744a-4a3f-8f25-dac6de5b7131","testObservationValueName0, ,testObservationValueName1");
        List<Observation> obsList = createObservationList(2);

        Encounter encounter = new Encounter(location, new EncounterType(null, "testId"), DATE, patient, visit, Collections.singletonList(provider.getPerson()), obsList);

        doReturn(patient).when(patientService).getPatientByUuid(eq(CONFIG_NAME), eq(patient.getUuid()));
        doReturn(provider).when(providerService).getProviderByUuid(eq(CONFIG_NAME), eq(provider.getUuid()));
        doReturn(visit).when(visitService).getVisitByUuid(eq(CONFIG_NAME), eq(visit.getUuid()));
        doReturn(encounter).when(encounterService).createEncounter(eq(CONFIG_NAME), eq(encounter));
        doReturn(Collections.singletonList(location))
                .when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        Encounter encounterCreated = openMRSActionProxyService.createEncounter(CONFIG_NAME, new DateTime(encounter.getEncounterDatetime()),
                encounter.getEncounterType().getUuid(), location.getName(), patient.getUuid(), provider.getUuid(),
                visit.getUuid(), form.getUuid(), observations);


        assertEquals(encounter, encounterCreated);
    }

    @Test
    public void shouldCreateEncounterAndObservationGroups() {
        Location location = new Location();
        location.setName("testLocation");

        Patient patient = new Patient();
        patient.setUuid("10");

        Provider provider = new Provider();
        provider.setUuid("20");

        Person person = new Person();
        person.setUuid("30");
        provider.setPerson(person);

        Visit visit = new Visit();
        visit.setUuid("40");

        Form form = new Form();
        form.setUuid("50");

        Map<String, String> observations = prepareObservationGroupsMap();
        List<Observation> obsList = createObservationGroups();

        Encounter encounter = new Encounter(location, new EncounterType(null, "testId"), DATE, patient, visit, Collections.singletonList(provider.getPerson()), obsList);

        doReturn(patient).when(patientService).getPatientByUuid(eq(CONFIG_NAME), eq(patient.getUuid()));
        doReturn(provider).when(providerService).getProviderByUuid(eq(CONFIG_NAME), eq(provider.getUuid()));
        doReturn(visit).when(visitService).getVisitByUuid(eq(CONFIG_NAME), eq(visit.getUuid()));
        doReturn(encounter).when(encounterService).createEncounter(eq(CONFIG_NAME), eq(encounter));
        doReturn(Collections.singletonList(location))
                .when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        Encounter encounterCreated = openMRSActionProxyService.createEncounter(CONFIG_NAME, new DateTime(encounter.getEncounterDatetime()),
                encounter.getEncounterType().getUuid(), location.getName(), patient.getUuid(), provider.getUuid(),
                visit.getUuid(), form.getUuid(), observations);

        assertEquals(encounter, encounterCreated);
    }

    @Test
    public void shouldCreatePatientWithGivenParameters() {
        Person person = createTestPerson();
        Concept causeOfDeath = createTestConcept("testCauseOfDeath");

        person.setDead(true);
        person.setCauseOfDeath(causeOfDeath);

        Location location = new Location();
        location.setName("testLocation");

        Identifier identifier = new Identifier("1000", new IdentifierType("CommCare CaseID"));

        Patient patient = new Patient(Collections.singletonList(identifier), person, "500", location);

        Map<String, String> identifiersMap = new HashMap<>();
        identifiersMap.put("CommCare CaseID", "1000");

        Map<String, String> personAttributes = new HashMap<>();
        personAttributes.put("8d8718c2-c2cc-11de-8d13-0010c6dffd0f", "testValue");

        doReturn(causeOfDeath).when(conceptService).getConceptByUuid(eq(CONFIG_NAME), eq(causeOfDeath.getUuid()));
        doReturn(Collections.singletonList(location))
                .when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        Person.Address personAddress = person.getPreferredAddress();

        openMRSActionProxyService.createPatient(CONFIG_NAME, person.getPreferredName().getGivenName(),
                person.getPreferredName().getMiddleName(), person.getPreferredName().getFamilyName(),
                personAddress.getAddress1(), personAddress.getAddress2(), personAddress.getAddress3(),
                personAddress.getAddress4(), personAddress.getAddress5(), personAddress.getAddress6(),
                personAddress.getCityVillage(), personAddress.getStateProvince(), personAddress.getCountry(),
                personAddress.getPostalCode(), personAddress.getCountyDistrict(), personAddress.getLatitude(),
                personAddress.getLongitude(), new DateTime(personAddress.getStartDate()),
                new DateTime(personAddress.getEndDate()), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), causeOfDeath.getUuid(),
                patient.getMotechId(), location.getName(), identifiersMap, personAttributes);

        verify(patientService).createPatient(eq(CONFIG_NAME), patientCaptor.capture());

        assertEquals(patient, patientCaptor.getValue());
    }

    @Test
    public void shouldCreatePatientWithDefaultLocationWhenLocationNameIsNotProvided() {
        Person person = createTestPerson();

        Location location = new Location();
        location.setName(OpenMRSActionProxyService.DEFAULT_LOCATION_NAME);

        Identifier identifier = new Identifier("1000", new IdentifierType("CommCare CaseID"), location);

        Patient patient = new Patient(Collections.singletonList(identifier), person, "500", location);

        Map<String, String> identifiersMap = new HashMap<>();
        identifiersMap.put("CommCare CaseID", "1000");

        Map<String, String> personAttributes = new HashMap<>();
        personAttributes.put("8d8718c2-c2cc-11de-8d13-0010c6dffd0f", "testValue");

        doReturn(Collections.singletonList(location))
                .when(locationService).getLocations(eq(CONFIG_NAME), eq(OpenMRSActionProxyService.DEFAULT_LOCATION_NAME));

        Person.Address personAddress = person.getPreferredAddress();

        openMRSActionProxyService.createPatient(CONFIG_NAME, person.getPreferredName().getGivenName(),
                person.getPreferredName().getMiddleName(), person.getPreferredName().getFamilyName(),
                personAddress.getAddress1(), personAddress.getAddress2(), personAddress.getAddress3(),
                personAddress.getAddress4(), personAddress.getAddress5(), personAddress.getAddress6(),
                personAddress.getCityVillage(), personAddress.getStateProvince(), personAddress.getCountry(),
                personAddress.getPostalCode(), personAddress.getCountyDistrict(), personAddress.getLatitude(),
                personAddress.getLongitude(), new DateTime(personAddress.getStartDate()),
                new DateTime(personAddress.getEndDate()), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), "", patient.getMotechId(),
                location.getName(), identifiersMap, personAttributes);

        verify(patientService).createPatient(eq(CONFIG_NAME), patientCaptor.capture());

        assertEquals(patient, patientCaptor.getValue());
    }

    @Test
    public void shouldNotUsedDefaultLocationWhenLocationForGivenNameIsNotFound() {
        Person person = createTestPerson();

        Location location = new Location();
        location.setName("testLocationNameForNotExistingLocation");

        Identifier identifier = new Identifier("1000", new IdentifierType("CommCare CaseID"));

        Patient patient = new Patient(Collections.singletonList(identifier), person, "500", null);

        Map<String, String> identifiersMap = new HashMap<>();
        identifiersMap.put("CommCare CaseID", "1000");

        Map<String, String> personAttributes = new HashMap<>();
        personAttributes.put("8d8718c2-c2cc-11de-8d13-0010c6dffd0f", "testValue");

        doReturn(Collections.emptyList()).when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        Person.Address personAddress = person.getPreferredAddress();

        openMRSActionProxyService.createPatient(CONFIG_NAME, person.getPreferredName().getGivenName(),
                person.getPreferredName().getMiddleName(), person.getPreferredName().getFamilyName(),
                personAddress.getAddress1(), personAddress.getAddress2(), personAddress.getAddress3(),
                personAddress.getAddress4(), personAddress.getAddress5(), personAddress.getAddress6(),
                personAddress.getCityVillage(), personAddress.getStateProvince(), personAddress.getCountry(),
                personAddress.getPostalCode(), personAddress.getCountyDistrict(), personAddress.getLatitude(),
                personAddress.getLongitude(), new DateTime(personAddress.getStartDate()),
                new DateTime(personAddress.getEndDate()), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), "", patient.getMotechId(),
                location.getName(), identifiersMap, personAttributes);

        verify(patientService).createPatient(eq(CONFIG_NAME), patientCaptor.capture());

        // the expected patient object has location value set to null, the actual object should be the same
        assertEquals(patient, patientCaptor.getValue());
    }

    @Test
    public void shouldUpdatePersonWithGivenParameters() {
        Person person = createTestPerson();
        Concept causeOfDeath = createTestConcept("testCauseOfDeathConcept");

        person.setDead(true);
        person.setCauseOfDeath(causeOfDeath);

        Person.Address personAddress = person.getPreferredAddress();

        Map<String, String> personAttributes = new HashMap<>();
        personAttributes.put("8d8718c2-c2cc-11de-8d13-0010c6dffd0f", "testValue");

        doReturn(causeOfDeath).when(conceptService).getConceptByUuid(eq(CONFIG_NAME), eq(causeOfDeath.getUuid()));
        openMRSActionProxyService.updatePerson(CONFIG_NAME, person.getUuid(), person.getPreferredName().getGivenName(),
                person.getPreferredName().getMiddleName(), person.getPreferredName().getFamilyName(),
                personAddress.getAddress1(), personAddress.getAddress2(), personAddress.getAddress3(),
                personAddress.getAddress4(), personAddress.getAddress5(), personAddress.getAddress6(),
                personAddress.getCityVillage(), personAddress.getStateProvince(), personAddress.getCountry(),
                personAddress.getPostalCode(), personAddress.getCountyDistrict(), personAddress.getLatitude(),
                personAddress.getLongitude(), new DateTime(personAddress.getStartDate()),
                new DateTime(personAddress.getEndDate()), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), causeOfDeath.getUuid(), personAttributes);

        verify(personService).updatePerson(eq(CONFIG_NAME), personCaptor.capture());
        assertEquals(person, personCaptor.getValue());
    }

    @Test
    public void shouldUpdatePersonWithBlankMiddleName() {
        Person person = createTestPerson();
        // the empty value from task action fields
        person.getPreferredName().setMiddleName("\"\"");

        Person.Address personAddress = person.getPreferredAddress();

        Map<String, String> personAttributes = new HashMap<>();
        personAttributes.put("8d8718c2-c2cc-11de-8d13-0010c6dffd0f", "testValue");

        openMRSActionProxyService.updatePerson(CONFIG_NAME, person.getUuid(), person.getPreferredName().getGivenName(),
                person.getPreferredName().getMiddleName(), person.getPreferredName().getFamilyName(),
                personAddress.getAddress1(), personAddress.getAddress2(), personAddress.getAddress3(),
                personAddress.getAddress4(), personAddress.getAddress5(), personAddress.getAddress6(),
                personAddress.getCityVillage(), personAddress.getStateProvince(), personAddress.getCountry(),
                personAddress.getPostalCode(), personAddress.getCountyDistrict(), personAddress.getLatitude(),
                personAddress.getLongitude(), new DateTime(personAddress.getStartDate()),
                new DateTime(personAddress.getEndDate()), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), null, personAttributes);

        verify(personService).updatePerson(eq(CONFIG_NAME), personCaptor.capture());
        // middleName should be converted to empty string
        person.getPreferredName().setMiddleName("");
        assertEquals(person, personCaptor.getValue());
    }

    @Test
    public void shouldUpdatePatientIdentifiers() {
        Patient patient = new Patient();
        patient.setUuid("10");

        Map<String, String> identifiersMap = new HashMap<>();
        identifiersMap.put("CommCare CaseID", "1000");

        openMRSActionProxyService.updatePatientIdentifiers(CONFIG_NAME, patient.getUuid(), identifiersMap);
        verify(patientService).updatePatientIdentifiers(eq(CONFIG_NAME), patientCaptor.capture());

        Identifier patientCaptorIdentifier = patientCaptor.getValue().getIdentifiers().get(0);

        assertEquals(patient.getUuid(), patientCaptor.getValue().getUuid());
        assertEquals("1000", patientCaptorIdentifier.getIdentifier());
        assertEquals("CommCare CaseID", patientCaptorIdentifier.getIdentifierType().getName());
    }

    @Test
    public void shouldCreateObservationWithGivenJsonParameter() {
        JsonObject observationObject = new JsonObject();

        String encounterUuid = "10";
        String conceptUuid = "20";
        String obsDatetime = "2016-07-29T18:29:50.000+0800";
        String comment = "testComment";

        observationObject.addProperty("encounter", encounterUuid);
        observationObject.addProperty("concept", conceptUuid);
        observationObject.addProperty("obsDatetime", obsDatetime);
        observationObject.addProperty("comment", comment);
        String observationJSON = observationObject.toString();

        Encounter encounter = new Encounter();
        encounter.setUuid(encounterUuid);

        Concept concept = new Concept();
        concept.setUuid(conceptUuid);

        Observation observation = new Observation();
        observation.setEncounter(encounter);
        observation.setConcept(concept);
        observation.setObsDatetime(new DateTime(obsDatetime).toDate());
        observation.setComment(comment);

        doReturn(observation).when(observationService).createObservationFromJson(eq(CONFIG_NAME),
                eq(observationJSON));

        Observation obsCreated = openMRSActionProxyService.createObservationJSON(CONFIG_NAME, observationJSON, null, null, null, null, null);

        assertEquals(observation, obsCreated);
    }


    @Test
    public void shouldCreateObservationWithReplacedParameters() {
        JsonObject observationObject = new JsonObject();

        String encounterUuid = "10";
        String conceptUuid = "20";
        String obsDatetime = "2016-07-29T18:29:50.000+0800";
        String comment = "testComment";
        String value = "testValue";

        String encounterUuidReplace = "100";
        String conceptUuidReplace = "200";
        String obsDatetimeReplace = "2010-07-29T18:29:50.000+0800";
        String commentReplace = "testComment2";

        observationObject.addProperty("encounter", encounterUuid);
        observationObject.addProperty("concept", conceptUuid);
        observationObject.addProperty("obsDatetime", obsDatetime);
        observationObject.addProperty("comment", comment);
        observationObject.addProperty("value", value);
        String observationJSON = observationObject.toString();

        observationObject = new JsonObject();
        observationObject.addProperty("encounter", encounterUuidReplace);
        observationObject.addProperty("concept", conceptUuidReplace);
        observationObject.addProperty("obsDatetime", new DateTime(obsDatetimeReplace).toString(fullDateTimeFormatter));
        observationObject.addProperty("comment", commentReplace);
        observationObject.addProperty("value", value);
        String observationJSONReplace = observationObject.toString();

        Encounter encounter = new Encounter();
        encounter.setUuid(encounterUuid);

        Concept concept = new Concept();
        concept.setUuid(conceptUuid);

        openMRSActionProxyService.createObservationJSON(CONFIG_NAME, observationJSON,
                encounterUuidReplace, conceptUuidReplace, new DateTime(obsDatetimeReplace), null, commentReplace);

        verify(observationService).createObservationFromJson(eq(CONFIG_NAME), observationCaptor.capture());
        assertEquals(observationJSONReplace, observationCaptor.getValue());
    }


    @Test
    public void shouldCreateVisitWithRequiredGivenParameters() {
        Patient patient = new Patient();
        patient.setUuid("10");

        DateTime startDateTime = new DateTime("2010-01-10T07:22:05Z");
        DateTime endDateTime = new DateTime("2012-08-01T07:22:05Z");

        Visit visit = new Visit(startDateTime.toDate(), endDateTime.toDate(), patient,
                new VisitType("ee1b6117-e40b-4082-8880-96aca7ea1472"));

        doReturn(patient).when(patientService).getPatientByUuid(eq(CONFIG_NAME), eq(patient.getUuid()));

        openMRSActionProxyService.createVisit(CONFIG_NAME, patient.getUuid(), new DateTime(visit.getStartDatetime()),
                new DateTime(visit.getStopDatetime()), visit.getVisitType().getUuid(), null);

        verify(visitService).createVisit(eq(CONFIG_NAME), visitCaptor.capture());
        assertEquals(visit, visitCaptor.getValue());
    }

    @Test
    public void shouldCreateVisitWithGivenParameters() {
        Location location = new Location();
        location.setName("testLocation");

        Patient patient = new Patient();
        patient.setUuid("10");

        DateTime startDateTime = new DateTime("2010-01-10T07:22:05Z");
        DateTime endDateTime = new DateTime("2012-08-01T07:22:05Z");

        Visit visit = new Visit(startDateTime.toDate(), endDateTime.toDate(), patient,
                new VisitType("ee1b6117-e40b-4082-8880-96aca7ea1472"), location);

        doReturn(patient).when(patientService).getPatientByUuid(eq(CONFIG_NAME), eq(patient.getUuid()));
        doReturn(Collections.singletonList(location))
                .when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        openMRSActionProxyService.createVisit(CONFIG_NAME, patient.getUuid(), new DateTime(visit.getStartDatetime()),
                new DateTime(visit.getStopDatetime()), visit.getVisitType().getUuid(), location.getName());

        verify(visitService).createVisit(eq(CONFIG_NAME), visitCaptor.capture());
        assertEquals(visit, visitCaptor.getValue());
    }

    public void shouldCreateProgramEnrollment() {
        Patient patient = new Patient();
        patient.setUuid("aaef37f5-ffaa-4f94-af6e-54907b0c0fd4");

        Program program = new Program();
        program.setUuid("a7bdec92-b9d4-4f1c-9323-ce515bf986ec");

        Location location = new Location();
        location.setUuid("d4ff64b5-ad57-4029-9564-949e76b80d07");
        location.setName("Unknown Location");

        DateTime dateEnrolled = new DateTime("2000-08-16T07:22:05Z");
        DateTime dateCompleted = new DateTime("2100-08-16T07:22:05Z");

        Map<String, String> programEnrollmentAttributes = new HashMap<>();
        programEnrollmentAttributes.put("8d8718c2-c2cc-11de-8d13-0010c6dffd0f", "testValue");

        List<Attribute> attributes = new ArrayList<>();
        Attribute attribute = new Attribute();
        Attribute.AttributeType attributeType = new Attribute.AttributeType();

        attributeType.setUuid("8d8718c2-c2cc-11de-8d13-0010c6dffd0f");
        attribute.setValue("testValue");
        attribute.setAttributeType(attributeType);

        attributes.add(attribute);

        ProgramEnrollment programEnrollment = new ProgramEnrollment();

        programEnrollment.setPatient(patient);
        programEnrollment.setProgram(program);
        programEnrollment.setDateEnrolled(dateEnrolled.toDate());
        programEnrollment.setDateCompleted(dateCompleted.toDate());
        programEnrollment.setLocation(location);
        programEnrollment.setAttributes(attributes);

        doReturn(Collections.singletonList(location)).when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        openMRSActionProxyService.createProgramEnrollment(CONFIG_NAME, patient.getUuid(), program.getUuid(),
                dateEnrolled, dateCompleted, location.getName(), programEnrollmentAttributes);

        verify(programEnrollmentService).createProgramEnrollment(eq(CONFIG_NAME), programEnrollmentCaptor.capture());
        assertEquals(programEnrollment, programEnrollmentCaptor.getValue());
    }

    @Test
    public void shouldUpdateProgramEnrollment() {

        DateTime dateEnrolled = new DateTime("2000-08-16T07:22:05Z");
        DateTime dateCompleted = new DateTime("2100-08-16T07:22:05Z");

        ProgramEnrollment.StateStatus state = new ProgramEnrollment.StateStatus();
        state.setUuid("4b812ac8-421c-470f-b4b7-88187cdbd2a5");

        List<ProgramEnrollment.StateStatus> statuses = new ArrayList<>();
        statuses.add(state);

        Map<String, String> programEnrollmentAttributes = new HashMap<>();
        programEnrollmentAttributes.put("8d8718c2-c2cc-11de-8d13-0010c6dffd0f", "testValue");

        List<Attribute> attributes = new ArrayList<>();
        Attribute attribute = new Attribute();

        attribute.setUuid("8d8718c2-c2cc-11de-8d13-0010c6dffd0f");
        attribute.setValue("testValue");

        attributes.add(attribute);

        ProgramEnrollment programEnrollment = new ProgramEnrollment();

        programEnrollment.setUuid("aaef37f5-ffaa-4f94-af6e-54907b0c0fd4");
        programEnrollment.setDateEnrolled(dateEnrolled.toDate());
        programEnrollment.setDateCompleted(dateCompleted.toDate());
        programEnrollment.setStates(statuses);
        programEnrollment.setAttributes(attributes);

        openMRSActionProxyService.updateProgramEnrollment(CONFIG_NAME, programEnrollment.getUuid(), dateCompleted,
                state.getUuid(), dateEnrolled, programEnrollmentAttributes);

        verify(programEnrollmentService).updateProgramEnrollment(eq(CONFIG_NAME), programEnrollmentCaptor.capture());
        assertEquals(programEnrollment, programEnrollmentCaptor.getValue());
    }

    @Test
    public void shouldChangeStateOfProgramEnrollment() {
        Program.State state = new Program.State();
        state.setUuid("c2d72f91-75b6-4bd9-a400-1c66a170f028");

        DateTime startDate = new DateTime("2000-08-16T07:22:05Z");
        DateTime completedDate = new DateTime("2010-08-16T07:22:05Z");

        ProgramEnrollment.StateStatus stateStatus = new ProgramEnrollment.StateStatus();
        stateStatus.setState(state);
        stateStatus.setStartDate(startDate.toDate());

        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setUuid("aaef37f5-ffaa-4f94-af6e-54907b0c0fd4");
        programEnrollment.setDateCompleted(completedDate.toDate());
        programEnrollment.setStates(Collections.singletonList(stateStatus));

        openMRSActionProxyService.changeStateOfProgramEnrollment(CONFIG_NAME, programEnrollment.getUuid(), completedDate,
                state.getUuid(), startDate);

        verify(programEnrollmentService).updateProgramEnrollment(eq(CONFIG_NAME), programEnrollmentCaptor.capture());
        assertEquals(programEnrollment, programEnrollmentCaptor.getValue());
    }

    @Test
    public void shouldGetCohortQueryReportWithoutMembersAndWithNoGivenParameters() {
        String cohortQueryUuid = "QQQ";

        ArgumentCaptor<String> uuidCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> parametersCaptor = ArgumentCaptor.forClass(Map.class);

        doReturn(prepareCohortQueryReport(false)).when(cohortService)
                .getCohortQueryReport(eq(CONFIG_NAME), eq(cohortQueryUuid), eq(Collections.EMPTY_MAP));

        openMRSActionProxyService.getCohortQueryReport(CONFIG_NAME, cohortQueryUuid, Collections.EMPTY_MAP);

        verify(cohortService).getCohortQueryReport(eq(CONFIG_NAME), uuidCaptor.capture(), parametersCaptor.capture());
        verify(eventRelay, never()).sendEventMessage(Matchers.<MotechEvent>any());
        assertEquals(cohortQueryUuid, uuidCaptor.getValue());
        assertEquals(Collections.EMPTY_MAP, parametersCaptor.getValue());
    }

    @Test
    public void shouldGetCohortQueryReportWithGivenParameters() {
        String cohortQueryUuid = "QQQ";
        Map<String, String> parameters = new HashMap<>();
        parameters.put("param1", "value");
        parameters.put("param2", "value");

        ArgumentCaptor<String> uuidCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> parametersCaptor = ArgumentCaptor.forClass(Map.class);
        ArgumentCaptor<MotechEvent> eventCaptor = ArgumentCaptor.forClass(MotechEvent.class);

        doReturn(prepareCohortQueryReport(true)).when(cohortService)
                .getCohortQueryReport(eq(CONFIG_NAME), eq(cohortQueryUuid), eq(parameters));

        openMRSActionProxyService.getCohortQueryReport(CONFIG_NAME, cohortQueryUuid, parameters);

        verify(cohortService).getCohortQueryReport(eq(CONFIG_NAME), uuidCaptor.capture(), parametersCaptor.capture());
        verify(eventRelay, times(2)).sendEventMessage(eventCaptor.capture());
        assertEquals(cohortQueryUuid, uuidCaptor.getValue());
        assertEquals(parameters, parametersCaptor.getValue());

        List<MotechEvent> capturedEvents = eventCaptor.getAllValues();
        assertEquals(createEventForMember(cohortQueryUuid, prepareCohortQueryReportMember("1")), capturedEvents.get(0));
        assertEquals(createEventForMember(cohortQueryUuid, prepareCohortQueryReportMember("2")), capturedEvents.get(1));
    }

    @Test
    public void shouldCreateOrderWithGivenParameters() {

        Patient patient = new Patient();
        patient.setUuid("10");

        Provider provider = new Provider();
        provider.setUuid("20");

        Encounter encounter = new Encounter();
        encounter.setUuid("50");

        Concept concept = createTestConcept("60");

        Order order = new Order("order", encounter, provider, patient, concept, Order.CareSetting.INPATIENT);

        doReturn(order).when(orderService).createOrder(eq(CONFIG_NAME), eq(order));
        Order orderCreated = openMRSActionProxyService.createOrder(CONFIG_NAME, "order", encounter.getUuid(), patient.getUuid(), concept.getUuid(), provider.getUuid(),  Order.CareSetting.INPATIENT.toString());

        assertEquals(order, orderCreated);
    }

    private Person createTestPerson() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setMiddleName("Robert");
        name.setFamilyName("Smith");
        person.setPreferredName(name);
        person.setNames(Collections.singletonList(name));

        Map<String, String> personAttributes = new HashMap<>();
        personAttributes.put("8d8718c2-c2cc-11de-8d13-0010c6dffd0f", "testValue");

        List<Attribute> attributes = new ArrayList<>();
        Attribute attribute = new Attribute();
        Attribute.AttributeType attributeType = new Attribute.AttributeType();

        attributeType.setUuid("8d8718c2-c2cc-11de-8d13-0010c6dffd0f");
        attribute.setValue("testValue");
        attribute.setAttributeType(attributeType);

        attributes.add(attribute);
        person.setAttributes(attributes);

        Person.Address address = new Person.Address("address 1", "address 2", "address 3", "address 4", "address 5",
                "address 6", "City", "State", "Country", "000000", "County district", "30", "50",
                DATE, new DateTime("2100-08-16T07:22:05Z").toDate());
        person.setPreferredAddress(address);
        person.setAddresses(Collections.singletonList(address));

        person.setGender("M");

        person.setBirthdate(DATE);
        person.setBirthdateEstimated(true);
        person.setDead(false);

        return person;
    }

    private Concept createTestConcept(String testConceptName) {
        Concept concept = new Concept();
        ConceptName conceptName = new ConceptName(testConceptName);

        concept.setNames(Collections.singletonList(conceptName));
        concept.setDatatype(new Concept.DataType("TEXT"));
        concept.setConceptClass(new Concept.ConceptClass("Test"));
        concept.setUuid("100");

        return concept;
    }

    private List<Observation> createObservationList(int observationsNumber) {
        List<Observation> observationList = new ArrayList<>();

        for (int i = 0; i < observationsNumber; i++) {
            Concept concept = new Concept();
            concept.setUuid("18ff53df-744a-4a3f-8f25-dac6de5b7131");

            Observation observation = new Observation();
            observation.setConcept(concept);
            observation.setValue(new Observation.ObservationValue("testObservationValueName" + Integer.toString(i)));
            observation.setObsDatetime(DATE);
            observationList.add(observation);
        }
        return observationList;
    }

    private CohortQueryReport prepareCohortQueryReport(boolean withMembers) {
        CohortQueryReport cohortQueryReport = new CohortQueryReport();

        List<CohortQueryReportMember> members = new ArrayList<>();
        if (withMembers) {
            members.add(prepareCohortQueryReportMember("1"));
            members.add(prepareCohortQueryReportMember("2"));
        }

        cohortQueryReport.setMembers(members);

        return cohortQueryReport;
    }

    private CohortQueryReportMember prepareCohortQueryReportMember(String suffix) {
        CohortQueryReportMember cohortQueryReportMember = new CohortQueryReportMember();

        cohortQueryReportMember.setUuid("testUuid" + suffix);
        cohortQueryReportMember.setDisplay("testDisplay" + suffix);

        return cohortQueryReportMember;
    }

    private MotechEvent createEventForMember(String cohortQueryUuid, CohortQueryReportMember member) {
        Map<String, Object> parameters = new HashMap<>();

        parameters.put(Keys.COHORT_QUERY_UUID, cohortQueryUuid);
        parameters.put(Keys.PATIENT_UUID, member.getUuid());
        parameters.put(Keys.PATIENT_DISPLAY, member.getDisplay());

        return new MotechEvent(EventSubjects.GET_COHORT_QUERY_MEMBER_EVENT.concat(CONFIG_NAME), parameters);
    }

    private Map<String, String> prepareObservationGroupsMap() {
        Map<String, String> observationsMap = new HashMap<>();

        observationsMap.put("parentUuid/child1Uuid/child2Child1Uuid", "Yes");
        observationsMap.put("parentUuid/child1Uuid/child1Child1Uuid", "No");
        observationsMap.put("parentUuid/child2Uuid", "2nd");
        observationsMap.put("parentUuid/child3Uuid", "Maybe");

        //observations with blank/null values should be omitted
        observationsMap.put("parentUuid/child1Uuid/child3Child1Uuid", null);
        observationsMap.put("parentUuid/child5Uuid", null);
        observationsMap.put("parentUuid/child6Uuid", "");

        return observationsMap;
    }

    private List<Observation> createObservationGroups() {
        Concept concept1 = new Concept();
        concept1.setUuid("child1Child1Uuid");

        Observation childChild1 = new Observation();
        childChild1.setConcept(concept1);
        childChild1.setValue(new Observation.ObservationValue("No"));
        childChild1.setObsDatetime(DATE);

        Concept concept2 = new Concept();
        concept2.setUuid("child2Child1Uuid");

        Observation childChild2 = new Observation();
        childChild2.setConcept(concept2);
        childChild2.setValue(new Observation.ObservationValue("Yes"));
        childChild2.setObsDatetime(DATE);

        Concept concept3 = new Concept();
        concept3.setUuid("child1Uuid");

        Observation child1 = new Observation();
        child1.setConcept(concept3);
        child1.setGroupMembers(Arrays.asList(childChild2, childChild1));
        child1.setObsDatetime(DATE);

        Concept concept4 = new Concept();
        concept4.setUuid("child2Uuid");

        Observation child2 = new Observation();
        child2.setConcept(concept4);
        child2.setValue(new Observation.ObservationValue("2nd"));
        child2.setObsDatetime(DATE);

        Concept concept5 = new Concept();
        concept5.setUuid("child3Uuid");

        Observation child3 = new Observation();
        child3.setConcept(concept5);
        child3.setValue(new Observation.ObservationValue("Maybe"));
        child3.setObsDatetime(DATE);

        Concept concept6 = new Concept();
        concept6.setUuid("parentUuid");

        Observation parentObservation = new Observation();
        parentObservation.setObsDatetime(DATE);
        parentObservation.setConcept(concept6);
        parentObservation.setGroupMembers(Arrays.asList(child1, child2, child3));

        return Collections.singletonList(parentObservation);
    }
 }