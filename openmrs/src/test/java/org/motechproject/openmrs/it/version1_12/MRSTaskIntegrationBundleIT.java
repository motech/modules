package org.motechproject.openmrs.it.version1_12;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.config.domain.SettingsRecord;
import org.motechproject.config.mds.SettingsDataService;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.util.Order;
import org.motechproject.openmrs.domain.*;
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
    private OpenMRSProgramEnrollmentService programEnrollmentService;

    @Inject
    private OpenMRSPatientService patientService;

    @Inject
    private OpenMRSEncounterService encounterService;

    @Inject
    private OpenMRSLocationService locationService;

    @Inject
    private OpenMRSProviderService providerService;

    @Inject
    private SettingsDataService settingsDataService;

    @Inject
    private OpenMRSPersonService personService;

    @Inject
    private OpenMRSObservationService observationService;

    @Inject
    private OpenMRSConceptService conceptService;

    @Inject
    private TaskActivitiesDataService taskActivitiesDataService;

    private Patient createdPatient;
    private ProgramEnrollment createdProgramEnrollment;

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
    public void testOpenMRSProgramEnrollmentDataSourceAndCreateProgramEnrollmentAction() throws InterruptedException, IOException {
        createProgramEnrollmentTestData();
        Long taskID = createProgramEnrollmentTestTask();

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        deleteTask(taskID);

        assertTrue(checkIfProgramEnrollmentWasCreatedProperly());
    }

    private Long createProgramEnrollmentTestTask() {
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("CREATE SettingsRecord", "data-services", MDS_CHANNEL_NAME,
                VERSION, TRIGGER_SUBJECT, TRIGGER_SUBJECT);

        TaskActionInformation actionInformation = new TaskActionInformation("Create Program Enrollment [" + DEFAULT_CONFIG_NAME + "]", OPENMRS_CHANNEL_NAME,
                OPENMRS_CHANNEL_NAME, VERSION, TEST_INTERFACE, "createProgramEnrollment");

        actionInformation.setSubject("validate");

        SortedSet<TaskConfigStep> taskConfigStepSortedSet = new TreeSet<>();
        taskConfigStepSortedSet.add(createProgramEnrollmentDataSource());
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addAll(taskConfigStepSortedSet);

        Map<String, String> values = new HashMap<>();
        values.put(Keys.PATIENT_UUID, "{{ad.openMRS.ProgramEnrollment-" + DEFAULT_CONFIG_NAME + "#0.patient.uuid}}");
        values.put(Keys.PROGRAM_UUID, "{{ad.openMRS.ProgramEnrollment-" + DEFAULT_CONFIG_NAME + "#0.program.uuid}}");
        values.put(Keys.DATE_ENROLLED, new DateTime("2010-01-16T00:00:00Z").toString());
        values.put(Keys.DATE_COMPLETED, new DateTime("2016-01-16T00:00:00Z").toString());
        values.put(Keys.LOCATION_NAME, locationService.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0).toString());
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        Task task = new Task("OpenMRSProgramEnrollmentTestTask1111", triggerInformation, Arrays.asList(actionInformation), taskConfig, true, true);
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        return task.getId();
    }

    private void createProgramEnrollmentTestData() {
        Program program = new Program();
        program.setUuid("187af646-373b-4459-8114-4724d7e07fd5");

        DateTime dateEnrolled = new DateTime("2010-01-16T00:00:00Z");
        DateTime dateCompleted = new DateTime("2016-01-16T00:00:00Z");
        DateTime stateStartDate = new DateTime("2011-01-16T00:00:00Z");

        Location location = locationService.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0);

        Program.State state = new Program.State();
        state.setUuid("6ac1bb86-f7ef-438c-8ea6-33050caa350d");

        ProgramEnrollment.StateStatus stateStatus = new ProgramEnrollment.StateStatus();
        stateStatus.setState(state);
        stateStatus.setStartDate(stateStartDate.toDate());

        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setProgram(program);
        programEnrollment.setPatient(createdPatient);
        programEnrollment.setDateEnrolled(dateEnrolled.toDate());
        programEnrollment.setDateCompleted(dateCompleted.toDate());
        programEnrollment.setLocation(location);
        programEnrollment.setStates(Collections.singletonList(stateStatus));

        createdProgramEnrollment = programEnrollmentService.createProgramEnrollment(DEFAULT_CONFIG_NAME, programEnrollment);
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

        person = personService.createPerson(DEFAULT_CONFIG_NAME, person);

        Provider provider = new Provider();
        provider.setIdentifier("KingOfTheNorth");
        provider.setPerson(person);

        return provider;
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

    private DataSource createProgramEnrollmentDataSource() {
        List<Lookup> lookupList = new ArrayList<>();
        lookupList.add(new Lookup("openMRS.patient.motechId", MOTECH_ID));
        lookupList.add(new Lookup("openMRS.programName", createdProgramEnrollment.getProgram().getName()));
        DataSource dataSource = new DataSource(OPENMRS_MODULE_NAME, new Long(4), new Long(0), "ProgramEnrollment-" + DEFAULT_CONFIG_NAME, "openMRS.lookup.motechIdAndProgramName", lookupList, false);
        dataSource.setOrder(0);
        return dataSource;
    }

    private boolean checkIfProgramEnrollmentWasCreatedProperly() {
        int programEnrollmentCount = 0;
        List<ProgramEnrollment> programEnrollmentList = programEnrollmentService.getProgramEnrollmentByPatientUuid(DEFAULT_CONFIG_NAME, createdProgramEnrollment.getPatient().getUuid());
        for (ProgramEnrollment programEnrollment : programEnrollmentList) {
            if (programEnrollment.getPatient().getUuid().equals(createdProgramEnrollment.getPatient().getUuid()) && programEnrollment.getProgram().getUuid().equals(createdProgramEnrollment.getProgram().getUuid())) {
                programEnrollmentCount++;
            }
        }
        return programEnrollmentCount == 2 ? true : false;
    }

    private void activateTrigger() {
        settingsDataService.create(new SettingsRecord());
    }

    private void deleteTask(Long taskID) {
        getTaskService().deleteTask(taskID);
    }
}

