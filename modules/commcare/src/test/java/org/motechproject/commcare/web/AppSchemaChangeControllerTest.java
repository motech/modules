package org.motechproject.commcare.web;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commcare.events.constants.EventSubjects.SCHEMA_CHANGE_EVENT;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class AppSchemaChangeControllerTest {

    @Mock
    private EventRelay eventRelay;

    private AppSchemaChangeController appSchemaChangeController;

    private MockMvc controller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        appSchemaChangeController = new AppSchemaChangeController(eventRelay);
        controller = MockMvcBuilders.standaloneSetup(appSchemaChangeController).build();
    }

    @Test
    public void shouldReceiveSchemaChange() throws Exception {
        appSchemaChangeController.receiveSchemaChange();

        verify(eventRelay).sendEventMessage(new MotechEvent(SCHEMA_CHANGE_EVENT));
    }

    @Test
    public void shouldHandleSchemaChangeRequest() throws Exception {
        controller.perform(
                get("/appSchemaChange")
        ).andExpect(
                status().is(HttpStatus.SC_OK)
        );

        verify(eventRelay).sendEventMessage(new MotechEvent(SCHEMA_CHANGE_EVENT));
    }
}
