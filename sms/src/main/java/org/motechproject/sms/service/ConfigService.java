package org.motechproject.sms.service;

import org.motechproject.sms.configs.Config;
import org.motechproject.sms.configs.Configs;

import java.util.List;

/**
 * Config service, manages SMS Configs. A config represents the way a particular user connects to an SMS provider.
 * See {@link org.motechproject.sms.configs.Config}
 */
public interface ConfigService {
    Config getDefaultConfig();
    Configs getConfigs();
    List<Config> getConfigList();
    boolean hasConfig(String name);
    Config getConfig(String name);
    Config getConfigOrDefault(String name);
    void updateConfigs(Configs configs);
    boolean hasConfigs();
}
