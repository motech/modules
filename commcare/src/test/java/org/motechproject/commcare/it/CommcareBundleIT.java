package org.motechproject.commcare.it;

import com.google.gson.JsonParser;
import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commcare.service.CommcareApplicationDataService;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.commcare.service.CommcareUserService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CommcareBundleIT extends BasePaxIT {

    @Inject
    private CommcareUserService commcareUserService;
    @Inject
    private CommcareCaseService commcareCaseService;
    @Inject
    private CommcareFormService commcareFormService;
    @Inject
    private CommcareApplicationDataService commcareApplicationDataService;

    @Test
    public void testSettingsController() throws IOException, InterruptedException {
        login();

        final String response = getHttpClient().get(String.format("http://localhost:%d/commcare/settings",
                TestContext.getJettyPort()), new BasicResponseHandler());

        assertNotNull(response);
        assertTrue(new JsonParser().parse(response).isJsonObject());
    }
}
