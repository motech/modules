package org.motechproject.commcare.gateway;

import org.motechproject.commcare.domain.CaseTask;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Component
public class CommcareCaseGateway{

    private CaseTaskXmlConverter caseTaskXmlConverter;

    @Autowired
    public CommcareCaseGateway(CaseTaskXmlConverter caseTaskXmlConverter) {
        this.caseTaskXmlConverter = caseTaskXmlConverter;
    }

    public void submitCase(CaseTask task){
        String request = caseTaskXmlConverter.convertToCaseXmlWithEnvelope(task);

        RestTemplate template = new RestTemplate();
        URI uri = template.postForLocation("", request);
    }
}
