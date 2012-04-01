package parser;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.motechproject.commcare.domain.Case;
import org.motechproject.commcare.parser.CommcareCaseParser;

import java.io.FileNotFoundException;

/**
 * Created by IntelliJ IDEA.
 * User: pchandra
 * Date: 3/24/12
 * Time: 6:38 PM
 * To change this template use File | Settings | File Templates.
 */
public class CommcareCaseParserTest extends TestCase {

    @Test
    public void testShouldParseCaseAttributesCorrectly() throws FileNotFoundException {
        CommcareCaseParser<Case> parser = new CommcareCaseParser<Case>(Case.class,caseXml());
        Case aCase = parser.parseCase();
        Assert.assertEquals("3F2504E04F8911D39A0C0305E82C3301",aCase.getCase_id());
        Assert.assertEquals("11/10/09 21:23:43",aCase.getDate_modified());
    }

    @Test
    public void testShouldParseCreateAttributesCorrectly() throws FileNotFoundException {
        CommcareCaseParser<Case> parser = new CommcareCaseParser<Case>(Case.class,caseXml());
        Case aCase = parser.parseCase();
        Assert.assertEquals("houshold_rollout_ONICAF",aCase.getCase_type_id());
        Assert.assertEquals("Smith",aCase.getCase_name());
    }

    @Test
    public void testShouldSetActionCorrectly() throws FileNotFoundException {
        CommcareCaseParser<Case> parser = new CommcareCaseParser<Case>(Case.class,caseXml());
        Case aCase = parser.parseCase();
        Assert.assertEquals("CREATE",aCase.getAction());
    }

    private String caseXml() {
        String caseXml = "<case>"+
        "<case_id>3F2504E04F8911D39A0C0305E82C3301</case_id>"+
        "<date_modified>11/10/09 21:23:43</date_modified>"+
        "<create>"+
        "<case_type_id>houshold_rollout_ONICAF</case_type_id>"+
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
