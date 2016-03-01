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
import static org.motechproject.commcare.util.DummyCommcareApplication.FORM_QUESTION1;
import static org.motechproject.commcare.util.DummyCommcareApplication.FORM_QUESTION2;
import static org.motechproject.commcare.util.DummyCommcareApplication.FORM_QUESTION3;
import static org.motechproject.commcare.util.DummyCommcareApplication.FORM_QUESTION4;
import static org.motechproject.commcare.util.DummyCommcareApplication.FORM_QUESTION5;
import static org.motechproject.commcare.util.DummyCommcareApplication.FORM_QUESTION6;
import static org.motechproject.commcare.util.DummyCommcareApplication.XMLNS1;
import static org.motechproject.commcare.util.DummyCommcareApplication.XMLNS2;
import static org.motechproject.commcare.util.DummyCommcareApplication.XMLNS3;
import static org.motechproject.commcare.util.DummyCommcareApplication.XMLNS4;
import static org.motechproject.commcare.util.DummyCommcareApplication.XMLNS5;

public class FormTriggerBuilderTest {

    @Mock
    private CommcareApplicationService applicationService;

    @Mock
    private CommcareConfigService configService;

    private Configs configs = ConfigsUtils.prepareConfigsWithTwoConfigs();

    private FormTriggerBuilder formTriggerBuilder;

    private static final int FORM_PREDEFINED_FIELDS = 11;
    private static final String BASE_SUBJECT_ONE = "org.motechproject.commcare.api.forms.ConfigOne";
    private static final String BASE_SUBJECT_TWO = "org.motechproject.commcare.api.forms.ConfigTwo";

    @Before
    public void setUp() {
        initMocks(this);
        when(configService.getConfigs()).thenReturn(configs);
        when(applicationService.getByConfigName("ConfigOne")).thenReturn(DummyCommcareApplication.getApplicationsForConfigOne());
        when(applicationService.getByConfigName("ConfigTwo")).thenReturn(DummyCommcareApplication.getApplicationsForConfigTwo());

        formTriggerBuilder = new FormTriggerBuilder(applicationService, configService);
    }

    @Test
    public void shouldBuildProperTriggerRequestForCases() {

        List<TriggerEventRequest> triggers = formTriggerBuilder.buildTriggers();

        assertFalse(triggers.isEmpty());

        int counter = 0;
        for (CommcareApplicationJson application: DummyCommcareApplication.getApplicationsForConfigOne()) {
            for (CommcareModuleJson module: application.getModules()) {
                counter += module.getFormSchemas().size();
            }
        }

        for (CommcareApplicationJson application: DummyCommcareApplication.getApplicationsForConfigTwo()) {
            for (CommcareModuleJson module: application.getModules()) {
                counter += module.getFormSchemas().size();
            }
        }

        assertEquals(counter, triggers.size());

        for(TriggerEventRequest request : triggers) {

            assertEquals(EventSubjects.FORMS_EVENT, request.getTriggerListenerSubject());

            String subject = request.getSubject();
            switch (subject) {
                case BASE_SUBJECT_ONE + "." + XMLNS1:
                    assertEquals(2 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form1 [app1: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION1));
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION2));
                    break;
                case BASE_SUBJECT_ONE + "." + XMLNS2:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form2 [app1: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION3));
                    break;
                case BASE_SUBJECT_ONE + "." + XMLNS3:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form3 [app1: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION4));
                    break;
                case BASE_SUBJECT_ONE + "." + XMLNS4:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form4 [app2: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION5));
                    break;
                case BASE_SUBJECT_TWO + "." + XMLNS5:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form5 [app1: ConfigTwo]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION6));
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
