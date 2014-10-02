package org.motechproject.sms.it;

import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.sms.audit.DeliveryStatus;
import org.motechproject.sms.audit.SmsRecord;
import org.motechproject.sms.audit.SmsRecordsDataService;
import org.motechproject.sms.configs.Config;
import org.motechproject.sms.configs.Configs;
import org.motechproject.sms.service.ConfigService;
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
import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Verify StatusController present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class StatusControllerIT extends BasePaxIT {
    private static final String CONFIG_NAME = "sample-it-config";
    private Configs backupConfigs;

    @Inject
    private ConfigService configService;

    @Inject
    private SmsRecordsDataService smsRecordsDataService;


    @Before
    public void waitForBeans() {
        // To prevent the annoying "BeanFactory not initialized or already closed" errors
        try { Thread.sleep(IntegrationTests.BUNDLE_MS_WAIT_TIME); } catch (InterruptedException e) {  }
    }

    @Before
    public void createUser() {
        try {
            createAdminUser();
        } catch (IOException|InterruptedException e) {
            getLogger().error("Unable to create the admin user needed for this test: {}", e.getMessage());
        }
    }

    @Before
    public void createConfigs() {
        backupConfigs = configService.getConfigs();
        Config config = new Config();
        config.setName(CONFIG_NAME);
        config.setTemplateName("Plivo");

        Configs configs = new Configs();
        configs.setConfigs(Arrays.asList(config));
        configs.setDefaultConfigName(CONFIG_NAME);

        configService.updateConfigs(configs);
    }

    @After
    public void restoreConfigs() {
        configService.updateConfigs(backupConfigs);
    }

    @Test
    public void verifyControllerFunctional() throws Exception {
        getLogger().info("verifyControllerFunctional");

        //Create & send a CDR status callback
        String messageId = UUID.randomUUID().toString();
        URIBuilder builder = new URIBuilder();
        builder.setScheme("http").setHost("localhost").setPort(TestContext.getJettyPort())
                .setPath(String.format("/sms/status/%s", CONFIG_NAME))
                .addParameter("Status", "sent")
                .addParameter("From", "+12065551212")
                .addParameter("To", "+12065551313")
                .addParameter("MessageUUID", messageId);
        URI uri = builder.build();
        HttpGet httpGet = new HttpGet(uri);
        assertTrue(SimpleHttpClient.execHttpRequest(httpGet, HttpStatus.SC_OK));

        //Verify we logged this
        List<SmsRecord> smsRecords = smsRecordsDataService.findByCriteria(null, null, null, null, null, null, null,
                null, messageId, null, null);
        assertEquals(1, smsRecords.size());
        assertEquals(smsRecords.get(0).getDeliveryStatus(), DeliveryStatus.DELIVERY_CONFIRMED);
    }
}
