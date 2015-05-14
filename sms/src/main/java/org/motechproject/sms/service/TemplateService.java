package org.motechproject.sms.service;

import org.motechproject.sms.templates.Template;
import org.motechproject.sms.templates.TemplateForWeb;

import java.util.List;
import java.util.Map;

/**
 * Template service, manages SMS Templates. A template represents the way all users connect to an SMS provider.
 * See {@link org.motechproject.sms.templates.Template}
 */
public interface TemplateService {
    Template getTemplate(String name);
    Map<String, TemplateForWeb> allTemplatesForWeb();
    void importTemplates(List<Template> templateList);
    void importTemplate(Template template);
}
