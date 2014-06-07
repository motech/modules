package org.motechproject.sms.templates;

import java.util.List;

/**
 * A simplified form of the template class used in the web settings UI
 */
public class TemplateForWeb {
    private String name;
    private List<String> configurables;

    public TemplateForWeb(Template template) {
        this.name = template.getName();
        this.configurables = template.getConfigurables();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getConfigurables() {
        return configurables;
    }

    public void setConfigurables(List<String> configurables) {
        this.configurables = configurables;
    }
}
