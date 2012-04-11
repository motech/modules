package org.motechproject.commcare.gateway;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.domain.CaseTask;
import org.springframework.web.client.RestTemplate;

import java.util.Properties;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareCaseGatewayTest  {

    @Mock
    private CaseTaskXmlConverter caseTaskXmlConverter;

    @Mock
    protected Properties commcareProperties;
    @Mock
    protected RestTemplate restTemplate;

    private CommcareCaseGateway commcareCaseGateway;

    @Before
    public void before(){
        commcareCaseGateway = new CommcareCaseGateway(caseTaskXmlConverter,commcareProperties, restTemplate );
    }

    @Test
    public void shouldFormXmlRequestAndPostItToCommCare(){
        CaseTask task = new CaseTask();
        when(commcareProperties.getProperty("commcare.hq.url")).thenReturn("someUrl");
        when(caseTaskXmlConverter.convertToCaseXmlWithEnvelope(task)).thenReturn("request");
        commcareCaseGateway.submitCase(task);
        verify(caseTaskXmlConverter).convertToCaseXmlWithEnvelope(task);
        verify(restTemplate).postForLocation("someUrl", "request");
    }
}
