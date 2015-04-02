package org.motechproject.openmrs19.it;

import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListener;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.openmrs19.domain.OpenMRSConcept;
import org.motechproject.openmrs19.domain.OpenMRSConceptName;
import org.motechproject.openmrs19.domain.OpenMRSEncounter;
import org.motechproject.openmrs19.domain.OpenMRSEncounterType;
import org.motechproject.openmrs19.domain.OpenMRSFacility;
import org.motechproject.openmrs19.domain.OpenMRSObservation;
import org.motechproject.openmrs19.domain.OpenMRSPatient;
import org.motechproject.openmrs19.domain.OpenMRSPerson;
import org.motechproject.openmrs19.domain.OpenMRSProvider;
import org.motechproject.openmrs19.domain.OpenMRSUser;
import org.motechproject.openmrs19.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs19.exception.HttpException;
import org.motechproject.openmrs19.exception.PatientNotFoundException;
import org.motechproject.openmrs19.exception.UserAlreadyExistsException;
import org.motechproject.openmrs19.exception.UserDeleteException;
import org.motechproject.openmrs19.service.EventKeys;
import org.motechproject.openmrs19.service.OpenMRSConceptService;
import org.motechproject.openmrs19.service.OpenMRSEncounterService;
import org.motechproject.openmrs19.service.OpenMRSFacilityService;
import org.motechproject.openmrs19.service.OpenMRSObservationService;
import org.motechproject.openmrs19.service.OpenMRSPatientService;
import org.motechproject.openmrs19.service.OpenMRSPersonService;
import org.motechproject.openmrs19.service.OpenMRSProviderService;
import org.motechproject.openmrs19.service.OpenMRSUserService;
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
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

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
    private OpenMRSUserService userAdapter;

    @Inject
    private OpenMRSFacilityService facilityAdapter;

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

    OpenMRSPerson personOne;
    OpenMRSPerson personTwo;
    OpenMRSEncounterType encounterType;
    OpenMRSEncounter encounter;
    OpenMRSFacility facility;
    OpenMRSUser user;
    OpenMRSPatient patient;
    OpenMRSObservation observation;
    OpenMRSProvider provider;
    OpenMRSConcept concept;

    String date = "2012-09-05";

    @Before
    public void setUp() throws UserAlreadyExistsException, ParseException, ConceptNameAlreadyInUseException, InterruptedException {
        mrsListener = new MrsListener();
        eventListenerRegistry.registerListener(mrsListener, Arrays.asList(EventKeys.CREATED_NEW_ENCOUNTER_SUBJECT,
                EventKeys.DELETED_ENCOUNTER_SUBJECT));

        encounter = createEncounter(prepareEncounterOne());
    }

    @Test
    public void shouldCreateEncounter() throws UserAlreadyExistsException, HttpException, ParseException, InterruptedException {

        assertTrue(mrsListener.created);
        assertFalse(mrsListener.deleted);

        assertEquals(encounter.getEncounterId(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_ID));
        assertEquals(encounter.getProvider().getProviderId(), mrsListener.eventParameters.get(EventKeys.PROVIDER_ID));
        assertEquals(encounter.getCreator().getUserId(), mrsListener.eventParameters.get(EventKeys.USER_ID));
        assertEquals(encounter.getFacility().getFacilityId(), mrsListener.eventParameters.get(EventKeys.FACILITY_ID));
        assertEquals(encounter.getDate(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_DATE));
        assertEquals(encounter.getEncounterType(), mrsListener.eventParameters.get(EventKeys.ENCOUNTER_TYPE));
    }

    @Test
    public void shouldGetLatestEncounter() {
        OpenMRSEncounter encounter = encounterAdapter.getLatestEncounterByPatientMotechId(patient.getMotechId(), encounterType.getName());

        assertNotNull(encounter);
        assertEquals(new LocalDate("2012-09-05"), encounter.getDate().toLocalDate());
    }

    @Test
    public void shouldDeleteEncounter() throws InterruptedException {

        synchronized (lock) {
            encounterAdapter.deleteEncounter(encounter.getEncounterId());
            assertNull(encounterAdapter.getEncounterById(encounter.getEncounterId()));

            lock.wait(60000);
        }

        assertTrue(mrsListener.created);
        assertTrue(mrsListener.deleted);
    }

    @After
    public void tearDown() throws UserDeleteException, PatientNotFoundException, InterruptedException {

        if (encounter != null) {
            encounterAdapter.deleteEncounter(encounter.getEncounterId());
        }
        if (observation != null) {
            obsAdapter.deleteObservation(observation.getObservationId());
        }
        if (user != null) {
            userAdapter.deleteUser(user.getUserId());
        }
        if (provider != null) {
            providerService.deleteProvider(provider.getProviderId());
        }
        if (patient != null) {
            patientAdapter.deletePatient(patient.getPatientId());
        }
        if (facility != null) {
            facilityAdapter.deleteFacility(facility.getFacilityId());
        }
        if (concept != null) {
            conceptAdapter.deleteConcept(concept.getUuid());
        }
        if (encounterType != null) {
            encounterAdapter.deleteEncounterType(encounterType.getUuid());
        }
        if (personOne != null) {
            personAdapter.deletePerson(personOne.getPersonId());
        }
        if (personTwo != null) {
            personAdapter.deletePerson(personTwo.getPersonId());
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

    private OpenMRSEncounter prepareEncounterOne() throws UserAlreadyExistsException, ParseException, ConceptNameAlreadyInUseException {

        preparePersonOne();
        preparePersonTwo();
        prepareConcept();
        prepareUser();
        prepareProvider();
        prepareFacility();
        preparePatient();
        prepareEncounterType();
        prepareObservations();

        Set<OpenMRSObservation> observations = new HashSet<>();
        observations.add(observation);

        OpenMRSEncounter encounter = new OpenMRSEncounter.OpenMRSEncounterBuilder()
                .withEncounterType(encounterType.getName()).withDate(format.parse(date)).withCreator(user)
                .withFacility(facility).withObservations(observations).withPatient(patient).withProvider(provider)
                .build();

        return encounter;
    }

    private OpenMRSEncounter createEncounter(OpenMRSEncounter encounter) throws InterruptedException {

        OpenMRSEncounter saved;

        synchronized (lock) {
            saved = encounterAdapter.createEncounter(encounter);
            assertNotNull(saved);

            lock.wait(6000);
        }

        return saved;
    }

    private void prepareConcept() throws ConceptNameAlreadyInUseException {
        OpenMRSConcept tempConcept = new OpenMRSConcept();
        tempConcept.setNames(Arrays.asList(new OpenMRSConceptName("FooConcept")));
        tempConcept.setDataType("Boolean");
        tempConcept.setConceptClass("Test");
        concept = conceptAdapter.createConcept(tempConcept);
    }

    private void preparePersonOne() {
        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName("FooFirstName");
        person.setLastName("FooLastName");
        person.setGender("M");
        String personUuid = personAdapter.createPerson(person).getPersonId();
        personOne = personAdapter.getByUuid(personUuid);
    }

    private void preparePersonTwo() {
        OpenMRSPerson person = new OpenMRSPerson();
        person.setFirstName("FooNameTwo");
        person.setLastName("FooLastNameTwo");
        person.setGender("F");
        String personUuid = personAdapter.createPerson(person).getPersonId();
        personTwo = personAdapter.getByUuid(personUuid);
    }

    private void prepareUser() throws UserAlreadyExistsException {
        OpenMRSUser tempUser = new OpenMRSUser();
        tempUser.setUserName("FooUserName");
        tempUser.setPerson(personOne);
        tempUser.setSecurityRole("Provider");
        user = userAdapter.createUser(tempUser);
    }

    private void prepareProvider() {
        OpenMRSProvider tempProvider = new OpenMRSProvider();
        tempProvider.setPerson(personOne);
        tempProvider.setIdentifier("FooIdentifier");
        String providerUuid = providerService.createProvider(tempProvider).getProviderId();
        provider = providerService.getByUuid(providerUuid);
    }

    private void prepareFacility() {
        OpenMRSFacility tempFacility = new OpenMRSFacility("FooName", "FooCountry", "FooRegion", "FooCountryDistrict", "FooStateProvince");
        String facilityUuid = facilityAdapter.createFacility(tempFacility).getFacilityId();
        facility = facilityAdapter.getFacility(facilityUuid);
    }

    private void preparePatient() {
        OpenMRSPatient tempPatient = new OpenMRSPatient();
        tempPatient.setFacility(facility);
        tempPatient.setPerson(personTwo);
        tempPatient.setMotechId("666");
        String patientUuid = patientAdapter.createPatient(tempPatient).getPatientId();
        patient = patientAdapter.getPatient(patientUuid);
    }

    private void prepareEncounterType() {
        OpenMRSEncounterType tempEncounterType = new OpenMRSEncounterType();
        tempEncounterType.setName("FooType");
        tempEncounterType.setDescription("FooDescription");
        String encounterUuid = encounterAdapter.createEncounterType(tempEncounterType).getUuid();
        encounterType = encounterAdapter.getEncounterTypeByUuid(encounterUuid);
    }

    private void prepareObservations() throws ParseException {
        OpenMRSObservation tempObservation = new OpenMRSObservation<>(format.parse(date), concept.getDisplay(), "true");
        tempObservation.setPatientId(patient.getMotechId());
        String observationUuid = obsAdapter.createObservation(tempObservation).getObservationId();
        observation = obsAdapter.getObservationById(observationUuid);
    }
}
