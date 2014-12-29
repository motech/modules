package org.motechproject.messagecampaign.it.osgi;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.joda.time.LocalDate;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.messagecampaign.contract.CampaignRequest;
import org.motechproject.messagecampaign.dao.CampaignEnrollmentDataService;
import org.motechproject.messagecampaign.dao.CampaignMessageRecordService;
import org.motechproject.messagecampaign.dao.CampaignRecordService;
import org.motechproject.messagecampaign.domain.campaign.CampaignType;
import org.motechproject.messagecampaign.service.CampaignEnrollmentRecord;
import org.motechproject.messagecampaign.service.CampaignEnrollmentsQuery;
import org.motechproject.messagecampaign.service.MessageCampaignService;
import org.motechproject.messagecampaign.domain.campaign.CampaignRecord;
import org.motechproject.security.service.MotechUserService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.utils.TestContext;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.inject.Inject;
import javax.xml.bind.DatatypeConverter;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.motechproject.security.constants.PermissionNames.ACTIVATE_USER_PERMISSION;
import static org.motechproject.security.constants.PermissionNames.ADD_USER_PERMISSION;
import static org.motechproject.security.constants.PermissionNames.DELETE_USER_PERMISSION;
import static org.motechproject.security.constants.PermissionNames.EDIT_USER_PERMISSION;
import static org.motechproject.security.constants.PermissionNames.MANAGE_USER_PERMISSION;
import static org.motechproject.security.constants.PermissionNames.UPDATE_SECURITY_PERMISSION;
import static org.motechproject.security.constants.PermissionNames.VIEW_SECURITY;
import static org.motechproject.security.constants.PermissionNames.VIEW_USER_PERMISSION;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class MessageCampaignBundleIT extends BasePaxIT {

    private static final int PORT = TestContext.getJettyPort();

    @Inject
    private MessageCampaignService messageCampaignService;

    @Inject
    private CampaignMessageRecordService campaignMessageRecordService;

    @Inject
    private CampaignEnrollmentDataService campaignEnrollmentDataService;

    @Inject
    private CampaignRecordService campaignRecordService;

    @Inject
    private MotechUserService motechUserService;

    @Before
    public void setUp() {
        getHttpClient().getCredentialsProvider().clear();
        getHttpClient().setCookieStore(null);
    }

    @After
    public void tearDown() {
        campaignEnrollmentDataService.deleteAll();
    }

    @Test
    public void testMessageCampaignService() {
        CampaignRecord campaign = new CampaignRecord();
        campaign.setName("PREGNANCY_CMP");
        campaign.setCampaignType(CampaignType.ABSOLUTE);

        messageCampaignService.saveCampaign(campaign);

        String externalId = "MessageCampaignBundleIT-" + UUID.randomUUID();
        CampaignRequest campaignRequest = new CampaignRequest(externalId, "PREGNANCY_CMP", new LocalDate(2020, 7, 10), null);

        try {
            messageCampaignService.enroll(campaignRequest);
            List<CampaignEnrollmentRecord> campaignEnrollmentRecords = messageCampaignService.search(new CampaignEnrollmentsQuery().withExternalId(externalId));
            assertTrue(campaignEnrollmentRecords.size() == 1);
        } finally {
            messageCampaignService.unenroll(campaignRequest.externalId(), campaignRequest.campaignName()); // Doesn't delete the doc
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
        setUpSecurityContext("admin", "admin");
        motechUserService.register("user-mc-noauth", "pass", "testmcnoauth@test.com", null, asList("Admin User"), Locale.ENGLISH);
        clearSecurityContext();

        HttpGet request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/enrollments/users", PORT));
        request.setHeader("Authorization", "Basic " + DatatypeConverter.printBase64Binary("user-mc-noauth:pass".getBytes("UTF-8")).trim());
        HttpResponse response = getHttpClient().execute(request, HttpStatus.SC_FORBIDDEN);
        assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());

        EntityUtils.consume(response.getEntity());

        request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/campaigns", PORT));
        request.setHeader("Authorization", "Basic " + DatatypeConverter.printBase64Binary("user-mc-noauth:pass".getBytes("UTF-8")).trim());
        response = getHttpClient().execute(request, HttpStatus.SC_FORBIDDEN);
        assertEquals(HttpStatus.SC_FORBIDDEN, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testControllersAsAuthorizedUser() throws Exception {
        setUpSecurityContext("admin", "admin");
        motechUserService.register("user-mc-auth", "pass", "testmcauth@test.com", "test", asList("Campaign Manager"), Locale.ENGLISH);
        clearSecurityContext();

        HttpGet request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/campaigns", PORT));
        request.addHeader("Authorization", "Basic " + DatatypeConverter.printBase64Binary("user-mc-auth:pass".getBytes("UTF-8")).trim());
        HttpResponse response = getHttpClient().execute(request);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());

        EntityUtils.consume(response.getEntity());
        request = new HttpGet(String.format("http://localhost:%d/messagecampaign/web-api/enrollments/users", PORT));
        request.addHeader("Authorization", "Basic " + DatatypeConverter.printBase64Binary("user-mc-auth:pass".getBytes("UTF-8")).trim());
        response = getHttpClient().execute(request);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    protected void setUpSecurityContext(String username, String password) {
        Authentication auth = new UsernamePasswordAuthenticationToken(new User(username, password, getPermissions()), password, getPermissions());
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    protected void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    private List<SimpleGrantedAuthority> getPermissions() {
        return asList(new SimpleGrantedAuthority(ADD_USER_PERMISSION), new SimpleGrantedAuthority(EDIT_USER_PERMISSION),
                new SimpleGrantedAuthority(MANAGE_USER_PERMISSION), new SimpleGrantedAuthority(EDIT_USER_PERMISSION),
                new SimpleGrantedAuthority(ACTIVATE_USER_PERMISSION), new SimpleGrantedAuthority(VIEW_USER_PERMISSION),
                new SimpleGrantedAuthority(DELETE_USER_PERMISSION), new SimpleGrantedAuthority(UPDATE_SECURITY_PERMISSION),
                new SimpleGrantedAuthority(VIEW_SECURITY));
    }
}
