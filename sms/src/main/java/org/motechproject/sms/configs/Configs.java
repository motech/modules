package org.motechproject.sms.configs;

import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.ArrayList;
import java.util.List;

import static org.apache.commons.lang.StringUtils.isBlank;
import static org.apache.commons.lang.StringUtils.isNotBlank;

//
//todo: expose list of configs as task action parameter values to send_sms?
//

/**
 * Represents all configs as well as the default config, sms-config.json
 */
public class Configs {

    /**
     * The name of the default configuration that will be used, if no configuration name is explicitly provided.
     */
    private String defaultConfigName;

    /**
     * The configurations in the system.
     */
    private List<Config> configs = new ArrayList<>();

    /**
     * Creates a new instance, used by Jackson.
     */
    public Configs() { }

    /**
     * Returns the default configuration, users can make one of the configurations default.
     * @return the default configuration, never null
     * @throws IllegalStateException if no default configuration is set
     */
    @JsonIgnore
    public Config getDefaultConfig() {
        if (isNotBlank(defaultConfigName)) {
            return getConfig(defaultConfigName);
        }
        throw new IllegalStateException("Trying to get default config, but no default config has been set.");
    }

    /**
     * Checks whether there are no configurations available.
     * @return true if there are no configurations, false otherwise
     */
    @JsonIgnore
    public boolean isEmpty() {
        return configs.isEmpty();
    }

    /**
     * Returns the name of the default configuration.
     * @return the name of the default configuration or null if it was not set
     */
    public String getDefaultConfigName() {
        return defaultConfigName;
    }

    /**
     * Sets the name of the default configuration.
     * @param name  the name of the default configuration
     */
    public void setDefaultConfigName(String name) {
        defaultConfigName = name;
    }

    /**
     * @return all available configurations
     */
    public List<Config> getConfigs() {
        return configs;
    }

    /**
     * @param configs all available configurations
     */
    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    /**
     * Fetches the config with the given name.
     * @param name the name of the config
     * @return the config with the given name, never null
     * @throws IllegalArgumentException if a configuration with the given name does not exist
     */
    public Config getConfig(String name) {
        for (Config config : configs) {
            if (name.equals(config.getName())) {
                return config;
            }
        }
        throw new IllegalArgumentException("'" + name + "': no such config");
    }

    /**
     * Checks whether a configuration with the given name exists.
     * @param name the name of the configuration
     * @return true if it exists, false otherwise
     */
    public boolean hasConfig(String name) {
        for (Config config : configs) {
            if (name.equals(config.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the config with the given name, or the default config if the provided name is null or empty.
     * @param name the name of the desired configuration, or null/empty string for the default configuration
     * @return the matching configuration
     * @throws IllegalStateException if a blank string is provided and there is no default configuration
     * @throws IllegalArgumentException if a configuration with the given name does not exist
     */
    public Config getConfigOrDefault(String name) {
        if (isBlank(name)) {
            return getDefaultConfig();
        }
        return getConfig(name);
    }
}
