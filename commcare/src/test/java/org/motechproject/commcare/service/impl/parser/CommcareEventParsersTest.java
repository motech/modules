package org.motechproject.commcare.service.impl.parser;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.impl.CommcareCaseEventParser;
import org.motechproject.commcare.service.impl.CommcareFormsEventParser;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commcare.util.DummyCommcareSchema;
import org.motechproject.commcare.util.ResponseXML;
import org.motechproject.commcare.web.CasesController;
import org.motechproject.commcare.web.FullFormController;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commcare.util.ResponseXML.ATTR1_VALUE;
import static org.motechproject.commcare.util.ResponseXML.ATTR2_VALUE;
import static org.motechproject.commcare.util.ResponseXML.CASE_ID;
import static org.motechproject.commcare.util.ResponseXML.USER_ID;
import static org.motechproject.commcare.util.ResponseXML.DATE_MODIFIED;
import static org.motechproject.commcare.util.ResponseXML.XMLNS;
import static org.motechproject.commcare.util.ResponseXML.DATE;
import static org.motechproject.commcare.util.ResponseXML.ATTR;

public class CommcareEventParsersTest {

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareConfigService configService;

    private FullFormController formsController;
    private CasesController casesController;
    private MockHttpServletRequest request;

    private CommcareFormsEventParser formsEventParser;
    private CommcareCaseEventParser caseEventParser;

    private MotechEvent formsEvent;
    private MotechEvent formsEventWithRepeatData;
    private MotechEvent caseEvent;

    private Config config;


    @Before
    public void setUp() throws EndpointNotSupported {
        // Initialize parsers
        formsEventParser = new CommcareFormsEventParser();
        caseEventParser = new CommcareCaseEventParser();

        initMocks(this);

        config = ConfigsUtils.prepareConfigOne();

        when(configService.getByName(config.getName())).thenReturn(config);

        // Mock hitting forms and cases endpoint
        formsController = new FullFormController(eventRelay, configService);
        casesController = new CasesController(eventRelay, configService);
        request = new MockHttpServletRequest();
        request.addHeader("received-on", "2012-07-21T15:22:34.046462Z");
        request.setPathInfo("/forms/"+ config.getName());

        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);
        formsController.receiveForm(ResponseXML.getFormXML(), request, null);
        formsController.receiveForm(ResponseXML.getFormXMLWithRepeatData(), request, null);

        request.setContent(ResponseXML.getCaseXML().getBytes());
        casesController.receiveCase(request, config.getName());

