package org.motechproject.openmrs19.tasks;

import org.apache.velocity.app.VelocityEngine;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.motechproject.openmrs19.config.Config;
import org.motechproject.openmrs19.config.Configs;
import org.motechproject.openmrs19.service.OpenMRSConfigService;
import org.motechproject.openmrs19.tasks.builder.OpenMRSTaskDataProviderBuilder;
import org.springframework.ui.velocity.VelocityEngineFactoryBean;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OpenMRSTaskDataProviderBuilderTest {

    private OpenMRSTaskDataProviderBuilder openMRSTaskDataProviderBuilder;

    @Mock
    private OpenMRSConfigService configService;

    @Before
    public void setUp() {
        openMRSTaskDataProviderBuilder = new OpenMRSTaskDataProviderBuilder();
    }

    @Test
    public void generatedProviderShouldReturnNullWhenConfigsListIsEmpty() {
        when(configService.getConfigs()).thenReturn(new Configs());
        openMRSTaskDataProviderBuilder.setOpenMRSConfigService(configService);

        assertNull(openMRSTaskDataProviderBuilder.generateDataProvider());
    }

    @Test
    public void generatedProviderShouldReturnJSONWithFourObjectsForOneConfiguration() throws IOException {
        assertEquals(4, getGeneratedJSONObjectsCount(1));
    }

    @Test
    public void generatedProviderShouldReturnJSONWithEightObjectsForTwoConfigurations() throws IOException {
        assertEquals(8, getGeneratedJSONObjectsCount(2));
    }

    private int getGeneratedJSONObjectsCount(int amountOfConfigs) throws IOException {
        List<Config> simpleConfigs = new ArrayList<>();

        for(int i = 0; i < amountOfConfigs; i++) {
            Config simpleConfig = new Config();
            simpleConfig.setName("simpleConfiguration" + Integer.toString(i));
            simpleConfigs.add(simpleConfig);
        }

        Configs configs = new Configs(simpleConfigs, "simpleConfiguration");

        when(configService.getConfigs()).thenReturn(configs);
        openMRSTaskDataProviderBuilder.setOpenMRSConfigService(configService);

        VelocityEngineFactoryBean vefb = new VelocityEngineFactoryBean();
        Map<String, Object> velocityPropertiesMap = new HashMap<String, Object>();
        velocityPropertiesMap.put("resource.loader", "class");
        velocityPropertiesMap.put("class.resource.loader.class", "org.motechproject.openmrs19.tasks.VelocityResourceLoader");
        vefb.setVelocityPropertiesMap(velocityPropertiesMap);

        VelocityEngine velocityEngine = vefb.createVelocityEngine();
        assertNotNull(velocityEngine);

        openMRSTaskDataProviderBuilder.setVelocityEngine(velocityEngine);

        String generatedJSON = openMRSTaskDataProviderBuilder.generateDataProvider();

        JSONObject jsonResult = new JSONObject(generatedJSON);
        JSONArray objects = jsonResult.getJSONArray("objects");

        return objects.length();
    }

}
