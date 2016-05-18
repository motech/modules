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

import java.util.List;

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
        String displayName = "Super long question label, that is so long, that the registration of the Commcare Task channel fails! (And this is caused by the fact, that the Task module display name column only allows 255 characters, while the label you are reading has got obviously way more than that)";
        String shortenedDisplayName = displayName.substring(0, 255);

        when(schemaService.retrieveApplications("ConfigOne")).thenReturn(DummyCommcareSchema.getApplicationsWithCustomQuestionLabel(displayName));
        List<ActionEventRequest> actionEventRequests = formActionBuilder.buildActions();

        assertEquals(actionEventRequests.get(0).getActionParameters().first().getDisplayName().length(), 255);
        assertEquals(actionEventRequests.get(0).getActionParameters().first().getDisplayName(), shortenedDisplayName);
    }

    @Test
    public void shouldNotAllowActionParametersDisplayNameBlank() {
        String blankDisplayName = "";

        when(schemaService.retrieveApplications("ConfigOne")).thenReturn(DummyCommcareSchema.getApplicationsWithCustomQuestionLabel(blankDisplayName));
        List<ActionEventRequest> actionEventRequests = formActionBuilder.buildActions();

        assertEquals(actionEventRequests.get(0).getActionParameters().first().getDisplayName(), actionEventRequests.get(0).getActionParameters().first().getKey());
    }
}