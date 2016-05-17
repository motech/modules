package org.motechproject.ihe.interop.service.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.ihe.interop.service.IHETemplateService;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IHETemplateServiceImplTest {

    private final String url = new String("http://someURL/xml");
    private final String template = "someTemplate";

    @InjectMocks
    private IHETemplateService iheTemplateService = new IHETemplateServiceImpl();

    @Mock
    private HttpClient httpClient;

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test
    public void shouldCreateCorrectPostMethod() throws IOException {

        PostMethod postMethod = new PostMethod(url);

        when(httpClient.executeMethod(postMethod)).thenReturn(200);

        iheTemplateService.sendTemplateToRecipientUrl("someTemplate", postMethod);

        assertEquals(new URI(url), postMethod.getURI());
        assertEquals("application/xml; charset=utf-8", postMethod.getRequestEntity().getContentType());
        assertEquals(template, ((StringRequestEntity) postMethod.getRequestEntity()).getContent());
    }
}
