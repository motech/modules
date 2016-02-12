package org.motechproject.ipf.service;

import org.motechproject.ipf.domain.IPFTemplate;

import java.util.List;

public interface IPFTemplateService {

    List<IPFTemplate> getAllTemplates();

    IPFTemplate getTemplateByName(String templateName);
}
