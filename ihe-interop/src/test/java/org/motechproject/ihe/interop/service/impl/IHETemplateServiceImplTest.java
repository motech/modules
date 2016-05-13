package org.motechproject.ihe.interop.service.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.motechproject.ihe.interop.service.IHETemplateService;
import org.powermock.modules.junit4.PowerMockRunner;

import java.io.IOException;
import java.net.UnknownHostException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.whenNew;

@RunWith(PowerMockRunner.class)
public class IHETemplateServiceImplTest {

    private final String url = new String("http://someURL:80/xml");
    private final String template = "someTemplate";

    @InjectMocks
    private IHETemplateService iheTemplateService = new IHETemplateServiceImpl();

    @InjectMocks
    private HttpClient httpClient = mock(HttpClient.class);

    @InjectMocks
    private PostMethod postMethod = mock(PostMethod.class);

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = UnknownHostException.class)
    public void shouldFailToConnect() throws IOException {
        iheTemplateService.sendTemplateToRecipientUrl(url, template);
    }

    @Test(expected = UnknownHostException.class)
    public void shouldCreateCorrectPostMethod() throws Exception {

        whenNew(PostMethod.class).withNoArguments().thenReturn(postMethod);
        whenNew(HttpClient.class).withNoArguments().thenReturn(httpClient);
        when(httpClient.executeMethod(postMethod)).thenReturn(200);

        iheTemplateService.sendTemplateToRecipientUrl(new String("http://someURL:80/xml"), "someTemplate");

        assertEquals(new URI(url), postMethod.getURI());
        assertEquals("aplication/xml", postMethod.getRequestEntity().getContentType());
        assertEquals("utf-8", ((StringRequestEntity) postMethod.getRequestEntity()).getCharset());
        assertEquals(template, ((StringRequestEntity) postMethod.getRequestEntity()).getContent());
    }
}
