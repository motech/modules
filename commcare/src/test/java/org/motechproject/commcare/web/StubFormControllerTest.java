package org.motechproject.commcare.web;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.events.constants.EventSubjects;
import org.motechproject.commcare.exception.EndpointNotSupported;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commons.api.TasksEventParser;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class StubFormControllerTest {

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareConfigService configService;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private StubFormController stubController = new StubFormController(eventRelay, configService);

    private Config config;

    private String json = "{\"received_on\":\"1-1-2012\",\"form_id\":\"id123\",\"case_ids\":[\"123\",\"345\"]}";

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);

        config = ConfigsUtils.prepareConfigOne();

        when(configService.getByName(config.getName())).thenReturn(config);
        when(request.getPathInfo()).thenReturn("/stub/" + config.getName());
    }

    @Test
    public void testIncomingFormStubJsonFailure() throws IOException, EndpointNotSupported {

        ArgumentCaptor<MotechEvent> argumentCall = ArgumentCaptor.forClass(MotechEvent.class);

        stubController.receiveFormEvent(request, "test");
        verify(eventRelay, times(1)).sendEventMessage(argumentCall.capture());

        MotechEvent event = argumentCall.getValue();

        assertEquals(event.getSubject(), EventSubjects.FORM_STUB_FAIL_EVENT);
    }

    @Test
    public void testIncomingFormStubJsonSuccess() throws IOException, EndpointNotSupported {

        ArgumentCaptor<MotechEvent> argumentCall = ArgumentCaptor.forClass(MotechEvent.class);

        stubController.receiveFormEvent(request, json);
        verify(eventRelay, times(1)).sendEventMessage(argumentCall.capture());

        MotechEvent event = argumentCall.getValue();

        assertEquals(event.getSubject(), EventSubjects.FORM_STUB_EVENT);

        Map<String, Object> eventParameters = event.getParameters();
        assertEquals(new HashSet<String>(asList(EventDataKeys.CONFIG_NAME, EventDataKeys.FORM_ID, EventDataKeys.CASE_IDS,
                        EventDataKeys.RECEIVED_ON, TasksEventParser.CUSTOM_PARSER_EVENT_KEY)),
                eventParameters.keySet());
        assertEquals(eventParameters.get(EventDataKeys.FORM_ID), "id123");
        assertEquals(asList("123", "345"), eventParameters.get(EventDataKeys.CASE_IDS));
        assertEquals(eventParameters.get(EventDataKeys.RECEIVED_ON), "1-1-2012");
    }
}
