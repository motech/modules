package org.motechproject.commcare.tasks.builder;

import org.motechproject.commcare.domain.FormSchemaJson;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class ConfigurationData {

    private String configName;

    private List<FormSchemaJson> forms;

    private Map<String, Set<String>> cases;

    public ConfigurationData(String configName, List<FormSchemaJson> forms, Map<String, Set<String>> cases) {
        this.configName = configName;
        this.forms = forms;
        this.cases = cases;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }

    public List<FormSchemaJson> getForms() {
        return forms;
    }

    public void setForms(List<FormSchemaJson> forms) {
        this.forms = forms;
    }

    public Map<String, Set<String>> getCases() {
        return cases;
    }

    public void setCases(Map<String, Set<String>> cases) {
        this.cases = cases;
    }
}
