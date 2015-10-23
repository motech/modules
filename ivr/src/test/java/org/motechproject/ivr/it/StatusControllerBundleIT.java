package org.motechproject.ivr.it;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.Configs;
import org.motechproject.ivr.domain.Template;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.utils.TestContext;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

/**
 * Verify StatusController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class StatusControllerBundleIT extends BasePaxIT {

    @Inject
    private CallDetailRecordDataService callDetailRecordDataService;

    @Inject
    private ConfigService configService;

    private Configs backupConfigs;

    @Before
    public void backupConfigs() {
        getLogger().info("backupConfigs");
        backupConfigs = configService.allConfigs();
    }

    @After
    public void restoreConfigs() {
        getLogger().info("restoreConfigs");
        configService.updateConfigs(backupConfigs);
    }

    @Before
    @After
    public void clearDatabase() {
        getLogger().info("clearDatabase");
        callDetailRecordDataService.deleteAll();
    }

    @Test
    public void shouldNotLogWhenPassedInvalidConfig() throws Exception {

        //Create a config
        List<Config> configList = Arrays.asList(new Config("foo", false, null, null, null, null, null, null, null, false, null, false, null));
        Configs configs = new Configs(configList, "foo");
        configService.updateConfigs(configs);

        //Create & send a CDR status callback
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort())
                .setPath("/ivr/status/bar");
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);

        HttpResponse response = getHttpClient().execute(httpGet, HttpStatus.SC_INTERNAL_SERVER_ERROR);

        assertEquals(HttpStatus.SC_INTERNAL_SERVER_ERROR, response.getStatusLine().getStatusCode());

        //Verify we did not log this CDR because service contains an invalid config
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(0, callDetailRecords.size());
    }

    @Test
    public void verifyControllerFunctional() throws Exception {
        getLogger().info("verifyControllerFunctional");

        //Create a config
        List<String> ignoredStatusFields = Arrays.asList("ignoreme", "ignoreme2");
        List<Config> configList = Arrays.asList(new Config("foo", false, null, null, ignoredStatusFields, "FROM:from", null, "ANSWERED: NEW STATUS", null, false, null, false, null));
        Configs configs = new Configs(configList, "foo");
        configService.updateConfigs(configs);

        //Create & send a CDR status callback
        String motechCallId = UUID.randomUUID().toString();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort()).setPath("/ivr/status/foo")
                .addParameter("FROM", "+12065551212")
                .addParameter("to", "+12066661212")
                .addParameter("callStatus", "ANSWERED")
                .addParameter("motechCallId", motechCallId)
                .addParameter("ignoreme", "xxx")
                .addParameter("ignoreme2", "xxx")
                .addParameter("foo", "bar");
        URI uri = builder.build();

        HttpResponse response = getHttpClient().get(uri.toString());
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

        //Verify we logged this CDR - by querying on its motechId - which is a GUID
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.findByMotechCallId(motechCallId);
        assertEquals(1, callDetailRecords.size());
        CallDetailRecord callDetailRecord = callDetailRecords.get(0);
        assertEquals("+12065551212", callDetailRecord.getFrom());
        assertEquals("+12066661212", callDetailRecord.getTo());
        assertFalse(callDetailRecord.getProviderExtraData().containsKey("ignoreme"));
        assertFalse(callDetailRecord.getProviderExtraData().containsKey("ignoreme2"));
        assertEquals("NEW STATUS", callDetailRecord.getCallStatus());
        assertEquals(1, callDetailRecord.getProviderExtraData().keySet().size());
        assertEquals(callDetailRecord.getProviderExtraData().get("foo"), "bar");
        assertNull(callDetailRecord.getTemplateName());
    }
}
