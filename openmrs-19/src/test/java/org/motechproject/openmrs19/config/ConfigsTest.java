package org.motechproject.openmrs19.config;

import org.junit.Test;
import org.motechproject.openmrs19.exception.config.ConfigurationAlreadyExistsException;
import org.motechproject.openmrs19.exception.config.ConfigurationNotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

public class ConfigsTest {

    private static final String SUFFIX_ONE = "one";
    private static final String SUFFIX_TWO = "two";
    private static final String SUFFIX_THREE = "three";

    @Test
    public void shouldAddMultipleValidConfigurations() {
        Configs configs = prepareConfigs();

        configs.add(ConfigDummyData.prepareConfig(SUFFIX_ONE));
        configs.add(ConfigDummyData.prepareConfig(SUFFIX_TWO));
        configs.add(ConfigDummyData.prepareConfig(SUFFIX_THREE));

        assertThat(configs, equalTo(prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE)));
    }

    @Test(expected = ConfigurationAlreadyExistsException.class)
    public void shouldThrowConfigurationAlreadyExistsExceptionWhenTryingToAddMultipleConfigurationsWithTheSameName() {
        Configs configs = prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE);

        try {
            configs.add(ConfigDummyData.prepareConfig(SUFFIX_TWO));
        } finally {
            assertThat(configs, equalTo(prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE)));
        }
    }

    @Test
    public void shouldUpdateConfigurationIfItExists() {
        Configs configs = prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE);

        Config config = ConfigDummyData.prepareConfig(SUFFIX_TWO);
        config.setOpenMrsUrl("otherOpenMrsUrl");
        config.setUsername("otherUsername");
        config.setPassword("otherPassword");
        config.setMotechId("otherMotechId");
        configs.update(config);

        Configs expectedConfigs = prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE);
        expectedConfigs.getConfigs().set(1, config);

        assertThat(configs, equalTo(expectedConfigs));
    }

    @Test(expected = ConfigurationNotFoundException.class)
    public void shouldThrowConfigurationNotFoundExceptionIfTryingToUpdateNonExistentConfiguration() {
        Configs configs = prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE);

        try {
            configs.update(ConfigDummyData.prepareConfig("four"));
        } finally {
            assertThat(configs, equalTo(prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE)));
        }
    }

    @Test
    public void shouldDeleteConfigurationIfItExists() {
        Configs configs = prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE);

        configs.delete(ConfigDummyData.getName(SUFFIX_TWO));

        assertThat(configs, equalTo(prepareConfigs(SUFFIX_ONE, SUFFIX_THREE)));
    }

    @Test(expected = ConfigurationNotFoundException.class)
    public void shouldThrowConfigurationNotFoundExceptionIfConfigurationWithTheGivenNameDoesNotExist() {
        Configs configs = prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE);

        try {
            configs.delete("nonexistentConfigurationName");
        } finally {
            assertThat(configs, equalTo(prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE)));
        }
    }

    @Test
    public void shouldSetNewDefaultConfigurationIfThePreviousHasBeenDeleted() {
        Configs configs = prepareConfigs(SUFFIX_ONE, SUFFIX_TWO, SUFFIX_THREE);

        configs.delete(ConfigDummyData.getName(SUFFIX_ONE));

        assertThat(configs, equalTo(prepareConfigs(SUFFIX_TWO, SUFFIX_THREE)));
    }

    @Test
    public void shouldUnsetDefaultConfigurationIfTheLastConfigurationIsDeleted() {
        Configs configs = prepareConfigs(SUFFIX_ONE);

        configs.delete(ConfigDummyData.getName(SUFFIX_ONE));

        assertThat(configs, equalTo(prepareConfigs()));
    }

    private Configs prepareConfigs(String... suffixes) {
        List<Config> configs = new ArrayList<>();

        for (String suffix : suffixes) {
            configs.add(ConfigDummyData.prepareConfig(suffix));
        }

        return new Configs(configs, configs.size() > 0 ? configs.get(0).getName() : null);
    }

}