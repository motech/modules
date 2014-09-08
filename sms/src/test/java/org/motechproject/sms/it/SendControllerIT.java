package org.motechproject.sms.it;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.sms.audit.SmsRecordsDataService;
import org.motechproject.sms.service.OutgoingSms;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.http.SimpleHttpClient;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertTrue;

/**
 * Verify SendController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class SendControllerIT extends BasePaxIT {
    @Inject
    private SmsRecordsDataService smsRecordsDataService;


    @Before
    public void waitForBeans() {
        // To prevent the annoying "BeanFactory not initialized or already closed" errors
        try { Thread.sleep(2000); } catch (InterruptedException e) {  }
    }

    @Before
    public void createUser() {
        try {
            createAdminUser();
        } catch (IOException|InterruptedException e) {
            getLogger().error("Unable to create the admin user needed for this test: {}", e.getMessage());
        }
    }

    @Test
    public void verifyFunctional() throws Exception {
        OutgoingSms outgoingSms = new OutgoingSms("foo", Arrays.asList("12065551212"), "hello, world");
        ObjectMapper mapper = new ObjectMapper();
        String outgoingSmsJson = mapper.writeValueAsString(outgoingSms);

        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort())
                .setPath("/sms/send");

        HttpPost httpPost = new HttpPost(builder.build());
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setEntity(new StringEntity(outgoingSmsJson));

        // We're specifying a nonexistent config so the controller should respond with a 404
        assertTrue(SimpleHttpClient.execHttpRequest(httpPost, HttpStatus.SC_NOT_FOUND, MOTECH_ADMIN_USERNAME,
                MOTECH_ADMIN_PASSWORD));

        //TODO: figure out how to create configs an then use them to "pretend send" using an SimpleHttpServer that
        //TODO: responds the way an SMS provider would.
    }
}
