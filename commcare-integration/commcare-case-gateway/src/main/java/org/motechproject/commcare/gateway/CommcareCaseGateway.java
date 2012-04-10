package org.motechproject.commcare.gateway;

//import org.motechproject.commcare.domain.CaseRequest;
//import org.motechproject.commcare.utils.CaseMapper;

import org.motechproject.commcare.request.CaseTask;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 3/26/12
 * Time: 9:54 AM
 * To change this template use File | Settings | File Templates.
 */
public class CommcareCaseGateway{

    private final CaseTaskXmlConverter caseTaskXmlConverter = new CaseTaskXmlConverter();

    public void submitCase(CaseTask task){
        //CaseRequest ccCase = caseTaskXmlConverter.mapToCase(task);
        //String caseXml = caseTaskXmlConverter.convertToXml(ccCase);

        String request = caseTaskXmlConverter.convertToCaseXmlWithEnvelope(task);

        RestTemplate template = new RestTemplate();
        URI uri = template.postForLocation("", request);
    }


}
