package org.motechproject.server.kookoo;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.junit.Ignore;
import org.motechproject.security.domain.MotechSecurityConfiguration;
import org.motechproject.security.repository.AllMotechSecurityRulesCouchdbImpl;
import org.motechproject.security.service.SecurityRuleLoader;
import org.motechproject.testing.osgi.BaseOsgiIT;
import org.motechproject.testing.utils.PollingHttpClient;
import org.motechproject.testing.utils.TestContext;
import org.osgi.framework.InvalidSyntaxException;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class KookooIvrServiceBundleIT extends BaseOsgiIT {

    private PollingHttpClient httpClient = new PollingHttpClient();

    private AllMotechSecurityRulesCouchdbImpl securityRules;
    private SecurityRuleLoader securityRuleLoad;

    public void testThatIVRServiceIsAvailableForImport() throws InvalidSyntaxException {
        Assert.assertNotNull(applicationContext.getBean("testKookooIVRService"));
    }

    // TODO: the two tests below consistently (well, not 100% consistently unfortunately) fail on ci.motechproject.org
    @Ignore
    public void testKooKooCallbackUrlIsNotAuthenticated() throws IOException, InterruptedException {
        checkSecurity();
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/kookoo/web-api/ivr", TestContext.getJettyPort()));

        HttpResponse response = httpClient.execute(httpGet);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Ignore
    public void testKooKooStatusCallbackUrlIsNotAuthenticated() throws IOException, InterruptedException {
        checkSecurity();
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/kookoo/web-api/ivr/callstatus", TestContext.getJettyPort()));

        HttpResponse response = httpClient.execute(httpGet);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Override
    protected List<String> getImports() {
        return asList("org.motechproject.ivr.service.contract",
                "org.motechproject.commons.couchdb.service");
    }

    @Override
    protected String[] getConfigLocations() {
        return new String[]{"/META-INF/osgi/testIVRKookooContext.xml"};
    }

    private void checkSecurity() {
        securityRules= (AllMotechSecurityRulesCouchdbImpl) applicationContext.getBean("securityRules");
        securityRuleLoad=(SecurityRuleLoader) applicationContext.getBean("securityRuleLoad");

        MotechSecurityConfiguration securityConfig = securityRules.getMotechSecurityConfiguration();

        if (securityRules == null || securityConfig == null) {
            securityRuleLoad.loadRules(applicationContext);
        }
    }
}
