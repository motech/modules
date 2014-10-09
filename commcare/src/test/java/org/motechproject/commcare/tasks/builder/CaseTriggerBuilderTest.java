package org.motechproject.commcare.tasks.builder;

import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class CaseTriggerBuilderTest {

    @Mock
    private CommcareSchemaService schemaService;

    private CaseTriggerBuilder caseTriggerBuilder;

    private static final int CASE_PREDEFINED_FIELDS = 8;

    @Test
    public void shouldBuildProperTriggerRequestForCases() {
        initMocks(this);
        when(schemaService.getAllCaseTypes()).thenReturn(getCases());
        caseTriggerBuilder = new CaseTriggerBuilder(schemaService);

        List<TriggerEventRequest> triggerEventRequests = caseTriggerBuilder.buildTriggers();

        assertFalse(triggerEventRequests.isEmpty());

        // One trigger for cases is always built, therefore we should always have one case more
        assertEquals(getCases().size() + 1, triggerEventRequests.size());

        for(TriggerEventRequest request : triggerEventRequests) {

            assertEquals(EventSubjects.CASE_EVENT, request.getTriggerListenerSubject());

            String subject = request.getSubject();
            switch (subject) {
                case "org.motechproject.commcare.api.case.birth":
                    assertEquals(3 + CASE_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Case: birth", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), "motherName"));
                    assertTrue(hasEventKey(request.getEventParameters(), "childName"));
                    assertTrue(hasEventKey(request.getEventParameters(), "dob"));
                    break;
                case "org.motechproject.commcare.api.case.appointment":
                    assertEquals(2 + CASE_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Case: appointment", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), "visitDate"));
                    assertTrue(hasEventKey(request.getEventParameters(), "isPregnant"));
                    break;
                case "org.motechproject.commcare.api.case":
                    assertEquals(1, request.getEventParameters().size());
                    assertEquals("caseId", request.getEventParameters().get(0).getEventKey());
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

    private Map<String, Set<String>> getCases() {
        Map<String, Set<String>> cases = new HashMap<>();
        Set<String> fields1 = new HashSet<>();
        Set<String> fields2 = new HashSet<>();

        fields1.add("motherName");
        fields1.add("childName");
        fields1.add("dob");

        fields2.add("visitDate");
        fields2.add("isPregnant");

        cases.put("birth", fields1);
        cases.put("appointment", fields2);

        return cases;
    }
}
