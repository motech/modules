package org.motechproject.commcare.tasks.builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commcare.util.DummyCommcareSchema;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commcare.util.DummyCommcareSchema.CASE_FIELD1;
import static org.motechproject.commcare.util.DummyCommcareSchema.CASE_FIELD2;
import static org.motechproject.commcare.util.DummyCommcareSchema.CASE_FIELD3;
import static org.motechproject.commcare.util.DummyCommcareSchema.CASE_FIELD4;
import static org.motechproject.commcare.util.DummyCommcareSchema.CASE_FIELD5;
import static org.motechproject.commcare.util.DummyCommcareSchema.CASE_FIELD6;

public class CaseTriggerBuilderTest {

    @Mock
    private CommcareSchemaService schemaService;

    @Mock
    private CommcareConfigService configService;

    private Configs configs = ConfigsUtils.prepareConfigsWithTwoConfigs();

    private CaseTriggerBuilder caseTriggerBuilder;

    private static final int CASE_PREDEFINED_FIELDS = 9;

    @Before
    public void setUp() {
        initMocks(this);
        when(configService.getConfigs()).thenReturn(configs);
        when(schemaService.retrieveApplications("ConfigOne")).thenReturn(DummyCommcareSchema.getApplicationsForConfigOne());
        when(schemaService.retrieveApplications("ConfigTwo")).thenReturn(DummyCommcareSchema.getApplicationsForConfigTwo());

        caseTriggerBuilder = new CaseTriggerBuilder(schemaService, configService);
    }

    @Test
    public void shouldBuildProperTriggerRequestForCases() {

        List<TriggerEventRequest> triggerEventRequests = caseTriggerBuilder.buildTriggers();

        assertFalse(triggerEventRequests.isEmpty());

        int counter = 0;
        for (CommcareApplicationJson application: DummyCommcareSchema.getApplicationsForConfigOne()) {
            counter += application.getModules().size();
        }

        for (CommcareApplicationJson application: DummyCommcareSchema.getApplicationsForConfigTwo()) {
            counter += application.getModules().size();
        }

        assertEquals(counter, triggerEventRequests.size());

        for(TriggerEventRequest request : triggerEventRequests) {

            assertEquals(EventSubjects.CASE_EVENT, request.getTriggerListenerSubject());

            String subject = request.getSubject();
            switch (subject) {
                case "org.motechproject.commcare.api.case.ConfigOne.birth":
                    assertEquals(3 + CASE_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Case: birth [app1: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD1));
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD2));
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD3));
                    break;
                case "org.motechproject.commcare.api.case.ConfigOne.appointment":
                    assertEquals(2 + CASE_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Case: appointment [app1: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD4));
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD5));
                    break;
                case "org.motechproject.commcare.api.case.ConfigOne.death":
                    assertEquals(1 + CASE_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Case: death [app2: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD6));
                    break;
                case "org.motechproject.commcare.api.case.ConfigOne.checkup":
                    assertEquals(1 + CASE_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Case: checkup [app3: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD6));
                    break;
                case "org.motechproject.commcare.api.case.ConfigTwo.visit":
                    assertEquals(3 + CASE_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Case: visit [app1: ConfigTwo]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD1));
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD2));
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD3));
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
}