        // Capture MotechEvents
        verify(eventRelay, times(3)).sendEventMessage(captor.capture());
        // Assign them to test fields for later use in tests
        formsEvent = captor.getAllValues().get(0);
        formsEventWithRepeatData = captor.getAllValues().get(1);
        caseEvent = captor.getAllValues().get(2);
    }

    @Test
    public void shouldParseFormsEventSubjectProperly() {
        String eventSubject = formsEvent.getSubject();
        Map<String, Object> eventParameters = formsEvent.getParameters();

        String parsedSubject = formsEventParser.parseEventSubject(eventSubject, eventParameters);

        assertEquals(EventSubjects.FORMS_EVENT, eventSubject);
        assertEquals(EventSubjects.FORMS_EVENT + "." + config.getName() + "." + DummyCommcareSchema.XMLNS1, parsedSubject);
    }

    @Test
    public void shouldParseFormsEventParametersProperly() {
        String eventSubject = formsEvent.getSubject();
        Map<String, Object> eventParameters = formsEvent.getParameters();

        Map<String, Object> parsedParameters = formsEventParser.parseEventParameters(eventSubject, eventParameters);

        assertTrue(parsedParameters.containsKey("/data/pregnant"));
        assertTrue(parsedParameters.containsKey("/data/dob"));
        assertTrue(parsedParameters.containsKey("/data/meta/username"));

        assertEquals(ATTR1_VALUE, parsedParameters.get("/data/pregnant"));
        assertEquals(ATTR2_VALUE, parsedParameters.get("/data/dob"));
        assertEquals("test", parsedParameters.get("/data/meta/username"));
    }

    @Test
    public void shouldParseFormsEventWithRepeatDataProperly() {
        String eventSubject = formsEventWithRepeatData.getSubject();
        Map<String, Object> eventParameters = formsEventWithRepeatData.getParameters();

        Map<String, Object> parsedParameters = formsEventParser.parseEventParameters(eventSubject, eventParameters);

        assertTrue(parsedParameters.containsKey("/data/clinic"));
        assertTrue(parsedParameters.containsKey("/data/diseases/disease_3893289/medication_55665"));
        assertTrue(parsedParameters.containsKey("/data/diseases/disease_3893289/medication_55666"));
        assertTrue(parsedParameters.containsKey("/data/diseases/disease_3893289/medication_55667"));
        assertTrue(parsedParameters.containsKey("/data/diseases/disease_9539823/medication_55666"));
        assertTrue(parsedParameters.containsKey("/data/diseases/disease_9539823/medication_55668"));


        assertEquals("Clinic1", parsedParameters.get("/data/clinic"));
        assertEquals("Med1", parsedParameters.get("/data/diseases/disease_3893289/medication_55665"));
        assertEquals("Med2", parsedParameters.get("/data/diseases/disease_3893289/medication_55666"));
        assertEquals("Med3", parsedParameters.get("/data/diseases/disease_3893289/medication_55667"));
        assertEquals("Med2", parsedParameters.get("/data/diseases/disease_9539823/medication_55666"));
        assertEquals("Med4", parsedParameters.get("/data/diseases/disease_9539823/medication_55668"));
    }

    @Test
    public void shouldParseCaseSubjectProperly() {
        String eventSubject = caseEvent.getSubject();
        Map<String, Object> eventParameters = caseEvent.getParameters();

        String parsedSubject = caseEventParser.parseEventSubject(eventSubject, eventParameters);

        assertEquals(EventSubjects.CASE_EVENT, eventSubject);
        assertEquals(EventSubjects.CASE_EVENT + "." + config.getName() + "." + ResponseXML.CASE_TYPE, parsedSubject);
    }

    @Test
    public void shouldParseCaseEventParametersProperly() {
        String eventSubject = caseEvent.getSubject();
        Map<String, Object> eventParameters = caseEvent.getParameters();

        Map<String, Object> parsedParameters = caseEventParser.parseEventParameters(eventSubject, eventParameters);

        assertTrue(parsedParameters.containsKey("caseType"));
        assertTrue(parsedParameters.containsKey("surname"));
        assertTrue(parsedParameters.containsKey("caseName"));
        assertTrue(parsedParameters.containsKey("dob_known"));
        assertTrue(parsedParameters.containsKey("dob_calc"));

        assertEquals(ResponseXML.CASE_TYPE, parsedParameters.get("caseType"));
        assertEquals("Susanna Bones", parsedParameters.get("caseName"));
        assertEquals("yes", parsedParameters.get("dob_known"));
        assertEquals("1990-09-09", parsedParameters.get("dob_calc"));
    }

    @Test
    public void shouldParseFormsEventAttributesProperly() {
        String eventSubject = formsEvent.getSubject();
        Map<String, Object> eventParameters = formsEvent.getParameters();

        Map<String, Object> parsedParameters = formsEventParser.parseEventParameters(eventSubject, eventParameters);

        assertTrue(parsedParameters.containsKey("/data/case/@case_id"));
        assertTrue(parsedParameters.containsKey("/data/case/@user_id"));
        assertTrue(parsedParameters.containsKey("/data/case/@date_modified"));
        assertTrue(parsedParameters.containsKey("/data/case/@xmlns"));
        assertTrue(parsedParameters.containsKey("/data/case/update/@date"));
        assertTrue(parsedParameters.containsKey("/data/case/update/number/@attr"));

        assertEquals(CASE_ID, parsedParameters.get("/data/case/@case_id"));
        assertEquals(USER_ID, parsedParameters.get("/data/case/@user_id"));
        assertEquals(DATE_MODIFIED, parsedParameters.get("/data/case/@date_modified"));
        assertEquals(XMLNS, parsedParameters.get("/data/case/@xmlns"));
        assertEquals(DATE, parsedParameters.get("/data/case/update/@date"));
        assertEquals(ATTR, parsedParameters.get("/data/case/update/number/@attr"));
    }
}
