package org.motechproject.commcare.parser;

import org.junit.Test;

import java.io.FileNotFoundException;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import org.mockito.Matchers;
import org.motechproject.commcare.domain.CaseXml;
import org.motechproject.commcare.domain.CommcareForm;
import org.motechproject.commcare.domain.FormValueElement;
import org.motechproject.commcare.parser.FormAdapter;

import static org.mockito.Mockito.when;

public class FormAdapterTest {

    @Test
    public void testShouldParseJsonFormHeader()
            throws FileNotFoundException {
        CommcareForm commcareForm = FormAdapter.readJson(jsonSampleForm());
        Assert.assertEquals("2012-09-29T17:24:52", commcareForm.getReceivedOn());
    }

    private String jsonSampleForm() {
        return "{\"archived\": false,\"form\": {\"#type\": \"data\", \"@name\": \"Case Update\",\"@uiVersion\": \"1\",\"@version\": \"186\",\"@xmlns\": \"http://openrosa.org/formdesigner/4B1B717C-0CF7-472E-8CC1-1CC0C45AA5E0\",\"case\": {\"@case_id\": \"8f8fd909-684f-402d-a892-f50e607fffef\",\"@date_modified\": \"2012-09-29T19:10:00\",\"@user_id\": \"f4c63df2ef7f9da2f93cab12dc9ef53c\",\"@xmlns\": \"http://commcarehq.org/case/transaction/v2\",\"update\": {\"data_node\": \"55\",\"dateval\": \"2012-09-26\",\"geodata\": \"5.0 5.0 5.0 5.0\",\"intval\": \"5\",\"multiselect\": \"b\",\"singleselect\": \"b\",\"text\": \"TEST\"}},\"data_node\": \"55\",\"geodata\": \"5.0 5.0 5.0 5.0\",\"meta\": {\"@xmlns\": \"http://openrosa.org/jr/xforms\",\"appVersion\": {\"#text\": \"v2.1.0dev (1d8fba-0884f9-unvers-2.1.0-Nokia/S40-generic) build 186 App #186 b:2012-Sep-27 r:2012-Sep-28\",\"@xmlns\": \"http://commcarehq.org/xforms\"},\"deviceID\": \"0LRGVM4SFN2VHCOVWOVC07KQX\",\"instanceID\": \"00460026-a33b-4c6b-a4b6-c47117048557\",\"timeEnd\": \"2012-09-29T19:10:00\",\"timeStart\": \"2012-09-29T19:08:46\",\"userID\": \"f4c63df2ef7f9da2f93cab12dc9ef53c\",\"username\": \"afrisis\"},\"old_data_node\": \"\",\"question1\": \"OK\",\"question11\": \"5\",\"question12\": \"2012-09-26\",\"question14\": \"OK\",\"question3\": \"b\",\"question7\": \"b\",\"text\": \"TEST\"},\"id\": \"00460026-a33b-4c6b-a4b6-c47117048557\",\"md5\": \"OBSOLETED\",\"metadata\": {\"@xmlns\": \"http://openrosa.org/jr/xforms\",\"appVersion\": \"@xmlns:http://commcarehq.org/xforms, #text:v2.1.0dev (1d8fba-0884f9-unvers-2.1.0-Nokia/S40-generic) build 186 App #186 b:2012-Sep-27 r:2012-Sep-28\",\"deprecatedID\": null,\"deviceID\": \"0LRGVM4SFN2VHCOVWOVC07KQX\",\"instanceID\": \"00460026-a33b-4c6b-a4b6-c47117048557\",\"timeEnd\": \"2012-09-29T19:10:00\",\"timeStart\": \"2012-09-29T19:08:46\",\"userID\": \"f4c63df2ef7f9da2f93cab12dc9ef53c\",\"username\": \"afrisis\"},\"received_on\": \"2012-09-29T17:24:52\",\"resource_uri\": \"\",\"type\": \"data\",\"uiversion\": \"1\",\"version\": \"186\"}";
    }

}
