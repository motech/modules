package org.motechproject.commcare.it;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.tasks.CommcareTasksNotifier;
import org.motechproject.commcare.tasks.action.CommcareValidatingChannel;
import org.motechproject.commcare.util.DummyCommcareSchema;
import org.motechproject.commcare.util.ResponseXML;
import org.motechproject.tasks.contract.ActionEventRequest;
import org.motechproject.tasks.contract.ActionParameterRequest;
import org.motechproject.tasks.domain.ActionEvent;
import org.motechproject.tasks.domain.Channel;
import org.motechproject.tasks.domain.EventParameter;
import org.motechproject.tasks.domain.Task;
import org.motechproject.tasks.domain.TaskActionInformation;
import org.motechproject.tasks.domain.TaskTriggerInformation;
import org.motechproject.tasks.domain.TriggerEvent;
import org.motechproject.tasks.osgi.test.AbstractTaskBundleIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.helper.ServiceRetriever;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.osgi.framework.BundleContext;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

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

    private static final Integer MAX_RETRIES_BEFORE_FAIL = 20;
    private static final Integer WAIT_TIME = 2000;

    @Before
    public void setUp() {
        clearDB();
        commcareTasksNotifier = (CommcareTasksNotifier) ServiceRetriever.getWebAppContext(bundleContext, COMMCARE_CHANNEL_NAME).getBean("commcareTasksNotifier");
    }

    @After
    public void tearDown() {
        clearDB();
    }

    @Test
    public void testCommcareTasksIntegration() throws InterruptedException, IOException {
        createMockCommcareSchema();
        commcareTasksNotifier.updateTasksInfo();

        waitForChannel(COMMCARE_CHANNEL_NAME);
        Channel channel = findChannel(COMMCARE_CHANNEL_NAME);

        verifyCommcareChannelHasCorrectTriggers(channel);
        createDummyActionChannel(channel);
        createTestTask();

        HttpResponse response = sendMockForm();

        // Make sure that Commcare controller returned 200 after handling the form
        Assert.assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
        // Give Tasks some time to process
        waitForTaskExecution();
        // Ask our OSGi service, which acts as Task action, to verify that correct values were received
        Assert.assertTrue(validatingChannel.verify());
    }

    private void createDummyActionChannel(Channel channel) {
        ActionParameterRequest actionParameterRequest = new ActionParameterRequest(ResponseXML.ATTR1, ResponseXML.ATTR1, 0);
        ActionParameterRequest actionParameterRequest2 = new ActionParameterRequest(ResponseXML.ATTR2, ResponseXML.ATTR2, 1);
        SortedSet<ActionParameterRequest> actionParameterRequests = new TreeSet<>();
        actionParameterRequests.add(actionParameterRequest);
        actionParameterRequests.add(actionParameterRequest2);

        ActionEventRequest actionEventRequest = new ActionEventRequest("dummyAction", "validate", null,
                TEST_INTERFACE, "execute", actionParameterRequests);

        channel.addActionTaskEvent(new ActionEvent(actionEventRequest));
        getChannelService().addOrUpdate(channel);
    }

    private void createTestTask() {
        TaskTriggerInformation triggerInformation = new TaskTriggerInformation("trigger", COMMCARE_CHANNEL_NAME, COMMCARE_CHANNEL_NAME, VERSION,
                "org.motechproject.commcare.api.forms.form1", "org.motechproject.commcare.api.forms");

        TaskActionInformation actionInformation = new TaskActionInformation("action", COMMCARE_CHANNEL_NAME, COMMCARE_CHANNEL_NAME, VERSION,
                TEST_INTERFACE, "execute");
        actionInformation.setSubject("validate");

        Map<String, String> values = new HashMap<>();
        values.put(ResponseXML.ATTR1, "{{trigger./data/pregnant}}");
        values.put(ResponseXML.ATTR2, "{{trigger./data/dob}}");
        actionInformation.setValues(values);

        Task task = new Task("CommcareTestTask", triggerInformation, Arrays.asList(actionInformation));
        getTaskService().save(task);

        getTriggerHandler().registerHandlerFor(task.getTrigger().getEffectiveListenerSubject());
    }

    private void verifyCommcareChannelHasCorrectTriggers(Channel channel) {
        List<TriggerEvent> triggerEvents = channel.getTriggerTaskEvents();
        List<ActionEvent> actionEvents = channel.getActionTaskEvents();

        assertTrue(actionEvents.isEmpty());
        assertEquals(7, triggerEvents.size());

        TaskTriggerInformation expectedForm1 = new TaskTriggerInformation();
        TaskTriggerInformation expectedForm2 = new TaskTriggerInformation();
        TaskTriggerInformation expectedCaseBirth = new TaskTriggerInformation();

        expectedForm1.setSubject("org.motechproject.commcare.api.forms.form1");
        assertTrue(channel.containsTrigger(expectedForm1));

        expectedForm2.setSubject("org.motechproject.commcare.api.forms.form2");
        assertTrue(channel.containsTrigger(expectedForm2));

        expectedCaseBirth.setSubject("org.motechproject.commcare.api.case.birth");
        assertTrue(channel.containsTrigger(expectedCaseBirth));

        TriggerEvent form1Trigger = channel.getTrigger(expectedForm1);
        assertEquals("org.motechproject.commcare.api.forms", form1Trigger.getTriggerListenerSubject());
        assertTriggerParameters(form1Trigger.getEventParameters(),
                Arrays.asList("/data/pregnant", "/data/dob", "/data/meta/username"));

        TriggerEvent form2Trigger = channel.getTrigger(expectedForm2);
        assertEquals("org.motechproject.commcare.api.forms", form2Trigger.getTriggerListenerSubject());
        assertTriggerParameters(form2Trigger.getEventParameters(),
                Arrays.asList("/data/patient_name", "/data/last_visit", "/data/medications", "/data/meta/username"));

        TriggerEvent caseTrigger = channel.getTrigger(expectedCaseBirth);
        assertEquals("org.motechproject.commcare.api.case", caseTrigger.getTriggerListenerSubject());
        assertTriggerParameters(caseTrigger.getEventParameters(),
                Arrays.asList("motherName", "childName", "dob", "caseName"));
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
        CommcareModuleJson moduleJson = new CommcareModuleJson();
        moduleJson.setFormSchemas(DummyCommcareSchema.getForms());
        moduleJson.setCaseType("birth");
        moduleJson.setCaseProperties(new ArrayList<>(DummyCommcareSchema.getCases().get("birth")));

        CommcareApplicationJson applicationJson = new CommcareApplicationJson("123", "TestApp", "none", Arrays.asList(moduleJson));

        applicationDataService.create(applicationJson);
    }

    private HttpResponse sendMockForm() throws IOException, InterruptedException {
        HttpPost httpPost = new HttpPost(String.format("http://localhost:%d/commcare/forms", PORT));
        HttpEntity body = new ByteArrayEntity(ResponseXML.getFormXML().getBytes("UTF-8"));
        httpPost.setEntity(body);
        return getHttpClient().execute(httpPost);
    }

    private void waitForTaskExecution() throws InterruptedException {
        int retries = 0;
        while (retries < MAX_RETRIES_BEFORE_FAIL && !validatingChannel.hasExecuted()) {
            retries++;
            Thread.sleep(WAIT_TIME);
        }
    }

    private void clearDB() {
        applicationDataService.deleteAll();
        for (Task task : getTaskService().getAllTasks()) {
            getTaskService().deleteTask(task.getId());
        }
    }
}
