package org.motechproject.openmrs19.it;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSConceptName;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSObservation;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.exception.ObservationNotFoundException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
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
import java.util.Date;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;


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
    private OpenMRSFacilityService facilityAdapter;

    @Inject
    EventListenerRegistryService eventListenerRegistry;

    MrsListener mrsListener;
    final Object lock = new Object();

    OpenMRSObservation observation;
    OpenMRSConcept concept;
    OpenMRSPerson person;
    OpenMRSFacility facility;
    OpenMRSPatient patient;

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

        OpenMRSObservation obs = obsAdapter.findObservation(patient.getMotechId(), concept.getDisplay());

        assertNotNull(obs);
    }

    @Test
    public void shouldFindListOfObservations() {

        List<OpenMRSObservation> obs = obsAdapter.findObservations(patient.getMotechId(), concept.getDisplay());

        assertNotNull(obs);
        assertTrue(obs.size() > 0);
    }

    @Test
    public void shouldVoidObservationWithoutReason() throws ObservationNotFoundException, InterruptedException {

        synchronized (lock) {
            obsAdapter.voidObservation(observation, null);
            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.voided);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldVoidObservationWithReason() throws ObservationNotFoundException, InterruptedException {

        synchronized (lock) {
            obsAdapter.voidObservation(observation, "FooReason");
            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.voided);
        assertFalse(mrsListener.deleted);
    }

    @Test
    public void shouldDeleteObservation() throws InterruptedException {

        synchronized (lock) {
            obsAdapter.deleteObservation(observation.getObservationId());
            assertNull(obsAdapter.getObservationByUuid(observation.getObservationId()));

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
            patientAdapter.deletePatient(patient.getPatientId());
        }
        if (facility != null) {
            facilityAdapter.deleteFacility(facility.getFacilityId());
        }
        if (person != null) {
            personAdapter.deletePerson(person.getPersonId());
        }
        if (concept != null) {
            conceptAdapter.deleteConcept(concept.getUuid());
        }

        eventListenerRegistry.clearListenersForBean("mrsTestListener");
    }

    private OpenMRSObservation prepareObservation() throws ConceptNameAlreadyInUseException {

        preparePerson();
        prepareFacility();
        preparePatient();
        prepareConcept();

        return new OpenMRSObservation<Boolean>(new Date(), concept.getDisplay(), patient.getMotechId(), true);
    }

    private OpenMRSObservation saveObservation(OpenMRSObservation observation) throws InterruptedException {

        OpenMRSObservation saved;

        synchronized (lock) {
            saved = obsAdapter.createObservation(observation);
            lock.wait(60000);
        }

        assertNotNull(saved);

        return saved;
    }

    private void deleteObservation(OpenMRSObservation observation) throws InterruptedException {

        synchronized (lock) {
            obsAdapter.deleteObservation(observation.getObservationId());
            lock.wait(60000);
        }

        assertNull(obsAdapter.getObservationByUuid(observation.getObservationId()));
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
        OpenMRSPerson person = new OpenMRSPerson();

        person.setFirstName("FooFirstName");
        person.setLastName("FooLastName");
        person.setGender("F");

        String uuid = personAdapter.createPerson(person).getPersonId();

        this.person = personAdapter.getPersonByUuid(uuid);
    }

    private void prepareFacility() {
        OpenMRSFacility tempFacility = new OpenMRSFacility("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince");
        String facilityUuid = facilityAdapter.createFacility(tempFacility).getFacilityId();
        facility = facilityAdapter.getFacilityByUuid(facilityUuid);
    }

    private void preparePatient() {
        OpenMRSPatient tempPatient = new OpenMRSPatient();
        tempPatient.setFacility(facility);
        tempPatient.setPerson(person);
        tempPatient.setMotechId("666");
        String patientUuid = patientAdapter.createPatient(tempPatient).getPatientId();
        patient = patientAdapter.getPatientByUuid(patientUuid);
    }

    private void prepareConcept() throws ConceptNameAlreadyInUseException {
        OpenMRSConcept tempConcept = new OpenMRSConcept();
        tempConcept.setNames(Arrays.asList(new OpenMRSConceptName("FooConcept")));
        tempConcept.setDataType("Boolean");
        tempConcept.setConceptClass("Test");
        String conceptUuid = conceptAdapter.createConcept(tempConcept).getUuid();
        concept = conceptAdapter.getConceptByUuid(conceptUuid);
    }
}
