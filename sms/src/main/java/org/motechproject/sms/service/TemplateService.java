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

    /**
     * Retrieves the template with the given name.
     * @param name the name of the template
     * @return the template with the matching name, never null
     * @throws IllegalArgumentException if no such template exists
     */
    Template getTemplate(String name);

    /**
     * Retrieves all templates as objects for the UI display.
     * @return map of the template representations, where keys are the names of the templates
     */
    Map<String, TemplateForWeb> allTemplatesForWeb();

    /**
     * Imports the custom provider templates provided by the user. These templates will
     * be added to the additional templates configuration.
     * @param templateList the templates to import
     */
    void importTemplates(List<Template> templateList);

    /**
     * Imports the custom provider template provided by the user. This template will
     * be added to the additional templates configuration.
     * @param template the template to import
     */
    void importTemplate(Template template);
}
