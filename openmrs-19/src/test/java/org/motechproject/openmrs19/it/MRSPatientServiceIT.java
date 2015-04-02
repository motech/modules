package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.OpenMRSAttribute;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSConceptName;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
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
    private OpenMRSFacilityService facilityAdapter;

    @Inject
    private OpenMRSPatientService patientAdapter;

    @Inject
    private OpenMRSPersonService personAdapter;

    @Inject
    private OpenMRSConceptService conceptAdapter;

    @Inject
    private EventListenerRegistryService eventListenerRegistry;

    MrsListener mrsListener;

    OpenMRSPatient patient;
    OpenMRSConcept causeOfDeath;

    @Before
    public void setUp() throws InterruptedException, ConceptNameAlreadyInUseException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_PATIENT_SUBJECT,
                EventKeys.UPDATED_PATIENT_SUBJECT, EventKeys.PATIENT_DECEASED_SUBJECT, EventKeys.DELETED_PATIENT_SUBJECT));

        String uuid = savePatient(preparePatient()).getPatientId();
        patient = patientAdapter.getPatient(uuid);
        prepareConceptOfDeath();
    }

    @Test
    public void shouldCreatePatient() {

        assertNotNull(patient.getMotechId());
        assertEquals(patient.getPatientId(), mrsListener.eventParameters.get(EventKeys.PATIENT_ID));
        assertEquals(patient.getPerson().getPersonId(), mrsListener.eventParameters.get(EventKeys.PERSON_ID));

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deceased);
        assertFalse(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldUpdatePatient() throws InterruptedException {

        final String newFirstName = "Changed Name";
        final String newAddress = "Changed Address";

        patient.getPerson().setFirstName(newFirstName);
        patient.getPerson().setAddress(newAddress);

        OpenMRSPatient updated;

        synchronized (lock) {
            updated = patientAdapter.updatePatient(patient);
            assertNotNull(updated);

            lock.wait(60000);
        }

        assertEquals("Changed Name", updated.getPerson().getFirstName());
        assertEquals("Changed Address", updated.getPerson().getAddress());

        assertEquals(updated.getPatientId(), mrsListener.eventParameters.get(EventKeys.PATIENT_ID));
        assertEquals(updated.getFacility().getFacilityId(), mrsListener.eventParameters.get(EventKeys.FACILITY_ID));
        assertEquals(updated.getPerson().getPersonId(), mrsListener.eventParameters.get(EventKeys.PERSON_ID));
        assertEquals(updated.getMotechId(), mrsListener.eventParameters.get(EventKeys.MOTECH_ID));

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deceased);
        assertTrue(mrsListener.updated);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldGetPatientByMotechId() {

        OpenMRSPatient fetched = patientAdapter.getPatientByMotechId(patient.getMotechId());

        assertNotNull(fetched);
        assertEquals(fetched, patient);
    }

    @Test
    public void shouldGetByUuid() {

        OpenMRSPatient fetched = patientAdapter.getPatient(patient.getPatientId());

        assertNotNull(fetched);
        assertEquals(fetched, patient);
    }

    @Test
    public void shouldSearchForPatient() throws InterruptedException, PatientNotFoundException {

        List<OpenMRSPatient> found = patientAdapter.search(patient.getPerson().getFirstName(), patient.getMotechId());

        assertEquals(patient.getPerson().getFirstName(), found.get(0).getPerson().getFirstName());
    }

    @Test
    public void shouldDeceasePerson() throws HttpException, PatientNotFoundException, InterruptedException {

        patientAdapter.deceasePatient(patient.getMotechId(), causeOfDeath, new Date(), null);
        OpenMRSPatient deceased = patientAdapter.getPatientByMotechId(patient.getMotechId());
        assertTrue(deceased.getPerson().isDead());
    }

    @Test
    public void shouldDeletePatient() throws PatientNotFoundException, InterruptedException {

        synchronized (lock) {
            patientAdapter.deletePatient(patient.getPatientId());
            assertNull(patientAdapter.getPatient(patient.getPatientId()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.updated);
        assertTrue(mrsListener.deleted);
        assertFalse(mrsListener.deceased);

        assertEquals(patient.getPatientId(), mrsListener.eventParameters.get(EventKeys.PATIENT_ID));
    }

    @After
    public void tearDown() throws InterruptedException, PatientNotFoundException {

        String uuid = patient.getFacility().getFacilityId();

        deletePatient(patient);

        if (uuid != null) {
            facilityAdapter.deleteFacility(uuid);
        }

        conceptAdapter.deleteConcept(causeOfDeath.getUuid());

        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private OpenMRSPatient preparePatient() {

        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName("John");
        person.setLastName("Smith");
        person.setAddress("10 Fifth Avenue");
        person.setBirthDateEstimated(false);
        person.setGender("M");

        OpenMRSAttribute attr = new OpenMRSAttribute("Birthplace", "Motech");
        List<OpenMRSAttribute> attributes = new ArrayList<>();
        attributes.add(attr);
        person.setAttributes(attributes);
        OpenMRSFacility facility = facilityAdapter.createFacility(new OpenMRSFacility("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince"));

        assertNotNull(facility);

        OpenMRSPatient patient = new OpenMRSPatient("602", person, facility);

        return patient;
    }

    private OpenMRSPatient savePatient(OpenMRSPatient patient) throws InterruptedException {

        OpenMRSPatient created;

        synchronized (lock) {
            created = patientAdapter.createPatient(patient);
            assertNotNull(created);

            lock.wait(60000);
        }

        return created;
    }

    private void deletePatient(OpenMRSPatient patient) throws PatientNotFoundException, InterruptedException {

        String facilityId = patient.getFacility().getFacilityId();

        patientAdapter.deletePatient(patient.getPatientId());
        assertNull(patientAdapter.getPatient(patient.getPatientId()));

        facilityAdapter.deleteFacility(facilityId);
    }

    private void prepareConceptOfDeath() throws ConceptNameAlreadyInUseException {
        OpenMRSConcept concept = new OpenMRSConcept();
        OpenMRSConceptName conceptName = new OpenMRSConceptName("FooConceptOne");

        concept.setNames(Arrays.asList(conceptName));
        concept.setDataType("TEXT");
        concept.setConceptClass("Test");

        String uuid =  conceptAdapter.createConcept(concept).getUuid();
        causeOfDeath = conceptAdapter.getConceptById(uuid);
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
