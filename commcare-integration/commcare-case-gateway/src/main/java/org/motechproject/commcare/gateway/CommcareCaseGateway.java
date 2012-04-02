package org.motechproject.commcare.gateway;

//import org.motechproject.commcare.domain.Case;
//import org.motechproject.commcare.utils.CaseMapper;

import com.thoughtworks.xstream.XStream;
import org.motechproject.commcare.request.*;
import org.motechproject.commcare.request.converter.PregnancyConverter;
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

    public void submitCase(CaseTask task){
        Case ccCase = mapToCase(task);
        String caseXml = convertToXml(ccCase);

        RestTemplate template = new RestTemplate();
        URI uri = template.postForLocation("", caseXml);

    }

    public Case mapToCase(CaseTask task) {
        Case ccCase = createCase(task);
        CreateElement create = createElement(task);
        UpdateElement update = updateElement(task);
        Pregnancy pregnancy = pregnancy(task);

        Index index = new Index();
        index.setPregnancy_id(pregnancy);


        ccCase.setCreate(create);
        ccCase.setUpdate(update);
        ccCase.setIndex(index);

        return ccCase;
    }

    private Case createCase(CaseTask task) {
        Case ccCase = new Case();
        ccCase.setCase_id(task.getCaseId());
        ccCase.setUser_id(task.getUserId());
        ccCase.setDate_Modified(task.getDateModified());
        return ccCase;
    }

    private Pregnancy pregnancy(CaseTask task) {
        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setCase_type(task.getPregnancy().getCase_type());
        pregnancy.setPregnancy_id(task.getPregnancy().getPregnancy_id());
        return pregnancy;
    }

    private UpdateElement updateElement(CaseTask task) {
        UpdateElement update = new UpdateElement();
        update.setTask_id(task.getTaskId());
        update.setDate_eligible(task.getDateEligible());
        update.setDate_expires(task.getDateExpires());
        return update;
    }

    private CreateElement createElement(CaseTask task) {
        CreateElement create = new CreateElement();
        create.setCase_type(task.getCaseType());
        create.setCase_name(task.getCaseName());
        create.setOwner_id(task.getOwnerId());
        return create;
    }

    public String convertToXml(Case ccCase){
        XStream xstream = new XStream();
        xstream.alias("case", Case.class);
        xstream.alias("create", CreateElement.class);
        xstream.alias("update", UpdateElement.class);
        xstream.registerConverter(new PregnancyConverter());
        xstream.alias("index", Index.class);

        xstream.useAttributeFor(Case.class, "case_id");
        xstream.useAttributeFor(Case.class, "user_id");
        xstream.useAttributeFor(Case.class, "xmlns");
        xstream.useAttributeFor(Case.class, "date_modified");

        String caseXml = xstream.toXML(ccCase);
        return  caseXml;
    }
}
