package org.motechproject.openmrs19.tasks.impl;

import org.joda.time.DateTime;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
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
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSLocationService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.tasks.OpenMRSActionProxyService;

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

    @Captor
    private ArgumentCaptor<Encounter> encounterCaptor;

    @Captor
    private ArgumentCaptor<Patient> patientCaptor;

    @Captor
    private ArgumentCaptor<Person> personCaptor;

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
