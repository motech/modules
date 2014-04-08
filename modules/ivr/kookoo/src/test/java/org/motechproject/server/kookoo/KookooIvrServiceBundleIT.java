package org.motechproject.server.kookoo;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.service.contract.IVRService;
import org.motechproject.security.domain.MotechURLSecurityRule;
import org.motechproject.security.repository.AllMotechSecurityRules;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.helper.ServiceRetriever;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.springframework.web.context.WebApplicationContext;

import javax.inject.Inject;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class KookooIvrServiceBundleIT extends BasePaxIT {

    @Inject
    private IVRService kookooIvrService;
    @Inject
    private BundleContext bundleContext;

    @Override
    protected boolean startHttpServer() {
        return true;
    }

    @Test
    public void testThatIVRServiceIsAvailableForImport() throws InvalidSyntaxException {
        assertTrue(kookooIvrService instanceof KookooCallServiceImpl);
    }

    @Test
    public void testKooKooCallbackUrlIsNotAuthenticated() throws IOException, InterruptedException, InvalidSyntaxException {
        checkSecurity();

        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/kookoo/web-api/ivr",
                TestContext.getJettyPort()));

        HttpResponse response = getHttpClient().execute(httpGet);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testKooKooStatusCallbackUrlIsNotAuthenticated() throws IOException, InterruptedException, InvalidSyntaxException {
        checkSecurity();

        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/kookoo/web-api/ivr/callstatus",
                TestContext.getJettyPort()));

        HttpResponse response = getHttpClient().execute(httpGet);

        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    private void checkSecurity() throws InvalidSyntaxException, InterruptedException {
        WebApplicationContext wsContext = ServiceRetriever.getWebAppContext(bundleContext,
                "org.motechproject.motech-platform-web-security");

        AllMotechSecurityRules allSecurityRules = wsContext.getBean(AllMotechSecurityRules.class);
        List<MotechURLSecurityRule> rules = allSecurityRules.getRules();

        if (rules != null && !rules.isEmpty()) {
            for (MotechURLSecurityRule rule : rules) {
                if ("/**/kookoo/web-api/**".equals(rule.getPattern())) {
                    getLogger().info("Security rule for Kookoo found");
                    Thread.sleep(2000);
                    return;
                }
            }
            getLogger().error("Kookoo security rule unavailable");
        }
    }
}
