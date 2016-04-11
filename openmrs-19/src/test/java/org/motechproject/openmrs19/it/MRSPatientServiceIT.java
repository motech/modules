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
import org.motechproject.openmrs19.domain.Patient;
import org.motechproject.openmrs19.domain.Person;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSLocationService;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSPatientServiceIT extends BasePaxIT {

    final Object lock = new Object();

    @Inject
    private OpenMRSLocationService facilityAdapter;

    @Inject
    private OpenMRSPatientService patientAdapter;

    @Inject
    private OpenMRSPersonService personAdapter;

    @Inject
    private OpenMRSConceptService conceptAdapter;

    @Inject
    private EventListenerRegistryService eventListenerRegistry;

    MrsListener mrsListener;

    Patient patient;
    Concept causeOfDeath;

    @Before
    public void setUp() throws InterruptedException, ConceptNameAlreadyInUseException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_PATIENT_SUBJECT,
                EventKeys.UPDATED_PATIENT_SUBJECT, EventKeys.PATIENT_DECEASED_SUBJECT, EventKeys.DELETED_PATIENT_SUBJECT));

        String uuid = savePatient(preparePatient()).getUuid();
        patient = patientAdapter.getPatientByUuid(uuid);
        prepareConceptOfDeath();
    }

    @Test
    public void shouldCreatePatient() {

        assertNotNull(patient.getMotechId());
        assertEquals(patient.getUuid(), mrsListener.eventParameters.get(EventKeys.PATIENT_ID));
        assertEquals(patient.getPerson().getUuid(), mrsListener.eventParameters.get(EventKeys.PERSON_ID));

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deceased);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldUpdatePatient() throws InterruptedException {

        final String newFirstName = "Changed Name";
        final String newAddress = "Changed Address";

        Person.Name name = patient.getPerson().getNames().get(0);
        name.setGivenName(newFirstName);

        Person.Address address = patient.getPerson().getAddresses().get(0);
        address.setAddress1(newAddress);

        synchronized (lock) {
            patientAdapter.updatePatient(patient);

            lock.wait(60000);
        }

        Patient updated = patientAdapter.getPatientByUuid(patient.getUuid());

        assertEquals("Changed Name", updated.getPerson().getPreferredName().getGivenName());
        assertEquals("Changed Address", updated.getPerson().getPreferredAddress().getAddress1());

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deceased);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldGetPatientByMotechId() {

        Patient fetched = patientAdapter.getPatientByMotechId(patient.getMotechId());

        assertNotNull(fetched);
        assertEquals(fetched, patient);
    }

    @Test
    public void shouldGetByUuid() {

        Patient fetched = patientAdapter.getPatientByUuid(patient.getUuid());

        assertNotNull(fetched);
        assertEquals(fetched, patient);
    }

    @Test
    public void shouldSearchForPatient() throws InterruptedException, PatientNotFoundException {

        List<Patient> found = patientAdapter.search(patient.getPerson().getPreferredName().getGivenName(), patient.getMotechId());

        assertEquals(patient.getPerson().getPreferredName().getGivenName(), found.get(0).getPerson().getPreferredName().getGivenName());
    }

    @Test
    public void shouldDeceasePerson() throws HttpException, PatientNotFoundException, InterruptedException {

        patientAdapter.deceasePatient(patient.getMotechId(), causeOfDeath, new Date(), null);
        Patient deceased = patientAdapter.getPatientByMotechId(patient.getMotechId());
        assertTrue(deceased.getPerson().getDead());
    }

    @Test
    public void shouldDeletePatient() throws PatientNotFoundException, InterruptedException {

        synchronized (lock) {
            patientAdapter.deletePatient(patient.getUuid());
            assertNull(patientAdapter.getPatientByUuid(patient.getUuid()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
        assertFalse(mrsListener.deceased);

        assertEquals(patient.getUuid(), mrsListener.eventParameters.get(EventKeys.PATIENT_ID));
    }

    @After
    public void tearDown() throws InterruptedException, PatientNotFoundException {

        String uuid = patient.getLocationForMotechId().getUuid();

        deletePatient(patient);

        if (uuid != null) {
            facilityAdapter.deleteLocation(uuid);
        }

        conceptAdapter.deleteConcept(causeOfDeath.getUuid());

        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private Patient preparePatient() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setFamilyName("Smith");
        person.setNames(Collections.singletonList(name));

        Person.Address address = new Person.Address();
        address.setAddress1("10 Fifth Avenue");
        person.setAddresses(Collections.singletonList(address));

        person.setBirthdateEstimated(false);
        person.setGender("M");

        Location location = facilityAdapter.createLocation(new Location("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince"));

        assertNotNull(location);

        return new Patient(person, "602", location);
    }

    private Patient savePatient(Patient patient) throws InterruptedException {

        Patient created;

        synchronized (lock) {
            created = patientAdapter.createPatient(patient);
            assertNotNull(created);

            lock.wait(60000);
        }

        return created;
    }

    private void deletePatient(Patient patient) throws PatientNotFoundException, InterruptedException {

        String facilityId = patient.getLocationForMotechId().getUuid();

        patientAdapter.deletePatient(patient.getUuid());
        assertNull(patientAdapter.getPatientByUuid(patient.getUuid()));

        facilityAdapter.deleteLocation(facilityId);
    }

    private void prepareConceptOfDeath() throws ConceptNameAlreadyInUseException {
        Concept concept = new Concept();
        ConceptName conceptName = new ConceptName("FooConceptOne");

        concept.setNames(Arrays.asList(conceptName));
        concept.setDatatype(new Concept.DataType("TEXT"));
        concept.setConceptClass(new Concept.ConceptClass("Test"));

        String uuid =  conceptAdapter.createConcept(concept).getUuid();
        causeOfDeath = conceptAdapter.getConceptByUuid(uuid);
    }

    public class MrsListener implements EventListener {

        private boolean created = false;
        private boolean updated = false;
        private boolean deceased = false;
        private boolean deleted = false;
        private Map<String, Object> eventParameters;

        public void handle(MotechEvent event) {
            if (event.getSubject().equals(EventKeys.CREATED_NEW_PATIENT_SUBJECT)) {
                created = true;
            } else if (event.getSubject().equals(EventKeys.UPDATED_PATIENT_SUBJECT)) {
                updated = true;
            } else if (event.getSubject().equals(EventKeys.PATIENT_DECEASED_SUBJECT)) {
                deceased = true;
            } else if (event.getSubject().equals(EventKeys.DELETED_PATIENT_SUBJECT)) {
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
}
