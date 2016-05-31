package org.motechproject.commcare.tasks.builder;


import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.tasks.contract.ActionEventRequest;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ImportFormActionBuilderTest {

    @Mock
    private CommcareConfigService configService;

    private ImportFormActionBuilder importFormActionBuilder;

    private Configs configs = ConfigsUtils.prepareConfigsWithTwoConfigs();

    @Before
    public void setUp() {
        initMocks(this);

        when(configService.getConfigs()).thenReturn(configs);

        importFormActionBuilder = new ImportFormActionBuilder(configService);
    }

    @Test
    public void buildActionsShouldReturnTwoActionsForTwoConfigs() {
        List<ActionEventRequest> eventRequests = importFormActionBuilder.buildActions();
        assertEquals(2, eventRequests.size());
    }
}
