package org.motechproject.ihe.interop.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.motechproject.ihe.interop.domain.HL7Recipient;
import org.motechproject.ihe.interop.service.IHETemplateService;

import java.io.IOException;
import java.net.UnknownHostException;

import static org.mockito.MockitoAnnotations.initMocks;

public class IHETemplateServiceImplTest {

    private final String url = new String("http://someURL/xml");
    private final String template = "someTemplate";
    private final HL7Recipient recipient = new HL7Recipient("someName", url, "someUsername", "somePassword");

    @InjectMocks
    private IHETemplateService iheTemplateService = new IHETemplateServiceImpl();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = UnknownHostException.class)
    public void shouldFailToConnect() throws IOException {
        iheTemplateService.sendTemplateToRecipientUrl(url, template);
    }

    @Test(expected = UnknownHostException.class)
    public void shouldFailToConnectWithBasicAuthentication() throws  IOException {
        iheTemplateService.sendTemplateToRecipientUrlWithBasicAuthentication(recipient, template);
    }
}
