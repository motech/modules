package org.motechproject.commcare.web;

import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.events.constants.EventDataKeys;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.EventRelay;
import org.springframework.test.web.server.MockMvc;
import org.springframework.test.web.server.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.motechproject.commcare.events.constants.EventSubjects.SCHEMA_CHANGE_EVENT;
import static org.springframework.test.web.server.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.server.result.MockMvcResultMatchers.status;

public class AppSchemaChangeControllerTest {

    @Mock
    private CommcareConfigService configService;

    @Mock
    private EventRelay eventRelay;

    private AppSchemaChangeController appSchemaChangeController;

    private MockMvc controller;

    private Config config;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        config = ConfigsUtils.prepareConfigOne();

        when(configService.getByName(config.getName())).thenReturn(config);

        appSchemaChangeController = new AppSchemaChangeController(eventRelay, configService);
        controller = MockMvcBuilders.standaloneSetup(appSchemaChangeController).build();
    }

    @Test
    public void shouldReceiveSchemaChange() throws Exception {
        appSchemaChangeController.receiveSchemaChange(config.getName());

        Map<String, Object> params = new HashMap<>();
        params.put(EventDataKeys.CONFIG_DOMAIN, config.getAccountConfig().getDomain());
        params.put(EventDataKeys.CONFIG_BASE_URL, config.getAccountConfig().getBaseUrl());
        params.put(EventDataKeys.CONFIG_NAME, config.getName());

        verify(eventRelay).sendEventMessage(new MotechEvent(SCHEMA_CHANGE_EVENT, params));
    }

    @Test
    public void shouldHandleSchemaChangeRequest() throws Exception {
        controller.perform(
                get("/appSchemaChange/" + config.getName())
        ).andExpect(
                status().is(HttpStatus.SC_OK)
        );

        Map<String, Object> params = new HashMap<>();
        params.put(EventDataKeys.CONFIG_DOMAIN, config.getAccountConfig().getDomain());
        params.put(EventDataKeys.CONFIG_BASE_URL, config.getAccountConfig().getBaseUrl());
        params.put(EventDataKeys.CONFIG_NAME, config.getName());

        verify(eventRelay).sendEventMessage(new MotechEvent(SCHEMA_CHANGE_EVENT, params));
    }
}
