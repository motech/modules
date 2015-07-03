package org.motechproject.sms.templates;

import java.util.List;

/**
 * A simplified form of the template class used in the web settings UI
 */
public class TemplateForWeb {

    /**
     * The unique name of the template.
     */
    private String name;

    /**
     * The names of fields that are configurable for this template.
     */
    private List<String> configurables;

    /**
     * Constructs this DTO from the provided {@link Template} object.
     * @param template the backing template
     */
    public TemplateForWeb(Template template) {
        this.name = template.getName();
        this.configurables = template.getConfigurables();
    }

    /**
     * @return the unique name of the template
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the unique name of the template
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the names of fields that are configurable for this template
     */
    public List<String> getConfigurables() {
        return configurables;
    }

    /**
     * @param configurables the names of fields that are configurable for this template
     */
    public void setConfigurables(List<String> configurables) {
        this.configurables = configurables;
    }
}
