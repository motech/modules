package org.motechproject.odk.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all of the configurations for the ODK module.
 */
public class Settings {

    private List<Configuration> configurations;

    public Settings() {
        if (this.configurations == null) {
            this.configurations = new ArrayList<>();
        }
    }

    public Settings(List<Configuration> configurations) {
        this.configurations = configurations;
    }

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }


}
