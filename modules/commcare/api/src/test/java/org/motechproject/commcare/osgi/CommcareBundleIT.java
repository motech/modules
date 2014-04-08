package org.motechproject.commcare.osgi;

import com.google.gson.JsonParser;
import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.commcare.service.CommcareCaseService;
import org.motechproject.commcare.service.CommcareFormService;
import org.motechproject.commcare.service.CommcareUserService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class CommcareBundleIT extends BasePaxIT {

    @Inject
    private CommcareUserService commcareUserService;
    @Inject
    private CommcareCaseService commcareCaseService;
    @Inject
    private CommcareFormService commcareFormService;

    @Override
    protected boolean startHttpServer() {
        return true;
    }

    @Test
    public void testServices() {
        assertNotNull(commcareCaseService);
        assertNotNull(commcareFormService);
        assertNotNull(commcareUserService);
    }

    @Test
    public void testSettingsController() throws IOException, InterruptedException {
        final String response = getHttpClient().get(String.format("http://localhost:%d/commcare/settings",
                TestContext.getJettyPort()), new BasicResponseHandler());

        assertNotNull(response);
        assertTrue(new JsonParser().parse(response).isJsonObject());
    }
}
