package org.motechproject.sms.templates;

import org.motechproject.server.config.SettingsFacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper template collection.
 */
public class Templates {

    /**
     * Templates as map, where keys are their names.
     */
    private Map<String, Template> templates = new HashMap<>();

    /**
     * Constructs this collection from the provided templates. The templates will
     * read their default values from the MOTECH configuration system through the provided settings facade.
     * @param settingFacade the settings facade from which default values will be read
     * @param templates the collection of templates from which this object will be built
     */
    public Templates(SettingsFacade settingFacade, List<Template> templates) {
        for (Template template : templates) {
            template.readDefaults(settingFacade);
            this.templates.put(template.getName(), template);
        }
    }

    /**
     * Returns the template with the given name.
     * @param name the name of the template
     * @return the matching template or null if no such template exists
     */
    public Template getTemplate(String name) {
        return templates.get(name);
    }

    /**
     * Returns this collection in a form suitable for the UI.
     * @return a map where the keys are template names and values are the simplified forms of templates
     * @see TemplateForWeb
     */
    public Map<String, TemplateForWeb> templatesForWeb() {
        Map<String, TemplateForWeb> ret = new HashMap<>();
        for (Map.Entry<String, Template> entry : templates.entrySet()) {
            ret.put(entry.getKey(), new TemplateForWeb(entry.getValue()));
        }
        return ret;
    }
}
