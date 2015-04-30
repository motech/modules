package org.motechproject.commcare.config;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.motechproject.commcare.exception.ConfigurationNotFoundException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Stores information about all the created Commcare configurations.
 */
public class Configs {

    private List<Config> configs;

    private String defaultConfigName;

    public Configs() {
        this(null);
    }

    public Configs(List<Config> configs) {
        if (configs != null) {
            this.configs = configs;
        } else {
            this.configs = new ArrayList<>();
        }
    }

    /**
     * Saves given configuration.
     *
     * @param config  the configuration to save
     */
    public void saveConfig(Config config) {
        if (!contains(config)) {
            configs.add(config);
        }
    }

    /**
     * Returns configuration by its name.
     *
     * @param name  the name of the configuration, null will return default configuration
     * @return  the configuration with given name or default configuration if null was given
     * @throws ConfigurationNotFoundException when configuration with the given name doesn't exists or if no default configuration
     * was defined(only if null was passes as parameter)
     */
    public Config getByName(String name) {

        Config configuration = null;

        if (name == null) {
            configuration = getDefault();
        } else {
            for (Config config : configs) {
                if (config.getName().equals(name)) {
                    configuration = config;
                }
            }

            if (configuration == null) {
                throw new ConfigurationNotFoundException(String.format("Configuration with name \"%s\" doesn't exists!", name));
            }
        }

        return configuration;
    }

    /**
     * Returns default configuration.
     *
     * @return  the default configuration
     * @throws ConfigurationNotFoundException if there were no default configuration defined
     */
    @JsonIgnore
    public Config getDefault() {

        if (StringUtils.isBlank(defaultConfigName)) {
            throw new ConfigurationNotFoundException("No default configuration defined!");
        }

        return getByName(defaultConfigName);
    }

    /**
     * Deletes the configuration with the given name.
     *
     * @param name  the name of the configuration
     */
    public void deleteConfig(String name) {

        Iterator<Config> it = configs.iterator();

        boolean deleted = false;

        while (it.hasNext()) {

            Config config = it.next();
            if (config.getName().equals(name)) {
                it.remove();
                deleted = true;
            }
        }

        if (!deleted) {
            throw new ConfigurationNotFoundException(String.format("Configuration with name \"%s\" doesn't exists!", name));
        } else if (defaultConfigName.equals(name)) {
            String newDefaultConfigName = "";

            if (!configs.isEmpty()) {
                newDefaultConfigName = configs.get(0).getName();
            }
            defaultConfigName = newDefaultConfigName;
        }
    }

    /**
     * Checks whether default configuration has been set.
     *
     * @return  true if there is default configuration, else otherwise
     */
    @JsonIgnore
    public boolean hasDefault() {
        return StringUtils.isNotBlank(defaultConfigName);
    }

    /**
     * Updates given configuration
     *
     * @param config  the update source
     */
    public void updateConfig(Config config) {
        deleteConfig(config.getName());
        configs.add(config);
    }

    /**
     * Checks whether given configuration already exists.
     *
     * @param configuration  the configuration to be checked
     * @return  true if configuration exists, false otherwise
     */
    public boolean contains(Config configuration) {
        for (Config config : configs) {
            if (config.getName().equals(configuration.getName()) &&
                    config.getAccountConfig().getBaseUrl().equals(configuration.getAccountConfig().getBaseUrl()) &&
                    config.getAccountConfig().getDomain().equals(configuration.getAccountConfig().getDomain())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if there is configuration for given URL and domain combination.
     *
     * @param baseUrl  the base URL of the configuration
     * @param domain  the domain of the configuration
     * @return  true of configuration exists, false otherwise
     */
    public boolean validateUrlAndDomain(String baseUrl, String domain) {

        for (Config config : configs) {
            if (baseUrl.equals(config.getAccountConfig().getBaseUrl()) && domain.equals(config.getAccountConfig().getDomain())) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks whether configuration with given name already exists.
     *
     * @param name  the name of the configuration
     * @return  true if configuration with given name already exists, false otherwise
     */
    public boolean nameInUse(String name) {
        for (Config config : configs) {
            if (config.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public boolean setDefaultConfigName(String name) {
        if (!name.equals(defaultConfigName)) {
            defaultConfigName = name;
            return true;
        }
        return false;
    }

    public String getDefaultConfigName() {
        return defaultConfigName;
    }
}
