package org.motechproject.commcare.util;

import org.motechproject.commcare.config.AccountConfig;
import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.config.Configs;

/**
 * Utility class for preparing configurations for tests.
 */
public class ConfigsUtils {

    public static Config prepareConfigOne() {

        Config config = new Config();
        config.setName("ConfigOne");
        config.setForwardStubs(true);
        config.setForwardSchema(true);
        config.setForwardForms(true);
        config.setForwardCases(true);
        config.setEventStrategy("full");

        AccountConfig accountConfig = new AccountConfig();
        accountConfig.setBaseUrl("https://baseOne.url");
        accountConfig.setDomain("domainOne");
        accountConfig.setUsername("userOne");
        accountConfig.setPassword("passOne");

        config.setAccountConfig(accountConfig);

        return config;
    }

    public static Config prepareConfigTwo() {

        Config config = new Config();
        config.setName("ConfigTwo");
        config.setForwardStubs(false);
        config.setForwardSchema(false);
        config.setForwardForms(false);
        config.setForwardCases(false);
        config.setEventStrategy("minimal");

        AccountConfig accountConfig = new AccountConfig();
        accountConfig.setBaseUrl("https://baseTwo.url");
        accountConfig.setDomain("domainTwo");
        accountConfig.setUsername("userTwo");
        accountConfig.setPassword("passTwo");

        config.setAccountConfig(accountConfig);

        return config;
    }

    public static Config prepareConfigThree() {

        Config config = new Config();
        config.setName("ConfigThree");
        config.setForwardStubs(false);
        config.setForwardSchema(false);
        config.setForwardForms(false);
        config.setForwardCases(false);
        config.setEventStrategy("minimal");

        AccountConfig accountConfig = new AccountConfig();
        accountConfig.setBaseUrl("https://baseThree.url");
        accountConfig.setDomain("domainThree");
        accountConfig.setUsername("userThree");
        accountConfig.setPassword("passThree");

        config.setAccountConfig(accountConfig);

        return config;
    }

    public static Configs prepareConfigsWithOneConfig() {

        Configs configs = new Configs();

        Config config = prepareConfigOne();

        configs.saveConfig(config);
        configs.setDefaultConfigName(config.getName());

        return configs;
    }
}
