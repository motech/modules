package org.motechproject.ivr.it;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.Template;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.ivr.service.TemplateService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.utils.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.http.SimpleHttpClient;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;


/**
 * Verify TemplateController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class TemplateControllerBundleIT extends BasePaxIT {

    @Inject
    private CallDetailRecordDataService callDetailRecordDataService;

    @Inject
    private TemplateService templateService;

    @Inject
    private ConfigService configService;

    private List<Template> backupTemplates;
    private List<Config> backupConfigs;

    @Before
    public void setup() {
        getLogger().info("setup");
        backupTemplates = templateService.allTemplates();
        backupConfigs = configService.allConfigs();
        templateService.updateTemplates(new ArrayList<Template>());
        callDetailRecordDataService.deleteAll();
    }

    @After
    public void cleanup() {
        getLogger().info("cleanup");
        templateService.updateTemplates(backupTemplates);
        configService.updateConfigs(backupConfigs);
        callDetailRecordDataService.deleteAll();
    }

    @Test
    public void shouldNotLogWhenPassedInvalidConfig() throws Exception {

        //Create a config
        configService.updateConfigs(Arrays.asList(new Config("foo", false, null, null, null, null, null, null, null, false, null)));

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
        configService.updateConfigs(Arrays.asList(new Config("conf", false, null, null, ignoredStatusFields, "FROM:from", null, null, null, false, null)));

        // Create a CDR we can use as a datasource in the template. A more elegant way to do that would be to create
        // an EUDE, but this works just as well.
        callDetailRecordDataService.create(new CallDetailRecord("world", null, null, null, null, null, null, "123abc",
                null, null));

        //Create a template
        templateService.updateTemplates(Arrays.asList(new Template("tmpl",
                "#set( $params = {\"motechCallId\" : \"123abc\"} )\n" +
                        "Hello, $dataServices.findMany(\"org.motechproject.ivr.domain.CallDetailRecord\", " +
                        "\"Find By Provider Call Id\", $params).get(0).configName"
        )));

        // Create & send a CDR status callback
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
        assertTrue(SimpleHttpClient.execHttpRequest(httpGet, "Hello, world"));

        // Verify we logged this CDR - by querying on its motechId - which is a GUID
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.findByMotechCallId(motechCallId);
        assertEquals(1, callDetailRecords.size());
        CallDetailRecord callDetailRecord = callDetailRecords.get(0);
        assertEquals("+12065551212", callDetailRecord.getFrom());
        assertEquals("+12066661212", callDetailRecord.getTo());
        assertFalse(callDetailRecord.getProviderExtraData().containsKey("ignoreme"));
        assertFalse(callDetailRecord.getProviderExtraData().containsKey("ignoreme2"));
        assertEquals("ANSWERED", callDetailRecord.getCallStatus());
        assertEquals(2, callDetailRecord.getProviderExtraData().keySet().size());
        assertEquals(callDetailRecord.getProviderExtraData().get("foo"), "bar");
        assertEquals(callDetailRecord.getProviderExtraData().get("var"), "world");
        assertEquals(callDetailRecord.getTemplateName(), "tmpl");
    }
}
