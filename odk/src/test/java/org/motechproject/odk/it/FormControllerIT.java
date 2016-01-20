package org.motechproject.odk.it;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.AuthPolicy;
import org.apache.http.entity.StringEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventListenerRegistryService;
import org.motechproject.odk.constant.EventParameters;
import org.motechproject.odk.constant.EventSubjects;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.utils.TestContext;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class FormControllerIT extends OdkBaseIT {

    private static final int WAIT_COUNT = 5;
    private static final int EXPECTED_EVENTS_SUCCESS = 7;
    private static final int EXPECTED_EVENTS_FAIL = 1;

    private static final String MOCK_ID_1 = "id1";
    private static final String MOCK_ID_2 = "id2";

    @Inject
    private EventListenerRegistryService registry;

    @Before
    public void setUp() throws IOException, InterruptedException {
        createAdminUser();

        getHttpClient().getCredentialsProvider().setCredentials(
                new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT, AuthScope.ANY_REALM, AuthPolicy.BASIC),
                new UsernamePasswordCredentials("motech", "motech")
        );

        login();
    }

    @Test
    public void testNestedRepeats() throws Exception{
        List<String> subjects = createSuccessSubjects();
        MockEventListener mockEventListener = new MockEventListener(MOCK_ID_1);
        registry.registerListener(mockEventListener,subjects);

        HttpPost request = new HttpPost(String.format("http://localhost:%d/odk/forms/%s/%s", TestContext.getJettyPort(), CONFIG_NAME, TITLE));
        StringEntity entity = new StringEntity(getJson());
        request.setEntity(entity);
        HttpResponse response = getHttpClient().execute(request);
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        int count = 0;
        while(mockEventListener.getEvents().size() < EXPECTED_EVENTS_SUCCESS && count < WAIT_COUNT) {
            count++;
            getLogger().debug("Number of events: " + mockEventListener.getEvents().size());

            if (count == WAIT_COUNT) {
                getLogger().error("Timeout");
                fail();
            }
            Thread.sleep(2000);
        }

        List<MotechEvent> events = mockEventListener.getEvents();
        assertNotNull(events);
        assertEquals(events.size(), 7);
    }

    @Test
    public void testFormReceiptFailure() throws Exception{
        MockEventListener mockEventListener = new MockEventListener(MOCK_ID_2);
        registry.registerListener(mockEventListener,EventSubjects.FORM_FAIL);
        String badConfigName = "bad_config_name";

        HttpPost request = new HttpPost(String.format("http://localhost:%d/odk/forms/%s/%s", TestContext.getJettyPort(), badConfigName, TITLE));
        StringEntity entity = new StringEntity(getJson());
        request.setEntity(entity);
        HttpResponse response = getHttpClient().execute(request);
        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);

        int count = 0;
        while (mockEventListener.getEvents().size() < EXPECTED_EVENTS_FAIL && count < WAIT_COUNT) {
            count++;
            getLogger().debug("Number of events: " + mockEventListener.getEvents().size());

            if (count == WAIT_COUNT) {
                getLogger().error("Timeout");
                fail();
            }
            Thread.sleep(2000);
        }

        MotechEvent event = mockEventListener.getEvents().get(0);
        assertNotNull(event);
        assertEquals(event.getSubject(), EventSubjects.FORM_FAIL);
        assertEquals(event.getParameters().get(EventParameters.FORM_TITLE), TITLE);
        assertEquals(event.getParameters().get(EventParameters.CONFIGURATION_NAME), badConfigName);

    }

    private List<String> createSuccessSubjects() {
        List<String> subjects = new ArrayList<>();
        subjects.add(createEventSubject(EventSubjects.REPEAT_GROUP, "outer_group"));
        subjects.add(createEventSubject(EventSubjects.REPEAT_GROUP, "outer_group/inner_group"));
        subjects.add(EventSubjects.RECEIVED_FORM + "." + CONFIG_NAME + "." + TITLE);
        subjects.add(EventSubjects.FORM_FAIL);
        return subjects;
    }


    private String createEventSubject(String base, String suffix) {
        return base + "." + CONFIG_NAME + "." + TITLE + "." + suffix;
    }
}
