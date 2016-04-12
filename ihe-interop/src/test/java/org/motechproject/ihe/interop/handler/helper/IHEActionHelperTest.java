package org.motechproject.ihe.interop.handler.helper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.motechproject.ihe.interop.domain.HL7Recipient;
import org.motechproject.ihe.interop.exception.RecipientNotFoundException;
import org.motechproject.ihe.interop.exception.TemplateNotFoundException;
import org.motechproject.ihe.interop.service.HL7RecipientsService;
import org.motechproject.ihe.interop.service.IHETemplateDataService;
import org.motechproject.ihe.interop.util.Constants;
import org.motechproject.ihe.interop.domain.CdaTemplate;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class IHEActionHelperTest {

    @Mock
    private HL7RecipientsService hl7RecipientsService;

    @Mock
    private IHETemplateDataService iheTemplateDataService;

    @Mock
    private CdaTemplate cdaTemplate;

    @Mock
    private HL7Recipient hl7Recipient;

    @InjectMocks
    private IHEActionHelper iheActionHelper = new IHEActionHelper();

    @Before
    public void setUp() {
        initMocks(this);
    }

    @Test(expected = TemplateNotFoundException.class)
    public void shouldThrowExceptionWhenTemplateNotFound() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.TEMPLATE_NAME_PARAM, "sampleTemplate2");
        params.put(Constants.RECIPIENT_NAME_PARAM, "sampleRecipient2");

        iheActionHelper.handleAction(params);
    }

    @Test(expected = RecipientNotFoundException.class)
    public void shouldThrowExceptionWhenRecipientNotFound() throws ClassNotFoundException, ParserConfigurationException, SAXException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put(Constants.TEMPLATE_NAME_PARAM, "sampleTemplate3");
        params.put(Constants.RECIPIENT_NAME_PARAM, "sampleRecipient3");

        when(iheTemplateDataService.findByName("sampleTemplate3")).thenReturn(cdaTemplate);
        iheActionHelper.handleAction(params);
    }
}
