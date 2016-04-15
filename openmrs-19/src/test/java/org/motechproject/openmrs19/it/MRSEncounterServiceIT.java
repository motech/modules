package org.motechproject.openmrs19.it;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.ConceptName;
import org.motechproject.openmrs19.domain.Encounter;
import org.motechproject.openmrs19.domain.EncounterType;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.domain.Provider;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSLocationService;
import org.motechproject.openmrs19.service.OpenMRSObservationService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSEncounterServiceIT extends BasePaxIT {

    @Inject
    private OpenMRSLocationService locationAdapter;

    @Inject
    private OpenMRSEncounterService encounterAdapter;

    @Inject
    private OpenMRSPatientService patientAdapter;

    @Inject
    private OpenMRSPersonService personAdapter;

    @Inject
    private OpenMRSObservationService obsAdapter;

    @Inject
    private EventListenerRegistryService eventListenerRegistry;

    @Inject
    private OpenMRSProviderService providerService;

    @Inject
    private OpenMRSConceptService conceptAdapter;

    final Object lock = new Object();

    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    MrsListener mrsListener;

    Person personOne;
    Person personTwo;
    EncounterType encounterType;
    Encounter encounter;
    Location location;
    Patient patient;
    Observation observation;
    Provider provider;
    Concept concept;

    String date = "2012-09-05";

    @Before
    public void setUp() throws ParseException, ConceptNameAlreadyInUseException, InterruptedException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT,
                EventKeys.DELETED_ENCOUNTER_SUBJECT));

        encounter = createEncounter(prepareEncounterOne());
    }

    @Test
    public void shouldCreateEncounter() throws HttpException, ParseException, InterruptedException {

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deleted);

        assertEquals(encounter.getUuid(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_ID));
        assertEquals(encounter.getProvider().getUuid(), mrsListener.eventParameters.get(EventKeys.PROVIDER_ID));
        assertEquals(encounter.getLocation().getUuid(), mrsListener.eventParameters.get(EventKeys.LOCATION_ID));
        assertEquals(encounter.getEncounterDatetime(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_DATE));
        assertEquals(encounter.getEncounterType().getUuid(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_TYPE));
    }

    @Test
    public void shouldGetLatestEncounter() {
        Encounter encounter = encounterAdapter.getLatestEncounterByPatientMotechId(patient.getMotechId(), encounterType.getName());

        assertNotNull(encounter);
        assertEquals(new LocalDate("2012-09-05"), new LocalDate(encounter.getEncounterDatetime()));
    }

    @Test
    public void shouldDeleteEncounter() throws InterruptedException {

        synchronized (lock) {
            encounterAdapter.deleteEncounter(encounter.getUuid());
            assertNull(encounterAdapter.getEncounterByUuid(encounter.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.deleted);
    }

    @After
    public void tearDown() throws PatientNotFoundException, InterruptedException {

        if (encounter != null) {
            encounterAdapter.deleteEncounter(encounter.getUuid());
        }
        if (observation != null) {
            obsAdapter.deleteObservation(observation.getUuid());
        }
        if (provider != null) {
            providerService.deleteProvider(provider.getUuid());
        }
        if (patient != null) {
            patientAdapter.deletePatient(patient.getUuid());
        }
        if (location != null) {
            locationAdapter.deleteLocation(location.getUuid());
        }
        if (concept != null) {
            conceptAdapter.deleteConcept(concept.getUuid());
        }
        if (encounterType != null) {
            encounterAdapter.deleteEncounterType(encounterType.getUuid());
        }
        if (personOne != null) {
            personAdapter.deletePerson(personOne.getUuid());
        }
        if (personTwo != null) {
            personAdapter.deletePerson(personTwo.getUuid());
        }

        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private boolean deleted = false;
        private Map<String, Object> eventParameters;

        public void handle(MotechEvent event) {
            if (event.getSubject().equals(EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT)) {
                created = true;
            } else if (event.getSubject().equals(EventKeys.DELETED_ENCOUNTER_SUBJECT)) {
                deleted = true;
            }

            eventParameters = event.getParameters();
            synchronized (lock) {
                lock.notify();
            }
        }

        @Override
        public String getIdentifier() {
            return "mrsTestListener";
        }
    }

    private Encounter prepareEncounterOne() throws ParseException, ConceptNameAlreadyInUseException {

        preparePersonOne();
        preparePersonTwo();
        prepareConcept();
        prepareProvider();
        prepareLocation();
        preparePatient();
        prepareEncounterType();
        prepareObservations();

        Encounter encounter = new Encounter(location, encounterType, format.parse(date), patient, provider.getPerson(),
                Collections.singletonList(observation));

        return encounter;
    }

    private Encounter createEncounter(Encounter encounter) throws InterruptedException {

        Encounter saved;

        synchronized (lock) {
            saved = encounterAdapter.createEncounter(encounter);
            assertNotNull(saved);

            lock.wait(6000);
        }

        return saved;
    }

    private void prepareConcept() throws ConceptNameAlreadyInUseException {
        Concept tempConcept = new Concept();
        tempConcept.setNames(Arrays.asList(new ConceptName("FooConcept")));
        tempConcept.setDatatype(new Concept.DataType("Boolean"));
        tempConcept.setConceptClass(new Concept.ConceptClass("Test"));
        concept = conceptAdapter.createConcept(tempConcept);
    }

    private void preparePersonOne() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("FooFirstName");
        name.setFamilyName("FooLastName");
        person.setNames(Collections.singletonList(name));

        person.setGender("M");
        String personUuid = personAdapter.createPerson(person).getUuid();
        personOne = personAdapter.getPersonByUuid(personUuid);
    }

    private void preparePersonTwo() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("FooNameTwo");
        name.setFamilyName("FooLastNameTwo");
        person.setNames(Collections.singletonList(name));

        person.setGender("F");
        String personUuid = personAdapter.createPerson(person).getUuid();
        personTwo = personAdapter.getPersonByUuid(personUuid);
    }

    private void prepareProvider() {
        Provider tempProvider = new Provider();
        tempProvider.setPerson(personOne);
        tempProvider.setIdentifier("FooIdentifier");
        String providerUuid = providerService.createProvider(tempProvider).getUuid();
        provider = providerService.getProviderByUuid(providerUuid);
    }

    private void prepareLocation() {
        Location tempLocation = new Location("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince");
        String locationUuid = locationAdapter.createLocation(tempLocation).getUuid();
        location = locationAdapter.getLocationByUuid(locationUuid);
    }

    private void preparePatient() {
        Patient tempPatient = new Patient();
        tempPatient.setLocationForMotechId(location);
        tempPatient.setPerson(personTwo);
        tempPatient.setMotechId("666");
        String patientUuid = patientAdapter.createPatient(tempPatient).getUuid();
        patient = patientAdapter.getPatientByUuid(patientUuid);
    }

    private void prepareEncounterType() {
        EncounterType tempEncounterType = new EncounterType("FooType");
        tempEncounterType.setDescription("FooDescription");

        String encounterUuid = encounterAdapter.createEncounterType(tempEncounterType).getUuid();
        encounterType = encounterAdapter.getEncounterTypeByUuid(encounterUuid);
    }

    private void prepareObservations() throws ParseException {
        Observation tempObservation = new Observation();

        tempObservation.setObsDatetime(format.parse(date));
        tempObservation.setConcept(concept);
        tempObservation.setValue(new Observation.ObservationValue("true"));
        tempObservation.setPerson(patient.getPerson());

        String observationUuid = obsAdapter.createObservation(tempObservation).getUuid();
        observation = obsAdapter.getObservationByUuid(observationUuid);
    }
}
