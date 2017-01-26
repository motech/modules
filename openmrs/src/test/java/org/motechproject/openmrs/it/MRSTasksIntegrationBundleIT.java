package org.motechproject.openmrs.it;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.config.domain.SettingsRecord;
import org.motechproject.config.mds.SettingsDataService;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.util.Order;
import org.motechproject.openmrs.domain.Attribute;
import org.motechproject.openmrs.domain.Encounter;
import org.motechproject.openmrs.domain.EncounterType;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.domain.Provider;
import org.motechproject.openmrs.domain.Visit;
import org.motechproject.openmrs.domain.VisitType;
import org.motechproject.openmrs.exception.ConceptNameAlreadyInUseException;
import org.motechproject.openmrs.exception.PatientNotFoundException;
import org.motechproject.openmrs.service.OpenMRSConceptService;
import org.motechproject.openmrs.service.OpenMRSEncounterService;
import org.motechproject.openmrs.service.OpenMRSLocationService;
import org.motechproject.openmrs.service.OpenMRSObservationService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.motechproject.openmrs.service.OpenMRSPersonService;
import org.motechproject.openmrs.service.OpenMRSProviderService;
import org.motechproject.openmrs.service.OpenMRSVisitService;
import org.motechproject.openmrs.tasks.constants.Keys;
import org.motechproject.tasks.domain.enums.TaskActivityType;
import org.motechproject.tasks.domain.mds.task.DataSource;
import org.motechproject.tasks.domain.mds.task.Lookup;
import org.motechproject.tasks.domain.mds.task.Task;
import org.motechproject.tasks.domain.mds.task.TaskActionInformation;
import org.motechproject.tasks.domain.mds.task.TaskActivity;
import org.motechproject.tasks.domain.mds.task.TaskConfig;
import org.motechproject.tasks.domain.mds.task.TaskConfigStep;
import org.motechproject.tasks.domain.mds.task.TaskTriggerInformation;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;
import org.motechproject.tasks.repository.TaskActivitiesDataService;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.motechproject.openmrs.tasks.OpenMRSActionProxyService.DEFAULT_LOCATION_NAME;
import static org.motechproject.openmrs.util.TestConstants.DEFAULT_CONFIG_NAME;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MRSTasksIntegrationBundleIT extends AbstractTaskBundleIT {

    @Inject
    private OpenMRSPatientService patientService;

    @Inject
    private OpenMRSEncounterService encounterService;

    @Inject
    private OpenMRSVisitService visitService;

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
    private EncounterType createdEncounterType;
    private String visitTypeUuid;

    private static final String OPENMRS_CHANNEL_NAME = "org.motechproject.openmrs";
    private static final String OPENMRS_MODULE_NAME = "openMRS";
    private static final String MDS_CHANNEL_NAME = "org.motechproject.motech-platform-dataservices-entities";
    private static final String VERSION = "0.29.0.SNAPSHOT";
    private static final String TEST_INTERFACE = "org.motechproject.openmrs.tasks.OpenMRSActionProxyService";
    private static final String TRIGGER_SUBJECT = "mds.crud.serverconfig.SettingsRecord.CREATE";
    private static final String MOTECH_ID = "654";
    private static final String TEST_MOTECH_ID = "333";
    private static final String TEST_OLD_ID = "testOldId";

    private static final Integer MAX_RETRIES_BEFORE_FAIL = 20;
    private static final Integer WAIT_TIME = 2000;
    private DateTime encounterDateTime = new DateTime("2012-01-16T00:00:00Z");
    private DateTime visitStartDateTime = new DateTime("2010-01-10T07:22:05Z");
    private DateTime visitStopDateTime = new DateTime("2014-08-01T07:22:05Z");

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

    @BeforeClass
    public static void initialize() throws IOException, InterruptedException {
        createAdminUser();
        login();
    }

    @Before
    public void setUp() throws IOException, InterruptedException, ParseException, ConceptNameAlreadyInUseException {
        setUpSecurityContext("motech", "motech", "manageTasks", "manageOpenMRS");

        waitForChannel(OPENMRS_CHANNEL_NAME);
        waitForChannel(MDS_CHANNEL_NAME);

        createPatientTestData();
        createEncounterTestData();
    }

    @After
    public void tearDown() throws PatientNotFoundException {
        List<Encounter> encounters = encounterService.getEncountersByEncounterType(DEFAULT_CONFIG_NAME, MOTECH_ID, createdEncounterType.getUuid());

        for (Encounter encounter : encounters) {
            encounterService.deleteEncounter(DEFAULT_CONFIG_NAME, encounter.getUuid());
        }

        encounterService.deleteEncounterType(DEFAULT_CONFIG_NAME, createdEncounterType.getUuid());

        providerService.deleteProvider(DEFAULT_CONFIG_NAME, createdProvider.getUuid());

        visitService.deleteVisit(DEFAULT_CONFIG_NAME, createdEncounter.getVisit().getUuid());

        visitService.deleteVisitType(DEFAULT_CONFIG_NAME, visitTypeUuid);

        patientService.deletePatient(DEFAULT_CONFIG_NAME, createdPatient.getUuid());

        Patient patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, String.format("%staskCreated", MOTECH_ID));
        if (patient != null) {
            patientService.deletePatient(DEFAULT_CONFIG_NAME, patient.getUuid());
        }

        patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, "John Smith");
        if (patient != null) {
            patientService.deletePatient(DEFAULT_CONFIG_NAME, patient.getUuid());
        }

        patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, "Jacob Lee");
        if (patient != null) {
            patientService.deletePatient(DEFAULT_CONFIG_NAME, patient.getUuid());
        }

        patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, TEST_MOTECH_ID);
        if (patient != null) {
            patientService.deletePatient(DEFAULT_CONFIG_NAME, patient.getUuid());
        }
    }

    @Test
    public void testOpenMRSPatientDataSourceAndCreatePatientAction() throws InterruptedException, IOException, PatientNotFoundException {
        updatePatientWithNewAttribute();

        Long taskID = createPatientTestTask();

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        checkIfPatientWasCreatedProperly();
    }

    @Test
    public void testOpenMRSEncounterDataSourceAndCreateEncounterAction() throws InterruptedException, IOException, ParseException, ConceptNameAlreadyInUseException {
        Long taskID = createEncounterTestTask();

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        checkIfEncounterWasCreatedProperly();
    }

    @Test
    public void testOpenMRSProviderDataSourceAndCreateEncounterAction() throws InterruptedException, IOException, ParseException, ConceptNameAlreadyInUseException {
        Long taskID = createProviderTestTask();

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        checkIfProviderWasCreatedProperly();
    }

    @Test
    public void testCreateEncounterPostActionParameter() throws InterruptedException {
        Task task = prepareCreateEncounterPostActionParameterTask();

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(task.getId()));

        Encounter encounter = encounterService.getLatestEncounterByPatientMotechId(DEFAULT_CONFIG_NAME, MOTECH_ID, createdEncounterType.getUuid());
        String firstEncounterUuid = encounter.getUuid();

        Patient patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, "Jacob Lee");
        Person.Address address = patient.getPerson().getPreferredAddress();

        assertEquals(firstEncounterUuid, address.getAddress1());
    }

    @Test
    public void testCreatePatientPostActionParameter() throws InterruptedException {
        Task task = prepareCreatePatientPostActionParameterTask();

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(task.getId()));

        Patient patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, "John Smith");
        String firstPatientUuid = patient.getUuid();

        patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, "Jacob Lee");
        Person.Address address = patient.getPerson().getPreferredAddress();

        assertEquals(firstPatientUuid, address.getAddress1());
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
        values.put(Keys.ADDRESS_1, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.birthdate}}");
        values.put(Keys.ADDRESS_2, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.uuid}}");
        values.put(Keys.ADDRESS_3, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.personAttributes.Birthplace}}");
        values.put(Keys.FAMILY_NAME, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.display}}");
        values.put(Keys.GENDER, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.gender}}");
        values.put(Keys.GIVEN_NAME, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.display}}");
        values.put(Keys.MOTECH_ID, "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.motechId}}" + "taskCreated");
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        values.put(Keys.IDENTIFIERS, "Old Identification Number:testOldId");
        actionInformation.setValues(values);

        Task task = new Task("OpenMRSPatientTestTask", triggerInformation, Collections.singletonList(actionInformation), taskConfig, true, true);
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
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addAll(taskConfigStepSortedSet);

        Map<String, String> values = new HashMap<>();
        values.put(Keys.PROVIDER_UUID, createdProvider.getUuid());
        values.put(Keys.PATIENT_UUID, "{{ad.openMRS.Encounter-" + DEFAULT_CONFIG_NAME + "#0.patient.uuid}}");
        values.put(Keys.ENCOUNTER_TYPE, "{{ad.openMRS.Encounter-" + DEFAULT_CONFIG_NAME + "#0.encounterType.name}}");
        values.put(Keys.ENCOUNTER_DATE, encounterDateTime.toString());
        values.put(Keys.LOCATION_NAME, "{{ad.openMRS.Encounter-" + DEFAULT_CONFIG_NAME + "#0.location.display}}");
        values.put(Keys.VISIT_UUID, "{{ad.openMRS.Encounter-" + DEFAULT_CONFIG_NAME + "#0.visit.uuid}}");
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        Task task = new Task("OpenMRSEncounterTestTask", triggerInformation, Collections.singletonList(actionInformation), taskConfig, true, true);
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        return task.getId();
    }

    private Long createProviderTestTask() {
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("CREATE SettingsRecord", "data-services", MDS_CHANNEL_NAME,
                VERSION, TRIGGER_SUBJECT, TRIGGER_SUBJECT);

        TaskActionInformation actionInformation = new TaskActionInformation("Create Patient [" + DEFAULT_CONFIG_NAME + "]", OPENMRS_CHANNEL_NAME,
                OPENMRS_CHANNEL_NAME, VERSION, TEST_INTERFACE, "createPatient");

        actionInformation.setSubject("validate");

        SortedSet<TaskConfigStep> taskConfigStepSortedSet = new TreeSet<>();
        taskConfigStepSortedSet.add(createProviderDataSource());
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addAll(taskConfigStepSortedSet);

        Map<String, String> values = new HashMap<>();
        values.put(Keys.GIVEN_NAME, "GivenName");
        values.put(Keys.FAMILY_NAME, "FamilyName");
        values.put(Keys.ADDRESS_1, "{{ad.openMRS.Provider-" + DEFAULT_CONFIG_NAME + "#0.uuid}}");
        values.put(Keys.ADDRESS_2, "{{ad.openMRS.Provider-" + DEFAULT_CONFIG_NAME + "#0.identifier}}");
        values.put(Keys.ADDRESS_3, "{{ad.openMRS.Provider-" + DEFAULT_CONFIG_NAME + "#0.person.uuid}}");
        values.put(Keys.GENDER, "M");
        values.put(Keys.MOTECH_ID, TEST_MOTECH_ID);
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        Task task = new Task("OpenMRSProviderTestTask", triggerInformation, Collections.singletonList(actionInformation), taskConfig, true, true);
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        return task.getId();
    }

    private void createPatientTestData() {
        createdPatient = patientService.createPatient(DEFAULT_CONFIG_NAME, preparePatient());
    }

    private void createEncounterTestData() throws ParseException, ConceptNameAlreadyInUseException {
        createdProvider = prepareProvider();
        createdEncounterType = prepareEncounterType();
        Visit createdVisit = prepareVisit();

        Location location = locationService.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0);
        Encounter encounter = new Encounter(location, createdEncounterType, encounterDateTime.toDate(),
                createdPatient, createdVisit, Collections.singletonList(createdProvider.getPerson()), null);

        createdEncounter = encounterService.createEncounter(DEFAULT_CONFIG_NAME, encounter);
    }

    private Task prepareCreateEncounterPostActionParameterTask() {
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("CREATE SettingsRecord", "data-services", MDS_CHANNEL_NAME,
                VERSION, TRIGGER_SUBJECT, TRIGGER_SUBJECT);

        ArrayList<TaskActionInformation> taskActions = new ArrayList();
        taskActions.add(prepareCreateEncounterActionInformation());
        taskActions.add(prepareCreatePatientActionInformation("{{pa.0.uuid}}", "Jacob Lee", false));

        Task task = new Task("OpenMRSEncounterPostActionParameterTestTask", triggerInformation, taskActions, new TaskConfig(), true, true);
        getTaskService().save(task);

        return task;
    }

    private Task prepareCreatePatientPostActionParameterTask(){
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("CREATE SettingsRecord", "data-services", MDS_CHANNEL_NAME,
                VERSION, TRIGGER_SUBJECT, TRIGGER_SUBJECT);

        SortedSet<TaskConfigStep> taskConfigStepSortedSet = new TreeSet<>();
        taskConfigStepSortedSet.add(createPatientDataSource());
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addAll(taskConfigStepSortedSet);

        ArrayList<TaskActionInformation> taskActions = new ArrayList();
        taskActions.add(prepareCreatePatientActionInformation("Wallstreet 15/2", "John Smith", true));
        taskActions.add(prepareCreatePatientActionInformation("{{pa.0.uuid}}", "Jacob Lee", true));

        Task task = new Task("OpenMRSPatientPostActionParameterTestTask", triggerInformation, taskActions, taskConfig, true, true);
        getTaskService().save(task);

        return task;
    }

    private TaskActionInformation prepareCreatePatientActionInformation(String address1, String motechId, boolean withPatientDataSourceBubbles) {
        TaskActionInformation actionInformation = new TaskActionInformation("Create Patient [" + DEFAULT_CONFIG_NAME + "]", OPENMRS_CHANNEL_NAME,
                OPENMRS_CHANNEL_NAME, VERSION, TEST_INTERFACE, "createPatient");
        actionInformation.setSubject(String.format("createPatient.%s", DEFAULT_CONFIG_NAME));

        Map<String, String> values = new HashMap<>();

        String familyName;
        String gender;
        String givenName;
        if (withPatientDataSourceBubbles) {
            familyName = "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.display}}";
            gender = "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.gender}}";
            givenName = "{{ad.openMRS.Patient-" + DEFAULT_CONFIG_NAME + "#0.person.display}}";
        } else {
            familyName = "Bond";
            gender = "M";
            givenName = "James";
        }
        values.put(Keys.ADDRESS_1, address1);
        values.put(Keys.FAMILY_NAME, familyName);
        values.put(Keys.GENDER, gender);
        values.put(Keys.GIVEN_NAME, givenName);
        values.put(Keys.MOTECH_ID, motechId);
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        return actionInformation;
    }

    private TaskActionInformation prepareCreateEncounterActionInformation(){
        TaskActionInformation actionInformation = new TaskActionInformation("Create Encounter [" + DEFAULT_CONFIG_NAME + "]", OPENMRS_CHANNEL_NAME,
                OPENMRS_CHANNEL_NAME, VERSION, TEST_INTERFACE, "createEncounter");
        actionInformation.setSubject(String.format("createEncounter.%s", DEFAULT_CONFIG_NAME));

        Map<String, String> values = new HashMap<>();
        values.put(Keys.PROVIDER_UUID, createdProvider.getUuid());
        values.put(Keys.PATIENT_UUID, createdPatient.getUuid());
        values.put(Keys.ENCOUNTER_TYPE, createdEncounterType.getUuid());
        values.put(Keys.ENCOUNTER_DATE, new DateTime("2015-01-16T00:00:00Z").toString());
        values.put(Keys.LOCATION_NAME, DEFAULT_LOCATION_NAME);
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        return actionInformation;
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

    private Visit prepareVisit() {
        VisitType tempVisitType = new VisitType();
        tempVisitType.setName("TestVisitType");

        visitTypeUuid = visitService.createVisitType(DEFAULT_CONFIG_NAME, tempVisitType).getUuid();
        tempVisitType = visitService.getVisitTypeByUuid(DEFAULT_CONFIG_NAME, visitTypeUuid);

        Visit tempVisit = new Visit();
        tempVisit.setPatient(createdPatient);
        tempVisit.setStartDatetime(visitStartDateTime.toDate());
        tempVisit.setStopDatetime(visitStopDateTime.toDate());
        tempVisit.setVisitType(tempVisitType);

        return visitService.createVisit(DEFAULT_CONFIG_NAME, tempVisit);
    }

    private void updatePatientWithNewAttribute() {

        Attribute.AttributeType attributeType = new Attribute.AttributeType();
        attributeType.setUuid("8d8718c2-c2cc-11de-8d13-0010c6dffd0f"); // this UUID indicates BirthPlace person Attribute on OpenMRS server

        Attribute personAttribute = new Attribute();
        personAttribute.setAttributeType(attributeType);
        personAttribute.setValue("New York");

        createdPatient.getPerson().getAttributes().add(personAttribute);

        patientService.updatePatient(DEFAULT_CONFIG_NAME, createdPatient);
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

    private EncounterType prepareEncounterType() {
        EncounterType tempEncounterType = new EncounterType("TestEncounterType", null);
        tempEncounterType.setDescription("TestDescription");

        String encounterUuid = encounterService.createEncounterType(DEFAULT_CONFIG_NAME, tempEncounterType).getUuid();
        return encounterService.getEncounterTypeByUuid(DEFAULT_CONFIG_NAME, encounterUuid);
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

        return taskActivities.size() == 1;
    }

    private DataSource createPatientDataSource() {
        List<Lookup> lookupList = new ArrayList<>();
        lookupList.add(new Lookup("openMRS.uuid", createdPatient.getUuid()));
        DataSource dataSource = new DataSource(OPENMRS_MODULE_NAME, 4L, 0L, "Patient-" + DEFAULT_CONFIG_NAME, "openMRS.lookup.uuid", lookupList, false);
        dataSource.setOrder(0);
        return dataSource;
    }

    private DataSource createEncounterDataSource() {
        List<Lookup> lookupList = new ArrayList<>();
        lookupList.add(new Lookup("openMRS.uuid", createdEncounter.getUuid()));
        DataSource dataSource = new DataSource(OPENMRS_MODULE_NAME, 4L, 0L, "Encounter-" + DEFAULT_CONFIG_NAME, "openMRS.lookup.uuid", lookupList, false);
        dataSource.setOrder(0);
        return dataSource;
    }

    private DataSource createProviderDataSource() {
        List<Lookup> lookupList = new ArrayList<>();
        lookupList.add(new Lookup("openMRS.uuid", createdProvider.getUuid()));
        DataSource dataSource = new DataSource(OPENMRS_MODULE_NAME, 4L, 0L, "Provider-" + DEFAULT_CONFIG_NAME, "openMRS.lookup.uuid", lookupList, false);
        dataSource.setOrder(0);
        return dataSource;
    }

    private void checkIfPatientWasCreatedProperly() {
        Patient patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, String.format("%staskCreated", MOTECH_ID));

        assertEquals(createdPatient.getUuid(), patient.getPerson().getPreferredAddress().getAddress2());
        assertEquals(createdPatient.getPerson().getBirthdate().toString(), patient.getPerson().getPreferredAddress().getAddress1());
        assertEquals(createdPatient.getPerson().getAttributes().get(0).getValue(), patient.getPerson().getPreferredAddress().getAddress3());
        assertEquals(String.format("%staskCreated", MOTECH_ID), patient.getMotechId());
        assertEquals(createdPatient.getPerson().getGender(), patient.getPerson().getGender());

        Person.Name actualName = patient.getPerson().getPreferredName();
        assertEquals(createdPatient.getPerson().getDisplay(), actualName.getFamilyName());
        assertEquals(createdPatient.getPerson().getDisplay(), actualName.getGivenName());

        assertNotNull(patient.getIdentifiers());
        assertEquals(1, patient.getIdentifiers().size());
        assertEquals(TEST_OLD_ID, patient.getIdentifiers().get(0).getIdentifier());
    }

    private void checkIfEncounterWasCreatedProperly() {
        List<Encounter> encounterList = encounterService.getEncountersByEncounterType(DEFAULT_CONFIG_NAME, MOTECH_ID, createdEncounterType.getUuid());

        assertEquals(2, encounterList.size());

        for (Encounter encounter : encounterList) {
            if (!Objects.equals(encounter.getUuid(), createdEncounter.getUuid())) {
                assertEquals(createdEncounter.getPatient().getUuid(), encounter.getPatient().getUuid());
                assertEquals(createdEncounter.getEncounterType().getUuid(), encounter.getEncounterType().getUuid());
                assertEquals(createdEncounter.getVisit().getUuid(), encounter.getVisit().getUuid());
                assertEquals(createdEncounter.getEncounterDatetime(), encounter.getEncounterDatetime());
            }
        }
    }

    private void checkIfProviderWasCreatedProperly() {
        Patient patient = patientService.getPatientByMotechId(DEFAULT_CONFIG_NAME, TEST_MOTECH_ID);

        Person.Address address = patient.getPerson().getPreferredAddress();

        assertEquals(createdProvider.getUuid(), address.getAddress1());
        assertEquals(createdProvider.getIdentifier(), address.getAddress2());
        assertEquals(createdProvider.getPerson().getUuid(), address.getAddress3());
    }

    private void activateTrigger() {
        settingsDataService.create(new SettingsRecord());
    }
}

