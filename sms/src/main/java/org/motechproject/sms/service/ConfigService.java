package org.motechproject.sms.service;

import org.motechproject.sms.configs.Config;
import org.motechproject.sms.configs.Configs;

import java.util.List;

/**
 * Config service, manages SMS Configs. A config represents the way a particular user connects to an SMS provider.
 * See {@link org.motechproject.sms.configs.Config}
 */
public interface ConfigService {

    /**
     * Returns the default configuration for sending SMS message. Only one of the configurations can be marked as
     * the default by the user.
     * @return the default configuration, never null
     * @throws IllegalStateException if no default configuration exists
     */
    Config getDefaultConfig();

    /**
     * Returns an object containing all SMS configurations from the system.
     * @return all SMS configurations
     */
    Configs getConfigs();

    /**
     * Returns a collection containing all SMS configurations from the system.
     * @return all SMS configurations
     */
    List<Config> getConfigList();

    /**
     * Checks whether a configuration with the given name exists.
     * @param name the name of the configuration to check
     * @return true if the configuration exists, false otherwise
     */
    boolean hasConfig(String name);

    /**
     * Returns the confguration with the given name.
     * @param name the name of the configuration
     * @return the config with the given name, never null
     * @throws IllegalArgumentException if a config with the given name does not exist
     */
    Config getConfig(String name);

    /**
     * Returns the config with the given name, or the default config if the provided name is null or empty.
     * @param name the name of the desired configuration, or null/empty string for the default configuration
     * @return the matching configuration
     * @throws IllegalStateException if a blank string is provided and there is no default configuration
     * @throws IllegalArgumentException if a configuration with the given name does not exist
     */
    Config getConfigOrDefault(String name);

    /**
     * Updates the SMS configs using the provided object. The configuration will be persisted with the Motech config
     * system. Configurations which are no longer part of the provided object will be dropped.
     * @param configs an object containing all the SMS configurations
     */
    void updateConfigs(Configs configs);

    /**
     * Checks whether there are no configurations available.
     * @return true if there are no configurations, false otherwise
     */
    boolean hasConfigs();

    /**
     * Returns the base url to this MOTECH server. The url is read from the platform settings.
     * @return the base url to this MOTECH server
     */
    String getServerUrl();
}
