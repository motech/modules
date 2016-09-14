package org.motechproject.openmrs.it.version1_12;

import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.config.domain.SettingsRecord;
import org.motechproject.config.mds.SettingsDataService;
import org.motechproject.mds.query.QueryParams;
import org.motechproject.mds.util.Order;
import org.motechproject.openmrs.domain.Location;
import org.motechproject.openmrs.domain.Patient;
import org.motechproject.openmrs.domain.Person;
import org.motechproject.openmrs.domain.Program;
import org.motechproject.openmrs.domain.ProgramEnrollment;
import org.motechproject.openmrs.exception.PatientNotFoundException;
import org.motechproject.openmrs.service.OpenMRSLocationService;
import org.motechproject.openmrs.service.OpenMRSPatientService;
import org.motechproject.openmrs.service.OpenMRSProgramEnrollmentService;
import org.motechproject.openmrs.tasks.constants.Keys;
import org.motechproject.tasks.domain.mds.task.DataSource;
import org.motechproject.tasks.domain.mds.task.Lookup;
import org.motechproject.tasks.domain.mds.task.Task;
import org.motechproject.tasks.domain.mds.task.TaskActionInformation;
import org.motechproject.tasks.domain.mds.task.TaskActivity;
import org.motechproject.tasks.domain.enums.TaskActivityType;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
public class MRSTaskIntegrationBundle1_12IT extends AbstractTaskBundleIT {

    @Inject
    private OpenMRSProgramEnrollmentService programEnrollmentService;

    @Inject
    private OpenMRSPatientService patientService;

    @Inject
    private OpenMRSLocationService locationService;

    @Inject
    private SettingsDataService settingsDataService;

    @Inject
    private TaskActivitiesDataService taskActivitiesDataService;

    private Patient createdPatient;
    private ProgramEnrollment createdProgramEnrollment;

    private static final String OPENMRS_CHANNEL_NAME = "org.motechproject.openmrs";
    private static final String OPENMRS_MODULE_NAME = "openMRS";
    private static final String MDS_CHANNEL_NAME = "org.motechproject.motech-platform-dataservices-entities";
    private static final String VERSION = "0.29";
    private static final String TEST_INTERFACE = "org.motechproject.openmrs.tasks.OpenMRSActionProxyService";
    private static final String TRIGGER_SUBJECT = "mds.crud.serverconfig.SettingsRecord.CREATE";
    private static final String MOTECH_ID = "654";
    private static final String STATE_UUID = "4b812ac8-421c-470f-b4b7-88187cdbd2a5";

    private static final Integer MAX_RETRIES_BEFORE_FAIL = 20;
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

    @BeforeClass
    public static void initialize() throws IOException, InterruptedException {
        createAdminUser();
        login();
    }

    @Before
    public void setUp() throws InterruptedException {
        waitForChannel(OPENMRS_CHANNEL_NAME);
        waitForChannel(MDS_CHANNEL_NAME);

        createProgramEnrollmentTestData();
    }

    @After
    public void clear() throws PatientNotFoundException {
        List<ProgramEnrollment> programEnrollmentList = programEnrollmentService.getProgramEnrollmentByPatientUuid(DEFAULT_CONFIG_NAME, createdProgramEnrollment.getPatient().getUuid());
        for (ProgramEnrollment programEnrollment : programEnrollmentList) {
            programEnrollmentService.deleteProgramEnrollment(DEFAULT_CONFIG_NAME, programEnrollment.getUuid());
        }
        patientService.deletePatient(DEFAULT_CONFIG_NAME, createdPatient.getUuid());
    }

    @Test
    public void testOpenMRSProgramEnrollmentDataSourceAndCreateProgramEnrollmentActionWithActiveProgram() throws InterruptedException, IOException, PatientNotFoundException {
        Long taskID = createProgramEnrollmentTestTask("OpenMRSProgramEnrollmentTestTaskWithActiveProgram", true);

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        checkIfProgramEnrollmentWasCreatedProperly(true);
    }

    @Test
    public void testOpenMRSProgramEnrollmentDataSourceAndCreateProgramEnrollmentActionWithNotActiveProgram() throws InterruptedException, IOException, PatientNotFoundException {
        Long taskID = createProgramEnrollmentTestTask("OpenMRSProgramEnrollmentTestTaskWithNotActiveProgram", false);

        activateTrigger();

        // Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        checkIfProgramEnrollmentWasCreatedProperly(false);
    }

