package org.motechproject.commcare.tasks.builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareSchemaService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.commcare.util.DummyCommcareSchema;
import org.motechproject.tasks.contract.ActionEventRequest;

import java.util.*;

import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import static org.junit.Assert.assertEquals;

public class FormActionBuilderTest {

    @Mock
    private CommcareSchemaService schemaService;

    @Mock
    private CommcareConfigService configService;

    private FormActionBuilder formActionBuilder;

    private Config config = ConfigsUtils.prepareConfigOne();

    @Before
    public void setUp() {
        initMocks(this);

        Configs configs = new Configs();
        configs.saveConfig(config);
        when(configService.getConfigs()).thenReturn(configs);

        formActionBuilder = new FormActionBuilder(schemaService, configService);
    }

    @Test
    public void shouldNotAllowActionParametersDisplayNameLongerThan255Characters() {
        when(schemaService.retrieveApplications("ConfigOne")).thenReturn(DummyCommcareSchema.getApplicationsForConfigFour());
        List<ActionEventRequest> actionEventRequests = formActionBuilder.buildActions();
        assertEquals(actionEventRequests.get(0).getActionParameters().first().getDisplayName().length(), 255);
    }
}