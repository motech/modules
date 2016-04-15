package org.motechproject.openmrs19.config;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.motechproject.openmrs19.exception.config.ConfigurationAlreadyExistsException;
import org.motechproject.openmrs19.exception.config.ConfigurationNotFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for storing the list of all configurations and the name of the default one.
 */
public class Configs {

    private List<Config> configs;

    private String defaultConfigName;

    public Configs() {
        this(new ArrayList<>(), null);
    }

    public Configs(List<Config> configs, String defaultConfigName) {
        this.configs = configs;
        this.defaultConfigName = defaultConfigName;
    }

    /**
     * Adds the given configuration. If configuration with the sam name already exists a
     * {@link ConfigurationAlreadyExistsException} exception will be thrown.
     *
     * @param config  the configuration to be added
     */
    public void add(Config config) {
        if (getByName(config.getName()) != null) {
            throw new ConfigurationAlreadyExistsException(config.getName());
        }

        configs.add(config);

        if (defaultConfigName == null) {
            defaultConfigName = config.getName();
        }
    }

    /**
     * Updates configuration with the same name with the passed data. If configuration with the name does not exist a
     * {@link ConfigurationNotFoundException} exception will be thrown.
     *
     * @param source  the configuration to serve as an update source
     */
    public void update(Config source) {
        for (Config config : configs) {
            if (StringUtils.equals(config.getName(), source.getName())) {
                config.setOpenMrsUrl(source.getOpenMrsUrl());
                config.setUsername(source.getUsername());
                config.setPassword(source.getPassword());
                config.setMotechPatientIdentifierTypeName(source.getMotechPatientIdentifierTypeName());
                config.getPatientIdentifierTypeNames().clear();
                config.getPatientIdentifierTypeNames().addAll(source.getPatientIdentifierTypeNames());
                return;
            }
        }
        throw new ConfigurationNotFoundException(source.getName());
    }

    /**
     * Deletes configuration with the given name. If configuration with the name does not exist a
     * {@link ConfigurationNotFoundException} exception will be thrown.
     *
     * @param name  the name of the configuration to be deleted
     */
    public void delete(String name) {
        Config config = getByName(name);

        if (config == null) {
            throw new ConfigurationNotFoundException(name);
        }

        configs.remove(config);

        if (StringUtils.equals(config.getName(), defaultConfigName)) {
            defaultConfigName = configs.size() > 0 ? configs.get(0).getName() : null;
        }
    }

    /**
     * Marks configuration with the given name as a default one. If configuration with the name does not exist a
     * {@link ConfigurationNotFoundException} exception will be thrown.
     *
     * @param name  the name of the configuration to be set as default
     */
    public void markAsDefault(String name) {
        if (getByName(name) != null) {
            defaultConfigName = name;
        } else {
            throw new ConfigurationNotFoundException(name);
        }
    }

    /**
     * Returns configuration by its name.
     *
     * @param name  the name of the configuration
     * @return the configuration with the given name
     */
    public Config getByName(String name) {
        for (Config config : configs) {
            if (StringUtils.equals(config.getName(), name)) {
                return config;
            }
        }
        return null;
    }

    /**
     * Returns the default configuration.
     *
     * @return the default configuration
     */
    public Config getDefault() {
        if (defaultConfigName != null) {
            return getByName(defaultConfigName);
        }
        return null;
    }

    /**
     * Returns the list of all configurations.
     *
     * @return the list of all configurations
     */
    public List<Config> getConfigs() {
        return configs;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Configs)) {
            return false;
        }

        Configs other = (Configs) o;

        return StringUtils.equals(defaultConfigName, other.defaultConfigName)
                && ObjectUtils.equals(configs, other.configs);
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(defaultConfigName).append(configs).toHashCode();
    }
}
