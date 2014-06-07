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
    private String defaultConfigName;
    private List<Config> configs = new ArrayList<Config>();

    public Configs() { }

    @JsonIgnore
    public Config getDefaultConfig() {
        if (isNotBlank(defaultConfigName)) {
            return getConfig(defaultConfigName);
        }
        throw new IllegalStateException("Trying to get default config, but no default config has been set.");
    }

    public String getDefaultConfigName() {
        return defaultConfigName;
    }

    public void setDefaultConfigName(String name) {
        defaultConfigName = name;
    }

    public List<Config> getConfigs() {
        return configs;
    }

    public void setConfigs(List<Config> configs) {
        this.configs = configs;
    }

    public Config getConfig(String name) {
        for (Config config : configs) {
            if (name.equals(config.getName())) {
                return config;
            }
        }
        throw new IllegalStateException("'" + name + "': no such config");
    }

    public boolean hasConfig(String name) {
        for (Config config : configs) {
            if (name.equals(config.getName())) {
                return true;
            }
        }
        return false;
    }

    public Config getConfigOrDefault(String name) {
        if (isBlank(name)) {
            return getDefaultConfig();
        }
        return getConfig(name);
    }
}
