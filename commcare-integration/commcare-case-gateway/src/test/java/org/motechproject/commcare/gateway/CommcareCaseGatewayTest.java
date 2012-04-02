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
public class CommcareCaseGatewayTest extends TestCase {

    public void testShouldCovertToCaseXml(){
        CreateElement create = new CreateElement();
        create.setCase_type("task");
        create.setCase_name("Tetanus 2");
        create.setOwner_id("1b23abcaa82aff8215a831");

        UpdateElement update = new UpdateElement();
        update.setTask_id("tt2");
        update.setDate_eligible("2011-12-03");
        update.setDate_expires("2012-03-03");

        Pregnancy pregnancy = new Pregnancy();
        pregnancy.setCase_type("pregnancy");
        pregnancy.setPregnancy_id("3F2504E04F8911D39A0C0305E82C3301");

        Index index = new Index();
        index.setPregnancy_id(pregnancy);

        Case ccCase = new Case();
        ccCase.setCase_id("3F2504E04F8911D39A0C0305E82C3301");
        ccCase.setUser_id("F0183EDA012765103CB106821BBA51A0");
        ccCase.setDate_Modified("2011-12-09T13:34:30");
        ccCase.setCreate(create);
        ccCase.setUpdate(update);

        ccCase.setIndex(index);

        CommcareCaseGateway gateway = new CommcareCaseGateway();
        String caseXml = gateway.convertToXml(ccCase);
        Assert.assertEquals(caseXml(),caseXml);
    }

    public void testShouldMapTaskToCaseCorrectly(){

        CaseTask task = new CaseTask();
        task.setCaseId("3F2504E04F8911D39A0C0305E82C3301");

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

}
