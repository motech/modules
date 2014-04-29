package org.motechproject.server.verboice;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.ivr.service.contract.IVRService;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.TestContext;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.InvalidSyntaxException;

import javax.inject.Inject;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class VerboiceBundleIT extends BasePaxIT {

    @Inject
    private IVRService verboiceIvrService;

    @Test
    public void testThatVerboiceIvrServicesIsAvailableOnImport() throws InvalidSyntaxException {
        assertTrue(verboiceIvrService instanceof VerboiceIVRService);
    }

    @Test
    public void testVerboiceCallBackAuthenticationSuccess() throws IOException, InterruptedException {
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/verboice/web-api/ivr?CallSid=123&motech_call_id=ABC",
                TestContext.getJettyPort()));
        addAuthHeader(httpGet, "motech", "motech");

        HttpResponse response = getHttpClient().execute(httpGet);

        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testVerboiceCallBackAuthenticationFailed() throws IOException, InterruptedException {
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/verboice/web-api/ivr?CallSid=123&motech_call_id=ABC",
                TestContext.getJettyPort()));
        addAuthHeader(httpGet, "bad", "user");

        HttpResponse response = getHttpClient().execute(httpGet, HttpStatus.SC_UNAUTHORIZED);

        assertNotNull(response);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testVerboiceStatusCallBackAuthenticationSuccess() throws IOException, InterruptedException {
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/verboice/web-api/ivr/callstatus?CallSid=123&motech_call_id=ABC",
                TestContext.getJettyPort()));
        addAuthHeader(httpGet, "motech", "motech");

        HttpResponse response = getHttpClient().execute(httpGet);

        assertNotNull(response);
        assertEquals(HttpStatus.SC_OK, response.getStatusLine().getStatusCode());
    }

    @Test
    public void testVerboiceStatusCallBackAuthenticationFailed() throws IOException, InterruptedException {
        HttpGet httpGet = new HttpGet(String.format("http://localhost:%d/verboice/web-api/ivr/callstatus?CallSid=123&motech_call_id=ABC",
                TestContext.getJettyPort()));
        addAuthHeader(httpGet, "bad", "user");

        HttpResponse response = getHttpClient().execute(httpGet, HttpStatus.SC_UNAUTHORIZED);

        assertNotNull(response);
        assertEquals(HttpStatus.SC_UNAUTHORIZED, response.getStatusLine().getStatusCode());
    }

    private void addAuthHeader(HttpGet httpGet, String userName, String password) {
        httpGet.addHeader("Authorization", "Basic " + new String(Base64.encodeBase64((userName + ":" + password).getBytes())));
    }
}
