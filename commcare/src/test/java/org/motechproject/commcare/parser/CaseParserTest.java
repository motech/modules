package org.motechproject.commcare.parser;

import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.motechproject.commcare.domain.CaseXml;
import org.motechproject.commcare.exception.CaseParserException;

import java.io.FileNotFoundException;

import static org.junit.Assert.assertEquals;


public class CaseParserTest {

    @Test
    public void shouldParseCaseAttributesCorrectly() throws FileNotFoundException, CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, caseXml(false));
        CaseXml aCase = parser.parseCase();

        assertEquals("3F2504E04F8911D39A0C0305E82C3301",
                aCase.getCaseId());
        assertEquals("2011-12-08T13:34:30", aCase.getDateModified());
        assertEquals("F0183EDA012765103CB106821BBA51A0",
                aCase.getUserId());
        assertEquals("2Z2504E04F8911D39A0C0305E82C3000",
                aCase.getOwnerId());
    }

    @Test
    public void shouldParseCreateAttributesCorrectly() throws FileNotFoundException, CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, caseXml(false));
        CaseXml aCase = parser.parseCase();
        assertEquals("houshold_rollout_ONICAF", aCase.getCaseType());
        assertEquals("Smith", aCase.getCaseName());
    }

    @Test
    public void shouldParseCaseAttributesCorrectlyForCaseWithNamespace() throws FileNotFoundException, CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, caseXml(true));
        CaseXml aCase = parser.parseCase();

        assertEquals("3F2504E04F8911D39A0C0305E82C3301",
                aCase.getCaseId());
        assertEquals("2011-12-08T13:34:30", aCase.getDateModified());
        assertEquals("F0183EDA012765103CB106821BBA51A0",
                aCase.getUserId());
        assertEquals("2Z2504E04F8911D39A0C0305E82C3000",
                aCase.getOwnerId());
    }

    @Test
    public void shouldParseCreateAttributesCorrectlyForCaseWithNamespace() throws FileNotFoundException, CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, caseXml(true));
        CaseXml aCase = parser.parseCase();
        assertEquals("houshold_rollout_ONICAF", aCase.getCaseType());
        assertEquals("Smith", aCase.getCaseName());
    }

    @Test
    public void shouldFetchAPIKeyFromCaseXML() throws FileNotFoundException, CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, caseXml(false));
        CaseXml aCase = parser.parseCase();
        assertEquals("API_KEY", aCase.getApiKey());
    }

    @Test
    public void shouldParseCloseAttributesCorrectly() throws FileNotFoundException, CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class,
                caseXmlForClose());
        CaseXml aCase = parser.parseCase();
        assertEquals("CLOSE", parser.getCaseAction());
        assertEquals("3F2504E04F8911D39A0C0305E82C3301", aCase.getCaseId());
    }

    @Test
    public void shouldSetActionCorrectly() throws FileNotFoundException, CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, caseXml(false));
        CaseXml aCase = parser.parseCase();
        assertEquals("CREATE", aCase.getAction());
    }

    @Test
    public void shouldParseIndexElementCorrectly() throws FileNotFoundException, CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, childXml());
        CaseXml aCase = parser.parseCase();
        assertEquals("45134cf7-90f8-4284-8ca1-16392fc0ce57",
                aCase.getCaseId());
        assertEquals("2012-04-03", aCase.getDateModified());
        assertEquals("d823ea3d392a06f8b991e9e4933348bd",
                aCase.getUserId());
        assertEquals("d823ea3d392a06f8b991e9e49394ce45",
                aCase.getOwnerId());
        assertEquals("motherCaseId",
                aCase.getFieldValues().get("mother_id"));
    }

    @Test
    public void shouldParseDataXmlns() throws CaseParserException {
        CaseParser<CaseXml> parser = new CaseParser<>(CaseXml.class, caseXmlWithDataTag());
        CaseXml aCase = parser.parseCase();

        assertEquals("3F2504E04F8911D39A0C0305E82C3301", aCase.getCaseId());
        assertEquals("2011-12-08T13:34:30", aCase.getDateModified());
        assertEquals("F0183EDA012765103CB106821BBA51A0", aCase.getUserId());
        assertEquals("2Z2504E04F8911D39A0C0305E82C3000", aCase.getOwnerId());

        assertEquals("http://myDomain.com/test/ns", aCase.getCaseDataXmlns());
    }

    private String caseXml(boolean withNamespace) {
        String namespace = withNamespace ? "n0:" : StringUtils.EMPTY;

        String caseXml = "<" + namespace + "case xmlns" + (withNamespace ? ":n0" : StringUtils.EMPTY) + "=\"http://commcarehq.org/case/transaction/v2\" case_id=\"3F2504E04F8911D39A0C0305E82C3301\" user_id=\"F0183EDA012765103CB106821BBA51A0\" date_modified=\"2011-12-08T13:34:30\" api_key=\"API_KEY\" >\n"
                + "<" + namespace + "create>"
                + "<"+ namespace + "case_type>houshold_rollout_ONICAF</"+ namespace + "case_type>"
                + "<"+ namespace + "case_name>Smith</" + namespace + "case_name>"
                + "<"+ namespace + "owner_id>2Z2504E04F8911D39A0C0305E82C3000</" + namespace + "owner_id>"
                + "</"+ namespace + "create>"
                + "<"+ namespace + "update>"
                + "<"+ namespace + "household_id>24/F23/3</"+ namespace + "household_id>"
                + "<"+ namespace + "primary_contact_name>Tom Smith</" + namespace + "primary_contact_name>"
                + "<"+ namespace + "visit_number>1</"+ namespace + "visit_number>" + "</" + namespace + "update>" + "</" + namespace + "case>";

        return caseXml;
    }

    private String caseXmlForClose() {
        return "<?xml version=\"1.0\"?>"
                + "<case xmlns=\"http://commcarehq.org/case/transaction/v2\" case_id=\"3F2504E04F8911D39A0C0305E82C3301\" date_modified=\"2012-04-03\" user_id=\"F0183EDA012765103CB106821BBA51A0\">"
                + "    <close />" + "</case>";
    }

    private String caseXmlWithDataTag() {
        return "<data xmlns=\"http://myDomain.com/test/ns\"><meta><userID>fb6e0b19cbe3ef683a10c4c4766a1ef3</userID></meta>"
                + caseXml(false) + "</data>";
    }

    private String childXml() {
        String caseXml = "<case xmlns=\"http://commcarehq.org/case/transaction/v2\" case_id=\"45134cf7-90f8-4284-8ca1-16392fc0ce57\" date_modified=\"2012-04-03\" user_id=\"d823ea3d392a06f8b991e9e4933348bd\">\n"
                + "<create>"
                + "<case_type>cc_bihar_newborn</case_type>"
                + "<case_name>RAM</case_name>"
                + "<owner_id>d823ea3d392a06f8b991e9e49394ce45</owner_id>"
                + "</create>"
                + "<update>"
                + "<gender>male</gender>"
                + "</update>"
                + "<index>"
                + "<mother_id case_type=\"cc_bihar_pregnancy\">motherCaseId</mother_id>"
                + "</index>" + "</case>";

        return caseXml;
    }
}
