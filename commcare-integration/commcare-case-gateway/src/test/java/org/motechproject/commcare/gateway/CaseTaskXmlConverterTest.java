package org.motechproject.commcare.gateway;

import org.joda.time.DateTimeZone;
import org.junit.Test;
import org.motechproject.commcare.domain.CaseTask;
import org.motechproject.util.DateUtil;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CaseTaskXmlConverterTest {

    private final String ownerId = "1b23abcaa82aff8215a831";
    private final String caseType = "task";
    private final String pregnancyCaseType = "pregnancy";

    @Test
    public void shouldConvertToCaseXmlWithEnvelopeCorrectly() throws ParserConfigurationException, IOException, SAXException {
        String motherCaseId = "3F2504E04F8911D39A0C0305E82C3301";
        String randomGeneratedCaseId = "3F2504E04F8911D39A0C0305E82C3300";
        String taskId = "tt2";
        String dateExpires = DateUtil.today().plusDays(5).toString();
        String dateEligible = DateUtil.today().toString();
        String currentTime = DateUtil.now().toDateTime(DateTimeZone.UTC).toString();
        CaseTask task = createCaseTask(randomGeneratedCaseId, motherCaseId, taskId, dateEligible,  dateExpires, currentTime);

        String caseXmlWithEnvelope = new CaseTaskXmlConverter().convertToCaseXmlWithEnvelope(task);

        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new InputSource(new StringReader(caseXmlWithEnvelope)));


        assertEquals("data", (doc).getDocumentElement().getTagName());
        assertEquals("http://bihar.commcarehq.org/pregnancy/task", (doc).getDocumentElement().getAttribute("xmlns"));

        Element meta = (Element)doc.getElementsByTagName("meta").item(0);
        assertEquals("http://openrosa.org/jr/xforms", meta.getAttribute("xmlns"));
        assertEquals(currentTime , meta.getElementsByTagName("timeStart").item(0).getTextContent());
        assertEquals(currentTime, meta.getElementsByTagName("timeEnd").item(0).getTextContent());
        assertEquals(CaseTaskXmlConverter.motechUserId, meta.getElementsByTagName("userID").item(0).getTextContent());
        assertNotNull(meta.getElementsByTagName("instanceID").item(0).getTextContent());

        Element caseEle = (Element)doc.getElementsByTagName("case").item(0);
        assertEquals(randomGeneratedCaseId, caseEle.getAttribute("case_id"));
        assertEquals(currentTime, caseEle.getAttribute("date_modified"));
        assertEquals(CaseTaskXmlConverter.motechUserId, caseEle.getAttribute("user_id"));
        assertEquals("http://commcarehq.org/case/transaction/v2", caseEle.getAttribute("xmlns"));

        Element createEle = (Element)caseEle.getElementsByTagName("create").item(0);
        assertEquals(caseType, createEle.getElementsByTagName("case_type").item(0).getTextContent());
        assertEquals("TT 2", createEle.getElementsByTagName("case_name").item(0).getTextContent());
        assertEquals(ownerId, createEle.getElementsByTagName("owner_id").item(0).getTextContent());

        Element updateEle = (Element)caseEle.getElementsByTagName("update").item(0);
        assertEquals(taskId, updateEle.getElementsByTagName("task_id").item(0).getTextContent());
        assertEquals(dateEligible, updateEle.getElementsByTagName("date_eligible").item(0).getTextContent());
        assertEquals(dateExpires, updateEle.getElementsByTagName("date_expires").item(0).getTextContent());

        Element indexEle = (Element)caseEle.getElementsByTagName("index").item(0);
        Element motherEle = (Element)indexEle.getElementsByTagName("mother_id").item(0);
        assertEquals(motherCaseId, motherEle.getTextContent());
        assertEquals(pregnancyCaseType, motherEle.getAttribute("case_type"));
    }

    private CaseTask createCaseTask(String caseId, String motherCaseId, String taskId, String dateEligible, String dateExpires, String currentTime) {
        CaseTask caseTask = new CaseTask();
        caseTask.setCaseId(caseId);
        caseTask.setCaseName("TT 2");
        caseTask.setOwnerId(ownerId);
        caseTask.setCurrentTime(currentTime);
        caseTask.setUserId(CaseTaskXmlConverter.motechUserId);
        caseTask.setTaskId(taskId);
        caseTask.setClientCaseId(motherCaseId);
        caseTask.setClientCaseType(pregnancyCaseType);
        caseTask.setDateEligible(dateEligible);
        caseTask.setDateExpires(dateExpires);
        return caseTask;
    }


}
