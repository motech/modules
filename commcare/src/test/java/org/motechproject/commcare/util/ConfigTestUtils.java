package org.motechproject.commcare.util;

import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.config.Config;

/**
 * Utility class for preparing simple configurations for tests.
 */
public class ConfigTestUtils {

    /**
     * Prepares config with the given {@code configName}.
     *
     * @param configName  the name of the configuration
     * @return the config with name given name
     */
    public static Config prepareConfig(String configName) {

        AccountConfig accountConfig = new AccountConfig();
        accountConfig.setDomain("domain");
        accountConfig.setBaseUrl("www.base.url");
        accountConfig.setUsername("username");
        accountConfig.setPassword("password");

        Config config = new Config();
        config.setName(configName);
        config.setAccountConfig(accountConfig);

        return config;
    }

    /**
     * Utility class, should not be initiated.
     */
    private ConfigTestUtils() {
    }
}
