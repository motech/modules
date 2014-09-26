package org.motechproject.ivr.service;


import org.motechproject.ivr.domain.Template;

import java.util.List;

/**
 * Template service, manages IVR templates. A {@link org.motechproject.ivr.domain.Template} usually contains text to be
 * returned to the IVR provider as CCXML/VXML.
 * See {@link org.motechproject.ivr.domain.Template}
 */
public interface TemplateService {
    Template getTemplate(String name);
    List<Template> allTemplates();
    boolean hasTemplate(String name);
    void updateTemplates(List<Template> templates);
}
