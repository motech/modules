package org.motechproject.commcare.service;

import org.motechproject.commcare.config.Config;
import org.motechproject.commcare.config.Configs;
import org.motechproject.commcare.exception.CommcareConnectionFailureException;

/**
 * Interface responsible for managing multiple configurations.
 */
public interface CommcareConfigService {

    /**
     * Creates the given configuration.
     *
     * @param configuration  the configuration to be saved
     * @return  the saved configuration
     */
    Config saveConfig(Config configuration) throws CommcareConnectionFailureException;

    /**
     * Deletes the configuration with the given name.
     *
     * @param name  the name of the configuration
     */
    void deleteConfig(String name);

    /**
     * Sets configuration with the given name as default.
     *
     * @param name  the name of the configuration
     */
    void setDefault(String name);

    /**
     * Returns the configuration with the given name.
     *
     * @param name  the name of the configuration
     * @return  the configuration with the given name
     */
    Config getByName(String name);

    /**
     * Returns the default configuration.
     *
     * @return  the default configuration
     */
    Config getDefault();

    /**
     * Returns all the stored configurations.
     *
     * @return  the stored configurations
     */
    Configs getConfigs();

    /**
     * Verifies CommCareHQ connection with the given configuration.
     *
     * @param config  the configuration to be verified
     * @return true if connection was successful
     * @throws IllegalArgumentException if one or more parameters are empty
     */
    boolean verifyConfig(Config config);

    /**
     * Returns the template for the configuration.
     *
     * @return  the template for the configuration
     */
    Config create();

    /**
     * Returns the base URL for CommcareHQ forwarding endpoints.
     *
     * @return  the base URL
     */
    String getBaseUrl();

    /**
     * Checks whether configuration with the given name exists.
     *
     * @return  true if config with given name exists, false otherwise
     */
    boolean exists(String config);
}
