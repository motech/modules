package org.motechproject.ivr.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Configs has list of IVR provider configurations with name of the default configuration.
 */
public class Configs {

    private String defaultConfig;
    private List<Config> configList = new ArrayList<>();

    public Configs() {

    }

    public Configs(List<Config> configList, String defaultConfig) {
        this.configList = configList;
        this.defaultConfig = defaultConfig;
    }

    public Configs(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public String getDefaultConfig() {
        return defaultConfig;
    }

    public void setDefaultConfig(String defaultConfig) {
        this.defaultConfig = defaultConfig;
    }

    public List<Config> getConfigList() {
        return configList;
    }

    public void setConfigList(List<Config> configList) {
        this.configList = configList;
    }

}
