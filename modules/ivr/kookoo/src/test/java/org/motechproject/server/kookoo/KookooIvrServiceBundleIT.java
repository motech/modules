package org.motechproject.server.kookoo;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.Assert;
import org.motechproject.security.domain.MotechURLSecurityRule;
import org.motechproject.security.repository.AllMotechSecurityRules;
import org.motechproject.testing.osgi.BaseOsgiIT;
import org.motechproject.testing.utils.PollingHttpClient;
import org.motechproject.testing.utils.TestContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;

public class KookooIvrServiceBundleIT extends BaseOsgiIT {

    private PollingHttpClient httpClient = new PollingHttpClient();

    public void testThatIVRServiceIsAvailableForImport() throws InvalidSyntaxException {
        Assert.assertNotNull(applicationContext.getBean("testKookooIVRService"));
    }

    public void ignoredTestKooKooCallbackUrlIsNotAuthenticated() throws IOException, InterruptedException, InvalidSyntaxException {
        checkSecurity();
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/kookoo/web-api/ivr", TestContext.getJettyPort()));

        HttpResponse response = httpClient.execute(httpGet);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    public void ignoredTestKooKooStatusCallbackUrlIsNotAuthenticated() throws IOException, InterruptedException, InvalidSyntaxException {
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

    private void checkSecurity() throws InvalidSyntaxException, InterruptedException {
        int retries = 0;

        do {
            for (ServiceReference ref : bundleContext.getAllServiceReferences(WebApplicationContext.class.getName() , null)) {
                if (ref.getBundle().getSymbolicName().equals("org.motechproject.motech-platform-web-security")) {
                    WebApplicationContext wsContext = (WebApplicationContext) bundleContext.getService(ref);

                    AllMotechSecurityRules allSecurityRules = wsContext.getBean(AllMotechSecurityRules.class);
                    List<MotechURLSecurityRule> rules = allSecurityRules.getRules();

                    if (rules != null && !rules.isEmpty()) {
                        for (MotechURLSecurityRule rule : rules) {
                            if ("/**/kookoo/web-api/**".equals(rule.getPattern())) {
                                System.out.println("Security rule for Kookoo found");
                                Thread.sleep(2000);
                                return;
                            }
                        }
                        System.out.print("Kookoo security rule unavailable");
                    }
                }
            }

            Thread.sleep(2000);
        } while (retries++ < 5);

        System.out.println("No security rules set up");
    }
}
