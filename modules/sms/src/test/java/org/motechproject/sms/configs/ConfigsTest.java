package org.motechproject.sms.configs;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ConfigsTest {

    private static final String NAME_ONE = "one";
    private static final String NAME_TWO = "two";
    private static final String NAME_THREE = "three";
    private Config configOne, configTwo;
    List<Config> configList = new ArrayList<>();

    @Before
    public void setup() {
        configOne = new Config();
        configOne.setName(NAME_ONE);
        configTwo = new Config();
        configTwo.setName(NAME_TWO);
        configList.add(configOne);
        configList.add(configTwo);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenGettingDefaultConfigWhileNoneSet() {
        Configs emptyConfigs = new Configs();
        emptyConfigs.getDefaultConfig();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenGettingDefaultConfigWithConfigsWhileNoneSetEven() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        configs.getDefaultConfig();
    }

    @Test
    public void shouldReturnConfig() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        assertEquals(configTwo, configs.getConfig(NAME_TWO));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenGettingInvalidConfig() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        configs.getConfig(NAME_THREE);
    }

    @Test
    public void shouldHaveConfig() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        assertEquals(true, configs.hasConfig(NAME_TWO));
    }

    @Test
    public void shouldNotHaveConfig() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        assertEquals(false, configs.hasConfig(NAME_THREE));
    }

    @Test
    public void shouldReturnDefaultConfig() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        configs.setDefaultConfigName(configList.get(configList.indexOf(configTwo)).getName());
        assertEquals(configTwo, configs.getDefaultConfig());
    }

    @Test
    public void shouldReturnDefaultConfigWhenGivenNull() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        configs.setDefaultConfigName(configList.get(configList.indexOf(configTwo)).getName());
        assertEquals(configTwo, configs.getConfigOrDefault(null));
    }

    @Test
    public void shouldReturnDefaultConfigWhenGivenNonNull() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        configs.setDefaultConfigName(configList.get(configList.indexOf(configTwo)).getName());
        assertEquals(configTwo, configs.getConfigOrDefault(NAME_TWO));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowWhenGettingInvalidConfigOrDefault() {
        Configs configs = new Configs();
        configs.setConfigs(configList);
        configs.setDefaultConfigName(configList.get(configList.indexOf(configTwo)).getName());
        configs.getConfigOrDefault(NAME_THREE);
    }
}
