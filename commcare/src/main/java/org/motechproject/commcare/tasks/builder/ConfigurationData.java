package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.domain.FormSchemaJson;

import java.util.Map;

/**
 * Helper class used for storing configuration name as well as all forms and cases that originated from the said
 * configuration.
 */
public class ConfigurationData {

    private String configName;

    private Map<FormSchemaJson, String> formsToApplication;

    private Map<String, String> casesToApplication;

    /**
     * Creates an instance of the {@link ConfigurationData} class. It will store the given {@code formsToApplication}
     * , {@code casesToApplication} mapped to application names for the configuration with the given {@code configName}.
     *
     * @param configName  the name of the configuration
     * @param forms  the list of all forms that originate from the given configuration
     * @param cases  the list of all cases that originate from the given configuration
     */
    public ConfigurationData(String configName, Map<FormSchemaJson, String> formsToApplication, Map<String, String> casesToApplication) {
        this.configName = configName;
        this.formsToApplication = formsToApplication;
        this.casesToApplication = casesToApplication;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public Map<FormSchemaJson, String> getFormsToApplication() {
        return formsToApplication;
    }

    public void setFormsToApplication(Map<FormSchemaJson, String> formsToApplication) {
        this.formsToApplication = formsToApplication;
    }

    public Map<String, String> getCasesToApplication() {
        return casesToApplication;
    }

    public void setCasesToApplication(Map<String, String> casesToApplication) {
        this.casesToApplication = casesToApplication;
    }
}
