package org.motechproject.commcare.tasks.builder;

import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.domain.FormSchemaJson;
import org.motechproject.commcare.domain.FormSchemaQuestionJson;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class FormTriggerBuilderTest {

    @Mock
    private CommcareSchemaService schemaService;

    private FormTriggerBuilder formTriggerBuilder;

    private static final int FORM_PREDEFINED_FIELDS = 7;

    @Test
    public void shouldBuildProperTriggerRequestForCases() {
        initMocks(this);
        when(schemaService.getAllFormSchemas()).thenReturn(getForms());
        formTriggerBuilder = new FormTriggerBuilder(schemaService);

        List<TriggerEventRequest> triggers = formTriggerBuilder.buildTriggers();

        assertFalse(triggers.isEmpty());

        assertEquals(getForms().size(), triggers.size());

        for(TriggerEventRequest request : triggers) {

            assertEquals(EventSubjects.FORMS_EVENT, request.getTriggerListenerSubject());

            String subject = request.getSubject();
            switch (subject) {
                case "org.motechproject.commcare.api.forms.form1":
                    assertEquals(2 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form1", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), "/data/pregnant"));
                    assertTrue(hasEventKey(request.getEventParameters(), "/data/dob_known"));
                    break;
                case "org.motechproject.commcare.api.forms.form2":
                    assertEquals(3 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form2", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), "/data/patient_name"));
                    assertTrue(hasEventKey(request.getEventParameters(), "/data/last_visit"));
                    assertTrue(hasEventKey(request.getEventParameters(), "/data/medications"));
                    break;
                default:
                    fail("Found trigger with incorrect subject: " + subject);
            }
        }
    }

    private boolean hasEventKey(List<EventParameterRequest> eventParameters, String key) {
        for (EventParameterRequest parameter : eventParameters) {
            if (parameter.getEventKey().equals(key)) {
                return true;
            }
        }

        return false;
    }

    private List<FormSchemaJson> getForms() {
        List<FormSchemaJson> forms = new ArrayList<>();

        Map<String, String> formNames1 = new HashMap<>();
        Map<String, String> formNames2 = new HashMap<>();
        formNames1.put("en", "form1");
        formNames2.put("en", "form2");

        FormSchemaQuestionJson questionJson1 = new FormSchemaQuestionJson();
        questionJson1.setQuestionLabel("Is Pregnant?");
        questionJson1.setQuestionValue("/data/pregnant");

        FormSchemaQuestionJson questionJson2 = new FormSchemaQuestionJson();
        questionJson2.setQuestionLabel("Is date of birth known?");
        questionJson2.setQuestionValue("/data/dob_known");

        FormSchemaQuestionJson questionJson3 = new FormSchemaQuestionJson();
        questionJson3.setQuestionLabel("Patient name");
        questionJson3.setQuestionValue("/data/patient_name");

        FormSchemaQuestionJson questionJson4 = new FormSchemaQuestionJson();
        questionJson4.setQuestionLabel("Last visit");
        questionJson4.setQuestionValue("/data/last_visit");

        FormSchemaQuestionJson questionJson5 = new FormSchemaQuestionJson();
        questionJson5.setQuestionLabel("Does patient take any medications?");
        questionJson5.setQuestionValue("/data/medications");

        FormSchemaJson formSchemaJson1 = new FormSchemaJson();
        formSchemaJson1.setFormNames(formNames1);
        formSchemaJson1.setQuestions(Arrays.asList(questionJson1, questionJson2));

        FormSchemaJson formSchemaJson2 = new FormSchemaJson();
        formSchemaJson2.setFormNames(formNames2);
        formSchemaJson2.setQuestions(Arrays.asList(questionJson3, questionJson4, questionJson5));

        forms.add(formSchemaJson1);
        forms.add(formSchemaJson2);

        return forms;
    }
}
