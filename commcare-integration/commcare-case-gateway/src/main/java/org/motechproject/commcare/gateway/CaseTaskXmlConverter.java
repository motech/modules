package org.motechproject.commcare.gateway;

import com.thoughtworks.xstream.XStream;
import org.motechproject.commcare.request.*;
import org.motechproject.commcare.request.converter.PregnancyConverter;
import org.motechproject.util.DateUtil;

import java.util.UUID;

public class CaseTaskXmlConverter {

     CaseRequest mapToCase(CaseTask task) {
        CaseRequest ccCase = createCase(task);
        CreateElement create = createElement(task);
        UpdateElement update = updateElement(task);
        Pregnancy pregnancy = pregnancy(task);

        Index index = new Index(pregnancy);

        ccCase.setCreateElement(create);
        ccCase.setUpdateElement(update);
        ccCase.setIndex(index);

        return ccCase;
    }

    private CaseRequest createCase(CaseTask task) {
        return new CaseRequest(task.getCaseId(),task.getUserId(),task.getDateModified());
    }

    private Pregnancy pregnancy(CaseTask task) {
        if(task.getPregnancy() == null)
            return  null;
        return new Pregnancy(task.getPregnancy().getPregnancy_id(),task.getPregnancy().getCase_type());
    }

    private UpdateElement updateElement(CaseTask task) {
        return new UpdateElement(task.getTaskId(),task.getDateEligible(),task.getDateExpires());
    }

    /*String convertToXml(CaseRequest ccCase){
        XStream xstream = new XStream();
        xstream.alias("case", CaseRequest.class);
        xstream.alias("create", CreateElement.class);
        xstream.alias("update", UpdateElement.class);

        xstream.registerConverter(new PregnancyConverter());
        xstream.alias("index", Index.class);

        xstream.useAttributeFor(CaseRequest.class, "case_id");
        xstream.useAttributeFor(CaseRequest.class, "user_id");
        xstream.useAttributeFor(CaseRequest.class, "xmlns");
        xstream.useAttributeFor(CaseRequest.class, "date_modified");

        String caseXml = xstream.toXML(ccCase);
        return  caseXml;
    }*/

    CreateElement createElement(CaseTask task) {

        return new CreateElement(task.getCaseType(),task.getCaseName(),task.getOwnerId());
    }

    public String convertToCaseXmlWithEnvelope(CaseTask task) {
        CaseRequest caseRequest = mapToCase(task);
        CommcareRequestData request = createRequestWithEnvelope(caseRequest);

        return convertToXml(request);
    }

    private String convertToXml(CommcareRequestData request) {

        XStream xstream = new XStream();

        xstream.alias("data", CommcareRequestData.class);
        xstream.useAttributeFor(CommcareRequestData.class, "xmlns");

        xstream.alias("meta", MetaElement.class);
        xstream.useAttributeFor(MetaElement.class, "xmlns");

        xstream.alias("case", CaseRequest.class);
        xstream.alias("case", CaseRequest.class);
        xstream.alias("create", CreateElement.class);
        xstream.alias("update", UpdateElement.class);

        xstream.registerConverter(new PregnancyConverter());
        xstream.alias("index", Index.class);

        xstream.useAttributeFor(CaseRequest.class, "case_id");
        xstream.useAttributeFor(CaseRequest.class, "user_id");
        xstream.useAttributeFor(CaseRequest.class, "xmlns");
        xstream.useAttributeFor(CaseRequest.class, "date_modified");

        return xstream.toXML(request);
    }

    private CommcareRequestData createRequestWithEnvelope(CaseRequest caseRequest) {

        return new CommcareRequestData("http://bihar.commcarehq.org/pregnancy/task\"", createMetaElement(),caseRequest);
    }

    private MetaElement createMetaElement() {
        return new MetaElement("http://openrosa.org/jr/xforms", UUID.randomUUID().toString(), DateUtil.now().toString(), DateUtil.now().toString(), "ananya-care");
    }

}