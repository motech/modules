package org.motechproject.sms.templates;

import org.motechproject.server.config.SettingsFacade;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Helper template collection
 */
public class Templates {
    private Map<String, Template> templates = new HashMap<String, Template>();

    public Templates(SettingsFacade settingFacade, List<Template> templates) {
        for (Template template : templates) {
            template.readDefaults(settingFacade);
            this.templates.put(template.getName(), template);
        }
    }

    public Template getTemplate(String name) {
        return templates.get(name);
    }

    public Map<String, TemplateForWeb> templatesForWeb() {
        Map<String, TemplateForWeb> ret = new HashMap<String, TemplateForWeb>();
        for (Map.Entry<String, Template> entry : templates.entrySet()) {
            ret.put(entry.getKey(), new TemplateForWeb(entry.getValue()));
        }
        return ret;
    }
}
