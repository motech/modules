package org.motechproject.odk.service;


import org.motechproject.odk.domain.Configuration;

import java.util.List;

/**
 * Service for CRUD operations on {@link org.motechproject.odk.domain.Configuration}
 */
public interface ConfigurationService {

    /**
     * Finds a {@link Configuration} identified by name.
     * @param name The name of the configuration.
     * @return {@link Configuration}
     */
    Configuration getConfigByName(String name);

    /**
     * If the configuration already exists it is updated. Otherwise, a new
     * configuration is saved.
     * @param configuration {@link Configuration}
     */
    void addOrUpdateConfiguration(Configuration configuration);

    /**
     * Removes the configuration specified by its name
     * @param configName The name of the configuration
     */
    void removeConfiguration(String configName);

    /**
     * Returns a list of all the Configurations.
     * @return A list of {@link Configuration}
     */
    List<Configuration> getAllConfigs();
}
