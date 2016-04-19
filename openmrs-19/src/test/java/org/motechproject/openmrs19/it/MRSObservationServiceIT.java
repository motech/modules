package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.Concept;
import org.motechproject.openmrs19.domain.ConceptName;
import org.motechproject.openmrs19.domain.Location;
import org.motechproject.openmrs19.domain.Observation;
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.exception.ObservationNotFoundException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSLocationService;
import org.motechproject.openmrs19.service.OpenMRSObservationService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.openmrs19.util.TestConstants.DEFAULT_CONFIG_NAME;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSObservationServiceIT extends BasePaxIT {

    @Inject
    private OpenMRSObservationService obsAdapter;

    @Inject
    private OpenMRSConceptService conceptAdapter;

    @Inject
    private OpenMRSPersonService personAdapter;

    @Inject
    private OpenMRSPatientService patientAdapter;

    @Inject
    private OpenMRSLocationService locationAdapter;

    @Inject
    EventListenerRegistryService eventListenerRegistry;

    MrsListener mrsListener;
    final Object lock = new Object();

    Observation observation;
    Concept concept;
    Person person;
    Location location;
    Patient patient;

    @Before
    public void setUp() throws InterruptedException, ConceptNameAlreadyInUseException {

        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_OBSERVATION_SUBJECT,
                EventKeys.DELETED_OBSERVATION_SUBJECT, EventKeys.VOIDED_OBSERVATION_SUBJECT));

        observation = saveObservation(prepareObservation());
    }

    @Test
    public void shouldCreateObservation() {
        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deleted);
        assertFalse(mrsListener.voided);
    }

    @Test
    public void shouldFindSearchedConcept() {

        Observation obs = obsAdapter.findObservation(DEFAULT_CONFIG_NAME, patient.getMotechId(), concept.getDisplay());

        assertNotNull(obs);
    }

    @Test
    public void shouldFindListOfObservations() {

        List<Observation> obs = obsAdapter.findObservations(DEFAULT_CONFIG_NAME, patient.getMotechId(), concept.getDisplay());

        assertNotNull(obs);
        assertTrue(obs.size() > 0);
    }

    @Test
    public void shouldVoidObservationWithoutReason() throws ObservationNotFoundException, InterruptedException {

        synchronized (lock) {
            obsAdapter.voidObservation(DEFAULT_CONFIG_NAME, observation, null);
            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.voided);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldVoidObservationWithReason() throws ObservationNotFoundException, InterruptedException {

        synchronized (lock) {
            obsAdapter.voidObservation(DEFAULT_CONFIG_NAME, observation, "FooReason");
            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.voided);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeleteObservation() throws InterruptedException {

        synchronized (lock) {
            obsAdapter.deleteObservation(DEFAULT_CONFIG_NAME, observation.getUuid());
            assertNull(obsAdapter.getObservationByUuid(DEFAULT_CONFIG_NAME, observation.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.voided);
        assertTrue(mrsListener.deleted);
    }

    @After
    public void tearDown() throws InterruptedException, PatientNotFoundException {

        if (observation != null) {
            deleteObservation(observation);
        }
        if (patient != null) {
            patientAdapter.deletePatient(DEFAULT_CONFIG_NAME, patient.getUuid());
        }
        if (location != null) {
            locationAdapter.deleteLocation(DEFAULT_CONFIG_NAME, location.getUuid());
        }
        if (person != null) {
            personAdapter.deletePerson(DEFAULT_CONFIG_NAME, person.getUuid());
        }
        if (concept != null) {
            conceptAdapter.deleteConcept(DEFAULT_CONFIG_NAME, concept.getUuid());
        }

        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private Observation prepareObservation() throws ConceptNameAlreadyInUseException {

        preparePerson();
        prepareLocation();
        preparePatient();
        prepareConcept();

        Observation observation = new Observation();
        observation.setObsDatetime(new Date());
        observation.setPerson(patient.getPerson());
        observation.setValue(new Observation.ObservationValue("true"));
        observation.setConcept(concept);

        return observation;
    }

    private Observation saveObservation(Observation observation) throws InterruptedException {

        Observation saved;

        synchronized (lock) {
            saved = obsAdapter.createObservation(DEFAULT_CONFIG_NAME, observation);
            lock.wait(60000);
        }

        assertNotNull(saved);

        return saved;
    }

    private void deleteObservation(Observation observation) throws InterruptedException {

        synchronized (lock) {
            obsAdapter.deleteObservation(DEFAULT_CONFIG_NAME, observation.getUuid());
            lock.wait(60000);
        }

        assertNull(obsAdapter.getObservationByUuid(DEFAULT_CONFIG_NAME, observation.getUuid()));
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private boolean deleted = false;
        private boolean voided = false;
        private Map<String, Object> eventParameters;

        public void handle(MotechEvent event) {

            if (event.getSubject().equals(EventKeys.CREATED_NEW_OBSERVATION_SUBJECT)) {
                created = true;
            } else if (event.getSubject().equals(EventKeys.DELETED_OBSERVATION_SUBJECT)) {
                deleted = true;
            } else if (event.getSubject().equals(EventKeys.VOIDED_OBSERVATION_SUBJECT)) {
                voided = true;
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

    private void preparePerson() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("FooFirstName");
        name.setFamilyName("FooLastName");
        person.setNames(Collections.singletonList(name));

        person.setGender("F");

        String uuid = personAdapter.createPerson(DEFAULT_CONFIG_NAME, person).getUuid();

        this.person = personAdapter.getPersonByUuid(DEFAULT_CONFIG_NAME, uuid);
    }

    private void prepareLocation() {
        Location tempLocation = new Location("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince");
        String locationUuid = locationAdapter.createLocation(DEFAULT_CONFIG_NAME, tempLocation).getUuid();
        location = locationAdapter.getLocationByUuid(DEFAULT_CONFIG_NAME, locationUuid);
    }

    private void preparePatient() {
        Patient tempPatient = new Patient();
        tempPatient.setLocationForMotechId(location);
        tempPatient.setPerson(person);
        tempPatient.setMotechId("666");
        String patientUuid = patientAdapter.createPatient(DEFAULT_CONFIG_NAME, tempPatient).getUuid();
        patient = patientAdapter.getPatientByUuid(DEFAULT_CONFIG_NAME, patientUuid);
    }

    private void prepareConcept() throws ConceptNameAlreadyInUseException {
        Concept tempConcept = new Concept();
        tempConcept.setNames(Arrays.asList(new ConceptName("FooConcept")));
        tempConcept.setDatatype(new Concept.DataType("Boolean"));
        tempConcept.setConceptClass(new Concept.ConceptClass("Test"));
        String conceptUuid = conceptAdapter.createConcept(DEFAULT_CONFIG_NAME, tempConcept).getUuid();
        concept = conceptAdapter.getConceptByUuid(DEFAULT_CONFIG_NAME, conceptUuid);
    }
}
