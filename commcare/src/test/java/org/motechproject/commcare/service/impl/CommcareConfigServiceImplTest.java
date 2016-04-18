package org.motechproject.commcare.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.client.CommCareAPIHttpClient;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;
import org.motechproject.commcare.service.CommcareDataForwardingEndpointService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.event.listener.EventRelay;
import org.motechproject.config.SettingsFacade;
import org.motechproject.config.domain.MotechSettings;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CommcareConfigServiceImplTest {



    private CommcareConfigServiceImpl configService;

    @Mock
    private SettingsFacade settingsFacade;

    @Mock
    private CommCareAPIHttpClient commCareAPIHttpClient;

    @Mock
    private EventRelay eventRelay;

    @Mock
    private CommcareDataForwardingEndpointService commcareDataForwardingEndpointService;

    @Mock
    private MotechSettings motechSettings;

    @Before
    public void setUp() {
        when(settingsFacade.getPlatformSettings()).thenReturn(motechSettings);
        // required in constructor
        when(settingsFacade.getRawConfig("commcare-configs.json")).thenReturn(new ByteArrayInputStream("{}".getBytes()));

        configService = new CommcareConfigServiceImpl(settingsFacade, commCareAPIHttpClient, eventRelay,
                commcareDataForwardingEndpointService);
    }

    @Test
    public void shouldReturnBaseUrl() {
        when(motechSettings.getServerUrl()).thenReturn("http://demo.motechproject.org");
        assertEquals("http://demo.motechproject.org/module/commcare/", configService.getBaseUrl());

        when(motechSettings.getServerUrl()).thenReturn("https://192.168.1.1:8080/motech-platform-server/");
        assertEquals("https://192.168.1.1:8080/motech-platform-server/module/commcare/", configService.getBaseUrl());
    }

    @Test
    public void shouldReturnBlankUrlForNullOrBlankServerUrl() {
        when(motechSettings.getServerUrl()).thenReturn(null);
        assertEquals("", configService.getBaseUrl());

        when(motechSettings.getServerUrl()).thenReturn("");
        assertEquals("", configService.getBaseUrl());
    }

    @Test
    public void shouldSaveNewConfiguration() throws CommcareConnectionFailureException {

        Config config = configService.create();
        config.setName("newName");

        Config savedConfig = configService.saveNewConfig(config);
        assertEquals(config.getName(), savedConfig.getName());
    }
    @Test
    public void shouldReturnConfigurationWithUpdatedName() throws CommcareConnectionFailureException {

        List<Config> configList = new ArrayList<>();
        configList.add(ConfigsUtils.prepareConfigOne());
        configList.add(ConfigsUtils.prepareConfigTwo());
        Config firstConfig = configList.get(0);

        Configs configs = new Configs(configList);
        configs.setDefaultConfigName(firstConfig.getName());
        configService.setConfigs(configs);

        Config updatedConfig = new Config();
        updatedConfig.setAccountConfig(firstConfig.getAccountConfig());
        updatedConfig.setName("UpdatedOne");

        String oldName = firstConfig.getName();
        Config savedConfig = configService.saveUpdatedConfig(updatedConfig, oldName);

        assertEquals("UpdatedOne", savedConfig.getName());
    }
}
