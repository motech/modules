package parser;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.motechproject.commcare.domain.Case;
import org.motechproject.commcare.parser.CommcareCaseParser;

import java.io.FileNotFoundException;

public class CommcareCaseParserTest extends TestCase {

    @Test
    public void testShouldParseCaseAttributesCorrectly() throws FileNotFoundException {
        CommcareCaseParser<Case> parser = new CommcareCaseParser<Case>(Case.class,caseXml());
        Case aCase = parser.parseCase();
        Assert.assertEquals("3F2504E04F8911D39A0C0305E82C3301",aCase.getCase_id());
        Assert.assertEquals("2011-12-08T13:34:30",aCase.getDate_modified());
        Assert.assertEquals("F0183EDA012765103CB106821BBA51A0",aCase.getUser_id());
    }

    @Test
    public void testShouldParseCreateAttributesCorrectly() throws FileNotFoundException {
        CommcareCaseParser<Case> parser = new CommcareCaseParser<Case>(Case.class,caseXml());
        Case aCase = parser.parseCase();
        Assert.assertEquals("houshold_rollout_ONICAF",aCase.getCase_type());
        Assert.assertEquals("Smith",aCase.getCase_name());
    }

    @Test
    public void testShouldSetActionCorrectly() throws FileNotFoundException {
        CommcareCaseParser<Case> parser = new CommcareCaseParser<Case>(Case.class,caseXml());
        Case aCase = parser.parseCase();
        Assert.assertEquals("CREATE",aCase.getAction());
    }

    private String caseXml() {
        String caseXml = "<case xmlns=\"http://commcarehq.org/case/transaction/v2\" case_id=\"3F2504E04F8911D39A0C0305E82C3301\" user_id=\"F0183EDA012765103CB106821BBA51A0\" date_modified=\"2011-12-08T13:34:30\" >\n" +
        "<create>"+
        "<case_type>houshold_rollout_ONICAF</case_type>"+
        "<case_name>Smith</case_name>"+
        "</create>"+
        "<update>"+
        "<household_id>24/F23/3</household_id>"+
        "<primary_contact_name>Tom Smith</primary_contact_name>"+
        "<visit_number>1</visit_number>"+
        "</update>"+
        "</case>";

        return caseXml;  //To change body of created methods use File | Settings | File Templates.
    }

}
