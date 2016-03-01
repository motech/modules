package org.motechproject.commcare.tasks.builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.domain.CommcareApplicationJson;
import org.motechproject.commcare.domain.CommcareModuleJson;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.service.CommcareApplicationService;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commcare.util.DummyCommcareApplication;
import org.motechproject.tasks.contract.EventParameterRequest;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commcare.util.DummyCommcareApplication.CASE_FIELD1;
import static org.motechproject.commcare.util.DummyCommcareApplication.CASE_FIELD2;
import static org.motechproject.commcare.util.DummyCommcareApplication.CASE_FIELD3;
import static org.motechproject.commcare.util.DummyCommcareApplication.CASE_FIELD4;
import static org.motechproject.commcare.util.DummyCommcareApplication.CASE_FIELD5;
import static org.motechproject.commcare.util.DummyCommcareApplication.CASE_FIELD6;

public class CaseTriggerBuilderTest {

    @Mock
    private CommcareApplicationService applicationService;

    @Mock
    private CommcareConfigService configService;

    private Configs configs = ConfigsUtils.prepareConfigsWithTwoConfigs();

    private CaseTriggerBuilder caseTriggerBuilder;

    private static final int CASE_PREDEFINED_FIELDS = 9;

    @Before
    public void setUp() {
        initMocks(this);
        when(configService.getConfigs()).thenReturn(configs);
        when(applicationService.getByConfigName("ConfigOne")).thenReturn(DummyCommcareApplication.getApplicationsForConfigOne());
        when(applicationService.getByConfigName("ConfigTwo")).thenReturn(DummyCommcareApplication.getApplicationsForConfigTwo());

        caseTriggerBuilder = new CaseTriggerBuilder(applicationService, configService);
    }

    @Test
    public void shouldBuildProperTriggerRequestForCases() {

        List<TriggerEventRequest> triggerEventRequests = caseTriggerBuilder.buildTriggers();

        assertFalse(triggerEventRequests.isEmpty());

        int counter = 0;
        for (CommcareApplicationJson application: DummyCommcareApplication.getApplicationsForConfigOne()) {
            for (CommcareModuleJson module: application.getModules()) {
                counter ++;
            }
        }

        for (CommcareApplicationJson application: DummyCommcareApplication.getApplicationsForConfigTwo()) {
            for (CommcareModuleJson module: application.getModules()) {
                counter ++;
            }
        }

        // One trigger for cases is always built (for every configuration), therefore we should always have one case more
        assertEquals(counter + 2, triggerEventRequests.size());


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
                case "org.motechproject.commcare.api.case.ConfigOne":
                    assertEquals(2, request.getEventParameters().size());
                    assertEquals("caseId", request.getEventParameters().get(0).getEventKey());
                    break;
                case "org.motechproject.commcare.api.case.ConfigTwo.visit":
                    assertEquals(3 + CASE_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Case: visit [app1: ConfigTwo]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD1));
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD2));
                    assertTrue(hasEventKey(request.getEventParameters(), CASE_FIELD3));
                    break;
                case "org.motechproject.commcare.api.case.ConfigTwo":
                    assertEquals(2, request.getEventParameters().size());
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
}
