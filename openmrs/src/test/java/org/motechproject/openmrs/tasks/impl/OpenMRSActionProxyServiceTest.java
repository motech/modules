package org.motechproject.openmrs.tasks.impl;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
import org.motechproject.openmrs.service.OpenMRSConceptService;
import org.motechproject.openmrs.service.OpenMRSEncounterService;
import org.motechproject.openmrs.service.OpenMRSLocationService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.motechproject.openmrs.service.OpenMRSPersonService;
import org.motechproject.openmrs.service.OpenMRSProgramEnrollmentService;
import org.motechproject.openmrs.service.OpenMRSProviderService;
import org.motechproject.openmrs.tasks.OpenMRSActionProxyService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSActionProxyServiceTest {

    private static final String CONFIG_NAME = "Configuration name";

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
    private OpenMRSProviderService providerService;

    @Mock
    private OpenMRSProgramEnrollmentService programEnrollmentService;

    @Captor
    private ArgumentCaptor<Encounter> encounterCaptor;

    @Captor
    private ArgumentCaptor<Patient> patientCaptor;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

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
        
        DateTime encounterDatetime = new DateTime("2000-08-16T07:22:05Z");
        Map<String, String> observations = new HashMap<>();
        observations.put("testConceptName","testObservationValueName");

        List<Observation> obsList = createObservationList();

        Encounter encounter = new Encounter(location, new EncounterType("testEncounterType"), encounterDatetime.toDate(), patient, provider.getPerson(), obsList);

        doReturn(patient).when(patientService).getPatientByUuid(eq(CONFIG_NAME), eq(patient.getUuid()));
        doReturn(provider).when(providerService).getProviderByUuid(eq(CONFIG_NAME), eq(provider.getUuid()));
        doReturn(Collections.singletonList(location))
                .when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        openMRSActionProxyService.createEncounter(CONFIG_NAME, new DateTime(encounter.getEncounterDatetime()),
                encounter.getEncounterType().getName(), location.getName(), patient.getUuid(), provider.getUuid(),
                observations);

        verify(encounterService).createEncounter(eq(CONFIG_NAME), encounterCaptor.capture());

        assertEquals(encounter, encounterCaptor.getValue());
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
                patient.getMotechId(), location.getName(), identifiersMap);

        verify(patientService).createPatient(eq(CONFIG_NAME), patientCaptor.capture());

        assertEquals(patient, patientCaptor.getValue());
    }

    @Test
    public void shouldCreatePatientWithDefaultLocationWhenLocationNameIsNotProvided() {
        Person person = createTestPerson();

        Location location = new Location();
        location.setName(OpenMRSActionProxyService.DEFAULT_LOCATION_NAME);

        Identifier identifier = new Identifier("1000", new IdentifierType("CommCare CaseID"));

        Patient patient = new Patient(Collections.singletonList(identifier), person, "500", location);

        Map<String, String> identifiersMap = new HashMap<>();
        identifiersMap.put("CommCare CaseID", "1000");

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
                location.getName(), identifiersMap);

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
                location.getName(), identifiersMap);

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

        doReturn(causeOfDeath).when(conceptService).getConceptByUuid(eq(CONFIG_NAME), eq(causeOfDeath.getUuid()));
        openMRSActionProxyService.updatePerson(CONFIG_NAME, person.getUuid(), person.getPreferredName().getGivenName(),
                person.getPreferredName().getMiddleName(), person.getPreferredName().getFamilyName(),
                personAddress.getAddress1(), personAddress.getAddress2(), personAddress.getAddress3(),
                personAddress.getAddress4(), personAddress.getAddress5(), personAddress.getAddress6(),
                personAddress.getCityVillage(), personAddress.getStateProvince(), personAddress.getCountry(),
                personAddress.getPostalCode(), personAddress.getCountyDistrict(), personAddress.getLatitude(),
                personAddress.getLongitude(), new DateTime(personAddress.getStartDate()),
                new DateTime(personAddress.getEndDate()), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), causeOfDeath.getUuid());

        verify(personService).updatePerson(eq(CONFIG_NAME), personCaptor.capture());
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

        ProgramEnrollment programEnrollment = new ProgramEnrollment();

        programEnrollment.setPatient(patient);
        programEnrollment.setProgram(program);
        programEnrollment.setDateEnrolled(dateEnrolled.toDate());
        programEnrollment.setDateCompleted(dateCompleted.toDate());
        programEnrollment.setLocation(location);

        doReturn(Collections.singletonList(location)).when(locationService).getLocations(eq(CONFIG_NAME), eq(location.getName()));

        openMRSActionProxyService.createProgramEnrollment(CONFIG_NAME, patient.getUuid(), program.getUuid(),
                dateEnrolled, dateCompleted, location.getName());

        verify(programEnrollmentService).createProgramEnrollment(eq(CONFIG_NAME), programEnrollmentCaptor.capture());
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

    private Person createTestPerson() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setMiddleName("Robert");
        name.setFamilyName("Smith");
        person.setPreferredName(name);
        person.setNames(Collections.singletonList(name));

        Person.Address address = new Person.Address("address 1", "address 2", "address 3", "address 4", "address 5",
                "address 6", "City", "State", "Country", "000000", "County district", "30", "50",
                new DateTime("2000-08-16T07:22:05Z").toDate(), new DateTime("2100-08-16T07:22:05Z").toDate());
        person.setPreferredAddress(address);
        person.setAddresses(Collections.singletonList(address));

        person.setGender("M");

        person.setBirthdate(new DateTime("2000-08-16T07:22:05Z").toDate());
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

    private List<Observation> createObservationList() {
        Observation observation = new Observation();

        ConceptName conceptName = new ConceptName("testConceptName");
        Concept concept = new Concept(conceptName);

        observation.setConcept(concept);
        observation.setValue(new Observation.ObservationValue("testObservationValueName"));
        observation.setObsDatetime(new DateTime("2000-08-16T07:22:05Z").toDate());

        return Collections.singletonList(observation);
    }
}
