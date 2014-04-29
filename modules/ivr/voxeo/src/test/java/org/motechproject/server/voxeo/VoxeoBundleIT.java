package org.motechproject.server.voxeo;

import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.service.contract.IVRService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.utils.server.RequestInfo;
import org.motechproject.testing.utils.server.StubServer;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class VoxeoBundleIT extends BasePaxIT {

    private static final String CONTEXT_PATH = "/SessionControl";
    private StubServer voxeoServer;

    @Inject
    private IVRService voxeoIvrService;

    @Override
    protected Collection<String> getAdditionalTestDependencies() {
        return Arrays.asList("org.motechproject:motech-testing-utils",
                "org.springframework:org.springframework.test",
                "org.mortbay.jetty:com.springsource.org.mortbay.jetty.server",
                "org.mortbay.jetty:com.springsource.org.mortbay.util");
    }

    @Before
    public void setUp() throws Exception {
        voxeoServer = new StubServer(TestContext.getVoxeoPort(), CONTEXT_PATH);
        voxeoServer.start();
    }

    @After
    public void tearDown() throws Exception {
        voxeoServer.stop();
    }

    @Test
    public void testThatVoxeoIVRServiceIsAvailable() {
        assertTrue(voxeoIvrService instanceof VoxeoIVRService);
    }

    @Test
    public void testThatCCXmlGenerationUrlIsAccessible() throws IOException, InterruptedException {
        String response = getHttpClient().get(String.format("http://localhost:%d/voxeo/ccxml",
                TestContext.getJettyPort()), new BasicResponseHandler());
        assertTrue(response.contains("<ccxml version=\"1.0\">"));
    }

    @Test
    public void testThatFlashUrlIsAccessible() throws IOException, InterruptedException {
        HttpResponse response = getHttpClient().get(
                String.format("http://localhost:%d/voxeo/flash?phoneNumber=1233&applicationName=test", TestContext.getJettyPort()));

        assertThatCallRequestWasMadeToVoxeoServer(voxeoServer.detailForRequest(CONTEXT_PATH));

        assertEquals(response.getStatusLine().getStatusCode(), HttpStatus.SC_OK);
    }

    private void assertThatCallRequestWasMadeToVoxeoServer(RequestInfo requestInfo) {
        assertNotNull(requestInfo);
        assertEquals("1233", requestInfo.getQueryParam("phonenum"));
    }
}
