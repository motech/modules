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
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.tasks.OpenMRSActionProxyService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSActionProxyServiceTest {

    @Mock
    private OpenMRSConceptService conceptService;

    @Mock
    private OpenMRSEncounterService encounterService;

    @Mock
    private OpenMRSLocationService locationService;

    @Mock
    private OpenMRSPatientService patientService;

    @Mock
    private OpenMRSProviderService providerService;

    @Captor
    private ArgumentCaptor<Encounter> encounterCaptor;

    @Captor
    private ArgumentCaptor<Patient> patientCaptor;

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

        doReturn(Collections.singletonList(location)).when(locationService).getLocations(eq(null), eq(location.getName()));
        doReturn(patient).when(patientService).getPatientByUuid(eq(null), eq(patient.getUuid()));
        doReturn(provider).when(providerService).getProviderByUuid(eq(null), eq(provider.getUuid()));

        openMRSActionProxyService.createEncounter(new DateTime(encounter.getEncounterDatetime()),
                encounter.getEncounterType().getName(), location.getName(), patient.getUuid(), provider.getUuid(), observations);

        verify(encounterService).createEncounter(eq(null), encounterCaptor.capture());

        assertEquals(encounter, encounterCaptor.getValue());
    }

    @Test
    public void shouldCreatePatientWithGivenParameters() {
        Person person = createTestPerson();
        Concept causeOfDeath = createTestConcept();

        person.setDead(true);
        person.setCauseOfDeath(causeOfDeath);

        Location location = new Location();
        location.setName("testLocation");

        Identifier identifier = new Identifier("1000", new IdentifierType("CommCare CaseID"));

        Patient patient = new Patient(Collections.singletonList(identifier), person, "500", location);

        Map<String, String> identifiersMap = new HashMap<>();
        identifiersMap.put("CommCare CaseID", "1000");

        doReturn(causeOfDeath).when(conceptService).getConceptByUuid(eq(null), eq(causeOfDeath.getUuid()));
        doReturn(Collections.singletonList(location)).when(locationService).getLocations(eq(null), eq(location.getName()));

        openMRSActionProxyService.createPatient(person.getPreferredName().getGivenName(), person.getPreferredName().getMiddleName(),
                person.getPreferredName().getFamilyName(), person.getPreferredAddress().getAddress1(), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), causeOfDeath.getUuid(), patient.getMotechId(),
                location.getName(), identifiersMap);

        verify(patientService).createPatient(eq(null), patientCaptor.capture());

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

        doReturn(Collections.singletonList(location)).when(locationService).getLocations(eq(null), eq(OpenMRSActionProxyService.DEFAULT_LOCATION_NAME));

        openMRSActionProxyService.createPatient(person.getPreferredName().getGivenName(), person.getPreferredName().getMiddleName(),
                person.getPreferredName().getFamilyName(), person.getPreferredAddress().getAddress1(), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), "", patient.getMotechId(),
                "", identifiersMap);

        verify(patientService).createPatient(eq(null), patientCaptor.capture());

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

        doReturn(Collections.emptyList()).when(locationService).getLocations(eq(null), eq(location.getName()));

        openMRSActionProxyService.createPatient(person.getPreferredName().getGivenName(), person.getPreferredName().getMiddleName(),
                person.getPreferredName().getFamilyName(), person.getPreferredAddress().getAddress1(), new DateTime(person.getBirthdate()),
                person.getBirthdateEstimated(), person.getGender(), person.getDead(), "", patient.getMotechId(),
                location.getName(), identifiersMap);

        verify(patientService).createPatient(eq(null), patientCaptor.capture());

        // the expected patient object has location value set to null, the actual object should be the same
        assertEquals(patient, patientCaptor.getValue());
    }

    private Person createTestPerson() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setMiddleName("Robert");
        name.setFamilyName("Smith");
        person.setPreferredName(name);
        person.setNames(Collections.singletonList(name));

        Person.Address address = new Person.Address();
        address.setAddress1("Gdynia");
        person.setPreferredAddress(address);
        person.setAddresses(Collections.singletonList(address));

        person.setGender("M");

        person.setBirthdate(new DateTime("2000-08-16T07:22:05Z").toDate());
        person.setBirthdateEstimated(true);
        person.setDead(false);

        return person;
    }

    private Concept createTestConcept() {
        Concept concept = new Concept();
        ConceptName conceptName = new ConceptName("testConceptName");

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
