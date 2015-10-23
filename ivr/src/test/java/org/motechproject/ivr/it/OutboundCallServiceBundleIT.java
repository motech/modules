package org.motechproject.ivr.it;

import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.domain.CallDetailRecord;
import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.Configs;
import org.motechproject.ivr.domain.HttpMethod;
import org.motechproject.ivr.exception.ConfigNotFoundException;
import org.motechproject.ivr.repository.CallDetailRecordDataService;
import org.motechproject.ivr.service.ConfigService;
import org.motechproject.ivr.service.OutboundCallService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.osgi.http.SimpleHttpServer;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


/**
 * Verify that the OutboundCallService is present & functional.
 */
@SuppressWarnings("ALL")
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class OutboundCallServiceBundleIT extends BasePaxIT {

    @Inject
    private OutboundCallService outboundCallService;
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
    public void restoreConfigs() throws IOException {
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
    public void verifyServiceFunctional() throws IOException {
        getLogger().info("verifyServiceFunctional()");

        String httpServerURI = SimpleHttpServer.getInstance().start("foo", HttpStatus.SC_OK, "OK");
        getLogger().debug("verifyServiceFunctional - We have a server listening at {}", httpServerURI);

        //Create a config
        Config config = new Config("conf123", false, null, null, null, null, null, null, HttpMethod.GET, false, httpServerURI, false, null);
        Configs configs = new Configs(Arrays.asList(config), "conf123");
        configService.updateConfigs(configs);

        Map<String, String> params = new HashMap<>();
        outboundCallService.initiateCall(config.getName(), params);

        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(1, callDetailRecords.size());
        assertEquals("conf123", callDetailRecords.get(0).getConfigName());
    }

    @Test
    public void shouldHandleInvalidServerResponse() throws IOException {
        getLogger().info("shouldHandleInvalidServerResponse()");

        String httpServerURI = SimpleHttpServer.getInstance().start("bar", HttpStatus.SC_BAD_REQUEST, "Eeek!");
        getLogger().debug("shouldHandleInvalidServerResponse - We have a server listening at {}", httpServerURI);

        //Create a config
        Config config = new Config("conf456", false, null, null, null, null, null, null, HttpMethod.GET, false, httpServerURI,false, null);
        Configs configs = new Configs(Arrays.asList(config), "conf456");
        getLogger().debug("shouldHandleInvalidServerResponse - We create a config  {}", config.toString());
        configService.updateConfigs(configs);

        boolean exceptionThrown = false;
        Map<String, String> params = new HashMap<>();
        try {
            outboundCallService.initiateCall("conf456", params);
        }
        catch (RuntimeException e) {
            exceptionThrown = true;
        }
        // We're expecting an exception to be thrown
        assertTrue(exceptionThrown);

        //And we're expecting to see one FAILED CDR in the database
        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(1, callDetailRecords.size());
        assertEquals(CallDetailRecord.CALL_FAILED, callDetailRecords.get(0).getCallStatus());
    }

    @Test
    public void shouldExecuteJsonRequest() throws IOException {
        getLogger().info("shouldExecuteJsonRequest()");

        String httpServerURI = SimpleHttpServer.getInstance().start("foo", HttpStatus.SC_OK, "OK");
        getLogger().debug("shouldExecuteJsonRequest - We have a server listening at {}", httpServerURI);

        //Create a config
        Config config = new Config("conf789", false, null, null, null, null, null, null, HttpMethod.POST, true, httpServerURI, false, null);
        Configs configs = new Configs(Arrays.asList(config), "conf789");
        configService.updateConfigs(configs);

        Map<String, String> params = new HashMap<>();
        params.put("api_key", "qwerty123");
        params.put("message_id", "123123");
        params.put("channel", "ivr");
        params.put("status_callback_url", "http://someUrl.com");
        params.put("subscribers", "[{\"phone\":\"48700123123\",\"language\":null}]");

        outboundCallService.initiateCall(config.getName(), params);

        List<CallDetailRecord> callDetailRecords = callDetailRecordDataService.retrieveAll();
        assertEquals(1, callDetailRecords.size());
        assertEquals("conf789", callDetailRecords.get(0).getConfigName());
    }

    @Test(expected = ConfigNotFoundException.class)
    public void shouldThrowConfigNotFoundException() {
        getLogger().info("shouldThrowConfigNotFoundException");
        Configs configs = new Configs();
        configService.updateConfigs(configs);
        Map<String, String> params = new HashMap<>();
        outboundCallService.initiateCall(params);
    }
}
