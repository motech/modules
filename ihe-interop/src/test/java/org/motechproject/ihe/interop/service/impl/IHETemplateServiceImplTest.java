package org.motechproject.ihe.interop.service.impl;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.motechproject.ihe.interop.service.IHETemplateService;

import java.io.IOException;
import java.net.UnknownHostException;

import static org.mockito.MockitoAnnotations.initMocks;
import static org.powermock.api.mockito.PowerMockito.mock;

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
}