    @Test
    public void testOpenMRSUpdateProgramEnrollmentAction() throws InterruptedException {
        Long taskID = updateProgramEnrollmentTestTask();

        activateTrigger();

        //Give Tasks some time to process
        assertTrue(waitForTaskExecution(taskID));

        checkIfProgramEnrollmentWasUpdatedProperly();
    }

    private Long createProgramEnrollmentTestTask(String taskName, boolean isActiveProgram) {
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("CREATE SettingsRecord", "data-services", MDS_CHANNEL_NAME,
                VERSION, TRIGGER_SUBJECT, TRIGGER_SUBJECT);

        TaskActionInformation actionInformation = new TaskActionInformation("Create Program Enrollment [" + DEFAULT_CONFIG_NAME + "]", OPENMRS_CHANNEL_NAME,
                OPENMRS_CHANNEL_NAME, VERSION, TEST_INTERFACE, "createProgramEnrollment");

        actionInformation.setSubject(String.format("createProgramEnrollment.%s", DEFAULT_CONFIG_NAME));

        SortedSet<TaskConfigStep> taskConfigStepSortedSet = new TreeSet<>();
        taskConfigStepSortedSet.add(createProgramEnrollmentDataSource(isActiveProgram));
        TaskConfig taskConfig = new TaskConfig();
        taskConfig.addAll(taskConfigStepSortedSet);

        Map<String, String> values = new HashMap<>();
        values.put(Keys.PATIENT_UUID, "{{ad.openMRS.ProgramEnrollment-" + DEFAULT_CONFIG_NAME + "#0.firstObject.patient.uuid}}");
        values.put(Keys.PROGRAM_UUID, "{{ad.openMRS.ProgramEnrollment-" + DEFAULT_CONFIG_NAME + "#0.firstObject.program.uuid}}");
        values.put(Keys.DATE_ENROLLED, new DateTime("2010-01-16T00:00:00Z").toString());
        if(!isActiveProgram) {
            values.put(Keys.DATE_COMPLETED, new DateTime("2016-01-16T00:00:00Z").toString());
        }
        values.put(Keys.LOCATION_NAME, locationService.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0).toString());
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        Task task = new Task(taskName, triggerInformation, Collections.singletonList(actionInformation), taskConfig, true, true);
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        return task.getId();
    }

    private Long updateProgramEnrollmentTestTask() {
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("CREATE SettingsRecord", "data-services", MDS_CHANNEL_NAME,
                VERSION, TRIGGER_SUBJECT, TRIGGER_SUBJECT);

        TaskActionInformation actionInformation = new TaskActionInformation("Update Program Enrollment [" + DEFAULT_CONFIG_NAME + "]", OPENMRS_CHANNEL_NAME,
                OPENMRS_CHANNEL_NAME, VERSION, TEST_INTERFACE, "changeStateOfProgramEnrollment");

        actionInformation.setSubject(String.format("changeStateOfProgramEnrollment.%s", DEFAULT_CONFIG_NAME));

        Map<String, String> values = new HashMap<>();
        values.put(Keys.PROGRAM_ENROLLMENT_UUID, createdProgramEnrollment.getUuid());
        values.put(Keys.STATE_UUID, STATE_UUID);
        values.put(Keys.STATE_START_DATE, new DateTime("2015-01-16T00:00:00Z").toString());
        values.put(Keys.CONFIG_NAME, DEFAULT_CONFIG_NAME);
        actionInformation.setValues(values);

        Task task = new Task("OpenMRSUpdateProgramEnrollmentTestTask", triggerInformation, Collections.singletonList(actionInformation), null, true, true);
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());

        return task.getId();
    }

    private void createProgramEnrollmentTestData() {
        createdPatient = patientService.createPatient(DEFAULT_CONFIG_NAME, preparePatient(MOTECH_ID));

        createProgramEnrollment(createdPatient, new DateTime("2010-01-16T00:00:00Z"), null);
        createProgramEnrollment(createdPatient, new DateTime("2010-01-16T00:00:00Z"), new DateTime("2016-01-16T00:00:00Z"));

    }

    private void createProgramEnrollment(Patient patient, DateTime dateEnrolled, DateTime dateCompleted) {
        Program program = new Program();
        program.setUuid("187af646-373b-4459-8114-4724d7e07fd5");

        DateTime stateStartDate = new DateTime("2011-01-16T00:00:00Z");

        Location location = locationService.getLocations(DEFAULT_CONFIG_NAME, DEFAULT_LOCATION_NAME).get(0);

        Program.State state = new Program.State();
        state.setUuid("6ac1bb86-f7ef-438c-8ea6-33050caa350d");

        ProgramEnrollment.StateStatus stateStatus = new ProgramEnrollment.StateStatus();
        stateStatus.setState(state);
        stateStatus.setStartDate(stateStartDate.toDate());

        ProgramEnrollment programEnrollment = new ProgramEnrollment();
        programEnrollment.setProgram(program);
        programEnrollment.setPatient(patient);

        if (dateEnrolled != null) {
            programEnrollment.setDateEnrolled(dateEnrolled.toDate());
        }
        if (dateCompleted != null) {
            programEnrollment.setDateCompleted(dateCompleted.toDate());
        }

        programEnrollment.setLocation(location);
        programEnrollment.setStates(Collections.singletonList(stateStatus));

        createdProgramEnrollment = programEnrollmentService.createProgramEnrollment(DEFAULT_CONFIG_NAME, programEnrollment);
    }

    private Patient preparePatient(String motechId) {
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

        return new Patient(person, motechId, location);
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

    private DataSource createProgramEnrollmentDataSource(boolean isActive) {
        List<Lookup> lookupList = new ArrayList<>();
        lookupList.add(new Lookup("openMRS.patient.motechId", MOTECH_ID));
        lookupList.add(new Lookup("openMRS.programName", createdProgramEnrollment.getProgram().getName()));
        if(isActive) {
            lookupList.add(new Lookup("openMRS.activeProgramOnly", "true"));
        } else {
            lookupList.add(new Lookup("openMRS.activeProgramOnly", "false"));
        }
        
        DataSource dataSource = new DataSource(OPENMRS_MODULE_NAME, 4L, 0L, "ProgramEnrollment-" + DEFAULT_CONFIG_NAME, "openMRS.lookup.motechIdAndProgramName", lookupList, false);
        dataSource.setOrder(0);
        return dataSource;
    }

    private void checkIfProgramEnrollmentWasCreatedProperly(boolean isActive) {
        List<ProgramEnrollment> programEnrollmentList = programEnrollmentService
                .getProgramEnrollmentByPatientUuid(DEFAULT_CONFIG_NAME, createdProgramEnrollment.getPatient().getUuid());

        if (isActive) {
            assertEquals(3, programEnrollmentList.size());
        } else {
            assertEquals(4, programEnrollmentList.size());
        }

        for (ProgramEnrollment programEnrollment : programEnrollmentList) {
            if (isActive) {
                if (!createdProgramEnrollment.getUuid().equals(programEnrollment.getUuid()) && isProgramActive(programEnrollment)) {
                    assertEqualsProgramEnrollments(createdProgramEnrollment, programEnrollment);
                    Assert.assertNull(programEnrollment.getDateCompleted());
                }
            } else {
                if (!createdProgramEnrollment.getUuid().equals(programEnrollment.getUuid()) && !isProgramActive(programEnrollment)) {
                    assertEqualsProgramEnrollments(createdProgramEnrollment, programEnrollment);
                    assertEquals(createdProgramEnrollment.getDateCompleted(), programEnrollment.getDateCompleted());
                }
            }
        }
    }

    private void assertEqualsProgramEnrollments(ProgramEnrollment createdProgramEnrollment, ProgramEnrollment programEnrollment ) {
        assertEquals(createdProgramEnrollment.getPatient().getUuid(), programEnrollment.getPatient().getUuid());
        assertEquals(createdProgramEnrollment.getProgram().getUuid(), programEnrollment.getProgram().getUuid());
        assertEquals(createdProgramEnrollment.getDateEnrolled(), programEnrollment.getDateEnrolled());
    }
    private boolean isProgramActive(ProgramEnrollment programEnrollment) {
        boolean result = false;

        if(programEnrollment.getDateCompleted() == null) {
            result = true;
        }

        return result;
    }

    private void checkIfProgramEnrollmentWasUpdatedProperly() {
        List<ProgramEnrollment> programEnrollmentList = programEnrollmentService
                .getProgramEnrollmentByPatientUuid(DEFAULT_CONFIG_NAME, createdProgramEnrollment.getPatient().getUuid());

        for (ProgramEnrollment programEnrollment : programEnrollmentList) {
            if (createdProgramEnrollment.getUuid().equals(programEnrollment.getUuid())) {
                assertEquals(createdProgramEnrollment.getUuid(), programEnrollment.getUuid());

                ProgramEnrollment.StateStatus stateStatus = programEnrollment.getCurrentState();

                assertEquals(STATE_UUID, stateStatus.getState().getUuid());
                assertEquals(new DateTime("2015-01-16T00:00:00Z").toDate(), stateStatus.getStartDate());
            }
        }
    }

    private void activateTrigger() {
        settingsDataService.create(new SettingsRecord());
    }
}

