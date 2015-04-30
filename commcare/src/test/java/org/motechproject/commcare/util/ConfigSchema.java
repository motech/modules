package org.motechproject.commcare.util;

import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.domain.FormSchemaQuestionJson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigSchema {

    public static final String FORM_QUESTION_ONE = "/data/fooQuestionOne";
    public static final String FORM_QUESTION_TWO = "/data/fooQuestionTwo";
    public static final String FORM_QUESTION_THREE = "/data/fooQuestionThree";
    public static final String FORM_QUESTION_FOUR = "/data/fooQuestionFour";

    public static final String CASE_FIELD_ONE = "fooFieldOne";
    public static final String CASE_FIELD_TWO = "fooFieldTwo";
    public static final String CASE_FIELD_THREE = "fooFieldThree";
    public static final String CASE_FIELD_FOUR = "fooFieldFour";

    public static List<FormSchemaJson> getForms(int i) {
        List<FormSchemaJson> forms = new ArrayList<>();

        Map<String, String> formNames1 = new HashMap<>();
        Map<String, String> formNames2 = new HashMap<>();
        formNames1.put("en", "fooFormOne" + i);
        formNames2.put("en", "fooFormTwo" + i);

        FormSchemaQuestionJson questionJson1 = new FormSchemaQuestionJson();
        questionJson1.setQuestionLabel("Foo question one" + i + ".");
        questionJson1.setQuestionValue(FORM_QUESTION_ONE);

        FormSchemaQuestionJson questionJson2 = new FormSchemaQuestionJson();
        questionJson2.setQuestionLabel("Foo question two" + i + ".");
        questionJson2.setQuestionValue(FORM_QUESTION_TWO);

        FormSchemaQuestionJson questionJson3 = new FormSchemaQuestionJson();
        questionJson3.setQuestionLabel("Foo question three" + i + ".");
        questionJson3.setQuestionValue(FORM_QUESTION_THREE);

        FormSchemaQuestionJson questionJson4 = new FormSchemaQuestionJson();
        questionJson4.setQuestionLabel("Foo question four" + i + ".");
        questionJson4.setQuestionValue(FORM_QUESTION_FOUR);

        FormSchemaJson formSchemaJson1 = new FormSchemaJson();
        formSchemaJson1.setFormNames(formNames1);
        formSchemaJson1.setQuestions(Arrays.asList(questionJson1, questionJson2));

        FormSchemaJson formSchemaJson2 = new FormSchemaJson();
        formSchemaJson2.setFormNames(formNames2);
        formSchemaJson2.setQuestions(Arrays.asList(questionJson3, questionJson4));

        forms.add(formSchemaJson1);
        forms.add(formSchemaJson2);

        return forms;
    }

    public static Map<String, Set<String>> getCases(int i) {
        Map<String, Set<String>> cases = new HashMap<>();
        Set<String> fields1 = new HashSet<>();
        Set<String> fields2 = new HashSet<>();

        fields1.add(CASE_FIELD_ONE);
        fields1.add(CASE_FIELD_TWO);

        fields2.add(CASE_FIELD_THREE);
        fields2.add(CASE_FIELD_FOUR);

        cases.put("fooCaseTypeOne" + i, fields1);
        cases.put("fooCaseTypeTwo" + i, fields2);

        return cases;
    }

}
