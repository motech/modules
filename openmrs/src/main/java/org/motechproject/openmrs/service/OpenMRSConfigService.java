package org.motechproject.openmrs.service;

import org.motechproject.openmrs.config.Config;
import org.motechproject.openmrs.config.Configs;
import org.motechproject.openmrs.exception.config.ConfigurationAlreadyExistsException;
import org.motechproject.openmrs.exception.config.ConfigurationNotFoundException;

/**
 * Service responsible for managing configurations for the OpenMRS servers.
 */
public interface OpenMRSConfigService {

    /**
     * Deletes all configurations.
     */
    void deleteAllConfigs();

    /**
     * Saves the given list of configurations.
     * @param configs list of configurations to be saved
     */
    void saveAllConfigs(Configs configs);

    /**
     * Saves the given configuration. If configuration with the same name already exists
     * {@link ConfigurationAlreadyExistsException} will be thrown.
     *
     * @param config  the configuration to be saved
     */
    void addConfig(Config config);

    /**
     * Updates configuration with the same name with the passed data. If configuration with the name does not exist a
     * {@link ConfigurationNotFoundException} exception will be thrown.
     *
     * @param config  the configuration to serve as an update source
     */
    void updateConfig(Config config);

    /**
     * Deletes configuration with the given name. If configuration with the name does not exist a
     * {@link ConfigurationNotFoundException} exception will be thrown.
     *
     * @param name  the name of the configuration to be deleted
     */
    void deleteConfig(String name);

    /**
     * Marks configuration with the given name as a default one. If configuration with the name does not exist a
     * {@link ConfigurationNotFoundException} exception will be thrown.
     *
     * @param name  the name of the configuration to be set as default
     */
    void markConfigAsDefault(String name);

    /**
     * Returns the list of all configurations.
     *
     * @return the list of all configurations
     */
    Configs getConfigs();

    /**
     * Returns configuration by its name.
     *
     * @param name  the name of the configuration
     * @return the configuration with the given name
     */
    Config getConfigByName(String name);

    /**
     * Returns the default configuration.
     *
     * @return the default configuration
     */
    Config getDefaultConfig();

    /**
     *  Verifies OpenMRS connection with the given configuration.
     *
     * @param config  the configuration to be verified
     * @return true if connection was successful
     */
    boolean verifyConfig(Config config);
}
