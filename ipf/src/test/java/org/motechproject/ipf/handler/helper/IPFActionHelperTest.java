package org.motechproject.ipf.handler.helper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.ipf.domain.IPFRecipient;
import org.motechproject.ipf.domain.IPFTemplate;
import org.motechproject.ipf.exception.RecipientNotFoundException;
import org.motechproject.ipf.exception.TemplateNotFoundException;
import org.motechproject.ipf.service.IPFRecipientsService;
import org.motechproject.ipf.service.IPFTemplateDataService;
import org.motechproject.ipf.util.Constants;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IPFActionHelperTest {

    @Mock
    private IPFRecipientsService ipfRecipientsService;

    @Mock
    private IPFTemplateDataService ipfTemplateDataService;

    @Mock
    private IPFTemplate ipfTemplate;

    @Mock
    private IPFRecipient ipfRecipient;

    @InjectMocks
    private IPFActionHelper ipfActionHelper = new IPFActionHelper();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = TemplateNotFoundException.class)
    public void shouldThrowExceptionWhenTemplateNotFound() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.TEMPLATE_NAME_PARAM, "sampleTemplate2");
        params.put(Constants.RECIPIENT_NAME_PARAM, "sampleRecipient2");

        ipfActionHelper.handleAction(params);
    }

    @Test(expected = RecipientNotFoundException.class)
    public void shouldThrowExceptionWhenRecipientNotFound() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.TEMPLATE_NAME_PARAM, "sampleTemplate3");
        params.put(Constants.RECIPIENT_NAME_PARAM, "sampleRecipient3");

        when(ipfTemplateDataService.findByName("sampleTemplate3")).thenReturn(ipfTemplate);
        ipfActionHelper.handleAction(params);
    }
}
