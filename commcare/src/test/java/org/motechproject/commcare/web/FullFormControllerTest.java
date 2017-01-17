package org.motechproject.commcare.web;

import com.google.common.collect.Multimap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.impl.CommcareFormsEventParser;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commcare.util.DummyCommcareSchema;
import org.motechproject.commcare.util.ResponseXML;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.mock.web.MockHttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static org.motechproject.commcare.events.constants.EventDataKeys.ATTRIBUTES;
import static org.motechproject.commcare.events.constants.EventDataKeys.ELEMENT_NAME;
import static org.motechproject.commcare.events.constants.EventDataKeys.RECEIVED_ON;
import static org.motechproject.commcare.events.constants.EventDataKeys.SUB_ELEMENTS;
import static org.motechproject.commcare.events.constants.EventDataKeys.VALUE;
import static org.motechproject.commcare.events.constants.EventSubjects.DEVICE_LOG_EVENT;
import static org.motechproject.commcare.events.constants.EventSubjects.FORMS_EVENT;
import static org.motechproject.commcare.util.ResponseXML.ATTR1;
import static org.motechproject.commcare.util.ResponseXML.ATTR2;
import static org.motechproject.commcare.util.ResponseXML.ATTR2_VALUE;

public class FullFormControllerTest {

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareConfigService configService;

    private FullFormController controller;
    private MockHttpServletRequest request;
    private Config config;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        controller = new FullFormController(eventRelay, configService);

        config = ConfigsUtils.prepareConfigOne();
        when(configService.getByName(config.getName())).thenReturn(config);

        request = new MockHttpServletRequest();
        request.addHeader("received-on", "2012-07-21T15:22:34.046462Z");
        request.setPathInfo("/forms/" + config.getName());
    }

    @Test
    public void testIncomingFormsFailure() throws EndpointNotSupported {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);

        controller.receiveForm("", request, null);
        verify(eventRelay, times(2)).sendEventMessage(captor.capture());
    }

    @Test
    public void testIncomingDeviceReport() throws EndpointNotSupported {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);

        controller.receiveForm(ResponseXML.getDeviceReportXML(), request, DummyCommcareSchema.APP_ID1);

        verify(eventRelay).sendEventMessage(captor.capture());
        MotechEvent event = captor.getValue();

        assertEquals(event.getSubject(), DEVICE_LOG_EVENT);

        Map<String, Object> parameters = event.getParameters();
        assertTrue(parameters.containsKey(RECEIVED_ON));
        assertTrue(parameters.containsKey(ATTRIBUTES));
        assertTrue(parameters.containsKey(SUB_ELEMENTS));
        assertEquals("device_report", parameters.get(VALUE));
        assertEquals("deviceLog", parameters.get(ELEMENT_NAME));

        assertEquals("2012-07-21T15:22:34.046462Z", parameters.get(RECEIVED_ON));

        Map<String, String> attributes = (Map<String, String>) parameters.get(ATTRIBUTES);
        assertEquals(2, attributes.size());
        assertEquals("http://code.javarosa.org/devicereport", attributes.get("xmlns"));
        assertEquals(DummyCommcareSchema.APP_ID1, attributes.get("app_id"));

        Multimap<String, Object> subElements = (Multimap<String, Object>) parameters.get(SUB_ELEMENTS);
        assertEquals(1, subElements.size());

        assertHasKeys(subElements, "device_id");
    }

    @Test
    public void testIncomingFormsSuccess() throws EndpointNotSupported {
        ArgumentCaptor<MotechEvent> captor = ArgumentCaptor.forClass(MotechEvent.class);

        controller.receiveForm(ResponseXML.getFormXML(), request, DummyCommcareSchema.APP_ID1);
        verify(eventRelay).sendEventMessage(captor.capture());

        MotechEvent event = captor.getValue();
        assertEquals(event.getSubject(), FORMS_EVENT);

        Map<String, Object> parameters = event.getParameters();
        assertTrue(parameters.containsKey(RECEIVED_ON));
        assertTrue(parameters.containsKey(ELEMENT_NAME));
        assertTrue(parameters.containsKey(SUB_ELEMENTS));
        assertTrue(parameters.containsKey(VALUE));
        assertTrue(parameters.containsKey(ATTRIBUTES));

        assertTrue(parameters.containsKey(TasksEventParser.CUSTOM_PARSER_EVENT_KEY));
        assertEquals(parameters.get(TasksEventParser.CUSTOM_PARSER_EVENT_KEY), CommcareFormsEventParser.PARSER_NAME);

        assertEquals("2012-07-21T15:22:34.046462Z", parameters.get(RECEIVED_ON));

        Map<String, String> attributes = (Map<String, String>) parameters.get(ATTRIBUTES);
        assertEquals(5, attributes.size());
        assertEquals("1", attributes.get("uiVersion"));
        assertEquals("41", attributes.get("version"));
        assertEquals(ResponseXML.FORM_NAME, attributes.get("name"));
        assertEquals(DummyCommcareSchema.XMLNS1, attributes.get("xmlns"));
        assertEquals(DummyCommcareSchema.APP_ID1, attributes.get("app_id"));

        Multimap<String, Object> subElements = (Multimap<String, Object>) parameters.get(SUB_ELEMENTS);
        assertEquals(5, subElements.size());

        assertHasKeys(subElements, ATTR1, ATTR2, "case", "cc_delegation_stub", "meta");

        List isPregnant = new ArrayList(subElements.get(ATTR2));
        assertEquals(1, isPregnant.size());
        assertEquals(ATTR2_VALUE, ((Map<String, Object>) isPregnant.get(0)).get(EventDataKeys.VALUE));
    }

    private void assertHasKeys(Multimap<String, Object> map, String... keys) {
        for(String key: keys) {
            assertTrue(map.containsKey(key));
        }
    }
}
