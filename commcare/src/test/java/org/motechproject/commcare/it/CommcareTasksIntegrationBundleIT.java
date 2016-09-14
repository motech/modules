package org.motechproject.commcare.it;

import com.google.gson.Gson;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.events.constants.DisplayNames;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.tasks.CommcareTasksNotifier;
import org.motechproject.commcare.tasks.action.CommcareValidatingChannel;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commcare.util.DummyCommcareSchema;
import org.motechproject.commcare.util.ResponseXML;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.contract.builder.ActionEventRequestBuilder;
import org.motechproject.tasks.contract.builder.ActionParameterRequestBuilder;
import org.motechproject.tasks.domain.enums.ParameterType;
import org.motechproject.tasks.domain.mds.channel.ActionEvent;
import org.motechproject.tasks.domain.mds.channel.ActionParameter;
import org.motechproject.tasks.domain.mds.channel.Channel;
import org.motechproject.tasks.domain.mds.channel.EventParameter;
import org.motechproject.tasks.domain.mds.channel.TriggerEvent;
import org.motechproject.tasks.domain.mds.channel.builder.ActionEventBuilder;
import org.motechproject.tasks.domain.mds.channel.builder.ActionParameterBuilder;
import org.motechproject.tasks.domain.mds.task.Task;
import org.motechproject.tasks.domain.mds.task.TaskActionInformation;
import org.motechproject.tasks.domain.mds.task.TaskTriggerInformation;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.helper.ServiceRetriever;
import org.motechproject.testing.utils.TestContext;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import static org.apache.commons.lang.StringUtils.equalsIgnoreCase;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CommcareTasksIntegrationBundleIT extends AbstractTaskBundleIT {

    private static final int PORT = TestContext.getJettyPort();

    @Inject
    private CommcareApplicationDataService applicationDataService;

    @Inject
    private BundleContext bundleContext;

    @Inject
    private CommcareValidatingChannel validatingChannel;

    private CommcareTasksNotifier commcareTasksNotifier;

    private static final String COMMCARE_CHANNEL_NAME = "org.motechproject.commcare";
    private static final String TEST_INTERFACE = "org.motechproject.commcare.tasks.action.CommcareValidatingChannel";
    private static final String VERSION = "0.25";
    private static final String COMMCARE_ACTION_PROXY_SERVICE = "org.motechproject.commcare.tasks.CommcareActionProxyService";

    private static final Integer MAX_RETRIES_BEFORE_FAIL = 20;
    private static final Integer WAIT_TIME = 2000;

    private Config config;

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
        clearDB();
        createAdminUser();
        login();
        commcareTasksNotifier = (CommcareTasksNotifier) ServiceRetriever.getWebAppContext(bundleContext, COMMCARE_CHANNEL_NAME).getBean("commcareTasksNotifier");
        setUpSecurityContext("motech", "motech", "manageTasks", "manageCommcare");
    }

    @After
    public void tearDown() {
        clearDB();
    }

    @Test
    public void testCommcareTasksIntegration() throws InterruptedException, IOException {

        config = ConfigsUtils.prepareConfigOne();
        HttpResponse configurationResponse = createConfiguration(config);
        assertEquals(HttpStatus.SC_OK, configurationResponse.getStatusLine().getStatusCode());

        configurationResponse = updateConfiguration(config, config.getName());
        assertEquals(HttpStatus.SC_OK, configurationResponse.getStatusLine().getStatusCode());

        createMockCommcareSchema();
        commcareTasksNotifier.updateTasksInfo();

        waitForChannel(COMMCARE_CHANNEL_NAME);
        Channel channel = findChannel(COMMCARE_CHANNEL_NAME);

        verifyCommcareChannelHasCorrectActionsAndTriggers(channel);
        createDummyActionChannel(channel);
        createTestTask();

        HttpResponse response = sendMockForm();

        // Make sure that Commcare controller returned 200 after handling the form
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        // Give Tasks some time to process
        waitForTaskExecution();
        // Ask our OSGi service, which acts as Task action, to verify that correct values were received
        assertTrue(validatingChannel.verify());
    }

    private void createDummyActionChannel(Channel channel) {
        ActionParameterRequest actionParameterRequest = new ActionParameterRequestBuilder().setKey(ResponseXML.ATTR1)
                .setDisplayName(ResponseXML.ATTR1).setOrder(0).createActionParameterRequest();
        ActionParameterRequest actionParameterRequest2 = new ActionParameterRequestBuilder().setKey(ResponseXML.ATTR2)
                .setDisplayName(ResponseXML.ATTR2).setOrder(1).createActionParameterRequest();
        ActionParameterRequest actionParameterRequest3 = new ActionParameterRequestBuilder().setKey("caseId")
                .setDisplayName(ResponseXML.CASE_ID).setOrder(2).createActionParameterRequest();
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<>();
        actionParameterRequests.add(actionParameterRequest);
        actionParameterRequests.add(actionParameterRequest2);
        actionParameterRequests.add(actionParameterRequest3);

        ActionEventRequest actionEventRequest = new ActionEventRequestBuilder().setDisplayName("dummyAction")
                .setSubject("validate").setDescription(null).setServiceInterface(TEST_INTERFACE)
                .setServiceMethod("execute").setActionParameters(actionParameterRequests).createActionEventRequest();

        channel.addActionTaskEvent(ActionEventBuilder.fromActionEventRequest(actionEventRequest).build());
        getChannelService().addOrUpdate(channel);
    }

    private void createTestTask() {
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("trigger", COMMCARE_CHANNEL_NAME, COMMCARE_CHANNEL_NAME, VERSION,
                "org.motechproject.commcare.api.forms." + config.getName() + "." + DummyCommcareSchema.XMLNS1, "org.motechproject.commcare.api.forms");

        TaskActionInformation actionInformation = new TaskActionInformation("action", COMMCARE_CHANNEL_NAME, COMMCARE_CHANNEL_NAME, VERSION,
                TEST_INTERFACE, "execute");
        actionInformation.setSubject("validate");

        Map<String, String> values = new HashMap<>();
        values.put(ResponseXML.ATTR1, "{{trigger./data/pregnant}}");
        values.put(ResponseXML.ATTR2, "{{trigger./data/dob}}");
        values.put("caseId", "{{trigger.caseId}}");
        actionInformation.setValues(values);

        Task task = new Task("CommcareTestTask", triggerInformation, Arrays.asList(actionInformation));
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());
    }

    private void verifyCommcareChannelHasCorrectActionsAndTriggers(Channel channel) {
        List<TriggerEvent> triggerEvents = channel.getTriggerTaskEvents();
        List<ActionEvent> actionEvents = channel.getActionTaskEvents();

        assertEquals(8, actionEvents.size());
        assertEquals(11, triggerEvents.size());

        TaskTriggerInformation expectedForm1 = new TaskTriggerInformation();
        TaskTriggerInformation expectedForm2 = new TaskTriggerInformation();
        TaskTriggerInformation expectedForm3 = new TaskTriggerInformation();
        TaskTriggerInformation expectedForm4 = new TaskTriggerInformation();
        TaskTriggerInformation expectedCaseBirth = new TaskTriggerInformation();
        TaskTriggerInformation expectedCaseAppointment = new TaskTriggerInformation();
        TaskTriggerInformation expectedCaseDeath = new TaskTriggerInformation();
        TaskTriggerInformation expectedStockTx = new TaskTriggerInformation();

        expectedForm1.setSubject("org.motechproject.commcare.api.forms." + config.getName() + "." + DummyCommcareSchema.XMLNS1);
        assertTrue(containsTrigger(channel, expectedForm1));

        expectedForm2.setSubject("org.motechproject.commcare.api.forms." + config.getName() + "." + DummyCommcareSchema.XMLNS2);
        assertTrue(containsTrigger(channel, expectedForm2));

        expectedForm3.setSubject("org.motechproject.commcare.api.forms." + config.getName() + "." + DummyCommcareSchema.XMLNS3);
        assertTrue(containsTrigger(channel, expectedForm3));

        expectedForm4.setSubject("org.motechproject.commcare.api.forms." + config.getName() + "." + DummyCommcareSchema.XMLNS4);
        assertTrue(containsTrigger(channel, expectedForm4));

        expectedCaseBirth.setSubject("org.motechproject.commcare.api.case." + config.getName() + ".birth");
        assertTrue(containsTrigger(channel, expectedCaseBirth));

        expectedCaseAppointment.setSubject("org.motechproject.commcare.api.case." + config.getName() + ".appointment");
        assertTrue(containsTrigger(channel, expectedCaseAppointment));

        expectedCaseDeath.setSubject("org.motechproject.commcare.api.case." + config.getName() + ".death");
        assertTrue(containsTrigger(channel, expectedCaseDeath));

        expectedStockTx.setSubject(EventSubjects.RECEIVED_STOCK_TRANSACTION + '.' + config.getName());
        assertTrue(containsTrigger(channel, expectedStockTx));

        TriggerEvent form1Trigger = getTrigger(channel, expectedForm1);
        assertEquals("org.motechproject.commcare.api.forms", form1Trigger.getTriggerListenerSubject());
        assertTriggerParameters(form1Trigger.getEventParameters(),
                Arrays.asList("/data/pregnant", "/data/dob", "/data/meta/username", "caseId"));

        TriggerEvent form2Trigger = getTrigger(channel, expectedForm2);
        assertEquals("org.motechproject.commcare.api.forms", form2Trigger.getTriggerListenerSubject());
        assertTriggerParameters(form2Trigger.getEventParameters(),
                Arrays.asList("/data/patient_name", "/data/meta/username", "/data/case/create/case_type"));

        TriggerEvent form3Trigger = getTrigger(channel, expectedForm3);
        assertEquals("org.motechproject.commcare.api.forms", form3Trigger.getTriggerListenerSubject());
        assertTriggerParameters(form3Trigger.getEventParameters(),
                Arrays.asList("/data/last_visit", "/data/meta/username", "/data/case/create/case_type"));

        TriggerEvent form4Trigger = getTrigger(channel, expectedForm4);
        assertEquals("org.motechproject.commcare.api.forms", form4Trigger.getTriggerListenerSubject());
        assertTriggerParameters(form4Trigger.getEventParameters(),
                Arrays.asList("/data/medications", "/data/meta/username", "/data/case/create/case_type"));

        TriggerEvent caseBirthTrigger = getTrigger(channel, expectedCaseBirth);
        assertEquals("org.motechproject.commcare.api.case", caseBirthTrigger.getTriggerListenerSubject());
        assertTriggerParameters(caseBirthTrigger.getEventParameters(),
                Arrays.asList("motherName", "childName", "dob", "caseName"));

        TriggerEvent caseAppointmentTrigger = getTrigger(channel, expectedCaseAppointment);
        assertEquals("org.motechproject.commcare.api.case", caseAppointmentTrigger.getTriggerListenerSubject());
        assertTriggerParameters(caseAppointmentTrigger.getEventParameters(),
                Arrays.asList("visitDate", "isPregnant", "caseName"));

        TriggerEvent caseDeathTrigger = getTrigger(channel, expectedCaseDeath);
        assertEquals("org.motechproject.commcare.api.case", caseDeathTrigger.getTriggerListenerSubject());
        assertTriggerParameters(caseDeathTrigger.getEventParameters(),
                Arrays.asList("dod", "caseName"));

        verifyTaskAction(channel, prepareStockLedgerAction());
        verifyTaskAction(channel, prepareCreateCaseAction());
        verifyTaskAction(channel, prepareUpdateCaseAction());
        verifyTaskAction(channel, prepareSubmitForm1Action());
        verifyTaskAction(channel, prepareSubmitForm2Action());
        verifyTaskAction(channel, prepareSubmitForm3Action());
        verifyTaskAction(channel, prepareSubmitForm4Action());
    }

    private void verifyTaskAction(Channel channel, ActionEvent expected) {
        TaskActionInformation expectedAction = new TaskActionInformation();
        expectedAction.setSubject(expected.getSubject());
        assertTrue(channel.containsAction(expectedAction));
        ActionEvent actual = channel.getAction(expectedAction);
        assertEquals(expected, actual);
    }

    private void assertTriggerParameters(List<EventParameter> eventParameters, List<String> expected) {
        outer:
        for(String expectedParameter : expected) {
            for (EventParameter eventParameter : eventParameters) {
                if (expectedParameter.equals(eventParameter.getEventKey())) {
                    continue outer;
                }
            }
            fail("Parameter "  + expectedParameter + " has not been found in the list of event parameters.");
        }
    }

    private void createMockCommcareSchema() {
        CommcareApplicationJson applicationJson1 = DummyCommcareSchema.getApplicationsForConfigOne().get(0);
        applicationJson1.setCommcareAppId("123");
        applicationJson1.setApplicationName("TestApp1");
        applicationJson1.setResourceUri("none");
        applicationJson1.setConfigName(config.getName());

        CommcareApplicationJson applicationJson2 = DummyCommcareSchema.getApplicationsForConfigOne().get(1);
        applicationJson2.setCommcareAppId("124");
        applicationJson2.setApplicationName("TestApp2");
        applicationJson2.setResourceUri("none");
        applicationJson2.setConfigName(config.getName());

        applicationDataService.create(applicationJson1);
        applicationDataService.create(applicationJson2);
    }

    private HttpResponse sendMockForm() throws IOException, InterruptedException {
        HttpPost httpPost = new HttpPost(String.format("http://localhost:%d/commcare/forms/%s", PORT, config.getName()));
        HttpEntity body = new ByteArrayEntity(ResponseXML.getFormXML().getBytes("UTF-8"));
        httpPost.setEntity(body);
        return getHttpClient().execute(httpPost);
    }

    private HttpResponse createConfiguration(Config config) throws IOException, InterruptedException {
        return updateConfiguration(config, null);
    }

    private HttpResponse updateConfiguration(Config config, String oldName) throws IOException, InterruptedException {
        HttpPost httpPost;
        if(oldName == null) {
            httpPost = new HttpPost(String.format("http://localhost:%d/commcare/configs", PORT));
        } else {
            httpPost = new HttpPost(String.format("http://localhost:%d/commcare/configs?oldName=%s", PORT, oldName));
        }

        httpPost.addHeader("content-type", "application/json");
        httpPost.addHeader("Authorization", "Basic " + DatatypeConverter.printBase64Binary("motech:motech".getBytes("UTF-8")).trim());

        Gson gson = new Gson();

        httpPost.setEntity(new StringEntity(gson.toJson(config)));

        return getHttpClient().execute(httpPost);
    }

    private void waitForTaskExecution() throws InterruptedException {
        getLogger().info("testCommcareTasksIntegration starts waiting for task to execute");
        int retries = 0;
        while (retries < MAX_RETRIES_BEFORE_FAIL && !validatingChannel.hasExecuted()) {
            retries++;
            Thread.sleep(WAIT_TIME);
        }
        getLogger().info("Task executed after " + retries + " retries, what took about "
                + (retries * WAIT_TIME) / 1000 + " seconds");
    }

    private ActionEvent prepareStockLedgerAction() {
        String serviceMethod = "queryStockLedger";

        SortedSet<ActionParameter> parameters = new TreeSet<>();
        ActionParameterBuilder builder;
        int order = 0;

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CONFIG_NAME)
                .setKey(EventDataKeys.CONFIG_NAME)
                .setValue(ConfigsUtils.prepareConfigOne().getName())
                .setRequired(true)
                .setHidden(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CASE_ID)
                .setKey(EventDataKeys.CASE_ID)
                .setType(ParameterType.UNICODE)
                .setRequired(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.SECTION_ID)
                .setKey(EventDataKeys.SECTION_ID)
                .setType(ParameterType.UNICODE)
                .setRequired(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.START_DATE)
                .setKey(EventDataKeys.START_DATE)
                .setType(ParameterType.DATE)
                .setRequired(false)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.END_DATE)
                .setKey(EventDataKeys.END_DATE)
                .setType(ParameterType.DATE)
                .setRequired(false)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.EXTRA_DATA)
                .setKey(EventDataKeys.EXTRA_DATA)
                .setType(ParameterType.MAP)
                .setRequired(false)
                .setOrder(order);
        parameters.add(builder.build());

        String displayName = String.format("Query Stock Ledger [%s]", config.getName());

        ActionEventBuilder actionBuilder = new ActionEventBuilder()
                .setDisplayName(displayName)
                .setServiceInterface(COMMCARE_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(EventSubjects.QUERY_STOCK_LEDGER + "." + config.getName())
                .setActionParameters(parameters);
        return actionBuilder.build();
    }

    private ActionEvent prepareCreateCaseAction() {
        String serviceMethod = "createCase";

        SortedSet<ActionParameter> parameters = new TreeSet<>();
        ActionParameterBuilder builder;
        int order = 0;

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CONFIG_NAME)
                .setKey(EventDataKeys.CONFIG_NAME)
                .setValue(ConfigsUtils.prepareConfigOne().getName())
                .setRequired(true)
                .setHidden(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CASE_TYPE)
                .setKey(EventDataKeys.CASE_TYPE)
                .setType(ParameterType.UNICODE)
                .setRequired(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.OWNER_ID)
                .setKey(EventDataKeys.OWNER_ID)
                .setType(ParameterType.UNICODE)
                .setRequired(false)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CASE_NAME)
                .setKey(EventDataKeys.CASE_NAME)
                .setType(ParameterType.UNICODE)
                .setRequired(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CASE_PROPERTIES)
                .setKey(EventDataKeys.FIELD_VALUES)
                .setType(ParameterType.MAP)
                .setRequired(false)
                .setOrder(order++);
        parameters.add(builder.build());

        String displayName = String.format("Create Case [%s]", config.getName());

        ActionEventBuilder actionBuilder = new ActionEventBuilder()
                .setDisplayName(displayName)
                .setServiceInterface(COMMCARE_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(EventSubjects.CREATE_CASE + "." + config.getName())
                .setActionParameters(parameters);
        return actionBuilder.build();
    }

    private ActionEvent prepareUpdateCaseAction() {
        String serviceMethod = "updateCase";

        SortedSet<ActionParameter> parameters = new TreeSet<>();
        ActionParameterBuilder builder;
        int order = 0;

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CONFIG_NAME)
                .setKey(EventDataKeys.CONFIG_NAME)
                .setValue(ConfigsUtils.prepareConfigOne().getName())
                .setRequired(true)
                .setHidden(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CASE_ID)
                .setKey(EventDataKeys.CASE_ID)
                .setType(ParameterType.UNICODE)
                .setRequired(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.OWNER_ID)
                .setKey(EventDataKeys.OWNER_ID)
                .setType(ParameterType.UNICODE)
                .setRequired(true)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CLOSE_CASE)
                .setKey(EventDataKeys.CLOSE_CASE)
                .setType(ParameterType.BOOLEAN)
                .setRequired(false)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName(DisplayNames.CASE_PROPERTIES)
                .setKey(EventDataKeys.FIELD_VALUES)
                .setType(ParameterType.MAP)
                .setRequired(false)
                .setOrder(order++);
        parameters.add(builder.build());

        String displayName = String.format("Update Case [%s]", config.getName());

        ActionEventBuilder actionBuilder = new ActionEventBuilder()
                .setDisplayName(displayName)
                .setServiceInterface(COMMCARE_ACTION_PROXY_SERVICE)
                .setServiceMethod(serviceMethod)
                .setSubject(EventSubjects.UPDATE_CASE + "." + config.getName())
                .setActionParameters(parameters);
        return actionBuilder.build();
    }

    private ActionEvent prepareSubmitForm1Action() {
        SortedSet<ActionParameter> parameters = new TreeSet<>();
        ActionParameterBuilder builder;
        int order = 0;

        builder = new ActionParameterBuilder()
                .setDisplayName("Is Pregnant?")
                .setKey(DummyCommcareSchema.FORM_QUESTION1)
                .setRequired(false)
                .setType(ParameterType.UNICODE)
                .setOrder(order++);
        parameters.add(builder.build());

        builder = new ActionParameterBuilder()
                .setDisplayName("Date of birth")
                .setKey(DummyCommcareSchema.FORM_QUESTION2)
                .setRequired(false)
                .setType(ParameterType.UNICODE)
                .setOrder(order++);
        parameters.add(builder.build());

        String displayName = String.format("Submit Form: form1 [TestApp1: %s]", config.getName());

        ActionEventBuilder actionBuilder = new ActionEventBuilder()
                .setDisplayName(displayName)
                .setSubject(EventSubjects.SUBMIT_FORM + ".http://openrosa.org/formdesigner/84FA38A2-93C1-4B9E-AA2A-0E082995FF9E." + config.getName())
                .setActionParameters(parameters);
        return actionBuilder.build();
    }

    private ActionEvent prepareSubmitForm2Action() {
        SortedSet<ActionParameter> parameters = new TreeSet<>();
        ActionParameterBuilder builder;
        int order = 0;

        builder = new ActionParameterBuilder()
                .setDisplayName("Patient name")
                .setKey(DummyCommcareSchema.FORM_QUESTION3)
                .setRequired(false)
                .setType(ParameterType.UNICODE)
                .setOrder(order++);
        parameters.add(builder.build());

        String displayName = String.format("Submit Form: form2 [TestApp1: %s]", config.getName());

        ActionEventBuilder actionBuilder = new ActionEventBuilder()
                .setDisplayName(displayName)
                .setSubject(EventSubjects.SUBMIT_FORM + ".http://openrosa.org/formdesigner/12KE58A2-54C5-1Z4B-AR2S-Z0345995RF9E." + config.getName())
                .setActionParameters(parameters);
        return actionBuilder.build();
    }

    private ActionEvent prepareSubmitForm3Action() {
        SortedSet<ActionParameter> parameters = new TreeSet<>();
        ActionParameterBuilder builder;
        int order = 0;

        builder = new ActionParameterBuilder()
                .setDisplayName("Last visit")
                .setKey(DummyCommcareSchema.FORM_QUESTION4)
                .setRequired(false)
                .setType(ParameterType.UNICODE)
                .setOrder(order++);
        parameters.add(builder.build());

        String displayName = String.format("Submit Form: form3 [TestApp1: %s]", config.getName());

        ActionEventBuilder actionBuilder = new ActionEventBuilder()
                .setDisplayName(displayName)
                .setSubject(EventSubjects.SUBMIT_FORM + ".http://openrosa.org/formdesigner/22KE58A2-54C5-1Z4B-AR2S-Z0345995RF9E." + config.getName())
                .setActionParameters(parameters);
        return actionBuilder.build();
    }

    private ActionEvent prepareSubmitForm4Action() {
        SortedSet<ActionParameter> parameters = new TreeSet<>();
        ActionParameterBuilder builder;
        int order = 0;

        builder = new ActionParameterBuilder()
                .setDisplayName("Does patient take any medications?")
                .setKey(DummyCommcareSchema.FORM_QUESTION5)
                .setRequired(false)
                .setType(ParameterType.UNICODE)
                .setOrder(order++);
        parameters.add(builder.build());

        String displayName = String.format("Submit Form: form4 [TestApp2: %s]", config.getName());

        ActionEventBuilder actionBuilder = new ActionEventBuilder()
                .setDisplayName(displayName)
                .setSubject(EventSubjects.SUBMIT_FORM + ".http://openrosa.org/formdesigner/32KE58A2-54C5-1Z4B-AR2S-Z0345995RF9E." + config.getName())
                .setActionParameters(parameters);
        return actionBuilder.build();
    }

    private boolean containsTrigger(Channel channel, TaskTriggerInformation info) {
        return getTrigger(channel, info) != null ;
    }

    private TriggerEvent getTrigger(Channel channel, TaskTriggerInformation info) {
        for (TriggerEvent trigger : channel.getTriggerTaskEvents()) {
            if (equalsIgnoreCase(trigger.getSubject(), info.getSubject())) {
                return trigger;
            }
        }
        return null;
    }

    private void clearDB() {
        applicationDataService.deleteAll();
        for (Task task : getTaskService().getAllTasks()) {
            getTaskService().deleteTask(task.getId());
        }
    }
}
