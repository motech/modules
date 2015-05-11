package org.motechproject.ivr.service;


import org.motechproject.ivr.domain.Template;

import java.util.List;

/**
 * Template service, manages IVR templates. A {@link org.motechproject.ivr.domain.Template} usually contains text to be
 * returned to the IVR provider as CCXML/VXML.
 * See {@link org.motechproject.ivr.domain.Template}
 */
public interface TemplateService {

    /**
     * Retrieves the template with the given name.
     * @param name the name of the template to retrieve
     * @return the template with the given name, never null
     * @throws org.motechproject.ivr.exception.TemplateNotFoundException if the template with the given name does not exist
     */
    Template getTemplate(String name);

    /**
     * Retrieves all templates stored in the configuration system.
     * @return the list of all templates
     */
    List<Template> allTemplates();

    /**
     * Checks whether the template with the given name exists.
     * @param name the name to check
     * @return true if the template with the given name exists, false otherwise
     */
    boolean hasTemplate(String name);

    /**
     * Updates the templates. The old templates collection is overwritten by the new one.
     * @param templates the collection of configurations to save
     */
    void updateTemplates(List<Template> templates);
}
