package org.motechproject.csd.osgi;

import com.google.inject.Inject;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.motechproject.csd.constants.CSDConstants;
import org.motechproject.csd.domain.CSD;
import org.motechproject.csd.service.CSDService;
import org.motechproject.csd.util.MarshallUtils;
import org.motechproject.testing.osgi.BasePaxIT;
import org.motechproject.testing.osgi.container.MotechNativeTestContainerFactory;
import org.motechproject.testing.utils.TestContext;
import org.ops4j.pax.exam.ExamFactory;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerSuite;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * Verify that CSDEndpoint is present & functional.
 */
@RunWith(PaxExam.class)
@ExamReactorStrategy(PerSuite.class)
@ExamFactory(MotechNativeTestContainerFactory.class)
public class CSDEndpointIT extends BasePaxIT {

    @Inject
    private CSDService csdService;

    @Ignore
    @Test
    public void verifySoapEndpoint() throws IOException, InterruptedException {
        login();

        HttpPost post;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("request.xml")) {
            String xml = IOUtils.toString(in);
            post = new HttpPost(String.format("http://localhost:%d/csd/", TestContext.getJettyPort()));
            post.setEntity(new StringEntity(xml));
        }

        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/soap+xml");

        // TODO: Do something more with the response
        String response = getHttpClient().execute(post, new BasicResponseHandler());
        assertNotNull(response);
        assertTrue(response.contains("env:Envelope"));
    }

    @Ignore
    @Test
    public void verifySoapWSAddressingEndpoint() throws IOException, InterruptedException {
        login();

        HttpPost post;
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("request-wsa.xml")) {
            String xml = IOUtils.toString(in);
            post = new HttpPost(String.format("http://localhost:%d/csd/", TestContext.getJettyPort()));
            post.setEntity(new StringEntity(xml));
        }

        post.setHeader(HttpHeaders.CONTENT_TYPE, "application/soap+xml");

        // TODO: Do something more with the response
        String response = getHttpClient().execute(post, new BasicResponseHandler());
        assertNotNull(response);
        assertTrue(response.contains("env:Envelope"));
    }

    @Test
    public void verifyXmlEndpoint() throws IOException, InterruptedException, JAXBException, SAXException {
        login();

        String response = getHttpClient().get(String.format("http://localhost:%d/csd/csd-getXml",
                        TestContext.getJettyPort()), new BasicResponseHandler());

        assertNotNull(response);
        CSD csd = (CSD) MarshallUtils.unmarshall(response, CSDConstants.CSD_SCHEMA, CSD.class);
        assertNotNull(csd);
        assertNotNull(csd.getServiceDirectory());
    }
}
