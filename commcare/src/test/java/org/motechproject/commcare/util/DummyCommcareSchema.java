package org.motechproject.commcare.util;

import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.domain.FormSchemaQuestionJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class DummyCommcareSchema {

    public static final String FORM_QUESTION1 = "/data/pregnant";
    public static final String FORM_QUESTION2 = "/data/dob";
    public static final String FORM_QUESTION3 = "/data/patient_name";
    public static final String FORM_QUESTION4 = "/data/last_visit";
    public static final String FORM_QUESTION5 = "/data/medications";

    public static final String CASE_FIELD1 = "motherName";
    public static final String CASE_FIELD2 = "childName";
    public static final String CASE_FIELD3 = "dob";
    public static final String CASE_FIELD4 = "visitDate";
    public static final String CASE_FIELD5 = "isPregnant";
    public static final String CASE_FIELD6 = "dod";

    public static final String XMLNS1 = "http://openrosa.org/formdesigner/84FA38A2-93C1-4B9E-AA2A-0E082995FF9E";
    public static final String XMLNS2 = "http://openrosa.org/formdesigner/12KE58A2-54C5-1Z4B-AR2S-Z0345995RF9E";
    public static final String XMLNS3 = "http://openrosa.org/formdesigner/22KE58A2-54C5-1Z4B-AR2S-Z0345995RF9E";
    public static final String XMLNS4 = "http://openrosa.org/formdesigner/32KE58A2-54C5-1Z4B-AR2S-Z0345995RF9E";
    public static final String XMLNS5 = "http://openrosa.org/formdesigner/13KE58A2-54C5-1Z4B-AR2S-Z0345995RF9E";

    public static List<CommcareApplicationJson> getApplicationsForConfigOne() {
        List<CommcareApplicationJson> applicationsInConfig1 = new ArrayList<>();

        Map<String, String> formNames1 = new HashMap<>();
        Map<String, String> formNames2 = new HashMap<>();
        Map<String, String> formNames3 = new HashMap<>();
        Map<String, String> formNames4 = new HashMap<>();

        formNames1.put("en", "form1");
        formNames2.put("en", "form2");
        formNames3.put("en", "form3");
        formNames4.put("en", "form4");

        List<String> fields1 = new ArrayList<>();
        List<String> fields2 = new ArrayList<>();
        List<String> fields3 = new ArrayList<>();

        fields1.add(CASE_FIELD1);
        fields1.add(CASE_FIELD2);
        fields1.add(CASE_FIELD3);

        fields2.add(CASE_FIELD4);
        fields2.add(CASE_FIELD5);

        fields3.add(CASE_FIELD6);

        FormSchemaQuestionJson questionJson1 = new FormSchemaQuestionJson();
        questionJson1.setQuestionLabel("Is Pregnant?");
        questionJson1.setQuestionValue(FORM_QUESTION1);

        FormSchemaQuestionJson questionJson2 = new FormSchemaQuestionJson();
        questionJson2.setQuestionLabel("Date of birth");
        questionJson2.setQuestionValue(FORM_QUESTION2);

        FormSchemaQuestionJson questionJson3 = new FormSchemaQuestionJson();
        questionJson3.setQuestionLabel("Patient name");
        questionJson3.setQuestionValue(FORM_QUESTION3);

        FormSchemaQuestionJson questionJson4 = new FormSchemaQuestionJson();
        questionJson4.setQuestionLabel("Last visit");
        questionJson4.setQuestionValue(FORM_QUESTION4);

        FormSchemaQuestionJson questionJson5 = new FormSchemaQuestionJson();
        questionJson5.setQuestionLabel("Does patient take any medications?");
        questionJson5.setQuestionValue(FORM_QUESTION5);

        FormSchemaJson formSchemaJson1 = new FormSchemaJson();
        formSchemaJson1.setFormNames(formNames1);
        formSchemaJson1.setQuestions(Arrays.asList(questionJson1, questionJson2));
        formSchemaJson1.setXmlns(XMLNS1);

        FormSchemaJson formSchemaJson2 = new FormSchemaJson();
        formSchemaJson2.setFormNames(formNames2);
        formSchemaJson2.setQuestions(Collections.singletonList(questionJson3));
        formSchemaJson2.setXmlns(XMLNS2);

        FormSchemaJson formSchemaJson3 = new FormSchemaJson();
        formSchemaJson3.setFormNames(formNames3);
        formSchemaJson3.setQuestions(Collections.singletonList(questionJson4));
        formSchemaJson3.setXmlns(XMLNS3);

        FormSchemaJson formSchemaJson4 = new FormSchemaJson();
        formSchemaJson4.setFormNames(formNames4);
        formSchemaJson4.setQuestions(Collections.singletonList(questionJson5));
        formSchemaJson4.setXmlns(XMLNS4);

        CommcareModuleJson commcareModuleJson1 = new CommcareModuleJson();
        commcareModuleJson1.setFormSchemas(Arrays.asList(formSchemaJson1, formSchemaJson2));
        commcareModuleJson1.setCaseType("birth");
        commcareModuleJson1.setCaseProperties(fields1);

        CommcareModuleJson commcareModuleJson2 = new CommcareModuleJson();
        commcareModuleJson2.setFormSchemas(Collections.singletonList(formSchemaJson3));
        commcareModuleJson2.setCaseType("appointment");
        commcareModuleJson2.setCaseProperties(fields2);

        CommcareModuleJson commcareModuleJson3 = new CommcareModuleJson();
        commcareModuleJson3.setFormSchemas(Collections.singletonList(formSchemaJson4));
        commcareModuleJson3.setCaseType("death");
        commcareModuleJson3.setCaseProperties(fields3);

        CommcareApplicationJson commcareApplicationJson1 = new CommcareApplicationJson();
        commcareApplicationJson1.setApplicationName("app1");
        commcareApplicationJson1.setModules(Arrays.asList(commcareModuleJson1, commcareModuleJson2));

        CommcareApplicationJson commcareApplicationJson2 = new CommcareApplicationJson();
        commcareApplicationJson2.setApplicationName("app2");
        commcareApplicationJson2.setModules(Collections.singletonList((commcareModuleJson3)));

        applicationsInConfig1.add(commcareApplicationJson1);
        applicationsInConfig1.add(commcareApplicationJson2);

        return applicationsInConfig1;
    }

    public static List<CommcareApplicationJson> getApplicationsForConfigTwo() {
        List<CommcareApplicationJson> applicationsInConfig2 = new ArrayList<>();

        Map<String, String> formNames5 = new HashMap<>();
        formNames5.put("en", "form5");

        List<String> fields1 = new ArrayList<>();

        fields1.add(CASE_FIELD1);
        fields1.add(CASE_FIELD2);
        fields1.add(CASE_FIELD3);

        FormSchemaQuestionJson questionJson6 = new FormSchemaQuestionJson();
        questionJson6.setQuestionLabel("Last visit");
        questionJson6.setQuestionValue(FORM_QUESTION4);

        FormSchemaJson formSchemaJson5 = new FormSchemaJson();
        formSchemaJson5.setFormNames(formNames5);
        formSchemaJson5.setQuestions(Collections.singletonList(questionJson6));
        formSchemaJson5.setXmlns(XMLNS5);

        CommcareModuleJson commcareModuleJson4 = new CommcareModuleJson();
        commcareModuleJson4.setFormSchemas(Collections.singletonList(formSchemaJson5));
        commcareModuleJson4.setCaseType("visit");
        commcareModuleJson4.setCaseProperties(fields1);

        CommcareApplicationJson commcareApplicationJson3 = new CommcareApplicationJson();
        commcareApplicationJson3.setApplicationName("app1");
        commcareApplicationJson3.setModules(Collections.singletonList((commcareModuleJson4)));

        applicationsInConfig2.add(commcareApplicationJson3);

        return applicationsInConfig2;
    }

    public static List<CommcareApplicationJson> getApplicationsForConfigThree() {
        List<CommcareApplicationJson> applicationsInConfig3 = new ArrayList<>();

        Map<String, String> formNames6 = new HashMap<>();
        formNames6.put("en", "form6");

        List<String> fields1 = new ArrayList<>();

        fields1.add(CASE_FIELD1);
        fields1.add(CASE_FIELD2);
        fields1.add(CASE_FIELD3);

        FormSchemaQuestionJson questionJson7 = new FormSchemaQuestionJson();
        questionJson7.setQuestionLabel("Last visit");
        questionJson7.setQuestionValue(FORM_QUESTION4);

        FormSchemaJson formSchemaWithNullXmlns = new FormSchemaJson();
        formSchemaWithNullXmlns.setFormNames(formNames6);
        formSchemaWithNullXmlns.setQuestions(Collections.singletonList(questionJson7));
        formSchemaWithNullXmlns.setXmlns(null);

        CommcareModuleJson commcareModuleJson5 = new CommcareModuleJson();
        commcareModuleJson5.setFormSchemas(Collections.singletonList(formSchemaWithNullXmlns));
        commcareModuleJson5.setCaseType("visit");
        commcareModuleJson5.setCaseProperties(fields1);

        CommcareApplicationJson commcareApplicationJson3 = new CommcareApplicationJson();
        commcareApplicationJson3.setApplicationName("app1");
        commcareApplicationJson3.setModules(Collections.singletonList((commcareModuleJson5)));

        applicationsInConfig3.add(commcareApplicationJson3);

        return applicationsInConfig3;
    }

    public static List<CommcareApplicationJson> getApplicationsWithCustomQuestionLabel(String questionLabel) {
        List<CommcareApplicationJson> applicationsInConfig = new ArrayList<>();
        Map<String, String> formNames = new HashMap<>();
        formNames.put("en", "form");
        List<String> fields = new ArrayList<>();
        fields.add("motherName");
        FormSchemaQuestionJson questionJson = new FormSchemaQuestionJson();
        questionJson.setQuestionLabel(questionLabel);
        questionJson.setQuestionValue("/data/question1");
        FormSchemaJson formSchemaJson = new FormSchemaJson();
        formSchemaJson.setFormNames(formNames);
        formSchemaJson.setQuestions(Arrays.asList(questionJson));
        formSchemaJson.setXmlns("http://openrosa.org/formdesigner/BC5BF86E-6586-484E-99DF-CFAE2D0604D3");
        CommcareModuleJson commcareModuleJson = new CommcareModuleJson();
        commcareModuleJson.setFormSchemas(Arrays.asList(formSchemaJson));
        commcareModuleJson.setCaseType("birth");
        commcareModuleJson.setCaseProperties(fields);
        CommcareApplicationJson commcareApplicationJson = new CommcareApplicationJson();
        commcareApplicationJson.setApplicationName("app");
        commcareApplicationJson.setModules(Arrays.asList(commcareModuleJson));
        applicationsInConfig.add(commcareApplicationJson);

        return applicationsInConfig;
    }
}
