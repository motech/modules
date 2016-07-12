package org.motechproject.openmrs.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.config.domain.SettingsRecord;
import org.motechproject.config.mds.SettingsDataService;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.util.Order;
import org.motechproject.openmrs.domain.*;
import org.motechproject.openmrs.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs.exception.PatientNotFoundException;
import org.motechproject.openmrs.service.*;
import org.motechproject.openmrs.tasks.OpenMRSTasksNotifier;
import org.motechproject.openmrs.tasks.constants.Keys;
import org.motechproject.tasks.domain.mds.channel.Channel;
import org.motechproject.tasks.domain.mds.task.*;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;
import org.motechproject.tasks.repository.TaskActivitiesDataService;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.helper.ServiceRetriever;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.openmrs.tasks.OpenMRSActionProxyService.DEFAULT_LOCATION_NAME;
import static org.motechproject.openmrs.util.TestConstants.DEFAULT_CONFIG_NAME;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSTaskIntegrationBundleIT extends AbstractTaskBundleIT {

    private OpenMRSTasksNotifier openMRSTasksNotifier;

    @Inject
    private BundleContext bundleContext;

    @Inject
    private OpenMRSPatientService patientService;

    @Inject
    private OpenMRSEncounterService encounterService;

    @Inject
    private OpenMRSLocationService locationService;

    @Inject
    private SettingsDataService settingsDataService;

    @Inject
    private OpenMRSConceptService conceptService;

    @Inject
    private OpenMRSObservationService observationService;

    @Inject
    private OpenMRSProviderService providerService;

    @Inject
    private OpenMRSPersonService personService;

    @Inject
    private TaskActivitiesDataService taskActivitiesDataService;

    private Patient createdPatient;
    private Encounter createdEncounter;
    private Provider createdProvider;
    private Date providerDate;
    private EncounterType createdEncounterType;
    private Observation createdObservation;
    private Concept createdConcept;

    private static final String OPENMRS_CHANNEL_NAME = "org.motechproject.openmrs";
    private static final String OPENMRS_MODULE_NAME = "openMRS";
    private static final String MDS_CHANNEL_NAME = "org.motechproject.motech-platform-dataservices-entities";
    private static final String VERSION = "0.29.0.SNAPSHOT";
    private static final String TEST_INTERFACE = "org.motechproject.openmrs.tasks.OpenMRSActionProxyService";
    private static final String TRIGGER_SUBJECT = "mds.crud.serverconfig.SettingsRecord.CREATE";
    private static final String MOTECH_ID = "654";

    private static final Integer MAX_RETRIES_BEFORE_FAIL = 200;
    private static final Integer WAIT_TIME = 2000;

    @Override
    protected Collection<String> getAdditionalTestDependencies() {
        return Arrays.asList(
                "org.motechproject:motech-tasks-test-utils",
                "org.motechproject:motech-tasks",
                "commons-beanutils:commons-beanutils",
                "commons-fileupload:commons-fileupload",
                "org.motechproject:motech-platform-web-security",
                "org.motechproject:motech-platform-server-bundle",
                "org.openid4java:com.springsource.org.openid4java",
                "net.sourceforge.nekohtml:com.springsource.org.cyberneko.html",
                "org.springframework.security:spring-security-openid"
        );
    }

    @Before
    public void setUp() throws IOException, InterruptedException {
        createAdminUser();
        login();
        openMRSTasksNotifier = (OpenMRSTasksNotifier) ServiceRetriever.getWebAppContext(bundleContext, OPENMRS_CHANNEL_NAME).getBean("openMrsTasksNotifier");
        setUpSecurityContext("motech", "motech", "manageTasks", "manageOpenMRS");

        waitForChannel(OPENMRS_CHANNEL_NAME);
        Channel channel = findChannel(OPENMRS_CHANNEL_NAME);
        waitForChannel(MDS_CHANNEL_NAME);
        Channel mdsChannel = findChannel(MDS_CHANNEL_NAME);

        createdPatient = patientService.createPatient(DEFAULT_CONFIG_NAME, preparePatient());
    }

    @Test
    public void testOpenMRSPatientDataSourceAndCreatePatientAction() throws InterruptedException, IOException, PatientNotFoundException {
        createPatientTestData();

        Long taskID = createPatientTestTask();

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        deleteTask(taskID);

        assertTrue(checkIfPatientWasCreatedProperly());

        clearPatientData();
    }

    @Test
    public void testOpenMRSEncounterDataSourceAndCreateEncounterAction() throws InterruptedException, IOException, ParseException, ConceptNameAlreadyInUseException {
        createEncounterTestData();

        Long taskID = createEncounterTestTask();

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        deleteTask(taskID);

        assertTrue(checkIfEncounterWasCreatedProperly());

        clearEncounterData();
    }

    private Long createPatientTestTask() {

        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("CREATE SettingsRecord", "data-services", MDS_CHANNEL_NAME,
                VERSION, TRIGGER_SUBJECT, TRIGGER_SUBJECT);

        TaskActionInformation actionInformation = new TaskActionInformation("Create Patient [" + DEFAULT_CONFIG_NAME + "]", OPENMRS_CHANNEL_NAME,
                OPENMRS_CHANNEL_NAME, VERSION, TEST_INTERFACE, "createPatient");

        actionInformation.setSubject(String.format("createPatient.%s", DEFAULT_CONFIG_NAME));

        SortedSet<TaskConfigStep> taskConfigStepSortedSet = new TreeSet<>();
        taskConfigStepSortedSet.add(createPatientDataSource());
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addAll(taskConfigStepSortedSet);

        Map<String, String> values = new HashMap<>();
        values.put(Keys.BIRTH_DATE, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.birthdate}}");
        values.put(Keys.FAMILY_NAME, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.display}}");
        values.put(Keys.GENDER, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.gender}}");
        values.put(Keys.GIVEN_NAME, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.display}}");
        values.put(Keys.MOTECH_ID, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.motechId}}" + "taskCreated");
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        Task task = new Task("OpenMRSPatientTestTask", triggerInformation, Arrays.asList(actionInformation), taskConfig, true, true);
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        return task.getId();
    }

    private Long createEncounterTestTask() {
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("CREATE SettingsRecord", "data-services", MDS_CHANNEL_NAME,
                VERSION, TRIGGER_SUBJECT, TRIGGER_SUBJECT);

        TaskActionInformation actionInformation = new TaskActionInformation("Create Encounter [" + DEFAULT_CONFIG_NAME + "]", OPENMRS_CHANNEL_NAME,
                OPENMRS_CHANNEL_NAME, VERSION, TEST_INTERFACE, "createEncounter");

        actionInformation.setSubject(String.format("createEncounter.%s", DEFAULT_CONFIG_NAME));

        SortedSet<TaskConfigStep> taskConfigStepSortedSet = new TreeSet<>();
        taskConfigStepSortedSet.add(createEncounterDataSource());
        TaskConfig taskConfig = new TaskConfig( );
        taskConfig.addAll(taskConfigStepSortedSet);

        Map<String, String> values = new HashMap<>();
        values.put(Keys.PROVIDER_UUID, createdProvider.getUuid());
        values.put(Keys.PATIENT_UUID, "{{ad.openMRS.Encounter-" + DEFAULT_CONFIG_NAME + "#0.patient.uuid}}");
        values.put(Keys.ENCOUNTER_TYPE, "{{ad.openMRS.Encounter-" + DEFAULT_CONFIG_NAME + "#0.encounterType.name}}");
        values.put(Keys.ENCOUNTER_DATE, "{{ad.openMRS.Encounter-" + DEFAULT_CONFIG_NAME + "#0.encounterDatetime}}");
        values.put(Keys.LOCATION_NAME, "{{ad.openMRS.Encounter-" + DEFAULT_CONFIG_NAME + "#0.location.display}}");
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        Task task = new Task("OpenMRSEncounterTestTaskasdasdasd992", triggerInformation, Arrays.asList(actionInformation), taskConfig, true, true);
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        return task.getId();
    }

    private void clearPatientData() throws PatientNotFoundException {

        Patient patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, String.format("%staskCreated", MOTECH_ID));
        patientService.deletePatient(DEFAULT_CONFIG_NAME, patient.getUuid());
    }

    private void clearEncounterData() {
        Encounter encounter = encounterService.getLatestEncounterByPatientMotechId(DEFAULT_CONFIG_NAME, MOTECH_ID, createdEncounterType.getName());
        encounterService.deleteEncounter(DEFAULT_CONFIG_NAME, encounter.getUuid());
        encounterService.deleteEncounter(DEFAULT_CONFIG_NAME, createdEncounter.getUuid());
    }

    private void createPatientTestData() {
        createdPatient = patientService.createPatient(DEFAULT_CONFIG_NAME, preparePatient());
    }

    private void createEncounterTestData() throws ParseException, ConceptNameAlreadyInUseException {

        createdProvider = prepareProvider();
        createdEncounterType = prepareEncounterType();

        createdObservation = prepareObservations();

        Location location = locationService.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0);
        Encounter encounter = new Encounter(location, createdEncounterType, providerDate, createdPatient, Collections.singletonList(createdProvider.getPerson()),
                Collections.singletonList(createdObservation));

        createdEncounter = encounterService.createEncounter(DEFAULT_CONFIG_NAME, encounter);
    }

    private Patient preparePatient() {
        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("Jack");
        name.setFamilyName("Black");
        person.setNames(Collections.singletonList(name));

        Person.Address address = new Person.Address();
        address.setAddress1("10 Kickapoo");
        person.setAddresses(Collections.singletonList(address));

        person.setBirthdateEstimated(false);
        person.setGender("M");
        person.setBirthdate(new DateTime("1999-01-16T00:00:00Z").toDate());

        Location location = locationService.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0);

        assertNotNull(location);

        return new Patient(person, MOTECH_ID, location);
    }

    private Provider prepareProvider() {

        Person person = new Person();

        Person.Name name = new Person.Name();
        name.setGivenName("John");
        name.setFamilyName("Snow");
        person.setNames(Collections.singletonList(name));

        person.setGender("M");

        String personUUID = personService.createPerson(DEFAULT_CONFIG_NAME, person).getUuid();
        Provider provider = new Provider();
        provider.setIdentifier("KingOfTheNorth");
        provider.setPerson(personService.getPersonByUuid(DEFAULT_CONFIG_NAME, personUUID));

        String providerUUID = providerService.createProvider(DEFAULT_CONFIG_NAME, provider).getUuid();

        return providerService.getProviderByUuid(DEFAULT_CONFIG_NAME, providerUUID);
    }

    private Observation prepareObservations() throws ParseException, ConceptNameAlreadyInUseException {
        Observation tempObservation = new Observation();

        providerDate = prepareProviderDate();
        createdConcept = prepareConcept();

        tempObservation.setObsDatetime(providerDate);
        tempObservation.setConcept(createdConcept);
        tempObservation.setValue(new Observation.ObservationValue("true"));
        tempObservation.setPerson(createdPatient.getPerson());

        String observationUuid = observationService.createObservation(DEFAULT_CONFIG_NAME, tempObservation).getUuid();
        return observationService.getObservationByUuid(DEFAULT_CONFIG_NAME, observationUuid);
    }

    private EncounterType prepareEncounterType() {
        EncounterType tempEncounterType = new EncounterType("TestEncounterType");
        tempEncounterType.setDescription("TestDescription");

        String encounterUuid = encounterService.createEncounterType(DEFAULT_CONFIG_NAME, tempEncounterType).getUuid();
        return encounterService.getEncounterTypeByUuid(DEFAULT_CONFIG_NAME, encounterUuid);
    }

    private Concept prepareConcept() throws ConceptNameAlreadyInUseException {
        Concept tempConcept = new Concept();
        tempConcept.setNames(Arrays.asList(new ConceptName("FooConcept")));
        tempConcept.setDatatype(new Concept.DataType("Boolean"));
        tempConcept.setConceptClass(new Concept.ConceptClass("Test"));
        return conceptService.createConcept(DEFAULT_CONFIG_NAME, tempConcept);
    }

    private Date prepareProviderDate() throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.parse("2011-01-16T00:00:00Z");
    }

    private boolean waitForTaskExecution(Long taskID) throws InterruptedException {
        getLogger().info("testOpenMRSTasksIntegration starts waiting for task to execute");
        int retries = 0;
        while (retries < MAX_RETRIES_BEFORE_FAIL && !hasTaskExecuted(taskID)) {
            retries++;
            Thread.sleep(WAIT_TIME);
        }
        if (retries == MAX_RETRIES_BEFORE_FAIL) {
            getLogger().info("Task execution failed");
            return false;
        }
        getLogger().info("Task executed after " + retries + " retries, what took about "
                + (retries * WAIT_TIME) / 1000 + " seconds");
        return true;
    }

    private boolean hasTaskExecuted(Long taskID) {
        Set<TaskActivityType> activityTypes = new HashSet<>();
        activityTypes.add(TaskActivityType.SUCCESS);
        QueryParams queryParams = new QueryParams((Order) null);
        List<TaskActivity> taskActivities = taskActivitiesDataService.byTaskAndActivityTypes(taskID, activityTypes, queryParams);

        return taskActivities.size() == 1 ? true : false;
    }

    private DataSource createPatientDataSource() {
        List<Lookup> lookupList = new ArrayList<>();
        lookupList.add(new Lookup("openMRS.uuid", createdPatient.getUuid()));
        DataSource dataSource = new DataSource(OPENMRS_MODULE_NAME, new Long(4), new Long(0), "Patient-" + DEFAULT_CONFIG_NAME, "openMRS.lookup.uuid", lookupList, false);
        dataSource.setOrder(0);
        return dataSource;
    }

    private DataSource createEncounterDataSource() {
        List<Lookup> lookupList = new ArrayList<>();
        lookupList.add(new Lookup("openMRS.uuid", createdEncounter.getUuid()));
        DataSource dataSource = new DataSource(OPENMRS_MODULE_NAME, new Long(4), new Long(0), "Encounter-" + DEFAULT_CONFIG_NAME, "openMRS.lookup.uuid", lookupList, false);
        dataSource.setOrder(0);
        return dataSource;
    }

    private boolean checkIfPatientWasCreatedProperly() {
        Patient patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, String.format("%staskCreated", MOTECH_ID));
        if (patient.getPerson().getBirthdate().equals(createdPatient.getPerson().getBirthdate()) &&
                patient.getMotechId().equals(String.format("%staskCreated", MOTECH_ID)) &&
                patient.getPerson().getGender().equals(createdPatient.getPerson().getGender())) {
            return true;
        }
        return false;
    }

    private boolean checkIfEncounterWasCreatedProperly() {
        boolean found = false;
        List<Encounter> encounterList = encounterService.getEncountersByEncounterType(DEFAULT_CONFIG_NAME, MOTECH_ID, createdEncounterType.getName());
        for (Encounter encounter : encounterList) {
            if (encounter.getUuid() != createdEncounter.getUuid() &&
                    encounter.getPatient().getUuid().equals(createdEncounter.getPatient().getUuid()) &&
                    encounter.getEncounterType().getUuid().equals(createdEncounter.getEncounterType().getUuid())) {
                found = true;
            }
        }
        return encounterList.size() == 2 && found ? true : false;
    }

    private void activateTrigger() {
        settingsDataService.create(new SettingsRecord());
    }

    private void deleteTask(Long taskID) {
        getTaskService().deleteTask(taskID);
    }
}

