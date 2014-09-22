package org.motechproject.ivr.it;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.CallStatus;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.Template;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.repository.TemplateDataService;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.http.SimpleHttpClient;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.*;


/**
 * Verify TemplateController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class TemplateControllerIT extends BasePaxIT {

    @Inject
    private CallDetailRecordDataService callDetailRecordDataService;

    @Inject
    private TemplateDataService templateDataService;

    @Inject
    private ConfigService configService;

    @Before
    public void setup() {
        getLogger().info("setup");
        templateDataService.deleteAll();
        callDetailRecordDataService.deleteAll();
    }

    @Test
    public void shouldNotLogWhenPassedInvalidConfig() throws Exception {

        //Create a config
        configService.updateConfigs(Arrays.asList(new Config("foo", null, null, null, null)));

        //Create & send a CDR status callback
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort())
                .setPath("/ivr/status/bar");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        assertTrue(SimpleHttpClient.execHttpRequest(httpGet, HttpStatus.SC_INTERNAL_SERVER_ERROR));

        //Verify we did not log this CDR because service contains an invalid config
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(0, callDetailRecords.size());
    }

    @Test
    public void verifyControllerFunctional() throws Exception {
        getLogger().info("verifyControllerFunctional");

        //Create a config
        List<String> ignoredStatusFields = Arrays.asList("ignoreme", "ignoreme2");
        configService.updateConfigs(Arrays.asList(new Config("conf", ignoredStatusFields, "FROM:from", null, null)));

        //Create a template
        templateDataService.create(new Template("tmpl", "Hello, ${var}!"));

        //Create & send a CDR status callback
        String motechCallId = UUID.randomUUID().toString();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort())
                .setPath("/ivr/template/conf/tmpl")
                .addParameter("FROM", "+12065551212")
                .addParameter("to", "+12066661212")
                .addParameter("callStatus", "ANSWERED")
                .addParameter("motechCallId", motechCallId)
                .addParameter("ignoreme", "xxx")
                .addParameter("ignoreme2", "xxx")
                .addParameter("var", "world")
                .addParameter("foo", "bar");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        assertTrue(SimpleHttpClient.execHttpRequest(httpGet, "Hello, world!"));

        // Verify we logged this CDR - by querying on its motechId - which is a GUID
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.findByMotechCallId(motechCallId);
        assertEquals(1, callDetailRecords.size());
        CallDetailRecord callDetailRecord = callDetailRecords.get(0);
        assertEquals("+12065551212", callDetailRecord.getFrom());
        assertEquals("+12066661212", callDetailRecord.getTo());
        assertFalse(callDetailRecord.getProviderExtraData().containsKey("ignoreme"));
        assertFalse(callDetailRecord.getProviderExtraData().containsKey("ignoreme2"));
        assertEquals(CallStatus.ANSWERED, callDetailRecord.getCallStatus());
        assertEquals(2, callDetailRecord.getProviderExtraData().keySet().size());
        assertEquals(callDetailRecord.getProviderExtraData().get("foo"), "bar");
        assertEquals(callDetailRecord.getProviderExtraData().get("var"), "world");
    }
}
