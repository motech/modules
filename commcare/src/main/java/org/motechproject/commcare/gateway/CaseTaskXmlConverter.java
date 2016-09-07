package org.motechproject.commcare.gateway;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.naming.NoNameCoder;
import com.thoughtworks.xstream.io.xml.DomDriver;
import org.joda.time.DateTime;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.commcare.domain.IndexTask;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.exception.MalformedCaseXmlException;
import org.motechproject.commcare.request.CaseRequest;
import org.motechproject.commcare.request.CloseElement;
import org.motechproject.commcare.request.CommcareRequestData;
import org.motechproject.commcare.request.CreateElement;
import org.motechproject.commcare.request.IndexSubElement;
import org.motechproject.commcare.request.MetaElement;
import org.motechproject.commcare.request.converter.CloseElementConverter;
import org.motechproject.commcare.request.converter.CreateElementConverter;
import org.motechproject.commcare.request.converter.IndexElementConverter;
import org.motechproject.commcare.request.converter.UpdateElementConverter;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

/**
 * Provides methods for serializing instances of the {@link CaseTask} class into XML strings.
 */
@Component
public class CaseTaskXmlConverter {

    private EventRelay eventRelay;

    @Autowired
    public CaseTaskXmlConverter(EventRelay eventRelay) {
        this.eventRelay = eventRelay;
    }

    /**
     * Serializes the given {@code task} into an XML string.
     *
     * @param task  the task to serialize
     * @return the XML string
     */
    public String convertToCaseXml(CaseTask task) {
        CaseRequest caseRequest = mapToCase(task);
        CommcareRequestData request = createRequestWithEnvelope(caseRequest);

        return convertToXml(request);
    }

    /**
     * Serializes the given {@code task} into an XML string. The XML will contain a "</close>" element.
     *
     * @param task  the task to serialize
     * @return the XML string
     */
    public String convertToCloseCaseXml(CaseTask task) {
        CaseRequest caseRequest = mapToCloseCase(task);
        CommcareRequestData request = createRequestWithEnvelope(caseRequest);
        return convertToCloseXml(request);
    }

    private CaseRequest mapToCloseCase(CaseTask task) {
        CaseRequest ccCase = createCase(task);
        CloseElement close = new CloseElement();
        ccCase.setCloseElement(close);
        return ccCase;
    }

    private CaseRequest mapToCase(CaseTask task) {
        CaseRequest ccCase = createCase(task);

        if (task.getCreateTask() != null) {
            CreateElement create = new CreateElement(task.getCreateTask()
                    .getCaseType(), task.getCreateTask().getCaseName(), task
                    .getCreateTask().getOwnerId());
            ccCase.setCreateElement(create);
        }

        if (task.getUpdateTask() != null) {
            ccCase.setUpdateElement(task.getUpdateTask());
        }

        if (task.getIndexTask() != null) {
            List<IndexSubElement> subElements = task.getIndexTask()
                    .getIndices();
            IndexTask indexElement = new IndexTask(subElements);
            ccCase.setIndexElement(indexElement);
        }

        if (task.getCloseTask() != null) {
            ccCase.setCloseElement(new CloseElement());
        }

        return ccCase;
    }

    private CaseRequest createCase(CaseTask task) {
        String caseId;

        if (task.getCreateTask() != null && task.getCaseId() == null) {
            caseId = UUID.randomUUID().toString();
        } else {
            caseId = task.getCaseId();
        }

        if (task.getDateModified() == null) {
            task.setDateModified(DateTime.now().toString());
        }

        return new CaseRequest(caseId, task.getUserId(),
                task.getDateModified(), task.getXmlns());
    }

    private String convertToCloseXml(CommcareRequestData request) {
        XStream xstream = mapEnvelope();
        xstream.aliasField("close", CaseRequest.class, "closeElement");

        return xstream.toXML(request);
    }

    private String convertToXml(CommcareRequestData request) {
        try {

            XStream xstream = mapEnvelope();

            if (request.getCcCase().getCreateElement() != null) {
                xstream.registerConverter(new CreateElementConverter());
                xstream.aliasField("create", CaseRequest.class, "createElement");
            } else {
                xstream.omitField(CaseRequest.class, "createElement");
            }

            if (request.getCcCase().getUpdateElement() != null) {
                xstream.registerConverter(new UpdateElementConverter());
                xstream.aliasField("update", CaseRequest.class, "updateElement");
            } else {
                xstream.omitField(CaseRequest.class, "updateElement");
            }

            IndexTask indexElement = request.getCcCase().getIndexElement();

            if (indexElement != null && indexElement.getIndices().size() > 0) {
                xstream.registerConverter(new IndexElementConverter());
                xstream.aliasField("index", CaseRequest.class, "indexElement");
            } else {
                xstream.omitField(CaseRequest.class, "indexElement");
            }

            if (request.getCcCase().getCloseElement() != null) {
                xstream.registerConverter(new CloseElementConverter());
                xstream.aliasField("close", CaseRequest.class, "closeElement");
            } else {
                xstream.omitField(CaseRequest.class, "closeElement");
            }

            xstream.omitField(CaseRequest.class, "dataXmlns");

            return xstream.toXML(request);
        } catch (MalformedCaseXmlException e) {
            MotechEvent motechEvent = new MotechEvent(
                    EventSubjects.MALFORMED_CASE_EXCEPTION);
            motechEvent.getParameters().put(EventDataKeys.MESSAGE,
                    e.getMessage());
            eventRelay.sendEventMessage(motechEvent);
        }

        return null;
    }

    private XStream mapEnvelope() {
        XStream xstream = new XStream(new DomDriver("UTF-8", new NoNameCoder()));

        xstream.alias("data", CommcareRequestData.class);
        xstream.useAttributeFor(CommcareRequestData.class, "xmlns");
        xstream.aliasField("case", CommcareRequestData.class, "ccCase");

        xstream.alias("meta", MetaElement.class);
        xstream.useAttributeFor(MetaElement.class, "xmlns");
        xstream.useAttributeFor(CaseRequest.class, "caseId");
        xstream.aliasField("case_id", CaseRequest.class, "caseId");
        xstream.useAttributeFor(CaseRequest.class, "userId");
        xstream.aliasField("user_id", CaseRequest.class, "userId");
        xstream.useAttributeFor(CaseRequest.class, "xmlns");
        xstream.useAttributeFor(CaseRequest.class, "dateModified");
        xstream.aliasField("date_modified", CaseRequest.class, "dateModified");

        return xstream;
    }

    private CommcareRequestData createRequestWithEnvelope(
            CaseRequest caseRequest) {
        MetaElement metaElement = new MetaElement(
                null, UUID.randomUUID().toString(),
                caseRequest.getDateModified(), caseRequest.getDateModified(),
                caseRequest.getUserId());
        return new CommcareRequestData(caseRequest.getDataXmlns() != null ? caseRequest.getDataXmlns() :
                "http://openrosa.org/jr/xforms", metaElement, caseRequest);
    }
}
