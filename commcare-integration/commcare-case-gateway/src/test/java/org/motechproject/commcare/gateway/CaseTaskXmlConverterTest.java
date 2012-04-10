package org.motechproject.commcare.gateway;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.motechproject.commcare.request.*;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 4/1/12
 * Time: 6:23 PM
 * To change this template use File | Settings | File Templates.
 */
public class CaseTaskXmlConverterTest extends TestCase {

    /*public void testShouldConvertToCaseXmlCorrectly(){

        CaseRequest ccCase = createCase();

        CaseTaskXmlConverter taskXmlConverter = new CaseTaskXmlConverter();
        String caseXml = taskXmlConverter.convertToXml(ccCase);

        Assert.assertEquals(caseXml(),caseXml);
    }*/

    public void testShouldConvertToCaseXmlWithEnvelopeCorrectly(){
        CaseRequest ccCase = createCase();
        CaseTaskXmlConverter taskXmlConverter = new CaseTaskXmlConverter();
        String caseXmlWithEnvelope = caseXmlWithEnvelope();
        Assert.assertEquals(caseXmlWithEnvelope(),caseXmlWithEnvelope);

    }

    private CaseRequest createCase() {
        CreateElement create = createElement();
        UpdateElement update = updateElement();
        Pregnancy pregnancy = pregnancyElement();

        Index index = new Index(pregnancy);

        CaseRequest ccCase = new CaseRequest("3F2504E04F8911D39A0C0305E82C3301","F0183EDA012765103CB106821BBA51A0","2011-12-09T13:34:30",create,update);
        ccCase.setIndex(index);
        return ccCase;
    }


    private Pregnancy pregnancyElement() {
        Pregnancy pregnancy = new Pregnancy("pregnancy","3F2504E04F8911D39A0C0305E82C3301");
        return pregnancy;
    }

    private UpdateElement updateElement() {
        UpdateElement update = new UpdateElement("tt2","2011-12-03","2012-03-03");
        return update;
    }

    private CreateElement createElement() {
        CreateElement create = new CreateElement("task","Tetanus 2","1b23abcaa82aff8215a831");
        return create;
    }


    private String caseXml() {
            return "<case user__id=\"F0183EDA012765103CB106821BBA51A0\" xmlns=\"http://commcarehq.org/case/transaction/v2\" date__modified=\"2011-12-09T13:34:30\" case__id=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                    "  <create>\n" +
                    "    <case__type>task</case__type>\n" +
                    "    <case__name>Tetanus 2</case__name>\n" +
                    "    <owner__id>1b23abcaa82aff8215a831</owner__id>\n" +
                    "  </create>\n" +
                    "  <update>\n" +
                    "    <task__id>tt2</task__id>\n" +
                    "    <date__eligible>2011-12-03</date__eligible>\n" +
                    "    <date__expires>2012-03-03</date__expires>\n" +
                    "  </update>\n" +
                    "  <index>\n" +
                    "    <pregnancy__id case__type=\"pregnancy\">3F2504E04F8911D39A0C0305E82C3301</pregnancy__id>\n" +
                    "  </index>\n" +
                    "</case>";
    }

    private String caseXmlWithEnvelope() {
            return "<data xmlns=\"http://bihar.commcarehq.org/pregnancy/task\">\n" +
                    "    <meta xmlns=\"http://openrosa.org/jr/xforms\">\n" +
                    "        <instanceID>PYV0ULC6SOXE5LR20F2CWBAG1</instanceID>\n" +
                    "        <timeStart>2012-04-03T17:37:06Z</timeStart>\n" +
                    "        <timeEnd>2012-04-03T17:38:14Z</timeEnd>\n" +
                    "        <userID>b0dfd1f5cc0ff8245600995843dd034d</userID>\n" +
                    "    </meta>\n" +
                    "<case user__id=\"F0183EDA012765103CB106821BBA51A0\" xmlns=\"http://commcarehq.org/case/transaction/v2\" date__modified=\"2011-12-09T13:34:30\" case__id=\"3F2504E04F8911D39A0C0305E82C3301\">\n" +
                    "  <create>\n" +
                    "    <case__type>task</case__type>\n" +
                    "    <case__name>Tetanus 2</case__name>\n" +
                    "    <owner__id>1b23abcaa82aff8215a831</owner__id>\n" +
                    "  </create>\n" +
                    "  <update>\n" +
                    "    <task__id>tt2</task__id>\n" +
                    "    <date__eligible>2011-12-03</date__eligible>\n" +
                    "    <date__expires>2012-03-03</date__expires>\n" +
                    "  </update>\n" +
                    "  <index>\n" +
                    "    <pregnancy__id case__type=\"pregnancy\">3F2504E04F8911D39A0C0305E82C3301</pregnancy__id>\n" +
                    "  </index>\n" +
                    "</case></data>";
    }

}
