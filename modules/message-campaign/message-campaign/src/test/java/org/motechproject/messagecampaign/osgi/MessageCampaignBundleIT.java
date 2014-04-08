package org.motechproject.messagecampaign.osgi;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.service.CampaignEnrollmentRecord;
import org.motechproject.messagecampaign.service.CampaignEnrollmentsQuery;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.messagecampaign.userspecified.CampaignRecord;
import org.motechproject.security.service.MotechUserService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.apache.commons.codec.binary.Base64.encodeBase64String;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class MessageCampaignBundleIT extends BasePaxIT {

    private static final int PORT = TestContext.getJettyPort();

    @Inject
    private MessageCampaignService messageCampaignService;

    @Inject
    private MotechUserService motechUserService;

    @Override
    protected boolean startHttpServer() {
        return true;
    }

    @Before
    public void setUp() {
        getHttpClient().getCredentialsProvider().clear();
    }

    @Test
    public void testMessageCampaignService() {
        CampaignRecord campaign = new CampaignRecord();
        campaign.setName("PREGNANCY");
        campaign.setCampaignType(CampaignType.ABSOLUTE);

        messageCampaignService.saveCampaign(campaign);

        String externalId = "MessageCampaignBundleIT-" + UUID.randomUUID();
        CampaignRequest campaignRequest = new CampaignRequest(externalId, "PREGNANCY", new LocalDate(2020, 7, 10), null);

        try {
            messageCampaignService.startFor(campaignRequest);
            List<CampaignEnrollmentRecord> campaignEnrollmentRecords = messageCampaignService.search(new CampaignEnrollmentsQuery().withExternalId(externalId));
            assertTrue(campaignEnrollmentRecords.size() == 1);
        } finally {
            messageCampaignService.stopAll(campaignRequest); // Doesn't delete the doc
        }
    }

    @Test
    public void testControllersAnonymous() throws Exception {
        HttpGet request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/campaigns", PORT));
        HttpResponse response = getHttpClient().execute(request, HttpStatus.SC_UNAUTHORIZED);

        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusLine().getStatusCode());

        EntityUtils.consume(response.getEntity());

        request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/enrollments/users", PORT));
        response = getHttpClient().execute(request, HttpStatus.SC_UNAUTHORIZED);

        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testControllersUnauthenticated() throws Exception {
        getHttpClient().getCredentialsProvider()
                .setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("mal", "icious"));

        HttpGet request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/campaigns", PORT));
        HttpResponse response = getHttpClient().execute(request, HttpStatus.SC_UNAUTHORIZED);

        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusLine().getStatusCode());

        EntityUtils.consume(response.getEntity());

        request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/enrollments/users", PORT));
        response = getHttpClient().execute(request, HttpStatus.SC_UNAUTHORIZED);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testControllersAsUnathorizedUser() throws Exception {
        motechUserService.register("user-mc-noauth", "pass", "testmcnoauth@test.com", null, asList("Admin User"), Locale.ENGLISH);

        HttpGet request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/enrollments/users", PORT));
        request.setHeader("Authorization", "Basic " + encodeBase64String("user-mc-noauth:pass".getBytes("UTF-8")).trim());
        HttpResponse response = getHttpClient().execute(request, HttpStatus.SC_FORBIDDEN);
        assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());

        EntityUtils.consume(response.getEntity());

        request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/campaigns", PORT));
        request.setHeader("Authorization", "Basic " + encodeBase64String("user-mc-noauth:pass".getBytes("UTF-8")).trim());
        response = getHttpClient().execute(request, HttpStatus.SC_FORBIDDEN);
        assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testControllersAsAuthorizedUser() throws Exception {
        motechUserService.register("user-mc-auth", "pass", "testmcauth@test.com", "test", asList("Campaign Manager"), Locale.ENGLISH);

        HttpGet request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/campaigns", PORT));
        request.addHeader("Authorization", "Basic " + encodeBase64String("user-mc-auth:pass".getBytes("UTF-8")).trim());
        HttpResponse response = getHttpClient().execute(request);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

        EntityUtils.consume(response.getEntity());

        request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/enrollments/users", PORT));
        request.addHeader("Authorization", "Basic " + encodeBase64String("user-mc-auth:pass".getBytes("UTF-8")).trim());
        response = getHttpClient().execute(request);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }
}
