package org.motechproject.commcare.util;

public final class ResponseXML {

    public static final String FORM_NAME = "form1";
    public static final String CASE_TYPE = "pregnancy";

    public static final String ATTR1 = "pregnant";
    public static final String ATTR1_VALUE = "true";

    public static final String ATTR2 = "dob";
    public static final String ATTR2_VALUE = "1980-01-01";

    public static final String CASE_ID = "e098a110-6b83-4ff7-9093-d8e0e8bfb9a3";
    public static final String USER_ID = "9ad3659b9c0f8c5d141d2d06857874df";
    public static final String DATE_MODIFIED = "2012-10-23T17:15:21.966-04";
    public static final String XMLNS = "http://commcarehq.org/case/transaction/v2";
    public static final String DATE = "2016-05-28";
    public static final String ATTR = "example";

    public static String getFormXML() {
        return "<data uiVersion=\"1\"\n" +
                "      version=\"41\"\n" +
                "      name=\"" + FORM_NAME + "\"\n" +
                "      xmlns:jrm=\"http://dev.commcarehq.org/jr/xforms\"\n" +
                "      xmlns=\"" + DummyCommcareSchema.XMLNS1 + "\">\n" +
                "  <" + ATTR1 + ">" + ATTR1_VALUE + "</" + ATTR1 + ">\n" +
                "  <" + ATTR2 + ">" + ATTR2_VALUE + "</" + ATTR2 + ">\n" +
                "  <n0:case case_id=\"" + CASE_ID + "\"\n" +
                "           user_id=\""+ USER_ID +"\"\n" +
                "           date_modified=\""+ DATE_MODIFIED +"\"\n" +
                "           xmlns:n0=\""+ XMLNS +"\">\n" +
                "    <n0:update date=\""+DATE+"\">\n" +
                "      <n0:number attr=\""+ATTR+"\">8</n0:number>\n" +
                "    </n0:update>\n" +
                "  </n0:case>\n" +
                "  <cc_delegation_stub delegation_id=\"0e6db0c4-d07f-435c-89e5-64855440605c\">\n" +
                "    <n1:case case_id=\"0e6db0c4-d07f-435c-89e5-64855440605c\"\n" +
                "             user_id=\"9ad3659b9c0f8c5d141d2d06857874df\"\n" +
                "             date_modified=\"2012-10-23T17:15:21.966-04\"\n" +
                "             xmlns:n1=\"http://commcarehq.org/case/transaction/v2\">\n" +
                "      <n1:close />\n" +
                "    </n1:case>\n" +
                "  </cc_delegation_stub>\n" +
                "  <n2:meta xmlns:n2=\"http://openrosa.org/jr/xforms\">\n" +
                "  <n2:deviceID>cloudcare</n2:deviceID>\n" +
                "  <n2:timeStart>2012-10-23T17:15:18.324-04</n2:timeStart>\n" +
                "  <n2:timeEnd>2012-10-23T17:15:21.966-04</n2:timeEnd>\n" +
                "  <n2:username>test</n2:username>\n" +
                "  <n2:userID>9ad3659b9c0f8c5d141d2d06857874df</n2:userID>\n" +
                "  <n2:instanceID>c24a85f9-703d-434c-b087-5759f3fa9937</n2:instanceID>\n" +
                "  <n3:appVersion xmlns:n3=\"http://commcarehq.org/xforms\">2.0</n3:appVersion>\n" +
                "  </n2:meta>\n" +
                "</data>";
    }

    public static String getFormXMLWithRepeatData() {
        return "<data uiVersion=\"1\"\n" +
                "      version=\"41\"\n" +
                "      name=\"Diseases\"\n" +
                "      xmlns:jrm=\"http://dev.commcarehq.org/jr/xforms\"\n" +
                "      xmlns=\"" + DummyCommcareSchema.XMLNS1 + "\">\n" +
                "  <clinic>Clinic1</clinic>\n" +
                "  <diseases>\n" +
                "    <disease id=\"3893289\" index=\"0\">\n" +
                "      <medication id=\"55665\">Med1</medication>\n" +
                "      <medication id=\"55666\">Med2</medication>\n" +
                "      <medication id=\"55667\">Med3</medication>\n" +
                "    </disease>\n" +
                "    <disease id=\"9539823\" index=\"1\">\n" +
                "      <medication id=\"55666\">Med2</medication>\n" +
                "      <medication id=\"55668\">Med4</medication>\n" +
                "    </disease>\n" +
                "  </diseases>\n" +
                "  <n2:meta xmlns:n2=\"http://openrosa.org/jr/xforms\">\n" +
                "  <n2:deviceID>cloudcare</n2:deviceID>\n" +
                "  <n2:timeStart>2012-10-23T17:15:18.324-04</n2:timeStart>\n" +
                "  <n2:timeEnd>2012-10-23T17:15:21.966-04</n2:timeEnd>\n" +
                "  <n2:username>test</n2:username>\n" +
                "  <n2:userID>9ad3659b9c0f8c5d141d2d06857874df</n2:userID>\n" +
                "  <n2:instanceID>c24a85f9-703d-434c-b087-5759f3fa9937</n2:instanceID>\n" +
                "  <n3:appVersion xmlns:n3=\"http://commcarehq.org/xforms\">2.0</n3:appVersion>\n" +
                "  </n2:meta>\n" +
                "</data>";
    }

    public static String getCaseXML() {
        return "<case case_id=\"e6552468-e7ac-4bd1-86bd-4fd72094ccc2\" date_modified=\"2014-09-24T13:14:50Z\"" +
                " user_id=\"2a34e758b7ed8a686e7fe8de29c3078c\" xmlns=\"http://commcarehq.org/case/transaction/v2\">" +
                "<create>" +
                "   <case_type>" + CASE_TYPE + "</case_type>" +
                "   <case_name>Susanna Bones</case_name>" +
                "   <owner_id>2a34e758b7ed8a686e7fe8de29c3078c</owner_id>" +
                "</create>" +
                "<update>" +
                "   <external_id>123456</external_id>" +
                "   <dob>1990-09-09</dob>" +
                "   <dob_calc>1990-09-09</dob_calc>" +
                "   <dob_known>yes</dob_known>" +
                "   <edd>2014-09-27</edd>" +
                "   <edd_calc>2014-09-27</edd_calc>" +
                "   <edd_known>yes</edd_known>" +
                "   <first_name>Susanna</first_name>" +
                "   <full_name>Susanna Bones</full_name>" +
                "   <health_id>123456</health_id>" +
                "   <household_head_health_id>123456</household_head_health_id>" +
                "   <mobile_phone_number>1234567890</mobile_phone_number>" +
                "   <surname>Bones</surname>" +
                "</update>" +
                "</case>";
    }

    public static String getDeviceReportXML() {
        return "<?xml version=\'1.0\' encoding=\'UTF-8\' standalone=\'no\'?> <device_report xmlns=\"http://code.javarosa.org/devicereport\"> <device_id>DEVICEIDJ0j09s0u</device_id></device_report>";
    }
}
