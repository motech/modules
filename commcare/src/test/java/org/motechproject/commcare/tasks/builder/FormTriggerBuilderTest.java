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
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION1;
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION2;
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION3;
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION4;
import static org.motechproject.commcare.util.DummyCommcareSchema.FORM_QUESTION5;
import static org.motechproject.commcare.util.DummyCommcareSchema.XMLNS1;
import static org.motechproject.commcare.util.DummyCommcareSchema.XMLNS2;
import static org.motechproject.commcare.util.DummyCommcareSchema.XMLNS3;
import static org.motechproject.commcare.util.DummyCommcareSchema.XMLNS4;
import static org.motechproject.commcare.util.DummyCommcareSchema.XMLNS5;
import static org.motechproject.commcare.util.DummyCommcareSchema.APP_ID1;
import static org.motechproject.commcare.util.DummyCommcareSchema.APP_ID2;

public class FormTriggerBuilderTest {

    @Mock
    private CommcareSchemaService schemaService;

    @Mock
    private CommcareConfigService configService;

    private Configs configs = ConfigsUtils.prepareConfigsWithThreeConfigs();

    private FormTriggerBuilder formTriggerBuilder;

    private static final int FORM_PREDEFINED_FIELDS = 11;
    private static final String BASE_SUBJECT_ONE = "org.motechproject.commcare.api.forms.ConfigOne";
    private static final String BASE_SUBJECT_TWO = "org.motechproject.commcare.api.forms.ConfigTwo";

    @Before
    public void setUp() {
        initMocks(this);
        when(configService.getConfigs()).thenReturn(configs);
        when(schemaService.retrieveApplications("ConfigOne")).thenReturn(DummyCommcareSchema.getApplicationsForConfigOne());
        when(schemaService.retrieveApplications("ConfigTwo")).thenReturn(DummyCommcareSchema.getApplicationsForConfigTwo());
        when(schemaService.retrieveApplications("ConfigThree")).thenReturn(DummyCommcareSchema.getApplicationsForConfigThree());

        formTriggerBuilder = new FormTriggerBuilder(schemaService, configService);
    }

    @Test
    public void shouldBuildProperTriggerRequestForCases() {

        List<TriggerEventRequest> triggers = formTriggerBuilder.buildTriggers();

        assertFalse(triggers.isEmpty());

        int counter = 0;
        for (CommcareApplicationJson application: DummyCommcareSchema.getApplicationsForConfigOne()) {
            for (CommcareModuleJson module: application.getModules()) {
                counter += module.getFormSchemas().size();
            }
        }

        for (CommcareApplicationJson application: DummyCommcareSchema.getApplicationsForConfigTwo()) {
            for (CommcareModuleJson module: application.getModules()) {
                counter += module.getFormSchemas().size();
            }
        }

        assertEquals(counter, triggers.size());

        for(TriggerEventRequest request : triggers) {

            assertEquals(EventSubjects.FORMS_EVENT, request.getTriggerListenerSubject());

            String subject = request.getSubject();
            switch (subject) {
                case BASE_SUBJECT_ONE + "." + XMLNS1 + APP_ID1:
                    assertEquals(2 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form1 [app1: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION1));
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION2));
                    break;
                case BASE_SUBJECT_ONE + "." + XMLNS2 + APP_ID1:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form2 [app1: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION3));
                    break;
                case BASE_SUBJECT_ONE + "." + XMLNS3 + APP_ID1:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form3 [app1: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION4));
                    break;
                case BASE_SUBJECT_ONE + "." + XMLNS4 + APP_ID2:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form4 [app2: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION5));
                    break;
                case BASE_SUBJECT_ONE + "." + XMLNS1:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form5 [app3: ConfigOne]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION5));
                    break;
                case BASE_SUBJECT_TWO + "." + XMLNS5 + APP_ID1:
                    assertEquals(1 + FORM_PREDEFINED_FIELDS, request.getEventParameters().size());
                    assertEquals("Received Form: form5 [app1: ConfigTwo]", request.getDisplayName());
                    assertTrue(hasEventKey(request.getEventParameters(), FORM_QUESTION4));
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
