package org.motechproject.ivr.service;


import org.motechproject.ivr.domain.Config;
import org.motechproject.ivr.domain.Configs;

/**
 * Config service, manages IVR configs. A {@link org.motechproject.ivr.domain.Config} represents the way to interact with an IVR provider.
 * See {@link org.motechproject.ivr.domain.Config}
 */
public interface ConfigService {

    /**
     * Retrieves the config with the given name.
     * @param name the name of the configuration
     * @return the config found for the given name, never null
     * @throws org.motechproject.ivr.exception.ConfigNotFoundException if a config with the given name does not exist
     */
    Config getConfig(String name);

    /**
     * Returns all stored configurations.
     * @return Configs object
     */
    Configs allConfigs();

    /**
     * Checks whether a configuration with the given name exists.
     * @param name the config name to check
     * @return true if a configuration with the given name exists, false otherwise
     */
    boolean hasConfig(String name);

    /**
     * Updates the configuration. The old configuration collection is overwritten by the new one.
     * @param configs the collection of configurations to save with default configuration
     */
    void updateConfigs(Configs configs);

    /**
     * Returns the default configuration name
     * @return String default configuration name
     */
    String getDefaultConfig();
}
