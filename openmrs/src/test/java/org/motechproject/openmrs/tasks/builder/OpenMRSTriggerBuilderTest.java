package org.motechproject.openmrs.tasks.builder;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.Configs;
import org.motechproject.openmrs.service.EventKeys;
import org.motechproject.openmrs.service.OpenMRSConfigService;
import org.motechproject.openmrs.tasks.constants.DisplayNames;
import org.motechproject.tasks.contract.TriggerEventRequest;

import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class OpenMRSTriggerBuilderTest {
    @Mock
    private OpenMRSConfigService configService;

    private Configs configs;

    private OpenMRSTriggerBuilder triggerBuilder;

    private static final String CONFIG_NAME = "openmrs";

    @Before
    public void setUp() {
        initMocks(this);
        configs = prepareConfigs();
        when(configService.getConfigs()).thenReturn(configs);
        triggerBuilder = new OpenMRSTriggerBuilder(configService);
    }

    @Test
    public void shouldBuildProperTriggerEventRequests() {
        List<TriggerEventRequest> triggerEventRequests = triggerBuilder.buildTriggers();
        assertFalse(triggerEventRequests.isEmpty());

        assertEquals(3, triggerEventRequests.get(0).getEventParameters().size());

        assertEquals(DisplayNameHelper.buildDisplayName(DisplayNames.COHORT_GOTMEMBER, CONFIG_NAME),
                triggerEventRequests.get(0).getDisplayName());

        assertEquals(EventKeys.BASE_SUBJECT + "Cohort.GotMember", triggerEventRequests.get(0).getSubject());
    }

    private Configs prepareConfigs(){
        Configs configs = new Configs();

        Config firstConfig = new Config();
        firstConfig.setName(CONFIG_NAME);
        firstConfig.setMotechPatientIdentifierTypeName("MOTECH Id");
        firstConfig.setOpenMrsUrl("http://172.0.2:8080/openmrs");
        firstConfig.setOpenMrsVersion("1.9");
        firstConfig.setPassword("Admin123");
        firstConfig.setUsername("admin");

        configs.add(firstConfig);

        return configs;
    }


}
