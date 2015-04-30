package org.motechproject.commcare.web;

import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.exception.CommcareAuthenticationException;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;
import org.motechproject.commcare.service.CommcareConfigService;
import org.motechproject.commcare.service.CommcareDataForwardingEndpointService;
import org.motechproject.commcare.util.ConfigsUtils;
import org.motechproject.server.config.domain.SettingsRecord;
import org.osgi.framework.BundleException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(MockitoJUnitRunner.class)
public class ConfigControllerTest {

    @Mock
    private CommcareConfigService configService;

    @Mock
    private SettingsRecord motechSettings;

    @Mock
    private CommcareDataForwardingEndpointService forwardingEndpointService;

    @InjectMocks
    private ConfigController controller = new ConfigController(configService);

    private Configs configs;

    @Before
    public void setUp() {
        initMocks(this);

        List<Config> configList = new ArrayList<>();
        configList.add(ConfigsUtils.prepareConfigOne());
        configList.add(ConfigsUtils.prepareConfigTwo());

        configs = new Configs(configList);

        when(configService.getConfigs()).thenReturn(configs);
    }

    @Test
    public void testGetSettings() {

        Configs configs = controller.getConfigs();

        assertEquals(configs, this.configs);
    }

    @Test
    public void shouldSetDefault() {

        String name = "someName";

        controller.makeDefault(name);

        verify(configService).setDefault(name);
    }

    @Test
    public void testSaveSettings() throws BundleException, CommcareAuthenticationException, CommcareConnectionFailureException {

        Config config = ConfigsUtils.prepareConfigThree();

        controller.saveConfig(config);

        verify(configService).saveConfig(config);
    }

    @Test
    public void shouldDeleteConfig() {

        String name = "someName";

        controller.deleteConfig(name);

        verify(configService).deleteConfig(name);
    }

    @Test
    public void shouldVerifyConfig() throws CommcareAuthenticationException {

        Config config = ConfigsUtils.prepareConfigOne();

        when(configService.verifyConfig(config)).thenReturn(true);

        controller.verifyConfig(config);

        verify(configService).verifyConfig(config);
    }

    @Test
    public void shouldGetBaseEndpoint() throws IOException {

        String baseUrl = "http://www.base.url";

        when(configService.getBaseUrl()).thenReturn(baseUrl);

        String returned = controller.getBaseEndpoints();

        assertEquals(new ObjectMapper().writeValueAsString(new StringMessage(baseUrl)), returned);
    }
}